package com.oldlane.cheeseblog.web.restapi;

import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Favorites;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.xo.service.FavoritesService;
import com.oldlane.cheeseblog.xo.vo.FavoritesVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oldlane
 * @date 2023/3/7 23:49
 */
@Api(tags = "收藏夹相关接口")
@RestController
@RequestMapping("/favorites")
@Slf4j
public class FavoritesRestApi {
    @Autowired
    private FavoritesService favoritesService;

    @ApiOperation(value = "创建收藏夹", notes = "创建收藏夹")
    @PostMapping("add")
    public Result createFavorites(@RequestBody Favorites favorites) {
        UserVO user = (UserVO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        favorites.setUserId(user.getId());
        favoritesService.save(favorites);
        return Result.ok();
    }

    @ApiOperation(value = "获取用户的收藏夹", notes = "获取用户的收藏夹")
    @PostMapping("get")
    public Result getFavoritesPage(@RequestBody FavoritesVO favoritesVO) {
        return favoritesService.getFavoritesPage(favoritesVO);
    }
}
