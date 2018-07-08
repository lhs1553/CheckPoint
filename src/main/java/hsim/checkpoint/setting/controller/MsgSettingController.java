package hsim.checkpoint.setting.controller;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.config.ValidationConfig;
import hsim.checkpoint.core.config.ValidationIntercepterConfig;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.filter.ValidationCorsFilter;
import hsim.checkpoint.core.init.InitCheckPoint;
import hsim.checkpoint.core.util.ParameterMapper;
import hsim.checkpoint.core.util.ValidationFileUtil;
import hsim.checkpoint.core.util.excel.PoiWorkBook;
import hsim.checkpoint.exception.resolver.ValidationExceptionResolver;
import hsim.checkpoint.setting.service.MsgExcelService;
import hsim.checkpoint.setting.service.MsgExcelServiceImpl;
import hsim.checkpoint.setting.service.MsgSettingService;
import hsim.checkpoint.setting.service.MsgSettingServiceImpl;
import hsim.checkpoint.setting.session.ValidationLoginInfo;
import hsim.checkpoint.setting.session.ValidationSessionComponent;
import hsim.checkpoint.setting.session.ValidationSessionInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

public class MsgSettingController {

    private MsgSettingService msgSettingService = ComponentMap.get(MsgSettingServiceImpl.class);
    private MsgExcelService msgExcelService = ComponentMap.get(MsgExcelServiceImpl.class);
    private ValidationSessionComponent validationSessionComponent = ComponentMap.get(ValidationSessionComponent.class);

    @PostMapping("/setting/auth")
    public ValidationSessionInfo login(@RequestBody ValidationLoginInfo info, HttpServletRequest req, HttpServletResponse res) {
        return this.validationSessionComponent.checkAuth(info, req, res);
    }

    @GetMapping("/setting/download/api/json/all")
    public void downloadApiJsonAll(HttpServletRequest req, HttpServletResponse res) {
        //this.validationSessionComponent.sessionCheck(req);
        List<ValidationData> list = this.msgSettingService.getAllValidationData();
        ValidationFileUtil.sendFileToHttpServiceResponse("validation.json", list, res);
    }

    @GetMapping("/setting/download/api/json")
    public void downloadApiJson(HttpServletRequest req, @RequestParam("method") String method, @RequestParam("url") String url, HttpServletResponse res) {
        //this.validationSessionComponent.sessionCheck(req);
        url = new String(Base64.getDecoder().decode(url));
        List<ValidationData> list = this.msgSettingService.getValidationData(method, url);
        ValidationFileUtil.sendFileToHttpServiceResponse(method + url.replaceAll("/", "-") + ".json", list, res);
    }

    @GetMapping("/setting/download/api/excel/all")
    public void downloadApiAll(HttpServletRequest req, HttpServletResponse res) {
        //this.validationSessionComponent.sessionCheck(req);
        PoiWorkBook workBook = this.msgExcelService.getAllExcels();
        workBook.writeFile("ValidationApis_" + System.currentTimeMillis(), res);
    }

    @GetMapping("/setting/download/api/excel")
    public void downloadApi(HttpServletRequest req, @RequestParam("method") String method, @RequestParam("url") String url, HttpServletResponse res) {
        //this.validationSessionComponent.sessionCheck(req);
        url = new String(Base64.getDecoder().decode(url));
        PoiWorkBook workBook = this.msgExcelService.getExcel(method, url);
        workBook.writeFile("ValidationApis_" + System.currentTimeMillis(), res);
    }

    @PostMapping("/setting/upload/json")
    public void uploadSetting(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.updateValidationData((MultipartHttpServletRequest) req);
    }


    @GetMapping("/setting/url/list/all")
    public List<ReqUrl> reqUrlAllList(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        return this.msgSettingService.getAllUrlList();
    }

    @GetMapping("/setting/param/from/url")
    public List<ValidationData> getValidationDataLists(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        ValidationData data = ParameterMapper.requestParamaterToObject(req, ValidationData.class, "UTF-8");
        return this.msgSettingService.getValidationData(data.getParamType(), data.getMethod(), data.getUrl());
    }

    @PostMapping("/setting/update/param/from/url")
    public void updateValidationDataLists(HttpServletRequest req, @RequestBody List<ValidationData> datas) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.updateValidationData(datas);
    }

    @DeleteMapping("/setting/delete/param/from/url")
    public void deleteValidationDataLists(HttpServletRequest req, @RequestBody List<ValidationData> datas) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.deleteValidationData(datas);
    }

    @DeleteMapping("/setting/delete/url")
    public void deleteValidationUrl(HttpServletRequest req, @RequestBody ReqUrl reqUrl) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.deleteValidationData(reqUrl);
    }


    @Bean
    public ValidationExceptionResolver validationExceptionResolver() {
        return ComponentMap.get(ValidationExceptionResolver.class);
    }

    @Bean
    public ValidationIntercepterConfig intercepterConfig() {
        return ComponentMap.get(ValidationIntercepterConfig.class);
    }

    @Bean
    public ValidationCorsFilter validationCorsFilter() {
        return ComponentMap.get(ValidationCorsFilter.class);
    }

    @Bean
    public ValidationConfig validationConfig() {
        return ComponentMap.get(ValidationConfig.class);
    }

    @Bean
    public InitCheckPoint initCheckPoint() {
       return ComponentMap.get(InitCheckPoint.class) ;
    }

}
