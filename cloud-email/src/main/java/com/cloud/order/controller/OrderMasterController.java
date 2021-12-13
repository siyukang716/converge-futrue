package com.cloud.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.order.entity.OrderMasterEntity;
import com.cloud.order.service.OrderMasterService;
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

import java.math.BigDecimal;


/**
 * @author wjg
 * @ClassName: OrderMasterController
 * @Description: TODO(这里用一句话描述这个类的作用)订单主表
 * @date 2021-08-29
 */
@Controller
@Api(value = "订单主表crud功能", tags = "")
@RequestMapping(value = "/order/master")
public class OrderMasterController {
    @Autowired
    private OrderMasterService orderMasterService;

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
    public PageDataResult getPageList(Page<OrderMasterEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<OrderMasterEntity> wrapper = new QueryWrapper<OrderMasterEntity>();
        IPage<OrderMasterEntity> page = orderMasterService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     *  用户下单
     * @param specsId   商品型号id
     * @param productName 商品名称
     * @param productCnt 购买商品数
     * @param productPrice 商品单价
     * @param paymentMoney 实际支付金额
     * @param orderMoney 订单金额
     * @return
     */
    @RequestMapping("/placeAnOrder")
    @ResponseBody
    public Result placeAnOrder(Integer  specsId, String productName, Integer productCnt, Double productPrice, BigDecimal paymentMoney, BigDecimal orderMoney) {
        Result result = Result.getInstance();
        boolean b = orderMasterService.placeAnOrder(specsId,productName,productCnt,productPrice,paymentMoney,orderMoney);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("恭喜你购买成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("库存不足,购买失败");
        }
        return result;
    }

    @RequestMapping("/placeAnOrderDCart")
    @ResponseBody
    public Result placeAnOrderDCart(Integer  specsId, String productName, Integer productCnt, Double productPrice, BigDecimal paymentMoney, BigDecimal orderMoney) {
        Result result = Result.getInstance();
        boolean b = orderMasterService.placeAnOrderDCart(specsId,productName,productCnt,productPrice,paymentMoney,orderMoney);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("恭喜你购买成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("库存不足,购买失败");
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
        boolean b = orderMasterService.removeById(id);
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
        OrderMasterEntity obj = orderMasterService.getById(id);
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