package com.cloud.cosmetology.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.cosmetology.entity.CUserEntity;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cloud.cosmetology.entity.CUserEntity;
import com.cloud.cosmetology.service.CUserService;

import java.util.List;

/**
 *
 * @ClassName: CUserController
 * @Description: TODO(这里用一句话描述这个类的作用)美容院-顾客
 * @author 豆芽菜
 * @date 2022-10-06
 */
@Controller
@Api(value = "美容院-顾客crud功能",tags = "美容院-顾客")
@RequestMapping(value ="c/user")
public class CUserController  {
    @Autowired
    private CUserService cUserService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("cosmetology/cuser");
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
    public PageDataResult getPageList(Page p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<CUserEntity> wrapper = new QueryWrapper<CUserEntity>();
        IPage<CUserEntity> page = cUserService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }

    /**
     * 顾客列表全量下拉
     * @param entity
     * @return
     */
    @GetMapping("/getUserList")
    @ResponseBody
    @ApiOperation(value = "顾客列表全量下拉",httpMethod = "GET")
    public Result getUserList(CUserEntity entity) {
        Result result = Result.getInstance();
        LambdaQueryWrapper<CUserEntity> wrapper = new QueryWrapper<CUserEntity>().lambda();
        wrapper.orderByDesc(CUserEntity::getUpdateTime);
        List<CUserEntity> prodList = cUserService.list(wrapper);
        result.setData(prodList);
        return result;
    }

    /**
     * 增加或修改
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    @ApiOperation(value = "新增修改接口", notes = "新增修改接口")
    public Result aOrU(CUserEntity entity) {
        Result result = Result.getInstance();
        boolean b = cUserService.saveOrUpdate(entity);
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
    public Result del(Long userId) {
        Result result = Result.getInstance();
        boolean b = cUserService.removeById(userId);
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
    public Result getById(Long userId) {
        Result result = Result.getInstance();
        CUserEntity obj = cUserService.getById(userId);
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