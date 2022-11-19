package com.oldlane.cheeseblog.creator.restapi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.enums.EArticleAudit;
import com.oldlane.cheeseblog.base.enums.EArticleStatus;
import com.oldlane.cheeseblog.base.global.BaseMessageConf;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.commons.entity.ArticleTag;
import com.oldlane.cheeseblog.commons.entity.Category;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import com.oldlane.cheeseblog.xo.service.ArticleTagService;
import com.oldlane.cheeseblog.xo.service.CategoryService;
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
import java.util.Objects;

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

    @Autowired
    private CategoryService categoryService;


    /**
     * 保存文章
     *
     * @param articleVO
     * @return
     */
    @ApiOperation("保存文章")
    @PostMapping("/saveArticle")
    @Transactional
    public Result saveArticle(@RequestBody ArticleVO articleVO) {
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Article article = new Article();
        BeanUtil.copyProperties(articleVO, article, "tagList", "categoryName");
        //分类专栏信息
        String categoryName = articleVO.getCategoryName();
        if (StrUtil.isNotBlank(categoryName)) {
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.eq(Category::getName, categoryName);
            Category category = categoryService.getOne(categoryLambdaQueryWrapper);
            if (category != null) {
                article.setCategoryId(category.getId());
            } else {
                Category c = new Category();
                c.setName(categoryName);
                c.setUserId(user.getId());
                categoryService.save(c);
                article.setCategoryId(c.getId());
            }
        }

        article.setAuthor(user.getNickname());
        article.setUserId(user.getId());
        if (Objects.equals(articleVO.getStatus(), EArticleStatus.COMMON)) {
            article.setAudit(EArticleAudit.PASS);
        }
        articleService.saveOrUpdate(article);

        log.info("articleId ====> {}", article.getId());

        //保存标签信息
        List<Long> tagList = articleVO.getTagIdList();
        if (tagList != null) {
            LambdaQueryWrapper<ArticleTag> removeQueryWrapper = new LambdaQueryWrapper<>();
            removeQueryWrapper.eq(ArticleTag::getArticleId, articleVO.getId());
            articleTagService.remove(removeQueryWrapper);
            for (Long tagId : tagList) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagId);
                articleTagService.save(articleTag);
            }
        }
        if (EArticleStatus.DRAFT.equals(articleVO.getStatus())) {
            return Result.ok().message(BaseMessageConf.DRAFT_SUCCESS);
        } else {
            return Result.ok().message(BaseMessageConf.PUBLISH_SUCCESS);
        }
    }

    @ApiOperation(value = "获取指定类型的文章", notes = "获取指定类型的文章")
    @GetMapping("list")
    @Transactional
    public Result getArticleList(@RequestParam(defaultValue = "all", required = false) String type, @RequestParam(required = false, defaultValue = "10") Integer pageSize, @RequestParam(required = false, defaultValue = "1") Integer pageNum) {
        return articleService.getArticleList(type, pageSize, pageNum);
    }
}
