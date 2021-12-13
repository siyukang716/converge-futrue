package com.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @description: 前端控制器
 * @author: aaa
 * @create: 2019-06-14 10:36
 */
@Controller
@Slf4j
public class IndexController {
    @RequestMapping("/toLogin")
    public String toLogin() {
        log.debug("-------------login------------");
        return "toLogin";
    }
    @RequestMapping("/")
    public String toLogin1() {
        log.debug("-------------login------------");
        return "toLogin";
    }
    @RequestMapping("/home")
    public String toHome() {
        log.debug("-------------home------------");
        return "home";
    }

    @RequestMapping("/page/welcome")
    public String welcome() {
        log.debug("-------------welcome------------");
        return "welcome-1";
    }

    @RequestMapping("/login")
    public String login() {
        log.debug("-------------login------------");
        return "login";
    }

    /**
     * 修改密码
     * @return
     */
    @RequestMapping("/changePassword")
    public String changePassword() {
        log.debug("-------------login------------");
        return "user-password";
    }

    /**
     * 基本资料
     * @return
     */
    @RequestMapping("/usersetting")
    public String usersetting() {
        log.debug("-------------login------------");
        return "user-setting";
    }
    /**
     * 跳转到无权限页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/unauthorized")
    public String unauthorized(HttpSession session, Model model) {
        return "unauthorized";
    }

    /**
     * 功能跳转
     * @return
     */
    @RequestMapping("/URLJUMP")
    public String toLogin(String url) {
        return url;
    }


}
