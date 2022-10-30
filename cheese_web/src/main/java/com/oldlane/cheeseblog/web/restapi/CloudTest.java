package com.oldlane.cheeseblog.web.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date: 2022/10/29 17:25
 * Description:
 */
@RestController
@RequestMapping("/test")
public class CloudTest {

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
