package com.oldlane.cheeseblog.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName chat_list
 */
@TableName(value = "chat_list")
@Data
public class ChatList implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户聊天关系表主键
     */
    private Long linkId;

    /**
     * 发送方是否在窗口
     */
    private Integer fromWindow;

    /**
     * 接收方是否在窗口
     */
    private Integer toWindow;

    /**
     * 未读数
     */
    private Integer unread;

    /**
     * 是否删除 0-否 1-是
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}