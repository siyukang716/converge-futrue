package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shiro.ShiroRealm;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.RolePermissionEntity;
import com.cloud.sys.mapper.RolePermissionMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> {
    @Autowired
    RolePermissionMapper rolePermissionMapper;


    @Transactional(rollbackFor = Exception.class)
    public void addObjs(String prems, Long roleid) {
        String[] split = prems.split(",");
        RolePermissionEntity entity = null;
        Long companyId = ShiroUtils.getLoginUser().getCompanyId();
        LambdaQueryWrapper<RolePermissionEntity> wrapper = null;
        for (String permId : split) {
            entity = new RolePermissionEntity();
            entity.setRoleId(roleid);
            entity.setPermitId(Long.valueOf(permId));
            entity.setCompanyId(companyId);
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RolePermissionEntity::getPermitId,permId).eq(RolePermissionEntity::getRoleId,roleid)
                    .eq(RolePermissionEntity::getCompanyId,companyId);
            int count = this.count(wrapper);
            if (count == 0)
                rolePermissionMapper.insert(entity);
        }
        //添加成功之后 清除缓存
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();
        //清除权限 相关的缓存
        shiroRealm.clearAllCache();
    }
    @Transactional(rollbackFor = Exception.class)
    public void delObjs(String prems, Long roleid) {
        String[] split = prems.split(",");
        Long companyId = ShiroUtils.getLoginUser().getCompanyId();
        RolePermissionEntity entity = null;
        LambdaQueryWrapper<RolePermissionEntity> wrapper = null;
        for (String permId : split) {
            entity = new RolePermissionEntity();
            entity.setRoleId(roleid);
            entity.setPermitId(Long.valueOf(permId));
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RolePermissionEntity::getPermitId,permId).eq(RolePermissionEntity::getRoleId,roleid)
                    .eq(RolePermissionEntity::getCompanyId,companyId);
            this.remove(wrapper);
        }
        //添加成功之后 清除缓存
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)SecurityUtils.getSecurityManager();
        ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();
        //清除权限 相关的缓存
        shiroRealm.clearAllCache();

    }
}
