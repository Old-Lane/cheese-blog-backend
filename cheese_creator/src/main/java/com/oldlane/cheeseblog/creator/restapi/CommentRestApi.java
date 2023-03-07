package com.oldlane.cheeseblog.creator.restapi;

import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.xo.service.CommentService;
import com.oldlane.cheeseblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oldlane
 * @date 2023/3/5 16:11
 */
@Api(value = "评论相关接口", tags = {"评论相关接口"})
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentRestApi {
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "获取我的最新评论", notes = "获取我的最新评论")
    @PostMapping("myNew")
    public Result getMyNewComment() {
        return commentService.getMyNewComment();
    }
}
