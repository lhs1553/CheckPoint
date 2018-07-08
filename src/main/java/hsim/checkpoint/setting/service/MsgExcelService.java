package hsim.checkpoint.setting.service;

import hsim.checkpoint.core.util.excel.PoiWorkBook;

public interface MsgExcelService {
    PoiWorkBook getAllExcels();

    PoiWorkBook getExcel(String method, String url);
}
