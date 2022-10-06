package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.SysUserDeptEntity;
import com.cloud.sys.service.SysUserDeptService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @ClassName: SysUserDeptController
 * @Description: TODO(这里用一句话描述这个类的作用)部门用户关联
 * @author 豆芽菜
 * @date 2021-10-11
 */
@Controller
@Api(value = "部门用户关联crud功能",tags = "部门用户关联crud功能")
@RequestMapping(value ="system/user")
public class SysUserDeptController  {
    @Autowired
    private SysUserDeptService sysUserDeptService;

    @GetMapping("/todeptList")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("");
        return model;
    }

    /**
     * 列表查询
     * @param p
     * @return
     */
    @GetMapping("/getdeptList")
    @ResponseBody
    @ApiOperation(value = "列表查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getUsers(Page<SysUserDeptEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<SysUserDeptEntity> wrapper = new QueryWrapper<SysUserDeptEntity>();
        IPage<SysUserDeptEntity> page = sysUserDeptService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改地址信息
     * @param
     * @return
     */
    @PostMapping("/aOrUDept")
    @ResponseBody
    public Result aOrU(SysUserDeptEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SysUserDeptEntity> wrapper = Wrappers.<SysUserDeptEntity>lambdaUpdate();
        boolean b = sysUserDeptService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除地址信息
     * @param
     * @return
     */
    @GetMapping("/delDept")
    @ResponseBody
    public Result del(Long linkId) {
        Result result = Result.getInstance();
        boolean b = sysUserDeptService.removeById(linkId);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @GetMapping("/getDeptById")
    @ResponseBody
    public Result getById(Long linkId) {
        Result result = Result.getInstance();
        SysUserDeptEntity obj = sysUserDeptService.getById(linkId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null){
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }



    @PostMapping("/staffTransfer")
    @ResponseBody
    @ApiOperation(value="岗位调动",notes="多人岗位调动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userIds",value="用户id  ,拼接",required=true),
            @ApiImplicitParam(name="deptid",value="部门id",required=true,dataType="long")
    })
    public Result staffTransfer(String userIds,Long deptid ) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SysUserDeptEntity> wrapper = Wrappers.<SysUserDeptEntity>lambdaUpdate();
        sysUserDeptService.staffTransfer(userIds,deptid);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
        return result;
    }
}