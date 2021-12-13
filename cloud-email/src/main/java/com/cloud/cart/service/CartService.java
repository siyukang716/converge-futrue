package com.cloud.cart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.cart.vo.BuyerCart;
import com.cloud.cart.vo.BuyerItem;
import com.cloud.order.entity.WarehouseProductEntity;
import com.cloud.order.mapper.WarehouseProductMapper;
import com.cloud.product.entity.ProductSpecsEntity;
import com.cloud.product.mapper.ProductSpecsMapper;
import com.cloud.shiro.ShiroUtils;
import com.cloud.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: CartService
 * @Author lenovo
 * @Date: 2021/9/3 11:00
 * @Version 1.0
 */
@Service
public class CartService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProductSpecsMapper productSpecsMapper;
    @Autowired
    private WarehouseProductMapper warehouseProductMapper;
    //保存购物车到Redis中
    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
        List<BuyerItem> items = buyerCart.getItems();
        if (items.size() > 0) {
            //redis中保存的是skuId 为key , amount 为value的Map集合
            Map<String, Object> hash = new HashMap<String, Object>();
            for (BuyerItem item : items) {
                //判断是否有同款
                if (redisUtil.hHasKey("buyerCart:" + username, String.valueOf(item.getSku().getSpecsId()))) {
                    redisUtil.hincr("buyerCart:" + username, String.valueOf(item.getSku().getSpecsId()), item.getAmount());

                } else {
                    hash.put(String.valueOf(item.getSku().getSpecsId()), item.getAmount());

                }

            }
            if (hash.size() > 0) {
                redisUtil.hmset("buyerCart:" + username, hash);

            }

        }


    }

    //取出Redis中购物车
    public BuyerCart selectBuyerCartFromRedis(String username) {
        BuyerCart buyerCart = new BuyerCart();
        //获取所有商品, redis中保存的是skuId 为key , amount 为value的Map集合
        Map<Object, Object> hgetAll = redisUtil.hmget("buyerCart:" + username);
        Set<Map.Entry<Object, Object>> entrySet = hgetAll.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            //entry.getKey(): skuId
            ProductSpecsEntity sku = new ProductSpecsEntity();
            sku.setSpecsId(Integer.valueOf((String) entry.getKey()));
            BuyerItem buyerItem = new BuyerItem();
            buyerItem.setSku(sku);
            //entry.getValue(): amount
            buyerItem.setAmount((Integer) entry.getValue());
            //添加到购物车中
            buyerCart.addItem(buyerItem);
        }

        return buyerCart;
    }


    //向购物车中的购物项 添加相应的数据, 通过skuId 查询sku对象, 颜色对象, 商品对象
    public void selectSkuById(List<BuyerItem> items) {
        List<Integer> collect = items.stream().map(e -> e.getSku().getSpecsId()).collect(Collectors.toList());
        QueryWrapper<ProductSpecsEntity> wrapper = new QueryWrapper<>();
        wrapper.in("ps.specs_id",collect);
        List<ProductSpecsEntity> list = productSpecsMapper.getCartSku(wrapper);
        Map<Integer, ProductSpecsEntity> m = list.stream().collect(Collectors.toMap(ProductSpecsEntity::getSpecsId, a -> a, (k1, k2) -> k1));
        items.forEach(e->e.setSku(m.get(e.getSku().getSpecsId())));
        //return items;
    }

    public BuyerCart selectBuyerCartFromRedisBySkuIds(String[] skuIds, String username) {
        BuyerCart buyerCart = new BuyerCart();
        //获取所有商品, redis中保存的是skuId 为key , amount 为value的Map集合
        Map<Object, Object> hgetAll = redisUtil.hmget("buyerCart:" + username);
        if (null != hgetAll && hgetAll.size() > 0) {
            Set<Map.Entry<Object, Object>> entrySet = hgetAll.entrySet();
            for (Map.Entry<Object, Object> entry : entrySet) {
                for (String skuId : skuIds) {
                    if (skuId.equals(entry.getKey())) {
                        //entry.getKey(): skuId
                        ProductSpecsEntity sku = new ProductSpecsEntity();
                        sku.setSpecsId((Integer) entry.getKey());
                        BuyerItem buyerItem = new BuyerItem();
                        buyerItem.setSku(sku);
                        //entry.getValue(): amount
                        buyerItem.setAmount((Integer)entry.getValue());
                        //添加到购物车中
                        buyerCart.addItem(buyerItem);
                    }
                }
            }
        }

        return buyerCart;
    }

    public Integer getInventory(Integer id){
        WarehouseProductEntity warehouseProductEntity = warehouseProductMapper.selectById(id);
        return warehouseProductEntity.getCurrentCnt();
    }

    public boolean delById(Integer specsId) {
        redisUtil.hdel("buyerCart:" + ShiroUtils.getLoginUserId(),String.valueOf(specsId));
        return true;
    }
}
