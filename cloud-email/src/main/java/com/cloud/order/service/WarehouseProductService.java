package com.cloud.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.order.entity.InventoryRecordEntity;
import com.cloud.order.entity.WarehouseProductEntity;
import com.cloud.order.mapper.WarehouseProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @ClassName: WarehouseProductEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wjg
 * @date 2021-08-05
 */
@Service
public class WarehouseProductService extends ServiceImpl<WarehouseProductMapper, WarehouseProductEntity> {

    /**
     * 根据 商品id 获取商品可用库存 返回库存对象
     * @param specsId
     * @param productCnt
     */
    public WarehouseProductEntity commodityAvailableInventory(Integer specsId, Integer productCnt){
        // 根据商品型号id 查询库存
        LambdaQueryWrapper<WarehouseProductEntity> lambda = new QueryWrapper<WarehouseProductEntity>().lambda();
        lambda.eq(WarehouseProductEntity::getProductId,specsId).gt(WarehouseProductEntity::getCurrentCnt,productCnt);
        List<WarehouseProductEntity> list = super.list(lambda);
        if (list.size() >0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 扣减库存
     *
     * @param productCnt
     * @param wpId
     * @return
     */
    public boolean deductionOfInventory(Integer wpId,Integer productCnt ){
        LambdaUpdateWrapper<WarehouseProductEntity> lambda = new UpdateWrapper<WarehouseProductEntity>().lambda();
        lambda.setSql(" current_cnt = current_cnt-" + productCnt);
        lambda.eq(WarehouseProductEntity::getWpId,wpId);
        return super.update(lambda);
    }

    public boolean aOrRInventory(Integer wpId, String businessType, String inventoryOperations, Integer amount) {
        if ("2".equals(inventoryOperations)){
            WarehouseProductEntity wp = this.getById(wpId);
            if (wp.getCurrentCnt() < amount)return false;
        }
        LambdaUpdateWrapper<WarehouseProductEntity> lambda = new UpdateWrapper<WarehouseProductEntity>().lambda();
        lambda.setSql("1".equals(inventoryOperations)," current_cnt = current_cnt+" + amount);
        lambda.setSql("2".equals(inventoryOperations)," current_cnt = current_cnt-" + amount);
        lambda.eq(WarehouseProductEntity::getWpId,wpId);
        super.update(lambda);
        InventoryRecordEntity log = new InventoryRecordEntity();
        log.setInventoryOperations(inventoryOperations);
        log.setBusinessId(wpId);
        log.setBusinessType(businessType);
        log.setAmount(amount);
        log.insert();

        return true;
    }
}