package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.util.StrUtil;
import com.oldlane.cheeseblog.base.global.BaseMessageConf;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "获取所有文章", notes = "获取所有文章")
    @GetMapping("list")
    public Result list(@ApiParam(name="排序规则", value = "sort") @RequestParam(name = "sort", defaultValue = "recommend") String sort,  @ApiParam(name="页码", value = "page") @RequestParam(name = "page", defaultValue = "1") Integer page, @ApiParam(name="页大小", value = "pageSize") @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, @ApiParam(name="用户id", value = "uid") @RequestParam(name = "uid", required = false) Long uid) {
        return articleService.listAll(sort, uid, page, pageSize);
    }

    @ApiOperation(value = "用户给文章点赞", notes = "用户给文章点赞")
    @PutMapping("like")
    public Result parseArticleByAid(@ApiParam(name = "文章id", value = "aid", required = false) @RequestParam(name = "aid", required = false) Long aid) {
        if (aid == null) {
            return Result.fail().message(BaseMessageConf.SYS_PARAMS_ERROR);
        }
        return articleService.parseArticleByAid(aid);
    }

    @ApiOperation(value = "根据文章id获取相关文章", notes = "根据文章id获取相关文章")
    @GetMapping("getAbout")
    @Transactional
    public Result getAboutById(@ApiParam(name = "id", value = "文章id") @RequestParam Long id) {
        return articleService.getAboutById(id);
    }
}
