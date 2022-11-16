package com.oldlane.cheeseblog.xo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldlane.cheeseblog.commons.entity.Comment;
import com.oldlane.cheeseblog.xo.mapper.CommentMapper;
import com.oldlane.cheeseblog.xo.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * @author: oldlane
 * @date: 2022/11/15 8:58
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

}
