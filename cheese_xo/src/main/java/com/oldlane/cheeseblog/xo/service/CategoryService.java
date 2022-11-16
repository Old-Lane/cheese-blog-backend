package com.oldlane.cheeseblog.xo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Category;

/**
* @author lenovo
* @description 针对表【category】的数据库操作Service
* @createDate 2022-11-02 23:21:18
*/
public interface CategoryService extends IService<Category> {

    Result getCategory(Long userId);
}
