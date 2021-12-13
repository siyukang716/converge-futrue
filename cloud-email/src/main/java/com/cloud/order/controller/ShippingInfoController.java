package com.cloud.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.order.entity.ShippingInfoEntity;
import com.cloud.order.service.ShippingInfoService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author wjg
 * @ClassName: ShippingInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)物流公司信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "物流公司信息crud功能", tags = "ShippingInfo")
@RequestMapping(value = "/shipping/info")
public class ShippingInfoController {
    @Autowired
    private ShippingInfoService shippingInfoService;


    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("order/shippinginfo");
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
    public PageDataResult getPageList(Page<ShippingInfoEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<ShippingInfoEntity> wrapper = new QueryWrapper<>();
            IPage<ShippingInfoEntity> page = shippingInfoService.page(p, wrapper);
            pdr.setTotals((int) page.getTotal());
            pdr.setList(page.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }


    /**
     * 增加或修改地址信息
     *
     * @param caentity
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result aOrU(ShippingInfoEntity caentity) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<ShippingInfoEntity> wrapper = Wrappers.<ShippingInfoEntity>lambdaUpdate();
            shippingInfoService.saveOrUpdate(caentity);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("保存成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除地址信息
     *
     * @param shipId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer shipId) {
        Result result = Result.getInstance();
        try {
            shippingInfoService.removeById(shipId);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("删除成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer shipId) {
        Result result = Result.getInstance();
        try {
            ShippingInfoEntity obj = shippingInfoService.getById(shipId);
            result.setData(obj);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("获取成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }


}