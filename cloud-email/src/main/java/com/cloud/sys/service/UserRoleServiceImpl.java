package com.cloud.sys.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import com.cloud.sys.UserRoleEntity;
import com.cloud.sys.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity>{

    @Autowired
    private UserServiceImpl userService;



    /**
     *赋予用户角色
     * @param ur
     */
    public void insertRole(UserRoleEntity ur) {
        this.lambdaUpdate().eq(UserRoleEntity::getUserId,ur.getUserId()).remove();
        ur.insert();
    }
    @Transactional(rollbackFor = Exception.class)
    public void setRole(UserRoleEntity ur, Integer version) {

        insertRole(ur);
        UserEntity user = new UserEntity();
        user.setId(ur.getUserId());
        user.setVersion(version);
        userService.updateById(user);
    }

    /**
     * 获取当前登录用户角色id
     * @return
     */
    public Long getLoginRoleId(){
        LambdaQueryWrapper<UserRoleEntity> lambda = new QueryWrapper<UserRoleEntity>().lambda();
        lambda.eq(UserRoleEntity::getUserId, ShiroUtils.getLoginUserId());
        Long roleId = getOne(lambda).getRoleId();
        return roleId;
    }
}
