package com.oldlane.cheeseblog.upload.restapi;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 图片相关接口
 * @author: oldlane
 * @date: 2022/11/8 8:22
 */
@Api(value = "图片相关接口", tags = {"图片相关接口"})
@RestController
@RequestMapping("/img")
@Slf4j
public class PictureRestApi {

    @Value("${cheese.picturePath}")
    private String uploadPath;

    /**
     * 将图片展示在网页上
     * @param filename
     * @param response
     */
    @GetMapping("/{filename}")
    public void getPicture(@PathVariable String filename, HttpServletResponse response) {
        try (FileInputStream fileInputStream = new FileInputStream(uploadPath + filename)) {
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
