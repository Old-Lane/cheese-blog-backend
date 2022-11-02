package com.oldlane.cheeseblog.web.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.oldlane.cheeseblog.utils.JwtUtil;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.oldlane.cheeseblog.base.global.RedisConstants.LOGIN_USER_TOKEN;

/**
 * @author: oldlane
 * @date: 2022/10/31 9:33
 */
@Slf4j
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //得到请求头信息authorization信息
        String accessToken = request.getHeader("Authorization");
        log.info("accessToken=> {}", accessToken);
        if (StrUtil.isBlank(accessToken)) {
            //为空放行
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = JwtUtil.parseJWT(accessToken);
        assert claims != null;
        String subject = claims.get("user_open_id", String.class);
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(LOGIN_USER_TOKEN + subject);
        if (userMap.isEmpty()) {
            response.setStatus(401);
            filterChain.doFilter(request, response);
            return;
        }
        UserVO userVO = BeanUtil.fillBeanWithMap(userMap, new UserVO(), true);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userVO, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
