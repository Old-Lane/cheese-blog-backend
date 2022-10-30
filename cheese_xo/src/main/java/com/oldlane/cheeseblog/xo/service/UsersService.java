package com.oldlane.cheeseblog.xo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.xo.vo.LoginFormVO;

/**
* @author lenovo
* @description 针对表【users】的数据库操作Service
* @createDate 2022-10-09 15:33:27
*/
public interface UsersService extends IService<Users> {

    Result login(LoginFormVO loginForm);

    Result sendCode(String email);

    Result logout(String token);
}
