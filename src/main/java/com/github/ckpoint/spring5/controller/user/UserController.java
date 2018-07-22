package com.github.ckpoint.spring5.controller.user;

import com.github.ckpoint.spring5.model.BaseModel;
import com.github.ckpoint.spring5.model.user.UserModel;
import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("/find")
    public UserModel findUser(@ValidationParam BaseModel findModel) {
        UserModel userModel = new UserModel();
        userModel.setId(findModel.getId());
        return userModel;
    }

    @PostMapping("/join")
    public UserModel joinUser(@ValidationBody UserModel userModel) {
        return userModel;
    }


    @PostMapping("/update")
    public UserModel updateUser(@ValidationBody UserModel userModel) {
        return userModel;
    }

    @DeleteMapping("/leave")
    public void leaveUser(@ValidationParam UserModel userModel) {
    }


}
