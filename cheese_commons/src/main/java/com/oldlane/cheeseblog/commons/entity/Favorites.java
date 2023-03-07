package com.oldlane.cheeseblog.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName favorites
 */
@TableName(value ="favorites")
@Data
public class Favorites implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 收藏名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 封面
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建者id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 可见性 0-自己可见 1-公开可见
     */
    @TableField(value = "visibility")
    private Integer visibility;

    /**
     * 文章数量
     */
    @TableField(value = "article_count")
    private Long articleCount;

    /**
     * 订阅数量
     */
    @TableField(value = "subscribe_count")
    private Long subscribeCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}