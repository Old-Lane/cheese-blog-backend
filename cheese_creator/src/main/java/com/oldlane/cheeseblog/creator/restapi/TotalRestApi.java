package com.oldlane.cheeseblog.creator.restapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.commons.entity.Follow;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import com.oldlane.cheeseblog.xo.service.FollowService;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author oldlane
 * @date 2023/3/5 16:59
 */
@Api(value = "总数相关接口", tags = {"总数相关接口"})
@RestController
@RequestMapping("/total")
@Slf4j
public class TotalRestApi {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private FollowService followService;

    @ApiOperation(value = "获取数据", notes = "获取数据")
    @PostMapping("get")
    public Result getTotal() {
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getUserId, user.getId());
        long articleCount = articleService.count(articleWrapper);
        List<Article> articles = articleService.list(articleWrapper);
        AtomicLong commentCount = new AtomicLong();
        AtomicLong collectCount = new AtomicLong();
        articles.forEach(item -> {
            commentCount.addAndGet(item.getCommentCount());
            collectCount.addAndGet(item.getCollectCount());
        });

        LambdaQueryWrapper<Follow> followWrapper = new LambdaQueryWrapper<>();
        followWrapper.eq(Follow::getUserId, user.getId());
        long followCount = followService.count(followWrapper);
        HashMap<String, String> map = new HashMap<>();
        map.put("articleCount", String.valueOf(articleCount));
        map.put("commentCount", String.valueOf(commentCount));
        map.put("collectCount", String.valueOf(collectCount));
        map.put("followCount", String.valueOf(followCount));
        return Result.ok(map);
    }

    @ApiOperation(value = "获取12个月份文章数量", notes = "获取12个月份文章数量")
    @PostMapping("month")
    public Result getMonthArticle() {
        return null;
    }
}
