package pl.kowalecki.dietplanner.service.document;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentService implements IDocumentService {

    DietPlannerApiClient dpaClient;

    @Override
    public Mono<ResponseEntity<byte[]>> downloadMealsPlan(String pageId) {
        return dpaClient.getMealNamesByMealId(pageId)
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


    public static byte[] createMealDocument(XWPFDocument document, ByteArrayOutputStream out, List<String> mealNames) throws IOException {
        setPageOrientationLandscape(document);
        XWPFTable table = createTableWithHeaders(document);
        fillTableWithMeals(table, mealNames);
        document.write(out);
        return out.toByteArray();
    }

    private static void fillTableWithMeals(XWPFTable table, List<String> mealNames) {
        int mealIndex = 0;
        int numDays = 7; // 7 dni tygodnia
        String[] mealTypes = {"Śniadanie", "Przekąska", "Obiad", "Kolacja"};
        int mealsPerDay = mealTypes.length; // 4 posiłki na dzień

        for (int colIndex = 1; colIndex <= numDays; colIndex++) {
            for (int rowIndex = 1; rowIndex <= mealsPerDay; rowIndex++) {
                XWPFTableRow row = table.getRow(rowIndex);
                if (row == null) {
                    row = table.createRow();
                }

                XWPFTableCell cell = row.getCell(colIndex);
                if (cell == null) {
                    cell = row.addNewTableCell();
                }

                if (mealIndex < mealNames.size()) {
                    String mealName = mealNames.get(mealIndex);
                    cell.setText(mealName == null || mealName.equals("-") || mealName.isEmpty() ? "" : mealName);
                    mealIndex++;
                } else {
                    cell.setText("");
                }
            }
        }
    }

    private static XWPFTable createTableWithHeaders(XWPFDocument document) {
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


    private static void removeCellBorders(XWPFTableCell cell, boolean top, boolean right, boolean bottom, boolean left) {
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

    private static void setPageOrientationLandscape(XWPFDocument document) {
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

    private static void centerCellContent(XWPFTableCell cell) {
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
