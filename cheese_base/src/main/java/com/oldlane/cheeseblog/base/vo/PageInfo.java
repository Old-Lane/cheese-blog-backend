package com.oldlane.cheeseblog.base.vo;

import lombok.Data;

/**
 * @author oldlane
 * @date 2023/3/8 0:01
 */
@Data
public class PageInfo {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 当前页
     */
    private Long currentPage;

    /**
     * 页大小
     */
    private Long pageSize;
}