package com.oldlane.cheeseblog.xo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Favorites;
import com.oldlane.cheeseblog.xo.vo.FavoritesVO;

/**
* @author lenovo
* @description 针对表【favorites】的数据库操作Service
* @createDate 2023-03-07 23:46:22
*/
public interface FavoritesService extends IService<Favorites> {

    Result getFavoritesPage(FavoritesVO favoritesVO);
}
