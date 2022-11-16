package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.bean.BeanUtil;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.utils.JwtUtil;
import com.oldlane.cheeseblog.xo.vo.NavVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.oldlane.cheeseblog.base.global.RedisConstants.LOGIN_USER_TOKEN;

/**
 * @author: oldlane
 * @date: 2022/10/31 10:29
 */
@RestController
@Slf4j
@Api(tags = "首页相关接口")
@RequestMapping("/index")
public class IndexRestApi {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/nav")
    public Result nav() {

        UserVO user = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("userVO ===> {}", user);

        NavVO navVO = new NavVO();
        log.info("nav:userVo => {}", user);
        if (user == null) {
            navVO.setIsLogin(false);
            return Result.ok(navVO).message("用户未登录").code(-101);
        }
        navVO.setIsLogin(true);
        navVO.setNickname(user.getNickname());
        navVO.setAvatar(user.getAvatar());
        navVO.setUid(user.getId());
        return Result.ok(navVO);
    }
}
