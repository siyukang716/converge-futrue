package com.cloud.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.product.entity.MmallAttributeKeyEntity;
import com.cloud.product.entity.ProductLinkSkuEntity;
import com.cloud.product.mapper.MmallAttributeKeyMapper;
import com.cloud.product.mapper.ProductLinkSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wjg
 * @ClassName: MmallAttributeKeyEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2021-08-24
 */
@Service
public class MmallAttributeKeyService extends ServiceImpl<MmallAttributeKeyMapper, MmallAttributeKeyEntity> {
    @Autowired
    private ProductLinkSkuMapper linkSkuMapper;
    @Autowired
    private  MmallAttributeKeyMapper mapper;

    /**
     * 根据商品id 查询中间表获取商品sku信息
     * @param productId
     * @return
     */
    public List<MmallAttributeKeyEntity> getProByPid(Integer productId) {
        List<Integer> makids = getMakids(productId);
        LambdaQueryWrapper<MmallAttributeKeyEntity> mwapper = Wrappers.<MmallAttributeKeyEntity>lambdaQuery();
        mwapper.in(MmallAttributeKeyEntity::getMakId,makids);
        return this.list(mwapper);
    }

    /**
     * 获取当前商品 sku  的key value  返回map
     * @param productId
     * @return
     */
    public Map getKeyandVal(Integer productId) {
        List<Integer> makids = getMakids(productId);
        QueryWrapper<MmallAttributeKeyEntity> lambda = new QueryWrapper<MmallAttributeKeyEntity>();
        lambda.in("k.mak_id",makids);
        List<Map<String,String>> list = mapper.getlistMaps(lambda);
        Map<String, List<Map<String, String>>> map = list.stream().collect(Collectors.groupingBy(e -> {
            return e.get("keyname");
        }));
        return map;
    }

    /**
     * 根据 商品id  获取商品key  ids
     * @param productId
     * @return
     */
    private List<Integer> getMakids(Integer productId){
        LambdaQueryWrapper<ProductLinkSkuEntity> lambda = Wrappers.<ProductLinkSkuEntity>lambdaQuery();
        lambda.select(ProductLinkSkuEntity::getMakId).eq(ProductLinkSkuEntity::getProductId,productId);
        List<ProductLinkSkuEntity> productLinkSku = linkSkuMapper.selectList(lambda);
        List<Integer> makids = productLinkSku.stream().map(ProductLinkSkuEntity::getMakId).collect(Collectors.toList());
        return makids;
    }
}