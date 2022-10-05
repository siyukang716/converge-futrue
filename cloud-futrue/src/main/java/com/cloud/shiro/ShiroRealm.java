package com.cloud.shiro;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.sys.PermissionEntity;
import com.cloud.sys.RoleEntity;
import com.cloud.sys.UserEntity;
import com.cloud.sys.mapper.PermissionMapper;
import com.cloud.sys.mapper.RoleMapper;
import com.cloud.sys.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
    //private static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    /**
     * 授权用户权限
     * 授权的方法是在碰到<shiro:hasPermission name=''></shiro:hasPermission>标签的时候调用的
     * 它会去检测shiro框架中的权限(这里的permissions)是否包含有该标签的name值,如果有,里面的内容显示
     * 如果没有,里面的内容不予显示(这就完成了对于权限的认证.)
     *
     * shiro的权限授权是通过继承AuthorizingRealm抽象类，重载doGetAuthorizationInfo();
     * 当访问到页面的时候，链接配置了相应的权限或者shiro标签才会执行此方法否则不会执行
     * 所以如果只是简单的身份认证没有权限的控制的话，那么这个方法可以不进行实现，直接返回null即可。
     *
     * 在这个方法中主要是使用类：SimpleAuthorizationInfo 进行角色的添加和权限的添加。
     * authorizationInfo.addRole(role.getRole()); authorizationInfo.addStringPermission(p.getPermission());
     *
     * 当然也可以添加set集合：roles是从数据库查询的当前用户的角色，stringPermissions是从数据库查询的当前用户对应的权限
     * authorizationInfo.setRoles(roles); authorizationInfo.setStringPermissions(stringPermissions);
     *
     * 就是说如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "perms[权限添加]");
     * 就说明访问/add这个链接必须要有“权限添加”这个权限才可以访问
     *
     * 如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "roles[100002]，perms[权限添加]");
     * 就说明访问/add这个链接必须要有 "权限添加" 这个权限和具有 "100002" 这个角色才可以访问
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //授权
        log.debug("授予角色和权限");
        //添加权限  和   角色信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        UserEntity user = (UserEntity) subject.getPrincipal();
//        if (user.getMobile().equals("18516596566")) {
//            // 超级管理员，添加所有角色、添加所有权限
//            authorizationInfo.addRole("*");
//            authorizationInfo.addStringPermission("*");
//        }else {
//            普通用户，查询用户的角色，更具角色查询权限
        Long id = user.getId();
        List<RoleEntity> roles = roleMapper.selectRolesByUid(id);
        roles.forEach(e->{
            authorizationInfo.addRole(e.getCode());
            List<PermissionEntity> perms = this.permissionMapper.selectPermissionByUid(e.getId());
            perms.forEach(el->{
                authorizationInfo.addStringPermission(el.getCode());
            });
        });




//        }
        return authorizationInfo;
    }
    /**
     * 登录认证
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        //UsernamePasswordToken用于存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
//        log.info("用户登录认证：验证当前Subject时获取到token为：" + ReflectionToStringBuilder
//                .toString(token, ToStringStyle.MULTI_LINE_STYLE));
        log.info("web--------------------------权限验证------------------------------");
        log.info("用户登录认证：验证当前Subject时获取到token为："+JSONObject.toJSONString(token));
        String username = token.getUsername();
        String password = new String(token.getPassword());
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getMobile,username);
        UserEntity user = userMapper.selectOne(wrapper);
        log.debug("用户登录认证！用户信息user：" + user);
        if (user == null) {
            // 用户不存在
            throw new UnknownAccountException("用户不存在！");
        }
//        if (!password.equals(user.getPassword())) {
//            throw new IncorrectCredentialsException("用户名或密码错误！");
//        }
//        if ("1".equals(user.getState())) {
//            throw new LockedAccountException("账号已被锁定,请联系管理员！");
//        }
//        CustomerLoginLogEntity logEntity = new CustomerLoginLogEntity();
//        logEntity.setCustomerId(existUser.getId());
//        logEntity.setLoginIp(IpUtil.getIpAddr(request));
//        logEntity.setLoginType(1);
//        logEntity.setLoginTime(new Date());
        // 密码存在
        // 第一个参数 ，登陆后，需要在session保存数据
        // 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
        // 第三个参数 ，realm名字
        //return new SimpleAuthenticationInfo(user, DigestUtils.md5Hex(user.getPassword()), getName());
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
        //return new SimpleAuthenticationInfo(user, user.getPassword(),new MyByteSource(user.getUsername()),getName());
    }

    /**
     * 清除所有缓存【实测无效】
     */
    public void clearCachedAuth(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }




    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
