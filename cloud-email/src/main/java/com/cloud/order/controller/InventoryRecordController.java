package com.cloud.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.order.entity.InventoryRecordEntity;
import com.cloud.order.service.InventoryRecordService;
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
 * @ClassName: InventoryRecordController
 * @Description: TODO(这里用一句话描述这个类的作用)库存记录
 * @date 2021-08-29
 */
@Controller
@Api(value = "库存记录crud功能", tags = "")
@RequestMapping(value = "/inventoryRecordEntity")
public class InventoryRecordController {
    @Autowired
    private InventoryRecordService inventoryRecordService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("");
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
    public PageDataResult getPageList(Page<InventoryRecordEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<InventoryRecordEntity> wrapper = new QueryWrapper<InventoryRecordEntity>();
        IPage<InventoryRecordEntity> page = inventoryRecordService.page(p, wrapper);
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
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result aOrU(InventoryRecordEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<InventoryRecordEntity> wrapper = Wrappers.<InventoryRecordEntity>lambdaUpdate();
        boolean b = inventoryRecordService.saveOrUpdate(entity);
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
     *
     * @param
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer id) {
        Result result = Result.getInstance();
        boolean b = inventoryRecordService.removeById(id);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer id) {
        Result result = Result.getInstance();
        InventoryRecordEntity obj = inventoryRecordService.getById(id);
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