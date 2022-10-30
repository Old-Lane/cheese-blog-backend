package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.xo.mapper.ArticleMapper;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【article】的数据库操作Service实现
* @createDate 2022-10-25 16:52:30
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

}




