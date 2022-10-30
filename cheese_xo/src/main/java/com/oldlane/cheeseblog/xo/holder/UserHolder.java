package com.oldlane.cheeseblog.xo.holder;

import com.oldlane.cheeseblog.xo.vo.UserVO;

/**
 * Date: 2022/10/10 8:55
 * Description: threadLocal操作
 */
public class UserHolder {
    private static final ThreadLocal<UserVO> tl = new ThreadLocal<>();

    public static void saveUser(UserVO user){
        tl.set(user);
    }

    public static UserVO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
