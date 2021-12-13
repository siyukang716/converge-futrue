package com.cloud.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.sys.service.ZTreeServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ztree")
@Api(value = "树组件",tags = "树组件")
public class ZTreeController {

    @Autowired
    private ZTreeServiceImpl zTreeService;

    /**
     * 获取角色树
     * @param id
     * @return
     */
    @RequestMapping("/getRole")
    @ResponseBody
    public List<Map<String,Object>> getRole(Long id){
        return zTreeService.getRole(id);
    }

    /***
     * 获取登录用户菜单列表
     * @param id
     * @return
     */
    @RequestMapping("/getPermTree")
    @ResponseBody
    public List<Map<String,Object>> getPermTree(Long id){
        return zTreeService.getPermTree(id);
    }
    @RequestMapping("/getAllPermTree")
    @ResponseBody
    public List<Map<String,Object>> getAllPermTree(Long id){
        return zTreeService.getAllPermTree(id);
    }

    /**
     * 角色菜单授权管理  树 已授权
     * @param roleId
     * @param id
     * @return
     */
    @RequestMapping("getRoleTermTree")
    @ResponseBody
    public  List<Map<String,Object>> getRoleTermTree(Long roleId,Long id){
        List<Map<String, Object>> roleTermTree = zTreeService.getRoleTermTree(roleId, id);
        for (Map<String, Object> map: roleTermTree ) {
            Long isParent = (Long) map.get("isParent");
            if (isParent == 0 ){
                map.put("isParent",false);
            }else {
                map.put("isParent",true);
            }
        }
        return roleTermTree;
    }

}
