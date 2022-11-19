package com.oldlane.cheeseblog.xo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.base.enums.*;
import com.oldlane.cheeseblog.base.global.BaseMessageConf;
import com.oldlane.cheeseblog.base.global.ECode;
import com.oldlane.cheeseblog.base.global.RedisConstants;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.*;
import com.oldlane.cheeseblog.xo.mapper.ArticleMapper;
import com.oldlane.cheeseblog.xo.service.*;
import com.oldlane.cheeseblog.xo.vo.ArticleVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【article】的数据库操作Service实现
* @createDate 2022-10-25 16:52:30
*/
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result getArticleList(String type, Integer pageSize, Integer pageNum) {
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        Page<Article> pageInfo = new Page<>(pageNum, pageSize);
        Page<ArticleVO> articleVOPage = new Page<>();
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();

        switch (type) {
            case EArticleType.ALL:
                break;
            case EArticleType.PUBLIC:
                articleQueryWrapper.eq(Article::getStatus, EArticleStatus.COMMON).eq(Article::getPermission, EArticlePermission.PUBLIC).eq(Article::getAudit, EArticleAudit.PASS);
                break;
            case EArticleType.PRIVATE:
                articleQueryWrapper.eq(Article::getStatus, EArticleStatus.COMMON).eq(Article::getPermission, EArticlePermission.PRIVATE).eq(Article::getAudit, EArticleAudit.PASS);
                break;
            case EArticleType.AUDIT:
                articleQueryWrapper.eq(Article::getAudit, EArticleAudit.AUDITING);
                break;
            case EArticleType.DRAFT:
                articleQueryWrapper.eq(Article::getStatus, EArticleStatus.DRAFT);
                break;
            case EArticleType.RECYCLE:
                articleQueryWrapper.eq(Article::getStatus, EArticleStatus.RECYCLE);
                break;
        }
        articleQueryWrapper.eq(Article::getUserId, userId);
        Page<Article> page = this.page(pageInfo, articleQueryWrapper);
        BeanUtil.copyProperties(page, articleVOPage, "records");
        List<Article> records = page.getRecords();

        List<ArticleVO> newRecords = records.stream().map(item -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtil.copyProperties(item, articleVO, "categoryId", "content");
            Long categoryId = item.getCategoryId();
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                articleVO.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, item.getId());
            List<ArticleTag> articleTagList = articleTagService.list(articleTagLambdaQueryWrapper);
            if (articleTagList != null) {
                List<Long> tagList = articleTagList.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
                articleVO.setTagIdList(tagList);
            }
            return articleVO;
        }).collect(Collectors.toList());

