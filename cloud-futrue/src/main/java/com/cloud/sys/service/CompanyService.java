package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shiro.PasswordEncryption;
import com.cloud.sys.*;
import com.cloud.sys.entity.CompanyEntity;
import com.cloud.sys.entity.SysUserDeptEntity;
import com.cloud.sys.mapper.CompanyMapper;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @ClassName: CompanyController
 * @Description: 公司维护
 * @author 豆芽菜
 * @date 2021-11-15
 */
@Service
public class CompanyService extends ServiceImpl<CompanyMapper, CompanyEntity> {

    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRoleServiceImpl userRoleService;
    @Autowired
    private SysUserDeptService userDeptService;
    @Autowired
    private RolePermissionServiceImpl rolePermissionService;

    /**
     * 公司注册事件
     * @param entity
     * @param userName
     * @param password
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveCustomize(CompanyEntity entity,String userName,String password) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<CompanyEntity> wrapper = Wrappers.<CompanyEntity>lambdaUpdate();
        if (null == entity.getCompanyId()){//公司新增
            LambdaQueryWrapper<UserEntity> l = new LambdaQueryWrapper<>();
            l.eq(UserEntity::getMobile,userName);
            UserEntity one = userService.getOne(l);
            if (one != null){
                result.setMessage("用户名重复o(╥﹏╥)o");
                return result;
            }
            save(entity);
            Long companyId = entity.getCompanyId();
            RoleEntity roleObj = new RoleEntity();
            roleObj.setCompanyId(companyId);
            roleObj.setCode("superman");
            roleObj.setRoleName(entity.getCompanyName()+"管理员");
            roleObj.setDescpt("管理员");
            roleObj.setPid(0l);
            roleObj.setId(companyId);
            roleService.save(roleObj);
            SysDeptEntity sysDeptEntity = new SysDeptEntity();
            sysDeptEntity.setDeptName(entity.getCompanyName());
            sysDeptEntity.setParentDeptId(-1l);
            sysDeptEntity.setCompanyId(entity.getCompanyId());
            sysDeptEntity.setDeptId(companyId);
            deptService.save(sysDeptEntity);
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(entity.getCompanyName()+"管理员");
            userEntity.setMobile(userName);
            userEntity.setCompanyId(entity.getCompanyId());
            userEntity.setPassword(PasswordEncryption.getMD5Password(password,""));
            userService.save(userEntity);
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRoleId(roleObj.getId());
            userRoleEntity.setUserId(userEntity.getId());
            userRoleService.save(userRoleEntity);
            SysUserDeptEntity userDeptEntity = new SysUserDeptEntity();
            userDeptEntity.setUserId(userEntity.getId());
            userDeptEntity.setDeptId(sysDeptEntity.getDeptId());
            userDeptService.save(userDeptEntity);
            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setCompanyId(companyId);
            rolePermission.setRoleId(companyId);
            rolePermission.setPermitId(1442413683209932801l);
            rolePermissionService.save(rolePermission);
            rolePermission.setPermitId(4l);
            rolePermissionService.save(rolePermission);
            rolePermission.setPermitId(1442413285573136385l);
            rolePermissionService.save(rolePermission);
        }else {
            updateById(entity);
        }

        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
        return  result;
    }
}