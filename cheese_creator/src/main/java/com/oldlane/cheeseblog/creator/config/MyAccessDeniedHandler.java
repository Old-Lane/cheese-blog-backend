package com.oldlane.cheeseblog.creator.config;

import com.alibaba.fastjson.JSONObject;
import com.oldlane.cheeseblog.base.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: oldlane
 * @date: 2022/11/14 0:23
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // TODO Auto-generated method stub
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 403);
        map.put("message", "没有权限访问");
        map.put("data", null);
//        response.getWriter().write(JSONObject.toJSONString(map));
        response.getWriter().print(JSONObject.toJSON(map));
    }

}
