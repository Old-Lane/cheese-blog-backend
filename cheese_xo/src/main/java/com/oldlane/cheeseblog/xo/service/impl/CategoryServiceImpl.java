package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.commons.entity.Category;
import com.oldlane.cheeseblog.xo.mapper.CategoryMapper;
import com.oldlane.cheeseblog.xo.service.CategoryService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【category】的数据库操作Service实现
* @createDate 2022-11-02 23:21:18
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

}




