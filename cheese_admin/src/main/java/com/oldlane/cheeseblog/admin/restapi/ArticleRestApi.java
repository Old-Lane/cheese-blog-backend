package com.oldlane.cheeseblog.admin.restapi;

import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: oldlane
 * @date: 2022/10/31 10:51
 */
@RestController
@Slf4j
@Api(value = "博客相关接口", tags = {"博客相关接口"})
public class ArticleRestApi {
    @Autowired
    private ArticleService articleService;

    @ApiOperation("保存至草稿箱")
    @PostMapping("/draft")
    public Result draft(@RequestParam String content) {
        Article article = new Article();
        article.setContent(content);
        article.setState(0);
        articleService.save(article);
        return Result.ok().message("成功保存至草稿箱");
    }
}
