package com.oldlane.cheeseblog.xo.utils;

import com.oldlane.cheeseblog.xo.vo.UserVO;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author: oldlane
 * @date: 2022/11/2 18:53
 */
public class UserVoUtil {

    public static UserVO getUserInfo() {
        return (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
