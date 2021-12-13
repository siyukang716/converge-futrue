package com.cloud.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.order.entity.WarehouseInfoEntity;
import com.cloud.order.service.WarehouseInfoService;
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

import java.util.List;


/**
 * @author wjg
 * @ClassName: WarehouseInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)仓库信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "仓库信息表crud功能", tags = "WarehouseInfoController")
@RequestMapping(value = "/warehouse/info")
public class WarehouseInfoController {
    @Autowired
    private WarehouseInfoService warehouseInfoService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("order/warehouseinfo");
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
    public PageDataResult getPageList(Page<WarehouseInfoEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<WarehouseInfoEntity> wrapper = new QueryWrapper<>();
            IPage<WarehouseInfoEntity> page = warehouseInfoService.page(p, wrapper);
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
    public Result aOrU(WarehouseInfoEntity entity) {
        Result result = Result.getInstance();
        try {
            //LambdaUpdateWrapper<WarehouseInfoEntity> wrapper = Wrappers.<WarehouseInfoEntity>lambdaUpdate();
            warehouseInfoService.saveOrUpdate(entity);
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
     * @param warehouseId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer warehouseId,Integer warehouseStatus) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<WarehouseInfoEntity> wrapper = Wrappers.<WarehouseInfoEntity>lambdaUpdate();
            wrapper.eq(WarehouseInfoEntity::getWarehouseId,warehouseId).set(WarehouseInfoEntity::getWarehouseStatus,warehouseStatus);
            warehouseInfoService.update(wrapper);
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
    public Result getById(Integer warehouseId) {
        Result result = Result.getInstance();
        try {
            WarehouseInfoEntity obj = warehouseInfoService.getById(warehouseId);
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


    @RequestMapping("/linkage")
    @ResponseBody
    @ApiOperation(value = "仓库信息", notes = "仓库信息下拉框")
    public Result linkage() {
        Result result = Result.getInstance();
        try {
            LambdaQueryWrapper<WarehouseInfoEntity> lambda = new QueryWrapper<WarehouseInfoEntity>().lambda();
            lambda.select(WarehouseInfoEntity :: getWarehouseId,WarehouseInfoEntity::getWarehoustName)
            .eq(WarehouseInfoEntity::getWarehouseStatus,1);
            List<WarehouseInfoEntity> list = warehouseInfoService.list(lambda);
            result.setData(list);
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