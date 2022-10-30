package com.oldlane.cheeseblog.base.global;

/**
 * Date: 2022/10/9 22:57
 * Description:
 */
public class RedisConstants {
    public static final String LOGIN_KEY_CODE = "login:code:";
    public static final Integer LOGIN_CODE_TTL = 5; //验证码过期时间

    public static final String LOGIN_USER_TOKEN = "login:token:";
    public static final Integer LOGIN_TOKEN_TTL = 30; //token过期时间

}
