package hsim.controller;

import hsim.lib.core.annotation.ValidationBody;
import hsim.lib.core.annotation.ValidationParam;
import hsim.test.model.CommonReqModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/main")
public class MainController extends ParentController {


    @PostMapping("/**")
    public CommonReqModel mainController(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }
}
