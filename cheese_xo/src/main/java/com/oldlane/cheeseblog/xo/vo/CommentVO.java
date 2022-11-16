package com.oldlane.cheeseblog.xo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: oldlane
 * @date: 2022/11/15 8:36
 */
@Data
public class CommentVO {

    /**
     * 对应文章id
     */
    private Long articleId;

    /**
     * 评论父id
     */
    private Long parentId;

    /**
     * 顶层根id
     */
    private Long rootParent;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 目标用户id
     */
    private Long targetId;

    /**
     * 评论内容
     */
    private String content;

}
