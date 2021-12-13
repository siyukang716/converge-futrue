package com.cloud.sys.controller;


import com.cloud.util.Result;
import com.cloud.sys.service.RolePermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单权限 关联表
 */
@RequestMapping("/rolePerm")
@Controller
public class RolePermController {
    @Autowired
    private RolePermissionServiceImpl rolePermissionService;

    @RequestMapping("/addObjs")
    @ResponseBody
    public Result addObjs(String prems, Long roleid){
        Result result = Result.getInstance();
        try {
            rolePermissionService.addObjs(prems,roleid);
            result.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
        }
        return result;
    }
    @RequestMapping("delObjs")
    @ResponseBody
    public  Result delObjs(String prems,Long roleid){
        Result result = Result.getInstance();
        try {
            rolePermissionService.delObjs(prems,roleid);
            result.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
        }
        return result;
    }
}
