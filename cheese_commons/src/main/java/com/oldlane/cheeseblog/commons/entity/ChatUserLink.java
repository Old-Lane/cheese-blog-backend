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
 * @TableName chat_user_link
 */
@TableName(value ="chat_user_link")
@Data
public class ChatUserLink implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 发送方id
     */
    private Long fromUser;

    /**
     * 接收方id
     */
    private Long toUser;

    /**
     * 时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}