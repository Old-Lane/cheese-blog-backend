package com.oldlane.cheeseblog.web.restapi;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oldlane
 * @date 2023/3/6 23:07
 */
@RestController
@RequestMapping("/chat")
@Api(tags = "聊天相关接口")
@Slf4j
public class ChatRestApi {

}
