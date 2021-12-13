package com.cloud.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.product.entity.MmallAttributeKeyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: MmallAttributeKeyMapper
 * @Description: TODO(这里用一句话描述这个类的作用)商品spu 属性key表
 * @author wjg
 * @date 2021-08-24
 */
public interface MmallAttributeKeyMapper extends BaseMapper<MmallAttributeKeyEntity> {

    List<Map<String,String>> getlistMaps(@Param("ew") QueryWrapper<MmallAttributeKeyEntity> lambda);
}