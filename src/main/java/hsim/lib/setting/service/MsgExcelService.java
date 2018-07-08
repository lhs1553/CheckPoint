package hsim.lib.setting.service;

import hsim.lib.core.util.excel.PoiWorkBook;

public interface MsgExcelService {
    PoiWorkBook getAllExcels();

    PoiWorkBook getExcel(String method, String url);
}
