package pl.kowalecki.dietplanner.controller.logged;

import com.fasterxml.jackson.core.type.TypeReference;
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
        if (!errors.isEmpty()){
            responseDTO = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.BADDATA)
                    .data(errors)
                    .build();
            return new ResponseEntity<>( responseDTO, HttpStatus.OK);
        }
        String url = "http://" + UrlTools.apiUrl + "/auth/meal/addMeal";
        ResponseEntity<ResponseDTO> apiResponse = webPageService.sendPostRequest(url, addMealRequestDTO, ResponseDTO.class, request, httpResponse);

        if (apiResponse.getBody() != null && apiResponse.getBody().getStatus() == ResponseDTO.ResponseStatus.OK) {
                responseDTO = ResponseDTO.builder()
                        .status(ResponseDTO.ResponseStatus.OK)
                        .message(apiResponse.getBody().getMessage()!=null?apiResponse.getBody().getMessage():"Meal created")
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
        //FIXME TO ROBI API
//        List<Meal> mealList = mealRepository.findAll();
//        model.addAttribute("mealList", mealList);
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
    //FIXME ODDZIELIĆ WEB OD API
//    @PostMapping(value = "/downloadMealDocument")
//    public ResponseEntity<byte[]> downloadMealDocument(@RequestParam List<Long> ids) throws IOException {
//        List<String> mealNames = mealRepositoryImpl.getMealNamesByIdList(ids);
//
//        try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            setPageOrientationLandscape(document);
//            XWPFTable table = createTableWithHeaders(document);
//            fillTableWithMeals(table, mealNames);
//
//            document.write(out);
//            HttpHeaders headers = createHttpHeaders();
//
//            return ResponseEntity
//                    .ok()
//                    .headers(headers)
//                    .body(out.toByteArray());
//        }
//    }

    private void fillTableWithMeals(XWPFTable table, List<String> mealNames) {
        int mealIndex = 0;
        String[] mealTypes = {"Śniadanie", "Przekąska", "Obiad", "Kolacja"};

        for (int rowIndex = 1; rowIndex <= mealTypes.length; rowIndex++) {
            XWPFTableRow row = table.getRow(rowIndex);
            for (int colIndex = 1; colIndex <= 7; colIndex++) {
                XWPFTableCell cell = row.getCell(colIndex);
                if (cell == null) {
                    cell = row.addNewTableCell();
                }
                if (mealIndex < mealNames.size()) {
                    String mealName = mealNames.get(mealIndex);
                    cell.setText(mealName.equals("-") || mealName.isEmpty() ? "" : mealName);
                    mealIndex++;
                } else {
                    cell.setText("");
                }
            }
        }
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "jedzonko.docx");
        return headers;
    }

    private XWPFTable createTableWithHeaders(XWPFDocument document) {
        XWPFTable table = document.createTable(5, 8);

        String[] daysOfWeek = {"", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"};
        XWPFTableRow headerRow = table.getRow(0);
        for (int colIndex = 0; colIndex < daysOfWeek.length; colIndex++) {
            XWPFTableCell cell = headerRow.getCell(colIndex);
            if (cell == null) {
                cell = headerRow.addNewTableCell();
            }
            cell.setText(daysOfWeek[colIndex]);
            centerCellContent(cell);
            if (colIndex == 0) {
                removeCellBorders(cell, true, true, true, true);
            } else {
                removeCellBorders(cell, true, true, false, true);
            }
        }

        String[] mealTypes = {"Śniadanie", "Przekąska", "Obiad", "Kolacja"};
        for (int rowIndex = 1; rowIndex <= mealTypes.length; rowIndex++) {
            XWPFTableRow row = table.getRow(rowIndex);
            XWPFTableCell cell = row.getCell(0);
            if (cell == null) {
                cell = row.addNewTableCell();
            }
            cell.setText(mealTypes[rowIndex - 1]);
            centerCellContent(cell);
            removeCellBorders(cell, true, false, true, true);
        }

        return table;
    }


    private void removeCellBorders(XWPFTableCell cell, boolean top, boolean right, boolean bottom, boolean left) {
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr = ctTc.getTcPr();
        if (tcPr == null) {
            tcPr = ctTc.addNewTcPr();
        }
        CTTcBorders borders = tcPr.getTcBorders();
        if (borders == null) {
            borders = tcPr.addNewTcBorders();
        }
        if (top) {
            CTBorder topBorder = borders.addNewTop();
            topBorder.setVal(STBorder.NONE);
            topBorder.setSz(BigInteger.valueOf(0));
        }
        if (right) {
            CTBorder rightBorder = borders.addNewRight();
            rightBorder.setVal(STBorder.NONE);
            rightBorder.setSz(BigInteger.valueOf(0));
        }
        if (bottom) {
            CTBorder bottomBorder = borders.addNewBottom();
            bottomBorder.setVal(STBorder.NONE);
            bottomBorder.setSz(BigInteger.valueOf(0));
        }
        if (left) {
            CTBorder leftBorder = borders.addNewLeft();
            leftBorder.setVal(STBorder.NONE);
            leftBorder.setSz(BigInteger.valueOf(0));
        }
    }

    private void setPageOrientationLandscape(XWPFDocument document) {
        CTBody body = document.getDocument().getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pageSize = section.getPgSz();
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
        pageSize.setW(BigInteger.valueOf(16840));
        pageSize.setH(BigInteger.valueOf(11900));
    }

    private void centerCellContent(XWPFTableCell cell) {
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
        }

        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr = ctTc.getTcPr();
        if (tcPr == null) {
            tcPr = ctTc.addNewTcPr();
        }
        if (!tcPr.isSetVAlign()) {
            tcPr.addNewVAlign();
        }
        tcPr.getVAlign().setVal(STVerticalJc.CENTER);
    }
}
