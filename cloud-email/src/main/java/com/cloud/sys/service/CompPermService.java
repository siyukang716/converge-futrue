package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.sys.RolePermissionEntity;
import com.cloud.sys.entity.CompPermEntity;
import com.cloud.sys.mapper.CompPermMapper;
import com.google.common.primitives.Longs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 *
 * @ClassName: CompPermController
 * @Description: 公司权限
 * @author 小可爱
 * @date 2021-11-16
 */
@Service
public class CompPermService extends ServiceImpl<CompPermMapper, CompPermEntity> {

    @Autowired
    private RolePermissionServiceImpl rolePermissionService;


    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(String perids, Long companyId) {
        long[] ids = Arrays.stream(perids.split(",")).mapToLong(Long::parseLong).toArray();
        int len = ids.length;
        for (int i = 0; i < len; i++) {
            LambdaQueryWrapper<CompPermEntity> lambda = new QueryWrapper<CompPermEntity>().lambda();
            lambda.eq(CompPermEntity::getCompanyId,companyId).eq(CompPermEntity::getPermId,ids[i]);
            CompPermEntity f = getOne(lambda);
            if (null == f){
                CompPermEntity entity = new CompPermEntity();
                entity.setCompanyId(companyId);
                entity.setPermId(ids[i]);
                super.save(entity);
            }
        }
        return true;
    }
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(String perids, Long companyId) {
        long[] ids = Arrays.stream(perids.split(",")).mapToLong(Long::parseLong).toArray();
        LambdaUpdateWrapper<CompPermEntity> lambda = new UpdateWrapper<CompPermEntity>().lambda();
        lambda.eq(CompPermEntity::getCompanyId,companyId);
        lambda.in(CompPermEntity::getPermId,Longs.asList(ids));
        super.remove(lambda);
        LambdaUpdateWrapper<RolePermissionEntity> wrapper = new UpdateWrapper<RolePermissionEntity>().lambda();
        wrapper.in(RolePermissionEntity::getPermitId,ids);
        wrapper.eq(RolePermissionEntity::getCompanyId,companyId);
        rolePermissionService.remove(wrapper);
        return true;
    }
}