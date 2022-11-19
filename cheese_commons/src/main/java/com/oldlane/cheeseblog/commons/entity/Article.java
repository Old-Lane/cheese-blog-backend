package com.oldlane.cheeseblog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文章标题
     */

    private String title;
    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 浏览数量
     */
    private Long views;

    /**
     * 评论数量
     */
    private Long commentCount;

    /**
     * 点赞数量
     */
    private Long likeCount;

    /**
     * 收藏数量
     */
    private Long collectCount;

    /**
     * 文章权限 0-公开 1-仅自己
     */
    private Integer permission;

    /**
     * 能否评论 0-不能 1-能
     */
    private Integer commentEnable;

    /**
     * 置顶 0-不置顶 1-置顶
     */
    private Integer top;

    /**
     * 审核状态 0-审核中 1-审核通过 2-审核未通过
     */
    private Integer audit;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 文章状态 0-草稿箱 1-回收站
     */
    private Integer status;

    /**
     * 作者
     */
    private String author;

    /**
     * 投稿用户id
     */
    private Long userId;

    /**
     * 文章来源 0-原创 1-转载
     */
    private Integer source;

    /**
     * 文章分类id
     */
    private Long categoryId;

    /**
     * 是否点赞
     */
    @TableField(exist = false)
    private Boolean isLiked;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}