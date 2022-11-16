package com.oldlane.cheeseblog.base.enums;

/**
 * 文章审核状态枚举类
 * @author: oldlane
 * @date: 2022/11/3 22:30
 */
public class EArticleAudit {
    /**
     * 审核中
     */
    public static final Integer AUDITING = 0;
    /**
     * 审核通过
     */
    public static final Integer PASS = 1;
    /**
     * 审核未通过
     */
    public static final Integer NOTPASS = 2;
}
