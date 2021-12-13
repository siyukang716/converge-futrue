package com.cloud.product.service;

import com.cloud.product.entity.ProductCategoryEntity;
import com.cloud.product.mapper.ProductCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: ProductCategoryEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wjg
 * @date 2021-08-05
 */
@Service
public class ProductCategoryService extends ServiceImpl<ProductCategoryMapper, ProductCategoryEntity> {
    @Autowired
    private ProductCategoryMapper mapper;
    public List<Map<String, Object>> ztree(Integer categoryId) {
        List<Map<String, Object>> maps = mapper.ztree(categoryId);
        return maps;
    }
}