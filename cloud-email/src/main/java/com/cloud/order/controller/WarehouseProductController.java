package com.cloud.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.order.entity.WarehouseProductEntity;
import com.cloud.order.service.WarehouseProductService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author wjg
 * @ClassName: WarehouseProductController
 * @Description: TODO(这里用一句话描述这个类的作用)商品库存表
 * @date 2021-08-05
 */
@Controller
@Api(value = "商品库存表crud功能", tags = "WarehouseProductController")
@RequestMapping(value = "/warehouse/product")
public class WarehouseProductController {
    @Autowired
    private WarehouseProductService warehouseProductService;
    @RequestMapping("/tolist/{productId}")
    public ModelAndView toList(@PathVariable(name = "productId") Integer productId) {
        ModelAndView model = new ModelAndView();
        model.addObject("productId",productId);
        model.setViewName("order/warehouseProduct");
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
    public PageDataResult getPageList(Page<WarehouseProductEntity> p, Integer productId) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<WarehouseProductEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("product_id",productId);
            IPage<WarehouseProductEntity> page = warehouseProductService.page(p, wrapper);
            pdr.setTotals((int) page.getTotal());
            pdr.setList(page.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }


    /**
     * 增加或修改
     *
     * @param entity
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result aOrU(WarehouseProductEntity entity) {
        Result result = Result.getInstance();
        try {
            //LambdaUpdateWrapper<WarehouseProductEntity> wrapper = Wrappers.<WarehouseProductEntity>lambdaUpdate();
            warehouseProductService.saveOrUpdate(entity);
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
     * 删除
     *
     * @param wpId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer wpId) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<WarehouseProductEntity> wrapper = Wrappers.<WarehouseProductEntity>lambdaUpdate();
            //wrapper.eq(WarehouseProductEntity::getWarehouseId,warehouseId).set(WarehouseProductEntity::getWarehouseStatus,warehouseStatus);
            //warehouseProductService.update(wrapper);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("操作成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer wpId) {
        Result result = Result.getInstance();
        try {
            WarehouseProductEntity obj = warehouseProductService.getById(wpId);
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

    /**
     * 库存维护
     * @param wpId  业务id
     * @param businessType 业务类型
     * @param inventoryOperations 加减类型
     * @param amount 数量
     * @return
     */
    @RequestMapping("/aOrRInventory")
    @ResponseBody
    public Result aOrRInventory(Integer wpId,String businessType,String inventoryOperations,
                                Integer amount ) {
        Result result = Result.getInstance();
        boolean b = warehouseProductService.aOrRInventory(wpId, businessType, inventoryOperations, amount);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("保存成功!!!!!!");
        if (!b){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("库存不足,操作失败!!!!");
        }
        return result;
    }


}