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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.IWebPageService;
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
import pl.kowalecki.dietplanner.utils.SerializationUtils;
import pl.kowalecki.dietplanner.utils.UrlTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/app/auth")
public class MealPageController {

    @Autowired
    IWebPageService webPageService;
    @Autowired
    HttpSession session;
    @Autowired
    private HttpSession httpSession;


    @GetMapping(value = "/addMeal")
    public String getListMeal(Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        webPageService.addCommonWebData(model);
        if (webPageService.isUserLoggedIn()) {
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
        }
        return "redirect:/app/auth/loggedUserBoard";
    }

    @PostMapping(value = "/addMeal")
    public void addMeal(@RequestBody AddMealRequestDTO addMealRequestDTO, Model model, HttpServletRequest request, HttpServletResponse httpResponse) {


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
    public String mealPage(Model model) {
        webPageService.addCommonWebData(model);
        //FIXME TO ROBI API
//        List<Meal> mealList = mealRepository.findAll();
//        model.addAttribute("mealList", mealList);
        return "pages/logged/foodBoardPage";
    }

    @PostMapping(value = "/generateMealBoard")
    public String resultPage(Model model, HttpSession httpSession, HttpServletRequest req, HttpServletResponse resp, @ModelAttribute("form") FoodBoardPageData form) {
        List<Long> idsList = form.getMealValues();
        //FIXME TO ROBI API
//        List<String> mealNames = mealRepositoryImpl.getMealNamesByIdList(idsList);
//        List<Meal> meal = mealRepository.findMealsByMealIdIn(idsList);
//
//        model.addAttribute("result", mealRepositoryImpl.getMealIngredientsFinalList(idsList, form.getMultiplier()));
//        model.addAttribute("meals", meal);
//        model.addAttribute("idsList", idsList);
//        model.addAttribute("mealNames", mealNames);
        return "pages/foodBoardResult";
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
