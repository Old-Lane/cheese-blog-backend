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
 * @TableName chat_message
 */
@TableName(value ="chat_message")
@Data
public class ChatMessage implements Serializable {
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
     * 发送方id
     */
    private Long fromUser;

    /**
     * 接收方id
     */
    private Long toUser;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 消息类型 0-文字 1-图片
     */
    private Integer type;

    /**
     * 是否为最新消息 0-否 1-是
     */
    private Integer isLatest;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}