package com.cloud.springbootencry.web;

import com.cloud.sys.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: TestController
 * @Author lenovo
 * @Date: 2021/9/24 13:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "test")
@Slf4j
public class TestController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "encry")
    @ResponseBody
    public String encry(){
        return "接口加密";
    }

    @PostMapping(value = "decrypt")
    @ResponseBody
    public String decrypt(@RequestBody String data) {
        log.info("controller接收的参数object={}", data.toString());
        return data.toString();
    }
}
