package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.SysCommonDictKvEntity;
import com.cloud.sys.service.SysCommonDictKvService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 小可爱
 * @ClassName: SysCommonDictKvController
 * @Description: TODO(这里用一句话描述这个类的作用)字典管理
 * @date 2021-10-12
 */
@Controller
@Api(value = "字典管理crud功能", tags = "字典管理")
@RequestMapping(value = "system/dict/kv")
public class SysCommonDictKvController {
    @Autowired
    private SysCommonDictKvService sysCommonDictKvService;

    @GetMapping("/tolist/{dictType}")
    public ModelAndView toList(@ApiParam(name = "dictType", value = "字典type", required = true) @PathVariable("dictType") String dictType) {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/syscommondictkv");
        model.addObject("dictType", dictType);
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
    @ApiOperation(value = "列表查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page p, @ApiParam(name = "dictType", value = "字典dictType", required = true) String dictType) {
        PageDataResult pdr = new PageDataResult();
        LambdaQueryWrapper<SysCommonDictKvEntity> lambda = new QueryWrapper<SysCommonDictKvEntity>().lambda();
        lambda.eq(SysCommonDictKvEntity::getDictId, dictType);
        lambda.eq(SysCommonDictKvEntity::getIsDel, 1);
        IPage<SysCommonDictKvEntity> page = sysCommonDictKvService.page(p, lambda);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改地址信息
     *
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(SysCommonDictKvEntity entity) {
        return sysCommonDictKvService.saveOrUpdateLocal(entity);
    }

    @PostMapping("/updateStatus")
    @ResponseBody
    @ApiOperation(value = "修改字典发布状态", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictStatus", value = "状态  1 发布 2 未发布", paramType = "query", required = true),
            @ApiImplicitParam(name = "dictChildId", value = "主键", paramType = "query", required = true)
    })
    public Result updateStatus(Integer dictStatus,Long dictChildId) {
        return sysCommonDictKvService.updateStatus(dictStatus,dictChildId);
    }

    /**
     * 删除地址信息
     *
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long dictChildId) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SysCommonDictKvEntity> wrapper = Wrappers.<SysCommonDictKvEntity>lambdaUpdate();
        wrapper.set(SysCommonDictKvEntity::getIsDel, 0);
        wrapper.eq(SysCommonDictKvEntity::getDictChildId, dictChildId);
        boolean b = sysCommonDictKvService.update(wrapper);
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
    public Result getById(Long dictChildId) {
        Result result = Result.getInstance();
        SysCommonDictKvEntity obj = sysCommonDictKvService.getById(dictChildId);
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
     * 根据字典类型ids获取字典key  value
     *
     * @param dictTypes
     * @return
     */
    @GetMapping("/getDictMapById")
    @ResponseBody
    @ApiOperation(value = "根据字典类型id获取字典key  value", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypes", value = "字典类型ids,多个用,分割", paramType = "query", required = true)
    })
    public Result getDictMapById(String dictTypes) {
        return sysCommonDictKvService.getDictMapById(dictTypes);
    }

    /**
     * 根据字典类型id获取字典 返回集合
     *
     * @param dictType
     * @return
     */
    @GetMapping("/getDictById")
    @ResponseBody
    @ApiOperation(value = "根据字典类型id获取字典key  value", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型id,多个用,分割", paramType = "query", required = true)
    })
    public Result getDictById(String dictType) {
        return sysCommonDictKvService.getDictById(dictType);
    }
}