//        articleVOPage.setRecords(newRecords);
        Map count = getCount(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("list", newRecords);
        result.put("pageInfo", articleVOPage);
        return Result.ok(result);
    }

    @Override
    public Result getArticleById(Long id) {
        Article article = this.getById(id);
        ArticleVO articleVO = new ArticleVO();
        BeanUtil.copyProperties(article, articleVO, "categoryId");
        Long categoryId = article.getCategoryId();
        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            articleVO.setCategoryName(category.getName());
        }
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, article.getId());
        List<ArticleTag> articleTagList = articleTagService.list(articleTagLambdaQueryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        if (articleTagList != null) {
            List<Long> tagIdList = articleTagList.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
            articleVO.setTagIdList(tagIdList);
            List<Tag> tagList = tagIdList.stream().map(item -> tagService.getById(item)).collect(Collectors.toList());
            map.put("tagInfo", tagList);
        }
        articleVO.setIsLiked(isLiked(id));
        map.put("articleInfo", articleVO);
        return Result.ok(map);
    }

    @Override
    public Result listAll(String sort, Long uid, Integer page, Integer pageSize) {
        Page<Article> articlePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
        if (uid != null) {
            articleQueryWrapper.eq(Article::getUserId, uid);
        }
        articleQueryWrapper.eq(Article::getPermission, EArticlePermission.PUBLIC).eq(Article::getStatus, EArticleStatus.COMMON).eq(Article::getAudit, EArticleAudit.PASS);
        switch (sort) {
            case EArticleSort.RECOMMEND:
                articleQueryWrapper.orderByDesc(Article::getCreateTime);
                break;
            case EArticleSort.NEWEST:
                articleQueryWrapper.orderByDesc(Article::getCreateTime);
                break;
            case EArticleSort.HOTTEST:
                articleQueryWrapper.orderByDesc(Article::getLikeCount);
        }
        this.page(articlePage, articleQueryWrapper);
        Page<Map<String, Object>> resultPage = new Page<>();
        BeanUtil.copyProperties(articlePage, resultPage, "records");
        List<Article> articleList = articlePage.getRecords();
        List<String> avatarResult = articleList.stream().map(item -> {
            item.setIsLiked(isLiked(item.getId()));
            Long userId = item.getUserId();
            Users user = usersService.getById(userId);
            return user.getAvatar();
        }).collect(Collectors.toList());
        List<List<Tag>> tagResult = articleList.stream().map(item -> {
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, item.getId());
            List<ArticleTag> articleTagList = articleTagService.list(articleTagLambdaQueryWrapper);
            if (articleTagList != null) {
                List<Long> tagIdList = articleTagList.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
                List<Tag> tagList = tagIdList.stream().map(item1 -> tagService.getById(item1)).collect(Collectors.toList());
                return tagList;
            }
            return null;
        }).collect(Collectors.toList());
        HashMap<String, Object> result = new HashMap<>();
        result.put("article", articleList);
        result.put("tagInfo", tagResult);
        result.put("avatar", avatarResult);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(result);
        resultPage.setRecords(list);
        return Result.ok(resultPage);
    }

    @Override
    public Result parseArticleByAid(Long aid) {
        log.info("当前登录==> {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return Result.fail().code(ECode.ERROR).message(BaseMessageConf.SYS_LOGIN_ERROR);
        }
        UserVO user = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(RedisConstants.ARTICLE_LIKED + aid, user.getId().toString());
        if (Boolean.TRUE.equals(isLiked)) {
            //已经点赞
            boolean isSuccess = update().setSql("like_count = like_count - 1").eq("id", aid).update();
            if (isSuccess) {
                stringRedisTemplate.opsForSet().remove(RedisConstants.ARTICLE_LIKED + aid, user.getId().toString());
            }
        } else {
            //没有点赞
            boolean isSuccess = update().setSql("like_count = like_count + 1").eq("id", aid).update();
            if (isSuccess) {
                stringRedisTemplate.opsForSet().add(RedisConstants.ARTICLE_LIKED + aid, user.getId().toString());
            }
        }
        return Result.ok().code(ECode.SUCCESS);
    }

    /**
     * 获取各种文章类型的数量
     * @return
     */
    public Map getCount(Long id) {
        HashMap<String, Object> counts = new HashMap<>();

        List<String> typeList = new ArrayList<>(Arrays.asList(EArticleType.ALL, EArticleType.PUBLIC, EArticleType.PRIVATE, EArticleType.AUDIT, EArticleType.DRAFT, EArticleType.RECYCLE));
        for (String type : typeList) {
            LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
            switch (type) {
                case EArticleType.ALL:
                    break;
                case EArticleType.PUBLIC:
                    articleQueryWrapper.eq(Article::getStatus, EArticleStatus.COMMON).eq(Article::getPermission, EArticlePermission.PUBLIC).eq(Article::getAudit, EArticleAudit.PASS);
                    break;
                case EArticleType.PRIVATE:
                    articleQueryWrapper.eq(Article::getStatus, EArticleStatus.COMMON).eq(Article::getPermission, EArticlePermission.PRIVATE).eq(Article::getAudit, EArticleAudit.PASS);
                    break;
                case EArticleType.AUDIT:
                    articleQueryWrapper.eq(Article::getAudit, EArticleAudit.AUDITING);
                    break;
                case EArticleType.DRAFT:
                    articleQueryWrapper.eq(Article::getStatus, EArticleStatus.DRAFT);
                    break;
                case EArticleType.RECYCLE:
                    articleQueryWrapper.eq(Article::getStatus, EArticleStatus.RECYCLE);
                    break;
            }
            articleQueryWrapper.eq(Article::getUserId, id);
            long count = this.count(articleQueryWrapper);
            counts.put(type, count);
        }
        return counts;
    }

    /**
     * 判断文章是否已点赞
     * @param aid
     * @return
     */
    public Boolean isLiked(Long aid) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return false;
        }
        UserVO user = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(RedisConstants.ARTICLE_LIKED + aid, user.getId().toString());
        return isLiked;
    }
}




