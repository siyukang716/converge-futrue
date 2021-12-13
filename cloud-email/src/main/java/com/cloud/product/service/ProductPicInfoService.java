package com.cloud.product.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.product.entity.ProductPicInfoEntity;
import com.cloud.product.mapper.ProductPicInfoMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: ProductPicInfoEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wjg
 * @date 2021-08-05
 */
@Service
public class ProductPicInfoService extends ServiceImpl<ProductPicInfoMapper, ProductPicInfoEntity> {

    public boolean saveOrUpdate(ProductPicInfoEntity entity){
        if (entity.getIsMaster() == 1){
            LambdaUpdateWrapper<ProductPicInfoEntity> wrapper = Wrappers.<ProductPicInfoEntity>lambdaUpdate();
            wrapper.eq(ProductPicInfoEntity::getProductId,entity.getProductId()).set(ProductPicInfoEntity::getIsMaster,0);
            this.update(wrapper);
        }
        return super.saveOrUpdate(entity);
    }
}