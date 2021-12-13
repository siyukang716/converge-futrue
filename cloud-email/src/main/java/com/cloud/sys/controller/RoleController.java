package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.RoleEntity;
import com.cloud.sys.service.RoleServiceImpl;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/role")
@Api(value = "角色模块",tags = "角色模块")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    RoleServiceImpl roleService;

    /**
     * 功能跳转
     * @return
     */
    @RequestMapping("/roleManage")
    public String roleManage(){
        return "auth/roleManage";
    }
    @RequestMapping("/rolePerm")
    public String rolePerm(){
        return "auth/rolePerm";
    }

    /**
     * 分页查询角色列表
     * @param p
     * @return
     */
    @RequestMapping("/getRoles")
    @ResponseBody
    public PageDataResult getRoles(Page<RoleEntity> p){
        PageDataResult pdr = new PageDataResult();
        try {
            LambdaQueryWrapper<RoleEntity> warpper = new LambdaQueryWrapper<>();
            Long companyId = ShiroUtils.getLoginUser().getCompanyId();
            warpper.eq(RoleEntity::getCompanyId,companyId);
            IPage<RoleEntity> page = roleService.page(p, warpper);
            pdr.setList(page.getRecords());
            pdr.setTotals((int) page.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(500);
        }

        return pdr;
    }

    /**
     * 角色 修改 与新增
     * @param role
     * @return
     */
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public Result saveOrUpdate(RoleEntity role){
        Result result = Result.getInstance();
        try {
            role.setCompanyId(ShiroUtils.getLoginUser().getCompanyId());
            boolean b = role.insertOrUpdate();
            result.setStatus(200);
            result.setData(role);
            result.setMessage("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
            result.setMessage("操作失败");
        }

        return result;
    }

    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @RequestMapping("/getObjById")
    @ResponseBody
    public Result getObjById(Long id){
        Result result = Result.getInstance();
        try {
            RoleEntity byId = roleService.getById(id);
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

    @RequestMapping("/deleteById")
    @ResponseBody
    public Result deleteById(Long id){
        Result result = Result.getInstance();
        try {
            boolean b = roleService.removeById(id);
            result.setStatus(200);
            result.setMessage("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
            result.setMessage("操作失败");
        }
        return result;
    }


}
