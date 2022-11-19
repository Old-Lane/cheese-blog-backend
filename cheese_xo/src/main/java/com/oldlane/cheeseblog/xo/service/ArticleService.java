package com.oldlane.cheeseblog.xo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;

/**
* @author lenovo
* @description 针对表【article】的数据库操作Service
* @createDate 2022-10-25 16:52:30
*/
public interface ArticleService extends IService<Article> {

    Result getArticleList(String type, Integer pageSize, Integer pageNum);

    Result getArticleById(Long id);

    Result listAll(String sort, Long uid, Integer page, Integer pageSize);

    Result parseArticleByAid(Long aid);
}
