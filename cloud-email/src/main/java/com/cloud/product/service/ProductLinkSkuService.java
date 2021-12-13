package com.cloud.product.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.product.entity.ProductLinkSkuEntity;
import com.cloud.product.mapper.ProductLinkSkuMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: ProductLinkSkuEntityService
 * @Description: 商品 属性关联表
 * @author wjg
 * @date 2021-08-25
 */
@Service
public class ProductLinkSkuService extends ServiceImpl<ProductLinkSkuMapper, ProductLinkSkuEntity> {

    public boolean saveOrUpdateL(Integer productId, String keys) {
        ProductLinkSkuEntity skuEntity = new ProductLinkSkuEntity();
        skuEntity.setProductId(productId);
        LambdaUpdateWrapper<ProductLinkSkuEntity> lambda = new UpdateWrapper<ProductLinkSkuEntity>().lambda();
        lambda.eq(ProductLinkSkuEntity :: getProductId,productId);
        this.remove(lambda);
        String[] split = keys.split(",");
        for (String s : split) {
            skuEntity.setMakId(Integer.valueOf(s));
            save(skuEntity);
        }
        return true;
    }
}