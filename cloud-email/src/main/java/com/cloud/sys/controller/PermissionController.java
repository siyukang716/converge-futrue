package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.PermissionEntity;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.PermissionServiceImpl;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/perm")
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    @Autowired
    PermissionServiceImpl permissionService;

    /**
     * 跳转到菜单列表
     * @return
     */
    @RequestMapping("/permList")
    public String permList() {
        return "auth/permList";
    }
    /**
     * 获取登录用户菜单功能列表列表
     * @return
     */
    @GetMapping("/getUserPerms")
    @ResponseBody
    public List<PermissionEntity> getUserPerms(){
        UserEntity exitUser = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        return permissionService.getUserPerms(exitUser.getId());
    }

    /**
     * 获取登录用户菜单功能列表列表
     * @return
     */
    @GetMapping("/getTheMenu")
    @ResponseBody
    public Result getTheMenu(){
        Result result = Result.getInstance();
        Map theMenu = permissionService.getTheMenu();
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setData(theMenu);
        return result;
    }





    /**
     * 功能菜单列表
     * @param p
     * @param user
     * @return
     */
    @RequestMapping("/getPageList")
    @ResponseBody
    @ApiOperation(value = "功能菜单列表", httpMethod = "GET")
    public PageDataResult getPageList(Page<PermissionEntity> p, UserEntity user){
        PageDataResult result = new PageDataResult();
        QueryWrapper<PermissionEntity> wrapper = new QueryWrapper<>();
        IPage<PermissionEntity> page = permissionService.page(p, wrapper);
        result.setTotals((int) page.getTotal());
        result.setList(page.getRecords());
        return result;
    }

    /**
     * 菜单 修改 和 新增
     * @param perm
     * @return
     */
    @RequestMapping("saveOrUpdate")
    @ResponseBody
    @ApiOperation(value = "新增修改", httpMethod = "POST")
    public Result saveOrUpdate(PermissionEntity perm){
        Result result = Result.getInstance();
        try {
            permissionService.saveOrUpdateCustomize(perm);
            result.setStatus(200);
            result.setData(perm);
            result.setMessage("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * 更具id 查询 对象
     * @param id
     * @return
     */
    @RequestMapping("/getObjById")
    @ResponseBody
    @ApiOperation(value = "查询对象", httpMethod = "GET")
    public Result getObjById(Long id){
        Result result = Result.getInstance();
        try {
            PermissionEntity byId = permissionService.getById(id);
            result.setStatus(200);
            result.setData(byId);
            result.setMessage("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * 更具id删除对象
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    @ResponseBody
    @ApiOperation(value = "删除", httpMethod = "GET")
    public Result deleteById(Long id){
        Result result = Result.getInstance();
        try {
            boolean b = permissionService.removeById(id);
            result.setStatus(200);
            result.setMessage("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
            result.setMessage("操作失败");
        }
        return result;
    }



    /**
     * 查询本公司已有数据权限
     * @param companyId
     * @return
     */
    @PostMapping("/getTermTreeBycompanyId")
    @ResponseBody
    @ApiOperation(value = "查询本公司已有数据权限", httpMethod = "POST")
    public List<Map<String,Object>> getTermTreeBycompanyId(Long id,Long companyId){
        return permissionService.getTermTreeBycompanyId(id,companyId);
    }

    /**
     * 查询当前登录人公司
     * @param id
     * @return
     */
    @PostMapping("/getTermTreeByLoginCompanyId")
    @ResponseBody
    @ApiOperation(value = "查询本公司已有数据权限", httpMethod = "POST")
    public List<Map<String,Object>> getTermTreeByLoginCompanyId(Long id){
        return permissionService.getTermTreeByLoginCompanyId(id);
    }






}
