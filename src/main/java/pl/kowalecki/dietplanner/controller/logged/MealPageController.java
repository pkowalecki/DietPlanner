package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.kowalecki.dietplanner.controller.helper.AddMealHelper;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;

import pl.kowalecki.dietplanner.model.DTO.MealStarterPackDTO;

import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;

import pl.kowalecki.dietplanner.services.dietplannerapi.DietPlannerApiService;
import pl.kowalecki.dietplanner.services.dietplannerapi.meal.DietPlannerApiMealService;

import pl.kowalecki.dietplanner.utils.UrlTools;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/app/auth")
public class MealPageController {

    DietPlannerApiService apiService;
    DietPlannerApiMealService apiMealService;
    AddMealHelper addMealHelper;

    @GetMapping(value = "/addMeal")
    public Mono<String> getListMeal(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        return apiService.getMealStarterPack()
                .map(mealList -> {
                    model.addAttribute("mealList", mealList);
                    return "pages/logged/addMeal";
                })
                .defaultIfEmpty("pages/logged/addMeal");
        }



    @PostMapping(value = "/addMeal")
    public ResponseEntity<ResponseBodyDTO> addMeal(@RequestBody AddMealRequestDTO addMealRequestDTO, Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, String> errors = new HashMap<>();
        errors = addMealHelper.checkData(addMealRequestDTO);
        ResponseBodyDTO responseBodyDTO;
        if (!errors.isEmpty()) {
            responseBodyDTO = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.BAD_DATA)
                    .data(errors)
                    .build();
            return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
        }
//        String url = "http://" + UrlTools.apiUrl + "/auth/meal/addMeal";
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendPostRequest(url, addMealRequestDTO, ResponseBodyDTO.class, request, httpResponse);
//            return apiMealService.addMeal(addMealRequestDTO).map(
//                    responseDto ->{
//                        model.addAttribute(responseDto);
//                        return responseDto;
//                    }
//            );
        responseBodyDTO = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.OK).build();
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
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
                .map(mealBoardData->{
                    model.addAttribute("meals", mealBoardData.getMealList());
                    model.addAttribute("result", mealBoardData.getIngredientsToBuy());
                    model.addAttribute("idsList", form.getMealIds());
                    return "pages/logged/foodBoardResult";
                });
    }

    @PostMapping(value = "/downloadMealDocument", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadMealDocument(@RequestParam("mealIds") List<Long> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealNamesById";
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
        ArrayList<Map<String, Object>> dane = new ArrayList<Map<String,Object>>();
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealHistory";
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
    public String getMealHistoryPage(@RequestParam("id")String param, Model model, HttpServletRequest request, HttpServletResponse response, InputStream inputStream) {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealHistory";
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendPostRequest(url, param, ResponseBodyDTO.class, request, response);
//        if (apiResponse.getStatusCode() == HttpStatus.OK && apiResponse.getBody() != null) {
//            MealHistoryDetailsDTO mealHistory = classMapper.convertToDTO(apiResponse.getBody().getData().get("mealHistory"), MealHistoryDetailsDTO.class);
//            model.addAttribute("mealHistory", mealHistory);
//        }
        return "pages/logged/mealHistoryPage";
    }

}
