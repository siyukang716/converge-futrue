package com.cloud.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.ProductInfoEntity;
import com.cloud.product.vo.ProductInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *
 * @ClassName: ProductInfoMapper
 * @Description: TODO(这里用一句话描述这个类的作用)商品信息表
 * @author wjg
 * @date 2021-08-05
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfoEntity> {

    IPage<ProductInfoVo> selectPagePartner(Page<ProductInfoEntity> p, @Param("ew")QueryWrapper<ProductInfoEntity> ew);

    Page<Map> getsales(Page p, @Param("ew")QueryWrapper<ProductInfoEntity> wrapper);
}