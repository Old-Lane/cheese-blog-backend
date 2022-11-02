package com.oldlane.cheeseblog.creator.restapi;

import cn.hutool.core.bean.BeanUtil;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.commons.entity.ArticleTag;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import com.oldlane.cheeseblog.xo.service.ArticleTagService;
import com.oldlane.cheeseblog.xo.service.TagService;
import com.oldlane.cheeseblog.xo.vo.ArticleVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: oldlane
 * @date: 2022/11/2 16:23
 */
@RestController
@RequestMapping("/article")
@Api(value = "博客相关接口", tags = {"博客相关接口"})
@Slf4j
public class ArticleRestApi {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagService articleTagService;


    @ApiOperation("保存至草稿箱")
    @PostMapping("/draft")
    @Transactional
    public Result draft(@RequestBody ArticleVO articleVO) {
        Article article = new Article();
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeanUtil.copyProperties(articleVO, article, "tagList");
        article.setState(0);
        article.setAuthor(user.getNickname());
        article.setUserId(user.getId());
        articleService.save(article);

        //保存标签信息
        List<Long> tagList = articleVO.getTagIdList();
        if (tagList != null) {
            for (Long tagId : tagList) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagId);
                articleTagService.save(articleTag);
            }
        }

        return Result.ok().message("成功保存至草稿箱");
    }
}
