package com.oldlane.cheeseblog.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oldlane.cheeseblog.base.enums.EStatus;
import com.oldlane.cheeseblog.base.result.Result;
import com.oldlane.cheeseblog.commons.entity.Tag;
import com.oldlane.cheeseblog.xo.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: oldlane
 * @date: 2022/11/4 11:19
 */
@RestController
@RequestMapping("/tag")
@Api(value = "标签相关接口", tags= {"标签相关接口"})
public class TagRestApi {
    @Autowired
    private TagService tagService;

    @ApiOperation(value = "获取所有可用标签", notes = "获取所有可用标签")
    @GetMapping("/enable")
    public Result enable() {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.eq(Tag::getStatus, EStatus.CAN);
        List<Tag> tagList = tagService.list(tagLambdaQueryWrapper);
        return Result.ok(tagList);
    }
}
