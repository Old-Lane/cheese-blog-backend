package com.oldlane.cheeseblog.xo.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * Date: 2022/10/17 9:32
 * Description: 导航栏DTO
 */
@Data
public class NavVO {
    private Boolean isLogin;
    private String avatar;
    private String nickname;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long uid;
}
