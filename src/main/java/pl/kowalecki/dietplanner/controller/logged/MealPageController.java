package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kowalecki.dietplanner.controller.helper.AddMealHelper;
import pl.kowalecki.dietplanner.model.DTO.*;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;
import pl.kowalecki.dietplanner.services.WebPage.MessageType;
import pl.kowalecki.dietplanner.services.dietplannerapi.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.services.document.DocumentService;
import pl.kowalecki.dietplanner.utils.DateUtils;
import pl.kowalecki.dietplanner.utils.MapUtils;
import pl.kowalecki.dietplanner.utils.TextTools;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/app/auth")
public class MealPageController {

    DietPlannerApiClient apiMealService;
    AddMealHelper addMealHelper;
    IWebPageService webPageService;

    @GetMapping(value = "/addMeal")
    public Mono<String> getListMeal(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        return apiMealService.getMealStarterPack()
                .map(mealStarterPack -> {
                    model.addAttribute("meal", new Meal());
                    model.addAttribute("ingredientNameList", MapUtils.mapIngredientName(mealStarterPack.getIngredientNameList()));
                    model.addAttribute("ingredientUnitList", MapUtils.mapIngredientUnit(mealStarterPack.getIngredientUnitList()));
                    model.addAttribute("mealTypeList", MapUtils.mapMealType(mealStarterPack.getMealTypeList()));
                    model.addAttribute("measurementTypeList", MapUtils.mapMeasurementType(mealStarterPack.getMeasurementTypeList()));
                    return "pages/logged/addEditMeal";
                })
                .defaultIfEmpty("pages/logged/addEditMeal");
    }


