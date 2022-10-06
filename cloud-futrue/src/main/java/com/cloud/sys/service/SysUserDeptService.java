package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.entity.SysUserDeptEntity;
import com.cloud.sys.mapper.SysUserDeptMapper;
import com.google.common.primitives.Longs;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @ClassName: SysUserDeptController
 * @Description: 部门用户关联
 * @author 豆芽菜
 * @date 2021-10-11
 */
@Service
public class SysUserDeptService extends ServiceImpl<SysUserDeptMapper, SysUserDeptEntity> {
    /**
     * 获取当前登录用户部门id
     * @return
     */
    public Long getDeptidByLoginUser(){
        LambdaQueryWrapper<SysUserDeptEntity> lambda = new QueryWrapper<SysUserDeptEntity>().lambda();
        lambda.eq(SysUserDeptEntity :: getUserId,ShiroUtils.getLoginUserId());
        List<SysUserDeptEntity> list = list(lambda);
        if (list.size() == 0)
            throw new RuntimeException("获取用户部门id失败");
        return list.get(0).getDeptId();
    }

    /**
     * 人员岗位调动
     * @param userIds
     * @param deptid
     */
    public void staffTransfer(String userIds, Long deptid) {
        long[] ids = Arrays.stream(userIds.split(",")).mapToLong(Long::parseLong).toArray();
        List<Long> uid = Longs.asList(ids);
        LambdaUpdateWrapper<SysUserDeptEntity> lambda = new UpdateWrapper<SysUserDeptEntity>().lambda();
        lambda.set(SysUserDeptEntity::getDeptId,deptid);
        lambda.in(SysUserDeptEntity::getUserId,uid);
        super.update(lambda);
    }
}