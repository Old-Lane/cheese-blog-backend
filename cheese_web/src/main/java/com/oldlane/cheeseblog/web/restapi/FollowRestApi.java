package com.oldlane.cheeseblog.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.global.BaseMessageConf;
import com.oldlane.cheeseblog.base.global.ECode;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Follow;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.xo.service.FollowService;
import com.oldlane.cheeseblog.xo.service.UsersService;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: oldlane
 * @date: 2022/11/20 21:37
 */
@Api(value = "关注相关接口", tags = {"关注相关接口"})
@RestController
@RequestMapping("/follow")
@Slf4j
public class FollowRestApi {

    @Autowired
    private FollowService followService;

    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "关注用户", notes = "关注用户")
    @PostMapping("follow")
    public Result followUser(@ApiParam(name = "被关注用户id", value = "id") @RequestParam(name = "id") Long id) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return Result.fail().code(ECode.ERROR).message(BaseMessageConf.SYS_LOGIN_ERROR);
        }
        UserVO user = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFansId, user.getId());
        List<Follow> followList = followService.list(followLambdaQueryWrapper);
        if (followList != null) {
            for (Follow follow : followList) {
                if (Objects.equals(follow.getUserId(), id)) {
                    return Result.fail().code(ECode.ERROR).message(BaseMessageConf.HAS_FOLLOWED);
                }
            }
        }
        Follow follow = new Follow();
        follow.setUserId(id);
        follow.setFansId(user.getId());
        followService.save(follow);
        return Result.ok().code(ECode.SUCCESS).message(BaseMessageConf.FOLLOW_SUCCESS);
    }

    @ApiOperation(value = "取关用户", notes = "取关用户")
    @DeleteMapping("unfollow")
    public Result unfollowUser(@ApiParam(name = "被关注用户id", value = "id") @RequestParam(name = "id") Long id) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return Result.fail().code(ECode.ERROR).message(BaseMessageConf.SYS_LOGIN_ERROR);
        }
        UserVO user = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFansId, user.getId());
        List<Follow> followList = followService.list(followLambdaQueryWrapper);
        if (followList != null) {
            for (Follow follow : followList) {
                if (Objects.equals(follow.getUserId(), id)) {
                    LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Follow::getUserId, follow.getUserId()).eq(Follow::getFansId, user.getId());
                    followService.remove(wrapper);
                    return Result.ok().code(ECode.SUCCESS).message(BaseMessageConf.UNFOLLOW_SUCCESS);
                }
            }
        }
        return Result.fail().code(ECode.ERROR).message(BaseMessageConf.HAS_NOT_FOLLOWED);
    }

    @ApiOperation(value = "获取用户的关注", notes = "获取用户的关注")
    @GetMapping("list/Follows")
    public Result getUserFollow(@ApiParam(name = "用户id", value = "uid", required = true) @RequestParam(name = "uid") Long uid) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFansId, uid);
        List<Follow> follows = followService.list(followLambdaQueryWrapper);
        if (follows.size() == 0) {
            return Result.ok().code(ECode.SUCCESS).message(BaseMessageConf.NO_FRIEND);
        }
        List<Map<String, Object>> result = follows.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            Users user = usersService.getById(item.getUserId());
            map.put("id", user.getId());
            map.put("nickname", user.getNickname());
            map.put("avatar", user.getAvatar());
            map.put("profile", user.getProfile());
            map.put("sex", user.getSex());
            //关注信息
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
                map.put("isFollowed", false);
            } else {
                UserVO me = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                LambdaQueryWrapper<Follow> followQueryWrapper = new LambdaQueryWrapper<>();
                followQueryWrapper.eq(Follow::getFansId, me.getId());
                List<Follow> followList = followService.list(followQueryWrapper);
                map.put("isFollowed", false);
                if (followList != null) {
                    for (Follow follow : followList) {
                        if (Objects.equals(follow.getUserId(), item.getUserId())) {
                            map.put("isFollowed", true);
                            break;
                        }
                    }
                }
            }
            return map;
        }).collect(Collectors.toList());
        return Result.ok(result);
    }

    @ApiOperation(value = "获取用户的粉丝", notes = "获取用户的粉丝")
    @GetMapping("list/Fans")
    public Result getUserFans(@ApiParam(name = "用户id", value = "uid", required = true) @RequestParam(name = "uid") Long uid) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, uid);
        List<Follow> follows = followService.list(followLambdaQueryWrapper);
        if (follows.size() == 0) {
            return Result.ok().code(ECode.SUCCESS).message(BaseMessageConf.NO_FANS);
        }
        List<Map<String, Object>> result = follows.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            Users user = usersService.getById(item.getFansId());
            map.put("id", user.getId());
            map.put("nickname", user.getNickname());
            map.put("avatar", user.getAvatar());
            map.put("profile", user.getProfile());
            map.put("sex", user.getSex());
            //关注信息
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
                map.put("isFollowed", false);
            } else {
                UserVO me = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                LambdaQueryWrapper<Follow> followQueryWrapper = new LambdaQueryWrapper<>();
                followQueryWrapper.eq(Follow::getUserId, me.getId());
                List<Follow> followList = followService.list(followQueryWrapper);
                map.put("isFollowed", false);
                if (followList.size() != 0) {
                    for (Follow follow : followList) {
                        if (Objects.equals(follow.getFansId(), item.getFansId())) {
                            map.put("isFollowed", true);
                            break;
                        }
                    }
                }
            }
            return map;
        }).collect(Collectors.toList());
        return Result.ok(result);
    }
}
