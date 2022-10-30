package com.oldlane.cheeseblog.xo.vo;

import lombok.Data;

/**
 * Date: 2022/10/9 21:34
 * Description: 登录提交信息
 */
@Data
public class LoginFormVO {
    private String code;

    private String email;

    private String password;

    private String username;
}
