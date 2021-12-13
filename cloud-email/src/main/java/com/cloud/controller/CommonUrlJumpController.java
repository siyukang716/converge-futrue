package com.cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "url")
public class CommonUrlJumpController {

    @GetMapping("/wx")
    public String index(String url) {
        return url;
    }
}
