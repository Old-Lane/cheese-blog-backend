package com.oldlane.cheeseblog.xo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: oldlane
 * @date: 2022/11/2 19:38
 */
@Data
public class ArticleVO {

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签id
     */
    private List<Long> tagIdList;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 是否点赞
     */
    private Boolean isLiked;

}
