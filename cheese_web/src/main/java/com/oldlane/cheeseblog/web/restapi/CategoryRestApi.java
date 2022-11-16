package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.util.StrUtil;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.xo.service.CategoryService;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类相关接口
 * @author: oldlane
 * @date: 2022/11/4 18:28
 */
@Api(value = "分类相关接口", tags = {"分类相关接口"})
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryRestApi {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取用户的分类专栏
     * @return
     */
    @GetMapping("list")
    public Result getCategory(@RequestParam(required = false) Long userId) {
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId != null) {
            return categoryService.getCategory(userId);
        } else {
            return categoryService.getCategory(user.getId());
        }
    }
}
