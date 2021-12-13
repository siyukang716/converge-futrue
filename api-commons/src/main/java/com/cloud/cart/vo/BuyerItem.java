package com.cloud.cart.vo;

import com.cloud.product.entity.ProductSpecsEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BuyerItem implements Serializable {

    private static final long serialVersionUID = 1L;

    //SKu对象
    private ProductSpecsEntity sku;

    //是否有货
    private Boolean isHave = true;

    //购买的数量
    private Integer amount = 1;

    //商品图片
    private List pic;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) //比较地址
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuyerItem other = (BuyerItem) obj;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (sku.getSpecsId() !=other.sku.getSpecsId())
            return false;
        return true;
    }
}