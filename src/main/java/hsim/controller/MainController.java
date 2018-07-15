package hsim.controller;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
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
public class MainController {

    @PostMapping("/bcdg")
    public CommonReqModel mainControllerbcdeg(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }

    @PostMapping("/abcdb")
    public CommonReqModel mainContrbba(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }


    @PostMapping("/bcd")
    public CommonReqModel mainControllera(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }
}
