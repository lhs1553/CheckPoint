package hsim.lib.core.util.excel;

import hsim.lib.core.util.ValidationObjUtil;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class PoiWorkSheet {

    private static final double DEFAULT_WIDTH = 1;

    private HSSFSheet sheet;
    private PoiCellStyle style;

    public PoiWorkSheet(PoiWorkBook workBook) {
        this.init(workBook, null);
    }

    public PoiWorkSheet(PoiWorkBook workBook, String sheetName) {
        this.init(workBook, sheetName);
    }

    public Row nextRow(int cnt) {
        Row lastrow = null;
        for (int i = 0; i < cnt; i++) {
            lastrow = this.nextRow();
        }
        return lastrow;
    }

    public Row nextRow() {
        return this.sheet.createRow(this.sheet.getLastRowNum() + 1);
    }

    public Row getLastRow() {
        return this.sheet.getRow(this.sheet.getLastRowNum());
    }

    private void init(PoiWorkBook workBook, String sheetName) {

        this.style = new PoiCellStyle(workBook);

        if (sheetName != null) {
            this.sheet = workBook.getWorkBook().createSheet(sheetName);
        } else {
            this.sheet = workBook.getWorkBook().createSheet();
        }

        this.sheet.createRow(0);

    }

    private int getCellCnt() {
        int cellCnt = this.getLastRow().getLastCellNum();
        return cellCnt < 0 ? 0 : cellCnt;
    }

    public List<Cell> createTitleCells(String... strs) {
        List<Cell> cells = new ArrayList<>();

        for (String s : strs) {
            Cell cell = this.createTitleCell(s, DEFAULT_WIDTH);
            cells.add(cell);
        }
        return cells;
    }

    public void createTitleCells(double width, String... strs) {
        for (String s : strs) {
            this.createTitleCell(s, width);
        }
    }

    public Cell createTitleCell(String str, double width) {

        int cellCnt = this.getCellCnt();

        Cell cell = this.getLastRow().createCell(cellCnt);
        cell.setCellValue(str);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(this.style.getStringCs());

        sheet.setColumnWidth(cellCnt, (int) (sheet.getColumnWidth(cellCnt) * width));

        return cell;
    }


    public void createValueCells(Object... values) {
        for (Object value : values) {
            if (value == null) {
                this.createCell("");
                continue;
            }

            if (value instanceof String) {
                this.createCell((String) value);
            } else if (value instanceof Date) {
                this.createCell((Date) value);
            } else if (ValidationObjUtil.isIntType(value.getClass())) {
                long longValue = Long.valueOf(value + "");
                this.createCell(longValue);
            } else if (ValidationObjUtil.isDoubleType(value.getClass())) {
                double doubleValue = Double.valueOf(value + "");
                this.createCell(doubleValue);
            } else {
                this.createCell(value);
            }
        }
    }

    private Cell getNextCell(CellType cellType) {
        int cellCnt = this.getCellCnt();
        Cell cell = this.getLastRow().createCell(cellCnt);
        cell.setCellType(cellType);
        cell.setCellStyle(this.style.getNumberCs());
        return cell;
    }

    public Cell createCell(double value) {

        Cell cell = this.getNextCell(CellType.NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(this.style.getNumberCs());
        return cell;
    }

    public Cell createCell(Long value) {
        Cell cell = this.getNextCell(CellType.NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(this.style.getNumberCs());
        return cell;
    }

    public Cell createCell(long value) {
        Cell cell = this.getNextCell(CellType.NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(this.style.getNumberCs());
        return cell;
    }


    public Cell createCell(Object obj) {

        Cell cell = this.getNextCell(CellType.STRING);
        cell.setCellValue(String.valueOf(obj));
        cell.setCellStyle(this.style.getStringCs());
        return cell;
    }

    public Cell createCell(String str) {

        Cell cell = this.getNextCell(CellType.STRING);
        cell.setCellValue(str);
        cell.setCellStyle(this.style.getStringCs());
        return cell;
    }

    public Cell createCell(Date date) {

        Cell cell = this.getNextCell(CellType.STRING);
        if (date != null) {
            cell.setCellValue(date);
        }
        cell.setCellStyle(this.style.getDateCs());
        return cell;
    }


}
