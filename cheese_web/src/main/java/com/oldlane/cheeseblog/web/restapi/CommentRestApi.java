package com.oldlane.cheeseblog.web.restapi;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldlane.cheeseblog.base.enums.EArticleCommentEnable;
import com.oldlane.cheeseblog.base.enums.ECommentSort;
import com.oldlane.cheeseblog.base.global.BaseMessageConf;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Article;
import com.oldlane.cheeseblog.commons.entity.Comment;
import com.oldlane.cheeseblog.commons.entity.Users;
import com.oldlane.cheeseblog.xo.service.ArticleService;
import com.oldlane.cheeseblog.xo.service.CommentService;
import com.oldlane.cheeseblog.xo.service.UsersService;
import com.oldlane.cheeseblog.xo.vo.CommentVO;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: oldlane
 * @date: 2022/11/15 9:05
 */
@Api(value = "评论相关接口", tags = {"评论相关接口"})
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentRestApi {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "发表评论", notes = "发表评论")
    @PostMapping("add")
    public Result sendComment(@RequestBody CommentVO commentVO) {
        log.info("comment==> {}", commentVO);
        UserVO user = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long articleId = commentVO.getArticleId();
        Article article = articleService.getById(articleId);
        Integer commentEnable = article.getCommentEnable();
        if (EArticleCommentEnable.CANNOT.equals(commentEnable)) {
            return Result.fail().message(BaseMessageConf.CLOSE_COMMENT);
        }
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentVO, comment);
        comment.setUserId(user.getId());
        commentService.save(comment);
        article.setCommentCount(article.getCommentCount() + 1);
        articleService.saveOrUpdate(article);
        return Result.ok().message(BaseMessageConf.SEND_COMMENT_SUCCESS);
    }

    @ApiOperation(value = "获取文章下的所有评论", notes = "获取文章下的所有评论")
    @GetMapping("list")
    public Result getCommentList(@ApiParam(value = "文章id") @RequestParam(name = "articleId") Long articleId, @ApiParam(value="排序规则") @RequestParam(name = "sort", defaultValue = "0") Integer sort) {
        Page<Comment> commentPage = new Page<>();
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        if (Objects.equals(sort, ECommentSort.NEW)) {
            commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        } else {
            commentLambdaQueryWrapper.orderByDesc(Comment::getLikeCount);
        }

        commentService.page(commentPage, commentLambdaQueryWrapper);

        List<Comment> commentPageRecords = commentPage.getRecords();
        commentPageRecords.forEach(item -> {
            Long userId = item.getUserId();
            Users user = usersService.getById(userId);
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);
            item.setUser(userVO);

            Long targetId = item.getTargetId();
            if (targetId != null) {
                Users target = usersService.getById(targetId);
                UserVO targetUser = new UserVO();
                BeanUtil.copyProperties(target, targetUser);
                item.setToUser(targetUser);
            }
        });

        List<Comment> result = new ArrayList<>();
        Map<Long, Comment> map = new HashMap<>();

        for (Comment comment : commentPageRecords) {
            if (comment.getParentId() == null) {
                result.add(comment);
            }
            map.put(comment.getId(), comment);
        }
        // 再次遍历，子评论放入到父评论的child中
        for (Comment comment : commentPageRecords) {

            Long id1 = comment.getRootParent();

            if (id1 != null) {
                Comment p = map.get(id1);
                if (p.getChildren() == null) {
                    p.setChildren(new ArrayList<>());
                }
                p.getChildren().add(comment);
            }
        }
        commentPage.setRecords(result);

        return Result.ok(commentPage);
    }
}
