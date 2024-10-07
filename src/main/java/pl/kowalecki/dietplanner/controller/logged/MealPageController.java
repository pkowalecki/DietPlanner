package pl.kowalecki.dietplanner.controller.logged;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.controller.DietplannerApiClient;
import pl.kowalecki.dietplanner.controller.helper.AddMealHelper;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.IngredientToBuyDTO;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;
import org.apache.poi.xwpf.usermodel.*;
import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;
import pl.kowalecki.dietplanner.services.WebPage.MessageType;
import pl.kowalecki.dietplanner.services.document.DocumentService;
import pl.kowalecki.dietplanner.utils.ClassMapper;
import pl.kowalecki.dietplanner.utils.SerializationUtils;
import pl.kowalecki.dietplanner.utils.UrlTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/app/auth")
public class MealPageController {

    private final ClassMapper classMapper;
//    IWebPageService webPageService;
    DietplannerApiClient apiClient;
    AddMealHelper addMealHelper;

    @GetMapping(value = "/addMeal")
    public String getListMeal(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealStarterPack";
        ResponseEntity<MealStarterPackDTO> apiResponse = apiClient.getMealStarterPack();

//        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseBodyDTO.ResponseStatus.OK) {
//            //TODO NAPISAĆ DTO KTÓRE BĘDZIE MIAŁO List<IngredientsNames>, List<MealTypes>, List<IngredientUnits>, List<MeasurementTypes>
//            if (!apiResponse.getBody().getData().isEmpty()) {
//                model.addAttribute("ingredientsNames", serializeAndReturnIngredientList(apiResponse.getBody().getData().get("ingredientsNames")));
//                model.addAttribute("mealTypes", serializeAndReturnMealTypeList(apiResponse.getBody().getData().get("mealTypes")));
//                model.addAttribute("ingredientUnits", serializeAndReturnIngredientUnitList(apiResponse.getBody().getData().get("ingredientUnits")));
//                model.addAttribute("measurementTypes", serializeAndReturnMeasurementTypeList(apiResponse.getBody().getData().get("measurementTypes")));
//                return "pages/logged/addMeal";
//            }
        return "pages/logged/addMeal";
        }
//        webPageService.setMsg(MessageType.ERROR, "Wystąpił błąd podczas wczytywania zakładki");
//        return "redirect:/app/auth/loggedUserBoard";


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
        ResponseEntity<String> apiRespo = apiClient.addMeal(addMealRequestDTO);

        if (apiRespo.getBody() != null && apiRespo.getStatusCode() == HttpStatus.OK) {
            responseBodyDTO = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.OK)
                    .message(apiRespo.getBody() != null ? apiRespo.getBody() : "Meal created")
                    .build();
            return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
        }
        responseBodyDTO = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.ERROR)
                .message("There was an error during API communication")
                .build();

        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
    }

    private List<IngredientName> serializeAndReturnIngredientList(Object ingredientsNames) {
        try {
            TypeReference<List<IngredientName>> typeRef = new TypeReference<List<IngredientName>>() {
            };
            return SerializationUtils.convertToList(ingredientsNames, typeRef);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private List<MealType> serializeAndReturnMealTypeList(Object mealTypes) {
        try {
            TypeReference<List<MealType>> typeRef = new TypeReference<List<MealType>>() {
            };
            return SerializationUtils.convertToList(mealTypes, typeRef);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private List<IngredientUnit> serializeAndReturnIngredientUnitList(Object ingredientUnits) {
        try {
            TypeReference<List<IngredientUnit>> typeRef = new TypeReference<List<IngredientUnit>>() {
            };
            return SerializationUtils.convertToList(ingredientUnits, typeRef);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private List<MeasurementType> serializeAndReturnMeasurementTypeList(Object measurementTypes) {
        try {
            TypeReference<List<MeasurementType>> typeRef = new TypeReference<List<MeasurementType>>() {
            };
            return SerializationUtils.convertToList(measurementTypes, typeRef);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping(value = "/generateMealBoard")
    public String mealPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/allMeal";
        ResponseEntity<List<Meal>> mealList = apiClient.getAllMeals();
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendGetRequest(url, ResponseBodyDTO.class, request, response);
//        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseBodyDTO.ResponseStatus.OK) {
//            if (!apiResponse.getBody().getData().isEmpty()) {
//                List<?> mealMapList = (List<?>) apiResponse.getBody().getData().get("mealList");
//                List<Meal> mealList = classMapper.convertToDTOList(mealMapList, Meal.class);
//                model.addAttribute("mealList", mealList);
//            }
//        }
        return "pages/logged/foodBoardPage";
    }


    @PostMapping(value = "/generateMealBoard")
    public String resultPage(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("form") FoodBoardPageData form) {
        FoodBoardPageRequest apiReq = new FoodBoardPageRequest();
        apiReq.setMealIds(form.getMealIds());
        apiReq.setMultiplier(form.getMultiplier());
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/generateFoodBoard";
        ResponseEntity<MealBoardDTO> mealBoardDTO = apiClient.generateFoodBoard(apiReq);
//        ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendPostRequest(url, apiReq, ResponseBodyDTO.class, request, response);
//        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseBodyDTO.ResponseStatus.OK) {
//            if (!apiResponse.getBody().getData().isEmpty()) {
//
//                List<?> mealMapList = (List<?>) apiResponse.getBody().getData().get("mealList");
//                List<Meal> mealList = classMapper.convertToDTOList(mealMapList, Meal.class);
//
//                List<?> ingredientToBuy = (List<?>) apiResponse.getBody().getData().get("ingredientToBuyDTOList");
//                List<IngredientToBuyDTO> ingredientsToBuyWeb = classMapper.convertToDTOList(ingredientToBuy, IngredientToBuyDTO.class);
//
//                model.addAttribute("meals", mealList);
//                model.addAttribute("result", ingredientsToBuyWeb);
//                model.addAttribute("idsList", form.getMealIds());
//
//            }
//        }
        return "pages/logged/foodBoardResult";
    }

    @PostMapping(value = "/downloadMealDocument", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadMealDocument(@RequestParam("mealIds") List<Long> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealNamesById";
        ResponseEntity<List<String>> mealMapList = apiClient.getMealNamesById(ids);
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

    @GetMapping(value = "/mealHistory")
    public String mealHistory(Model model, HttpServletRequest request, HttpServletResponse response) {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealHistory";
        ResponseEntity<HttpStatus> mealHistory = apiClient.getMealHistory();
//        ResponseEntity<HttpStatus> apiResponse = webPageService.sendGetRequest(url, HttpStatus.class, request, response);
        return "pages/logged/mealHistoryPage";
    }

}
