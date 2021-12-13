package com.cloud.product.mapper;

import com.cloud.product.entity.ProductCategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: ProductCategoryMapper
 * @Description: TODO(这里用一句话描述这个类的作用)商品分类表
 * @author wjg
 * @date 2021-08-05
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategoryEntity> {

    List<Map<String, Object>> ztree(@Param("pId") Integer id);
}