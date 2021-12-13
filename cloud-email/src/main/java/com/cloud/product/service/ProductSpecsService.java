package com.cloud.product.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.product.entity.ProductSpecsEntity;
import com.cloud.product.entity.ProductSpecsSkuEntity;
import com.cloud.product.mapper.ProductSpecsMapper;
import com.cloud.product.mapper.ProductSpecsSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: ProductSpecsEntityService
 * @Description: 商品 sku信息
 * @author wjg
 * @date 2021-08-26
 */
@Service
public class ProductSpecsService extends ServiceImpl<ProductSpecsMapper, ProductSpecsEntity> {

    @Autowired
    private ProductSpecsSkuMapper skuMapper;

    public boolean saveOrUpdate(ProductSpecsEntity entity,String sku){
        this.saveOrUpdate(entity);
        LambdaUpdateWrapper<ProductSpecsSkuEntity> wrapper = Wrappers.<ProductSpecsSkuEntity>lambdaUpdate();
        wrapper.eq(ProductSpecsSkuEntity :: getSpecsId,entity.getSpecsId());
        skuMapper.delete(wrapper);
        String[] skus = sku.split("@@@@");
        ProductSpecsSkuEntity skuEntity = null;
        for (String s : skus) {
            String[] skuObj = s.split("@&@");
            skuEntity = new ProductSpecsSkuEntity();
            skuEntity.setSpecsId(entity.getSpecsId());
            skuEntity.setKey(skuObj[0]);
            skuEntity.setValue(skuObj[1]);
            skuMapper.insert(skuEntity);
        }


        return true;
    }
}