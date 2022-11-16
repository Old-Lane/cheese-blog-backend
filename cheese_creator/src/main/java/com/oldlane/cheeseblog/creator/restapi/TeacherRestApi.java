package com.oldlane.cheeseblog.creator.restapi;

import com.oldlane.cheeseblog.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: oldlane
 * @date: 2022/11/13 22:22
 */
@Api(value = "老师相关接口", tags = {"老师相关接口"})
@RestController
@RequestMapping("/teacher")
@Slf4j
public class TeacherRestApi {

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/getClasses")
    @ApiOperation(value = "获取班级列表", notes = "获取班级列表")
    public Result getClasses() {
        return Result.ok().message("ok666");
    }
}
