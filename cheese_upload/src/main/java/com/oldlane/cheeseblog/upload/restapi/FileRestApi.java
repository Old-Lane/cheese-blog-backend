package com.oldlane.cheeseblog.upload.restapi;

import com.oldlane.cheeseblog.base.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传相关接口
 * @author: oldlane
 * @date: 2022/11/7 10:22
 */
@Api(value = "文件上传相关接口", tags = {"文件上传相关接口"})
@RestController
@RequestMapping("/file")
@Slf4j
public class FileRestApi {

    @Value("${cheese.picturePath}")
    private String uploadPath;

    /**
     * 文件上传
     * @param file 文件
     * @return 文件名称
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("file ===> {}", file);
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(uploadPath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok(fileName);
    }
}
