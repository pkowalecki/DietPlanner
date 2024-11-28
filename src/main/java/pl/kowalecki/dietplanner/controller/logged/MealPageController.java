package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.kowalecki.dietplanner.controller.helper.AddMealHelper;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;

import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;

import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;
import pl.kowalecki.dietplanner.services.WebPage.MessageType;
import pl.kowalecki.dietplanner.services.dietplannerapi.meal.DietPlannerApiMealService;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/app/auth")
public class MealPageController {

    DietPlannerApiMealService apiMealService;
    AddMealHelper addMealHelper;
    IWebPageService iWebPageService;

    @GetMapping(value = "/addMeal")
    public Mono<String> getListMeal(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        return apiMealService.getMealStarterPack()
                .map(mealList -> {
                    model.addAttribute("mealList", mealList);
                    return "pages/logged/addMeal";
                })
                .defaultIfEmpty("pages/logged/addMeal");
    }


    @PostMapping(value = "/addMeal")
    public Mono<ResponseEntity<Object>> addMeal(@RequestBody AddMealRequestDTO addMealRequestDTO, Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, String> errors = new HashMap<>();
        errors = addMealHelper.checkData(addMealRequestDTO);
        if (!errors.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(errors));
        }
        return apiMealService.addMeal(addMealRequestDTO).flatMap(
                        response -> {
                            if (response.getStatusCode().is2xxSuccessful()) {
                                iWebPageService.setMsg(MessageType.SUCCESS, "Posiłek został dodany");
                                return Mono.just(ResponseEntity.ok().build());
                            } else if (response.getStatusCode().is4xxClientError()) {
                                iWebPageService.setMsg(MessageType.ERROR, "Nie udało się dodać posiłku.");
                                return Mono.just(ResponseEntity.status(response.getStatusCode()).build());
                            } else {
                                iWebPageService.setMsg(MessageType.ERROR, "Wystąpił nieoczekiwany błąd serwera.");
                                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                            }
                        })
                .onErrorResume(error -> {
                    iWebPageService.setMsg(MessageType.ERROR, "Błąd połączenia z API");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to connect to API: " + error.getMessage()));
                });
    }

    @GetMapping(value = "/generateMealBoard")
    public Mono<String> mealPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        return apiMealService.getAllMeals()
                .map(mealList -> {
                    model.addAttribute("mealList", mealList);
                    return "pages/logged/foodBoardPage";
                })
                .defaultIfEmpty("pages/logged/foodBoardPage");

    }


    @PostMapping(value = "/generateMealBoard")
    public Mono<String> resultPage(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("form") FoodBoardPageData form) {
        FoodBoardPageRequest apiReq = new FoodBoardPageRequest();
        apiReq.setMealIds(form.getMealIds());
        apiReq.setMultiplier(form.getMultiplier());
        return apiMealService.generateMealBoard(apiReq)
                .map(mealBoardData -> {
                    model.addAttribute("meals", mealBoardData.getMealList());
                    model.addAttribute("result", mealBoardData.getIngredientsToBuy());
                    model.addAttribute("idsList", form.getMealIds());
                    return "pages/logged/foodBoardResult";
                });
    }

    @PostMapping(value = "/downloadMealDocument", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadMealDocument(@RequestParam("mealIds") List<Long> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String url = "http://" + UrlTools.MEAL_SERVICE_URL + "/meal/getMealNamesById";
//        ResponseEntity<List<String>> mealMapList = apiClient.getMealNamesById(ids);
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendPostRequest(url, ids, ResponseBodyDTO.class, request, response);
//        if (apiResponse.getStatusCode() == HttpStatus.OK && apiResponse.getBody() != null) {
//            List<String> mealMapList = (List<String>) apiResponse.getBody().getData().get("mealNames");
//            System.out.println(mealMapList);
//            try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//                byte[] documentBytes = DocumentService.createMealDocument(document, out, mealMapList);
//                response.setContentType("application/octet-stream");
//                response.setHeader("Content-Disposition", "attachment; filename=\"jedzonko.docx\"");
//                response.getOutputStream().write(documentBytes);
//                response.getOutputStream().flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//                webPageService.setMsg(MessageType.ERROR, "There was error during file generation");
//            }
//        }
    }

    @GetMapping(value = "/mealsHistory")
    public String mealsHistory(Model model, HttpServletRequest request, HttpServletResponse response) {
        ArrayList<Map<String, Object>> dane = new ArrayList<Map<String, Object>>();
//        String url = "http://" + UrlTools.MEAL_SERVICE_URL + "/meal/getMealHistory";
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendGetRequest(url, ResponseBodyDTO.class, request, response);
//        if (apiResponse.getStatusCode() == HttpStatus.OK && apiResponse.getBody() != null) {
//            List<?> mealHistoryList = (List<?>) apiResponse.getBody().getData().get("mealHistoryList");
//            List<MealHistoryDTO> mealHistory = classMapper.convertToDTOList(mealHistoryList, MealHistoryDTO.class);
//            for (MealHistoryDTO historyDTO : mealHistory) {
//                Map<String, Object> meal = new HashMap<>();
//                meal.put("id", historyDTO.getPublic_id());
//                meal.put("userId", historyDTO.getUserId());
//                meal.put("mealsId", historyDTO.getMealsIds());
//                meal.put("created", DateUtils.parseDateToddmmyy(historyDTO.getCreated()));
//                meal.put("multiplier", historyDTO.getMultiplier());
//                dane.add(meal);
//            }
//            model.addAttribute("mealHistory", dane);
//        }
        return "pages/logged/mealsHistoryPage";
    }

    @PostMapping(value = "/mealHistory")
    public String getMealHistoryPage(@RequestParam("id") String param, Model model, HttpServletRequest request, HttpServletResponse response, InputStream inputStream) {
//        String url = "http://" + UrlTools.MEAL_SERVICE_URL + "/meal/getMealHistory";
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendPostRequest(url, param, ResponseBodyDTO.class, request, response);
//        if (apiResponse.getStatusCode() == HttpStatus.OK && apiResponse.getBody() != null) {
//            MealHistoryDetailsDTO mealHistory = classMapper.convertToDTO(apiResponse.getBody().getData().get("mealHistory"), MealHistoryDetailsDTO.class);
//            model.addAttribute("mealHistory", mealHistory);
//        }
        return "pages/logged/mealHistoryPage";
    }

}
