package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cloud.sys.entity.DictSeqEntity;
import com.cloud.sys.mapper.DictSeqMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: DictSeqController
 * @Description: 字典序列
 * @author 小可爱
 * @date 2021-11-21
 */
@Service
public class DictSeqService extends ServiceImpl<DictSeqMapper, DictSeqEntity> {

    /**
     * 更加业务类型获取字典对象
     * @param type
     * @return
     */
    public DictSeqEntity getDictByType(String type){
        LambdaQueryWrapper<DictSeqEntity> lambda = new QueryWrapper<DictSeqEntity>().lambda();
        lambda.eq(DictSeqEntity::getBusinessType,type);
        return super.getOne(lambda);
    }

    public void update(String type,Integer num){
        LambdaUpdateWrapper<DictSeqEntity> lambda = new UpdateWrapper<DictSeqEntity>().lambda();
        lambda.set(DictSeqEntity::getSerialNumber,num);
        lambda.eq(DictSeqEntity::getBusinessType,type);
        update(lambda);
    }

}