package pl.kowalecki.dietplanner.controller.logged;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.IWebPageService;
import pl.kowalecki.dietplanner.controller.helper.AddMealHelper;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.IngredientToBuyDTO;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.page.FoodBoardPageData;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import pl.kowalecki.dietplanner.utils.ClassMapper;
import pl.kowalecki.dietplanner.utils.SerializationUtils;
import pl.kowalecki.dietplanner.utils.UrlTools;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/app/auth")
public class MealPageController {

    private final ClassMapper classMapper;
    IWebPageService webPageService;
    HttpSession session;
    private HttpSession httpSession;
    AddMealHelper addMealHelper;

    @GetMapping(value = "/addMeal")
    public String getListMeal(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/getMealStarterPack";
        ResponseEntity<ResponseDTO> apiResponse = webPageService.sendGetRequest(url, ResponseDTO.class, request, httpResponse);
        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseDTO.ResponseStatus.OK) {
            if (!apiResponse.getBody().getData().isEmpty()) {
                model.addAttribute("ingredientsNames", serializeAndReturnIngredientList(apiResponse.getBody().getData().get("ingredientsNames")));
                model.addAttribute("mealTypes", serializeAndReturnMealTypeList(apiResponse.getBody().getData().get("mealTypes")));
                model.addAttribute("ingredientUnits", serializeAndReturnIngredientUnitList(apiResponse.getBody().getData().get("ingredientUnits")));
                model.addAttribute("measurementTypes", serializeAndReturnMeasurementTypeList(apiResponse.getBody().getData().get("measurementTypes")));
                return "pages/logged/addMeal";
            }
        }
        webPageService.setErrorMsg("Wystąpił błąd podczas wczytywania zakładki");
        return "redirect:/app/auth/loggedUserBoard";
    }

    @PostMapping(value = "/addMeal")
    public ResponseEntity<ResponseDTO> addMeal(@RequestBody AddMealRequestDTO addMealRequestDTO, Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        Map<String, String> errors = new HashMap<>();
        errors = addMealHelper.checkData(addMealRequestDTO);
        ResponseDTO responseDTO;
        if (!errors.isEmpty()) {
            responseDTO = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.BADDATA)
                    .data(errors)
                    .build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/addMeal";
        ResponseEntity<ResponseDTO> apiResponse = webPageService.sendPostRequest(url, addMealRequestDTO, ResponseDTO.class, request, httpResponse);

        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseDTO.ResponseStatus.OK) {
            responseDTO = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.OK)
                    .message(apiResponse.getBody().getMessage() != null ? apiResponse.getBody().getMessage() : "Meal created")
                    .build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
        responseDTO = ResponseDTO.builder()
                .status(ResponseDTO.ResponseStatus.ERROR)
                .message("There was an error during API communication")
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
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
        webPageService.addCommonWebData(model);
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/allMeal";
        ResponseEntity<ResponseDTO> apiResponse = webPageService.sendGetRequest(url, ResponseDTO.class, request, response);
        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseDTO.ResponseStatus.OK) {
            if (!apiResponse.getBody().getData().isEmpty()) {
                List<?> mealMapList = (List<?>) apiResponse.getBody().getData().get("mealList");
                List<Meal> mealList = classMapper.convertToDTOList(mealMapList, Meal.class);
                model.addAttribute("mealList", mealList);
            }
        }
        return "pages/logged/foodBoardPage";
    }

    @PostMapping(value = "/generateMealBoard")
    public String resultPage(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("form") FoodBoardPageData form) {
        FoodBoardPageRequest apiReq = new FoodBoardPageRequest();
        apiReq.setMealIds(form.getMealIds());
        apiReq.setMultiplier(form.getMultiplier());
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/generateFoodBoard";
        ResponseEntity<ResponseDTO> apiResponse = webPageService.sendPostRequest(url, apiReq, ResponseDTO.class, request, response);
        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseDTO.ResponseStatus.OK) {
            if (!apiResponse.getBody().getData().isEmpty()) {

                List<?> mealMapList = (List<?>) apiResponse.getBody().getData().get("mealList");
                List<Meal> mealList = classMapper.convertToDTOList(mealMapList, Meal.class);

                List<?> ingredientToBuy = (List<?>) apiResponse.getBody().getData().get("ingredientToBuyDTOList");
                List<IngredientToBuyDTO> ingredientsToBuyWeb = classMapper.convertToDTOList(ingredientToBuy, IngredientToBuyDTO.class);

                model.addAttribute("meals", mealList);
                model.addAttribute("result", ingredientsToBuyWeb);
                model.addAttribute("idsList", form.getMealIds());

            }
        }
        return "pages/logged/foodBoardResult";
    }

    @PostMapping(value = "/downloadMealDocument")
    public void downloadMealDocument(@RequestParam("mealIds") List<Long> ids, HttpServletRequest request, HttpServletResponse response) {
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/downloadMealDocument";
        ResponseEntity<byte[]> apiResponse = webPageService.sendPostRequest(url, ids, byte[].class, request, response);
        //Todo tu będzie blok obsługi dokumentu z api
    }
}
