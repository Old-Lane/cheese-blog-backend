package com.oldlane.cheeseblog.xo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.commons.entity.Comment;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.xo.mapper.CommentMapper;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import com.oldlane.cheeseblog.xo.service.CommentService;
import com.oldlane.cheeseblog.xo.service.UsersService;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: oldlane
 * @date: 2022/11/15 8:58
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UsersService usersService;

    @Override
    public Result getMyNewComment() {
        //从security中获取自己信息
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getUserId, user.getId());
        List<Article> articles = articleService.list(queryWrapper);
        List<Comment> result = new ArrayList<>();
        articles.forEach(item -> {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getArticleId, item.getId()).ne(Comment::getUserId, user.getId());
            List<Comment> commentList = this.list(wrapper);
            commentList.forEach(comment -> {
                Long userId = comment.getUserId();
                Users user1 = usersService.getById(userId);
                UserVO userVO = new UserVO();
                BeanUtil.copyProperties(user1, userVO);
                comment.setUser(userVO);
            });
            result.addAll(commentList);
        });
        result.sort((t1, t2) -> t2.getCreateTime().compareTo(t1.getCreateTime()));
        return Result.ok(result);
    }
}
