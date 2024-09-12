package pl.kowalecki.dietplanner.services.document;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class DocumentService {


    public static byte[] createMealDocument(XWPFDocument document, ByteArrayOutputStream out, List<String> mealNames) throws IOException {
        setPageOrientationLandscape(document);
        XWPFTable table = createTableWithHeaders(document);
        fillTableWithMeals(table, mealNames);
        document.write(out);
        return out.toByteArray();
    }

    private static void fillTableWithMeals(XWPFTable table, List<String> mealNames) {
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
