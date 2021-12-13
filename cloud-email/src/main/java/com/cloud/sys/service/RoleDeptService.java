package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.sys.entity.RoleDeptEntity;
import com.cloud.sys.mapper.RoleDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: RoleDeptController
 * @Description: 角色部门数据权限
 * @author 小可爱
 * @date 2021-11-18
 */
@Service
public class RoleDeptService extends ServiceImpl<RoleDeptMapper, RoleDeptEntity> {
    @Autowired
    private RoleDeptMapper roleDeptMapper;
    @Autowired
    private UserRoleServiceImpl userRoleService;
    public IPage<Map> pageCustomize(Page p,Long roleId) {
        QueryWrapper<RoleDeptEntity> wrapper = new QueryWrapper<RoleDeptEntity>();
        wrapper.eq("sud.role_id",roleId);
        return roleDeptMapper.pageCustomize(p,wrapper);
    }


    /**
     * 获取当前登录人角色的部门使用数据权限
     */
    public List<RoleDeptEntity> getDeptByRole(){
        Long roleId = userRoleService.getLoginRoleId();
        LambdaQueryWrapper<RoleDeptEntity> lambda = new QueryWrapper<RoleDeptEntity>().lambda();
        lambda.eq(RoleDeptEntity::getRoleId,roleId);
        List<RoleDeptEntity> list = super.list(lambda);
        return list;
    }
    /**
     * 获取当前登录人角色的部门使用数据权限  部门id集合
     */
    public Set getDeptByRoleSet(){
        List<RoleDeptEntity> lis = getDeptByRole();
        if (null == lis || lis.size() == 0)return null;
        Set<Long> collect = lis.stream().map(RoleDeptEntity::getDeptId).collect(Collectors.toSet());
        return collect;
    }
}