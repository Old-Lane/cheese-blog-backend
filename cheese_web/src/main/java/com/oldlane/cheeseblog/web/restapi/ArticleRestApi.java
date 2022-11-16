package com.oldlane.cheeseblog.web.restapi;

import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: oldlane
 * @date: 2022/11/5 20:43
 */
@Api(value = "文章相关接口", tags = {"文章相关接口"})
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleRestApi {
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "根据文章id获取文章", notes = "根据文章id获取文章")
    @GetMapping("getArticle")
    @Transactional
    public Result getArticleById(@ApiParam(name = "id", value = "文章id") @RequestParam Long id) {
        return articleService.getArticleById(id);
    }
}
