package hsim.checkpoint.setting.service;

import hsim.checkpoint.util.excel.PoiWorkBook;

public interface MsgExcelService {
    PoiWorkBook getAllExcels();

    PoiWorkBook getExcel(String method, String url);
}
