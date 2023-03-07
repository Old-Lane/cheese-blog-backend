package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Favorites;
import com.oldlane.cheeseblog.xo.mapper.FavoritesMapper;
import com.oldlane.cheeseblog.xo.service.FavoritesService;
import com.oldlane.cheeseblog.xo.vo.FavoritesVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lenovo
 * @description 针对表【favorites】的数据库操作Service实现
 * @createDate 2023-03-07 23:46:22
 */
@Service
@Slf4j
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites>
        implements FavoritesService {

    @Override
    public Result getFavoritesPage(FavoritesVO favoritesVO) {
        Page<Favorites> favoritesPage = new Page<>(favoritesVO.getCurrentPage(), favoritesVO.getPageSize());
        LambdaQueryWrapper<Favorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorites::getUserId, favoritesVO.getUserId());
        log.info("SecurityContextHolder=> {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UserVO user = new UserVO();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser" || !Objects.equals(favoritesVO.getUserId(), user.getId())) {
            queryWrapper.eq(Favorites::getVisibility, 1);
        }
        if (favoritesVO.getKeyword() != null) {
            queryWrapper.like(Favorites::getName, favoritesVO.getKeyword());
        }
        queryWrapper.orderByDesc(Favorites::getCreateTime);
        Page<Favorites> page = this.page(favoritesPage, queryWrapper);
        return Result.ok(page);
    }
}




