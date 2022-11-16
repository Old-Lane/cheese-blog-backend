package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Follow;
import com.oldlane.cheeseblog.commons.entity.Role;
import com.oldlane.cheeseblog.commons.entity.UserRole;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.xo.service.FollowService;
import com.oldlane.cheeseblog.xo.service.RoleService;
import com.oldlane.cheeseblog.xo.service.UserRoleService;
import com.oldlane.cheeseblog.xo.service.UsersService;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

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

    @GetMapping("/getInfo")
    public Result getInfoById() {
        UserVO userVO = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.ok(userVO);
    }

    @ApiOperation(value = "获取个人信息卡片", notes = "获取个人信息卡片")
    @GetMapping("card")
    public Result getUserCardInfo(@ApiParam(name="id", value = "需要获取人的id") @RequestParam(value = "id") Long id) {
        HashMap<String, Object> result = new HashMap<>();
        Users user = usersService.getById(id);
        result.put("id", user.getId());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, id);
        //粉丝数量
        Long fans = followService.count(followLambdaQueryWrapper);
        result.put("fans", fans);
        LambdaQueryWrapper<Follow> friendQueryWrapper = new LambdaQueryWrapper<>();
        friendQueryWrapper.eq(Follow::getFansId, id);
        Long friends = followService.count(friendQueryWrapper);
        result.put("friends", friends);
        return Result.ok(result);
    }
}
