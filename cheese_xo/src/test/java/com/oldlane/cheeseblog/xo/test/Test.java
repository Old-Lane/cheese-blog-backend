package com.oldlane.cheeseblog.xo.test;

import com.oldlane.cheeseblog.xo.utils.UserVoUtil;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: oldlane
 * @date: 2022/11/2 18:54
 */
//@SpringBootTest
public class Test {

    @org.junit.jupiter.api.Test
    public void test() {
        UserVO userInfo = UserVoUtil.getUserInfo();
        System.out.println(userInfo);
    }
}