    @PostMapping(value = "/addOrUpdateMeal")
    public Mono<ResponseEntity<Map<String, String>>> addMeal(@RequestBody AddMealRequestDTO addMealRequestDTO, Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, String> errors = addMealHelper.checkData(addMealRequestDTO);

        if (!errors.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(errors));
        }
        return apiMealService.addOrUpdateMeal(addMealRequestDTO)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(webPageService.addMessageToPage(MessageType.SUCCESS, "Posiłek został dodany")));
                    } else {
                        String errorMessage = response.getStatusCode().is4xxClientError()
                                ? "Nie udało się dodać posiłku."
                                : "Wystąpił nieoczekiwany błąd serwera";
                        return Mono.just(ResponseEntity.status(response.getStatusCode()).body(webPageService.addMessageToPage(MessageType.ERROR, errorMessage)));
                    }
                })
                .onErrorResume(error -> Mono.just(ResponseEntity.internalServerError()
                        .body(webPageService.addMessageToPage(MessageType.ERROR, "Wystąpił nieoczekiwany błąd serwera"))));
    }

    @GetMapping(value = "/generateMealBoard")
    public Mono<String> mealPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        return apiMealService.getMealNamesByUserId()
                .map(mealList -> {
                    model.addAttribute("days", DateUtils.getDaysOfWeek());

                    Map<String, List<Map<String, String>>> mealsByType = new HashMap<>();
                    List<String> mealTypes = List.of("BREAKFAST", "SNACK", "LUNCH", "SUPPER");

                    for (String type : mealTypes) {
                        List<Map<String, String>> mappedMeals = MapUtils.mapToFtl(
                                mealList.stream()
                                        .filter(meal -> Arrays.asList(meal.getMealTypes().split(",\\s*"))
                                                .contains(type))
                                        .collect(Collectors.toList()),
                                "mealId", "name"
                        );

                        mealsByType.put(type.toLowerCase() + "List", mappedMeals);
                    }

                    model.addAllAttributes(mealsByType);
                    return "pages/logged/foodBoardPage";
                })
                .defaultIfEmpty("pages/logged/foodBoardPage");

    }


    @PostMapping(value = "/generateMealBoard")
    public Mono<String> resultPage(@RequestBody Map<String, Object> rawData, Model model) {
        Map<String, Map<String, Long>> meals = prepareMeals(rawData);
        Double multiplier = rawData.containsKey("multiplier")
                ? Double.valueOf(rawData.get("multiplier").toString()) : 1.0;

        List<Long> mealIds = prepareIds(meals);

        FoodBoardPageRequest requestData = new FoodBoardPageRequest();
        requestData.setMealIds(mealIds);
        requestData.setMultiplier(multiplier);

        return apiMealService.generateMealBoard(requestData)
                .map(mealBoardData -> {
                    model.addAttribute("result", mealBoardData);
                    model.addAttribute("idsList", requestData.getMealIds());
                    return "pages/logged/foodBoardResult";
                });
    }

    private Map<String, Map<String, Long>> prepareMeals(Map<String, Object> rawData) {
        Map<String, Map<String, Long>> meals = new HashMap<>();

        rawData.forEach((key, value) -> {
            if (key.startsWith("meals[")) {
                String[] parts = key.replaceAll("[^0-9]+", " ").trim().split(" ");
                if (parts.length == 2) {
                    String day = parts[0];
                    String mealType = parts[1];

                    if (TextTools.isTextLengthOk(day, 1, 7) && TextTools.isTextLengthOk(mealType, 1, 4)) {
                        meals.computeIfAbsent(day, k -> new HashMap<>())
                                .put(mealType, Long.valueOf(value.toString()));
                    }
                }
            }
        });

        return meals;
    }


    private List<Long> prepareIds(Map<String, Map<String, Long>> meals) {
        List<Long> ids = new ArrayList<>();
        for (Map<String, Long> day : meals.values()) {
            for (Long meal : day.values()) {
                ids.add(meal != null ? meal : 0L);
            }
        }
        return ids;
    }

    @PostMapping(value = "/downloadMealDocument", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> downloadMealDocument(@RequestParam("mealIds") List<Long> ids) {
        return apiMealService.getMealNamesByMealId(ids)
                .flatMap(mealNames -> {
                    try (XWPFDocument document = new XWPFDocument();

                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                        byte[] documentBytes = DocumentService.createMealDocument(document, out, mealNames);

                        return Mono.just(ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"jedzonko.docx\"")
                                .body(documentBytes));
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Błąd generowania dokumentu", e));
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(("Błąd serwera: " + e.getMessage()).getBytes(StandardCharsets.UTF_8))));
    }

    @GetMapping(value = "/mealsHistory")
    public Mono<String> mealsHistory(Model model, HttpServletRequest request, HttpServletResponse response) {
        return apiMealService.getMealHistoryList()
                .flatMap(mealHistory -> {
                    model.addAttribute("mealHistory", mealHistory);
                    return Mono.just("pages/logged/mealsHistoryPage");
                })
                .defaultIfEmpty("pages/logged/mealsHistoryPage");
    }

    @PostMapping(value = "/mealHistory")
    public Mono<String> getMealHistoryPage(@RequestParam("id") String param, Model model, HttpServletRequest request, HttpServletResponse response, InputStream inputStream) {
        return apiMealService.getMealHistoryById(param)
                .map(mealHistory -> {
                    model.addAttribute("mealHistory", mealHistory);
                    return "pages/logged/mealHistoryPage";
                });
        //TODO DAĆ TU RETURNA JAKIEGOŚ FAJNEGO.

    }

    @GetMapping(value = "/details/{id}")
    public Mono<String> getMealDetailsPage(@PathVariable Long id, Model model) {
        String actionType = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/app/auth/editMeal/")
                .toUriString();
        return apiMealService.getMealDetails(id)
                .map(response -> {
                    model.addAttribute("meal", response);
                    model.addAttribute("url", actionType);
                    return "pages/logged/mealDetailsPage";
                });
    }

    @GetMapping(value = "/editMeal/{id}")
    public Mono<String> getEditMealPage(@PathVariable Long id, Model model) {
        return Mono.zip(
                apiMealService.getMealStarterPack(),
                apiMealService.getMealDetails(id)
        ).map(tuple -> {

            MealStarterPack starterPack = tuple.getT1();
            Meal mealDetails = tuple.getT2();

            model.addAttribute("ingredientNameList", MapUtils.mapIngredientName(starterPack.getIngredientNameList()));
            model.addAttribute("ingredientUnitList", MapUtils.mapIngredientUnit(starterPack.getIngredientUnitList()));
            model.addAttribute("mealTypeList", MapUtils.mapMealType(starterPack.getMealTypeList()));
            model.addAttribute("measurementTypeList", MapUtils.mapMeasurementType(starterPack.getMeasurementTypeList()));
            model.addAttribute("meal", mealDetails);
            model.addAttribute("selectedMealTypeIds", mealDetails.getMealTypes().stream().map(MealType::getId).collect(Collectors.toList()));
            return "pages/logged/addEditMeal";
        });

    }

}
