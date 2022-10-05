package com.cloud.controller.weixin;

import com.cloud.service.EmailService;
import com.spring.commons.entity.CommonResult;
import com.spring.commons.entity.ToEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;
    @GetMapping(value = "send")
    public CommonResult sendEmain(ToEmail toEmail){
        return emailService.commonEmail(toEmail);
    }

    @GetMapping(value = "htmlsend")
    public CommonResult sendhtmlEmain(ToEmail toEmail){
        return emailService.htmlEmail(toEmail);
    }

    @GetMapping(value = "web")
    public String hello(){
        return "hello";
    }
}
