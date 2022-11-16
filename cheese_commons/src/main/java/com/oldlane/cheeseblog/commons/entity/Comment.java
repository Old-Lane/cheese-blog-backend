package com.oldlane.cheeseblog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @TableName article
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 评论父id
     */
    private Long parentId;

    /**
     * 最顶层父id
     */
    private Long rootParent;

    /**
     * 对应文章id
     */
    private Long articleId;

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

    /**
     * 点赞数量
     */
    private Long likeCount;

    /**
     * 回复数量
     */
    private Long replyCount;

    /**
     * 评论类型
     */
    private String type;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 发表人
     */
    @TableField(exist = false)
    private Object user;

    /**
     * 被评论人
     */
    @TableField(exist = false)
    private Object toUser;

    /**
     * 子评论
     */
    @TableField(exist = false)
    private List<Comment> children;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}