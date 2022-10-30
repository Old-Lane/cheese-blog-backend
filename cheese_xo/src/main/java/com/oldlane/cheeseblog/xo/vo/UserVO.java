package com.oldlane.cheeseblog.xo.vo;

import lombok.Data;

/**
 * Date: 2022/10/9 23:27
 * Description:
 */
@Data
public class UserVO {
    private Long id;
    private String avatar;
    private String nickname;
    private String username;
    private String email;
    private String role;
}
