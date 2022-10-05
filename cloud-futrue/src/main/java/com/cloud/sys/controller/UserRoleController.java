package com.cloud.sys.controller;


import com.cloud.sys.UserRoleEntity;
import com.cloud.sys.service.UserRoleServiceImpl;
import com.cloud.sys.service.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/urcon")
@Api(value = "用户角色功能",tags = "用户角色功能")
public class UserRoleController {
    @Autowired
    private UserRoleServiceImpl userRoleService;


    /**
     * 赋予用户角色
     * @param ur
     * @return
     */
    @RequestMapping("/setRole")
    @ResponseBody
    public String setRole(UserRoleEntity ur, Integer version){
        try {
            userRoleService.setRole(ur,version);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "操作异常，请稍后重试！";
        }
    }
}
