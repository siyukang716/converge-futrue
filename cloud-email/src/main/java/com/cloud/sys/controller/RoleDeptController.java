package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.RoleDeptEntity;
import com.cloud.sys.service.RoleDeptService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @ClassName: RoleDeptController
 * @Description: TODO(这里用一句话描述这个类的作用)角色部门数据权限
 * @author 小可爱
 * @date 2021-11-18
 */
@Controller
@Api(value = "角色部门数据权限crud功能",tags = "角色部门数据权限")
@RequestMapping(value ="sys/role/dept")
public class RoleDeptController  {
    @Autowired
    private RoleDeptService roleDeptService;

    @GetMapping("/tolist/{roleId}")
    @ApiOperation(value = "跳转功能",httpMethod = "GET")
    public ModelAndView toList(@PathVariable("roleId")Long roleId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/roledept");
        model.addObject("roleId",roleId);
        return model;
    }

    /**
     * 列表查询
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询",httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "页码", required = true, dataType = "int"),
        @ApiImplicitParam(name="size",value="每页数据",required=true,dataType = "int"),
        @ApiImplicitParam(name="roleId",value="角色id",required=true,dataType = "long")
    })
    public PageDataResult getPageList(Page p,Long roleId) {
        PageDataResult pdr = new PageDataResult();
        IPage page = roleDeptService.pageCustomize(p,roleId);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    @ApiOperation(value = "新增修改接口", notes = "新增修改接口")
    public Result aOrU(RoleDeptEntity entity) {
        Result result = Result.getInstance();
        boolean b = roleDeptService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    @ApiOperation(value = "删除接口", notes = "删除接口")
    public Result del(Long rDPurview) {
        Result result = Result.getInstance();
        boolean b = roleDeptService.removeById(rDPurview);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation(value = "根据主键查询对象接口", notes = "根据主键查询对象接口")
    public Result getById(Long rDPurview) {
        Result result = Result.getInstance();
        RoleDeptEntity obj = roleDeptService.getById(rDPurview);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null){
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}