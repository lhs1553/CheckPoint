package com.github.ckpoint.spring5.model.user;

import com.github.ckpoint.spring5.model.BaseModel;
import com.github.ckpoint.spring5.model.user.type.Gender;
import com.github.ckpoint.spring5.model.user.type.Membership;
import lombok.Data;

@Data
public class UserModel extends BaseModel {

    private String name;
    private String loginId;
    private String password;

    private Membership membership;
    private String nickName;
    private Gender gender;
    private String email;
    private String contactNumber;
    private String address;
}
