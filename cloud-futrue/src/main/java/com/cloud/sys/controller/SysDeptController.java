package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.SysDeptEntity;
import com.cloud.sys.service.SysDeptService;
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

import java.util.List;
import java.util.Map;


/**
 * @author
 * @ClassName: SysDeptController
 * @Description: TODO(这里用一句话描述这个类的作用)部门
 * @date 2021-09-20
 */
@Controller
@Api(value = "部门crud功能", tags = "部门crud功能")
@RequestMapping(value = "system/dept")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/sysdept.html");
        return model;
    }

    /**
     * 列表查询
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<SysDeptEntity> p) {
        PageDataResult pdr = new PageDataResult();
        LambdaQueryWrapper<SysDeptEntity> wrapper = new QueryWrapper<SysDeptEntity>().lambda();
        wrapper.eq(SysDeptEntity::getCompanyId, ShiroUtils.getLoginUser().getCompanyId());
        IPage<SysDeptEntity> page = sysDeptService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改
     *
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(SysDeptEntity entity) {
        Result result = Result.getInstance();
        entity.setCompanyId(ShiroUtils.getLoginUser().getCompanyId());
        boolean b = sysDeptService.saveOrUpdateCustomize(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long deptId) {
        Result result = Result.getInstance();
        boolean b = sysDeptService.removeById(deptId);
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
    public Result getById(Long deptId) {
        Result result = Result.getInstance();
        SysDeptEntity obj = sysDeptService.getById(deptId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 获取当前登录部门人员组织机构树
     *
     * @param dept_id
     * @return
     */
    @GetMapping("/tree")
    @ResponseBody
    @ApiOperation(value = "获取子部门", notes = "获取当前登录部门人员组织机构树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dept_id", value = "部门id", required = true)
    })
    public List<Map> getTree(Long dept_id) {
        Result result = Result.getInstance();
        List<Map> obj = sysDeptService.getTree(dept_id);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return obj;
    }

    /**
     * 获取数据权限树
     * @param isSelf
     * @return
     */
    @GetMapping("/getpurviewtree")
    @ResponseBody
    @ApiOperation(value = "获取数据权限树", notes = "参数：isSelf 为 true时 获取当前登录部门树  为false是 获取当前登录角色部门数据权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isSelf", value = "是否自己", required = true,dataType = "boolean")
    })
    public List<Map> getPurviewTree(Boolean isSelf) {
        Result result = Result.getInstance();
        List<Map> obj = sysDeptService.getPurviewTree(isSelf);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return obj;
    }


    /**
     * 获取所有子部门
     *
     * @param dept_id
     * @return
     */
    @GetMapping("/getAllChildDeptsById")
    @ResponseBody
    @ApiOperation(value = "获取所有子部门", notes = "根据指定部门id获取所有子部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dept_id", value = "部门id", required = true)
    })
    public Result getAllChildDeptsById(Long dept_id) {
        Result result = Result.getInstance();
        List<SysDeptEntity> obj = sysDeptService.getAllChildDeptsById(dept_id);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}