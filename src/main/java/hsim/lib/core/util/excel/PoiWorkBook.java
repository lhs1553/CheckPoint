package hsim.lib.core.util.excel;

import hsim.lib.core.util.ValidationFileUtil;
import hsim.lib.exception.ValidationLibException;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
public class PoiWorkBook {

    private HSSFWorkbook workBook;

    public PoiWorkBook() {
        this.workBook = new HSSFWorkbook();
    }

    public PoiWorkSheet createSheet() {
        return new PoiWorkSheet(this, null);
    }

    public PoiWorkSheet createSheet(String sheetName) {
        return new PoiWorkSheet(this, sheetName);
    }

    public void writeFile(String fn, HttpServletResponse res) {

        ValidationFileUtil.initFileSendHeader(res, ValidationFileUtil.getEncodingFileName(fn + ".xls"), null);

        try {
            this.workBook.write(res.getOutputStream());
        } catch (IOException e) {
            throw new ValidationLibException("workbook write error  : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


}
