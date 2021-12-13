package com.cloud.applets.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.applets.entity.AppletsUserRelationEntity;
import com.cloud.applets.mapper.AppletsUserRelationMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: AppletsUserRelationController
 * @Description:   小程序系统用户关联
 * @author 小可爱
 * @date 2021-11-13
 */
@Service
public class AppletsUserRelationService extends ServiceImpl<AppletsUserRelationMapper, AppletsUserRelationEntity> {
    /**
     * 检索
     * @param wxUserId
     * @return
     */
    public AppletsUserRelationEntity getOneCustomize(Long wxUserId) {
        LambdaQueryWrapper<AppletsUserRelationEntity> lambda = new QueryWrapper<AppletsUserRelationEntity>().lambda();
        lambda.eq(AppletsUserRelationEntity::getWxUserId,wxUserId);
        return getOne(lambda);
    }
}