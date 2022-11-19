package com.oldlane.cheeseblog.base.global;

/**
 * 消息
 * @author: oldlane
 * @date: 2022/10/31 10:39
 */
public class BaseMessageConf {

    /**
     * 系统相关
     */
    public static final String SYS_PARAMS_ERROR = "系统参数错误";
    public static final String SYS_LOGIN_ERROR = "请先登录";

    /**
     * 博客相关
     */
    public static final String PUBLISH_SUCCESS = "发布成功";
    public static final String DRAFT_SUCCESS = "上传草稿箱成功";

    /**
     * 评论相关
     */
    public static final String SEND_COMMENT_SUCCESS = "发表评论成功";
    public static final String CLOSE_COMMENT = "作者未开启评论";

    /**
     * 用户相关
     */
    public static final String HAS_FOLLOWED = "你已关注该用户";
    public static final String HAS_NOT_FOLLOWED = "你还未关注该用户";
    public static final String FOLLOW_SUCCESS = "关注成功";
    public static final String UNFOLLOW_SUCCESS = "取关成功";
}
