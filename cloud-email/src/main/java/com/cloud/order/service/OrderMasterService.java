package com.cloud.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.order.entity.InventoryRecordEntity;
import com.cloud.order.entity.OrderDetailEntity;
import com.cloud.order.entity.OrderMasterEntity;
import com.cloud.order.entity.WarehouseProductEntity;
import com.cloud.order.mapper.OrderDetailMapper;
import com.cloud.order.mapper.OrderMasterMapper;
import com.cloud.shiro.ShiroUtils;
import com.cloud.util.RedisUtil;
import com.hutool.HutoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 *
 * @ClassName: OrderMasterEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wjg
 * @date 2021-08-05
 */
@Service
public class OrderMasterService extends ServiceImpl<OrderMasterMapper, OrderMasterEntity> {
    @Autowired
    private WarehouseProductService wpService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private RedisUtil redisUtil;
    /**
     *  用户下单
     * @param specsId   商品型号id
     * @param productName 商品名称
     * @param productCnt 购买商品数
     * @param productPrice 商品单价
     * @param paymentMoney 实际支付金额
     * @return
     */
    public boolean placeAnOrder(Integer specsId, String productName, Integer productCnt, Double productPrice, BigDecimal paymentMoney,BigDecimal orderMoney) {
        WarehouseProductEntity wp = wpService.commodityAvailableInventory(specsId,productCnt);
        if (null == wp)
            return false;
         //1.根据信息组装订单主表
        OrderMasterEntity omEntity = new OrderMasterEntity();
        omEntity.setOrderSn(HutoolUtils.getSnowflakeId());
        omEntity.setCustomerId(ShiroUtils.getLoginUserId());
        omEntity.setPaymentMethod(1);
        omEntity.setOrderMoney(orderMoney);
        omEntity.setPaymentMoney(paymentMoney);
        this.save(omEntity);
        //2.组装订单详情表
        OrderDetailEntity detailEntity = new OrderDetailEntity();
        detailEntity.setOrderId(omEntity.getOrderId());
        detailEntity.setProductId(specsId);
        detailEntity.setProductName(productName);
        detailEntity.setProductCnt(productCnt);
        detailEntity.setProductPrice(new BigDecimal(productPrice));
        detailEntity.setWId(wp.getWpId());
        orderDetailMapper.insert(detailEntity);

        //扣减库存
        wpService.deductionOfInventory(wp.getWpId(),productCnt);
        /**
         * 库存记录  inventoryRecord
         * id
         * 业务id  business_id
         * 业务类型 1 订单 2 进货 business_type
         * 库存操作 1 加 2 减 inventory_operations
         * 数额 Amount
         */
        InventoryRecordEntity ir = new InventoryRecordEntity();
        ir.setAmount(1);
        ir.setBusinessId(omEntity.getOrderId());
        ir.setBusinessType("1");
        ir.setInventoryOperations("2");
        ir.insert();
        return true;
    }


    public boolean placeAnOrderDCart(Integer specsId, String productName, Integer productCnt, Double productPrice, BigDecimal paymentMoney,BigDecimal orderMoney) {
        boolean b = placeAnOrder(specsId, productName, productCnt, productPrice, paymentMoney, orderMoney);
        if (!b)return false;
        redisUtil.hdel("buyerCart:" + ShiroUtils.getLoginUserId(),String.valueOf(specsId));
        return true;
    }



}