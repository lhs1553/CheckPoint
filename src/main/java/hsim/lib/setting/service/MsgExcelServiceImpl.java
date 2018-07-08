package hsim.lib.setting.service;

import hsim.lib.core.component.ComponentMap;
import hsim.lib.core.component.validationRule.rule.ValidationRule;
import hsim.lib.core.component.validationRule.type.StandardValueType;
import hsim.lib.core.domain.ReqUrl;
import hsim.lib.core.domain.ValidationData;
import hsim.lib.core.repository.ValidationDataRepository;
import hsim.lib.core.store.ValidationStore;
import hsim.lib.core.type.ParamType;
import hsim.lib.core.util.excel.PoiWorkBook;
import hsim.lib.core.util.excel.PoiWorkSheet;
import hsim.lib.core.util.excel.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class MsgExcelServiceImpl implements MsgExcelService {

    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);


    private int createTitleCell(PoiWorkSheet sheet, int maxDeepLevel) {
        sheet.createTitleCell("Name", 1);
        for (int i = 0; i < maxDeepLevel; i++) {
            sheet.createTitleCells("Child" + (i + 1));
        }
        return maxDeepLevel + 1;
    }

    private void createNameValueCell(PoiWorkSheet sheet, ValidationData data, int nameCellCnt) {
        String[] names = new String[nameCellCnt];
        for (int i = 0; i < names.length; i++) {
            names[i] = "";
        }

        for (int i = 0; i < names.length; i++) {
            names[i] = i == data.getDeepLevel() ? data.getName() : i < data.getDeepLevel() ? "└" : "";
        }

        sheet.createValueCells(names);
    }

    private Object getValidationStr(ValidationRule rule) {

        if (rule.getStandardValueType().equals(StandardValueType.NONE)) {
            return rule.isUse() ? "O" : "X";
        }

        return rule.isUse() ? rule.getStandardValue() : "";
    }

    private void createValidationDataRow(PoiWorkSheet sheet, ValidationData data, int nameCellCnt) {
        sheet.nextRow();
        this.createNameValueCell(sheet, data, nameCellCnt);
        sheet.createValueCells(data.getType(), data.getTypeClass().getName());

        data.getValidationRules().forEach(rule -> {
            sheet.createValueCells(this.getValidationStr(rule));
        });

        if (TypeCheckUtil.isObjClass(data.getTypeClass())) {
            List<ValidationData> childs = this.validationDataRepository.findByParentId(data.getId());
            for (ValidationData child : childs) {
                this.createValidationDataRow(sheet, child, nameCellCnt);
            }
        }
    }

    private boolean createParamSheet(PoiWorkSheet sheet, ParamType paramType, ReqUrl reqUrl) {
        List<ValidationData> datas = this.validationDataRepository.findByParamTypeAndUrlAndMethod(paramType, reqUrl.getUrl(), reqUrl.getMethod());
        if (datas.isEmpty()) {
            return false;
        }

        sheet.createTitleCells(1.5, paramType.getAnnotationName());
        sheet.nextRow();

        int maxDeppLevel = datas.stream().map(data -> data.getDeepLevel()).max(Comparator.comparing(Integer::valueOf)).get();
        int nameCellCnt = this.createTitleCell(sheet, maxDeppLevel);
        sheet.createTitleCells(1.5, "Type", "TypeClass");

        datas.get(0).getValidationRules().forEach(rule -> {
            sheet.createTitleCells(1.5, rule.getRuleName());
        });

        datas.stream().filter(d -> d.getDeepLevel() < 1).forEach(d -> this.createValidationDataRow(sheet, d, nameCellCnt));
        sheet.nextRow(4);
        return true;
    }

    private void createReqUrlSheet(PoiWorkBook workBook, ReqUrl reqUrl) {
        PoiWorkSheet sheet = workBook.createSheet(reqUrl.getMethod() + reqUrl.getUrl().replaceAll("/", "|"));

        sheet.nextRow();
        sheet.createTitleCells(1.5, reqUrl.getMethod(), reqUrl.getUrl());
        sheet.nextRow(2);

        for (ParamType paramType : ParamType.values()) {
            if (this.createParamSheet(sheet, paramType, reqUrl)) {
                sheet.nextRow(3);
            }
        }
    }


    @Override
    public PoiWorkBook getAllExcels() {
        PoiWorkBook workBook = new PoiWorkBook();

        List<ReqUrl> reqUrls = this.validationDataRepository.findAllUrl();
        for (ReqUrl reqUrl : reqUrls) {
            this.createReqUrlSheet(workBook, reqUrl);
        }
        return workBook;
    }

    @Override
    public PoiWorkBook getExcel(String method, String url) {
        PoiWorkBook workBook = new PoiWorkBook();
        this.createReqUrlSheet(workBook, new ReqUrl(url, method));
        return workBook;
    }
}
