package com.oldlane.cheeseblog.xo.vo;

import com.oldlane.cheeseblog.base.vo.PageInfo;
import lombok.Data;

/**
 * @author oldlane
 * @date 2023/3/7 23:56
 */
@Data
public class FavoritesVO extends PageInfo {
    /**
     * 用户id
     */
    private Long userId;
}
