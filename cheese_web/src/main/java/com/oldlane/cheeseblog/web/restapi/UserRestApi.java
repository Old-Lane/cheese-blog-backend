package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.global.BaseMessageConf;
import com.oldlane.cheeseblog.base.global.ECode;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.*;
import com.oldlane.cheeseblog.xo.service.*;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author: oldlane
 * @date: 2022/11/14 1:15
 */
@Api(value = "用户相关接口", tags = {"用户相关接口"})
@RestController
@RequestMapping("/user")
@Slf4j
public class UserRestApi {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private FollowService followService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/getInfo")
    public Result getInfoById() {
        UserVO userVO = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.ok(userVO);
    }

    @ApiOperation(value = "获取个人信息卡片", notes = "获取个人信息卡片")
    @GetMapping("card")
    public Result getUserCardInfo(@ApiParam(name="id", value = "需要获取人的id") @RequestParam(name = "id") Long id) {
        HashMap<String, Object> result = new HashMap<>();
        Users user = usersService.getById(id);
        result.put("id", user.getId());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("profile", user.getProfile());
        result.put("sex", user.getSex());
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, id);
        //粉丝数量
        Long fans = followService.count(followLambdaQueryWrapper);
        result.put("fans", fans);
        LambdaQueryWrapper<Follow> friendQueryWrapper = new LambdaQueryWrapper<>();
        friendQueryWrapper.eq(Follow::getFansId, id);
        //关注数量
        Long friends = followService.count(friendQueryWrapper);
        result.put("friends", friends);
        //文章相关数量
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getUserId, id);
        List<Article> articles = articleService.list(articleLambdaQueryWrapper);
        Long likeCount = 0L;
        Long commentCount = 0L;
        Long collectCount = 0L;
        Long viewCount = 0L;
        for (Article article : articles) {
            likeCount += article.getLikeCount();
            commentCount += article.getCommentCount();
            collectCount += article.getCollectCount();
            viewCount += article.getViews();
        }
        result.put("likeCount", likeCount);
        result.put("commentCount", commentCount);
        result.put("collectCount", collectCount);
        result.put("viewCount", viewCount);
        result.put("articleCount", (long) articles.size());
        result.put("createTime", user.getCreateTime());
        //关注信息
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            result.put("isFollowed", false);
        } else {
            UserVO me = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LambdaQueryWrapper<Follow> followQueryWrapper = new LambdaQueryWrapper<>();
            followQueryWrapper.eq(Follow::getFansId, me.getId());
            List<Follow> followList = followService.list(followQueryWrapper);
            result.put("isFollowed", false);
            if (followList.size() != 0) {
                for (Follow follow : followList) {
                    if (Objects.equals(follow.getUserId(), id)) {
                        result.put("isFollowed", true);
                        break;
                    }
                }
            }
        }
        return Result.ok(result);
    }

    @ApiOperation(value = "获取个人信息详细")
    @GetMapping("detail")
    public Result getUserInfoDetail() {
        UserVO userVO = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.getById(userVO.getId());
        return Result.ok(user);
    }

    @ApiOperation(value = "修改个人信息详细")
    @GetMapping("modify")
    public Result modify(@RequestBody Users user) {
        usersService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "获取前十作者信息")
    @GetMapping("top10")
    public Result getAuthorTop10() {
        return usersService.getAuthorTop10();
    }

}
