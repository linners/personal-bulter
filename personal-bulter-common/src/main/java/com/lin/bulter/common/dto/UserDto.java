package com.lin.bulter.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private String userName;
    private String passWord;
    private String encryptPwd;
    private Integer userId;
    private String salt;
    private List<String> roles;

}
