package com.cloud.applets.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.applets.entity.AppletsUserEntity;
import com.cloud.applets.mapper.AppletsUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: AppletsUserController
 * @Description: 小程序用户表
 * @author 小可爱
 * @date 2021-11-13
 */
@Service
public class AppletsUserService extends ServiceImpl<AppletsUserMapper, AppletsUserEntity> {


    /**
     * 根据条件检索对象
     * @param openId  微信用户id
     * @param unionId 用户小程序唯一id
     * @return
     */
    public AppletsUserEntity getOneCustomize(String openId,String unionId){
        LambdaQueryWrapper<AppletsUserEntity> lambda = new QueryWrapper<AppletsUserEntity>().lambda();
        lambda.eq(!StrUtil.isBlank(openId),AppletsUserEntity::getOpenId,openId)
        .eq(!StrUtil.isBlank(unionId),AppletsUserEntity::getUnionId,unionId);
        return getOne(lambda);
    }
}