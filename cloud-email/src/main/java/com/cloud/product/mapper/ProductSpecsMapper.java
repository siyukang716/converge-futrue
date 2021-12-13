package com.cloud.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.product.entity.ProductSpecsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @ClassName: ProductSpecsMapper
 * @Description: TODO(这里用一句话描述这个类的作用)商品 sku信息
 * @author wjg
 * @date 2021-08-26
 */
public interface ProductSpecsMapper extends BaseMapper<ProductSpecsEntity> {

    List<ProductSpecsEntity> getCartSku(@Param("ew") QueryWrapper<ProductSpecsEntity> wrapper);
}