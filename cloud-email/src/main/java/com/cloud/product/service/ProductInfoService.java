package com.cloud.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.product.entity.ProductCategoryEntity;
import com.cloud.product.entity.ProductInfoEntity;
import com.cloud.product.entity.ProductPicInfoEntity;
import com.cloud.product.entity.ProductSpecsEntity;
import com.cloud.product.mapper.ProductInfoMapper;
import com.cloud.product.mapper.ProductPicInfoMapper;
import com.cloud.product.mapper.ProductSpecsMapper;
import com.cloud.product.vo.ProductInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: ProductInfoEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wjg
 * @date 2021-08-05
 */
@Service
public class ProductInfoService extends ServiceImpl<ProductInfoMapper, ProductInfoEntity> {
    @Autowired
    private ProductInfoMapper mapper;
    @Autowired
    private ProductPicInfoMapper picInfoMapper;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductSpecsMapper specsMapper;
    public IPage<ProductInfoVo> selectPagePartner(Page<ProductInfoEntity> p, QueryWrapper<ProductInfoEntity> wrapper) {
        IPage<ProductInfoVo> page = mapper.selectPagePartner(p, wrapper);
        List<ProductInfoVo> records = page.getRecords();
        List<Integer> ids = new ArrayList<>();
        records.forEach(e->{
            ids.add(e.getOneCategoryId());
            ids.add(e.getTwoCategoryId());
            ids.add(e.getThreeCategoryId());
        });
        List<Integer> collect = ids.stream().distinct().collect(Collectors.toList());
        LambdaQueryWrapper<ProductCategoryEntity> lambda = new QueryWrapper<ProductCategoryEntity>().lambda();
        lambda.in(ProductCategoryEntity :: getCategoryId,collect);
        List<ProductCategoryEntity> list = productCategoryService.list(lambda);
        Map<Integer, String> map = list.stream().collect(Collectors.toMap(ProductCategoryEntity::getCategoryId, ProductCategoryEntity::getCategoryName));
        records.forEach(e->{
            e.setOneCategoryName(map.get(e.getOneCategoryId()));
            e.setTwoCategoryName(map.get(e.getTwoCategoryId()));
            e.setThreeCategoryName(map.get(e.getThreeCategoryId()));
        });
        return page;
    }

    public IPage<ProductInfoEntity> getProductPage(Page<ProductInfoEntity> p) {

        IPage<ProductInfoEntity> page = this.page(p);
        page.getRecords().forEach(e->{
            LambdaQueryWrapper<ProductPicInfoEntity> wrapper = new QueryWrapper<ProductPicInfoEntity>().lambda();
            wrapper.eq(ProductPicInfoEntity::getProductId,e.getProductId());
            List<ProductPicInfoEntity> piclist = getPicByid(e.getProductId());
            Map<Integer, List<ProductPicInfoEntity>> picMap =
                    piclist.stream().collect(Collectors.groupingBy(ProductPicInfoEntity::getIsMaster));
            e.setPic(picMap);
        });
        return page;
    }

    public Map getProductDetails(Integer productId) {
        ProductInfoEntity product = this.getById(productId);
        List<ProductPicInfoEntity> pic = getPicByid(productId);
        LambdaQueryWrapper<ProductSpecsEntity> wrapper = new QueryWrapper<ProductSpecsEntity>().lambda();
        wrapper.eq(ProductSpecsEntity::getProductId, productId);
        List<ProductSpecsEntity> specs = specsMapper.selectList(wrapper);
        Map map = new HashMap();
        map.put("product",product);
        map.put("pic",pic);
        map.put("specs",specs);
        return  map;
    }

    /**
     * 根据商品id 获取商品图片信息
     * @param productId
     * @return
     */
    private List<ProductPicInfoEntity> getPicByid(Integer productId){
        LambdaQueryWrapper<ProductPicInfoEntity> wrapper = new QueryWrapper<ProductPicInfoEntity>().lambda();
        wrapper.eq(ProductPicInfoEntity::getProductId,productId);
        return picInfoMapper.selectList(wrapper);
    }

    /**
     * 销售信息展示
     * @param p
     * @return
     */
    public IPage getsales(Page p) {
        QueryWrapper<ProductInfoEntity> wrapper = new QueryWrapper<>();
        Page<Map> map = mapper.getsales(p,wrapper);
        return map;
    }
}