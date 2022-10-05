package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.PermissionEntity;
import com.cloud.sys.RolePermissionEntity;
import com.cloud.sys.entity.IpConfigEntity;
import com.cloud.sys.entity.SysLogoinfoConfigEntity;
import com.cloud.sys.mapper.PermissionMapper;
import com.cloud.sys.mapper.SysLogoinfoConfigMapper;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> {
    private static Logger logger  = LoggerFactory.getLogger(PermissionServiceImpl.class);
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    private SysLogoinfoConfigMapper configMapper;
    @Autowired
    private RolePermissionServiceImpl rolePermissionService;

    @Autowired
    private IpConfigService ipConfigService;



    public List<PermissionEntity> getUserPerms(Long id) {
        return permissionMapper.getUserPerms(id);
    }

    /**
     * 动态加载权限列表
     * @return
     */
    public Map<String, String> loadFilterChainDefinitionMap() {
        // 权限控制map
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置过滤:不会被拦截的链接 -> 放行 start ----------------------------------------------------------
        // 放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/static/**", "anon");

        // 登陆
        filterChainDefinitionMap.put("/api/auth/login/**", "anon");
        // 三方登录
        filterChainDefinitionMap.put("/api/auth/loginByQQ", "anon");
        filterChainDefinitionMap.put("/api/auth/afterlogin.do", "anon");
        // 退出
        filterChainDefinitionMap.put("/api/auth/logout", "anon");
        // 放行未授权接口，重定向使用
        filterChainDefinitionMap.put("/api/auth/unauth", "anon");
        // token过期接口
        filterChainDefinitionMap.put("/api/auth/tokenExpired", "anon");
        // 被挤下线
        filterChainDefinitionMap.put("/api/auth/downline", "anon");
        // 放行 end ----------------------------------------------------------

        // 从数据库或缓存中查取出来的url与resources对应则不会被拦截 放行
//        List<Menu> permissionList = menuMapper.selectList( null );
//        if ( !CollectionUtils.isEmpty( permissionList ) ) {
//            permissionList.forEach( e -> {
//                if ( StringUtils.isNotBlank( e.getUrl() ) ) {
//                    // 根据url查询相关联的角色名,拼接自定义的角色权限
//                    List<Role> roleList = roleMapper.selectRoleByMenuId( e.getId() );
//                    StringJoiner zqRoles = new StringJoiner(",", "zqRoles[", "]");
//                    if ( !CollectionUtils.isEmpty( roleList ) ){
//                        roleList.forEach( f -> {
//                            zqRoles.add( f.getCode() );
//                        });
//                    }

                    // 注意过滤器配置顺序不能颠倒
                    // ① 认证登录
                    // ② 认证自定义的token过滤器 - 判断token是否有效
                    // ③ 角色权限 zqRoles：自定义的只需要满足其中一个角色即可访问  ;  roles[admin,guest] : 默认需要每个参数满足才算通过，相当于hasAllRoles()方法
                    // ④ zqPerms:认证自定义的url过滤器拦截权限  【注：多个过滤器用 , 分割】
//                    filterChainDefinitionMap.put( "/api" + e.getUrl(),"authc,token,roles[admin,guest],zqPerms[" + e.getResources() + "]" );
                   // filterChainDefinitionMap.put( "/api" + e.getUrl(),"authc,token,"+ zqRoles.toString() +",zqPerms[" + e.getResources() + "]" );
//                        filterChainDefinitionMap.put("/api/system/user/listPage", "authc,token,zqPerms[user1]"); // 写死的一种用法
//                }
//            });
//        }
//        // ⑤ 认证登录  【注：map不能存放相同key】
//        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }
    /**
     * 当菜单权限变化时 实现动态设置
     * @param shiroFilterFactoryBean
     * @param roleId
     * @param isRemoveSession
     */
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, Integer roleId, Boolean isRemoveSession) {
        synchronized (this) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                //throw new MyException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空拦截管理器中的存储
            manager.getFilterChains().clear();
            // 清空拦截工厂中的存储,如果不清空这里,还会把之前的带进去
            //            ps:如果仅仅是更新的话,可以根据这里的 map 遍历数据修改,重新整理好权限再一起添加
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            // 动态查询数据库中所有权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitionMap());
            // 重新构建生成拦截
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                manager.createChain(entry.getKey(), entry.getValue());
            }
            logger.info("--------------- 动态生成url权限成功！ ---------------");

        }
    }

    public Map getTheMenu() {
        Long loginUserId = ShiroUtils.getLoginUserId();
        List<Map<String,Object>> userPerms = permissionMapper.getMapUserPerms(loginUserId);

        Map<String,Object> map = new HashMap<>();

        Map<String,Object> homeInfo = new HashMap<>();
        homeInfo.put( "title","首页");
        homeInfo.put("href", "/page/welcome");
        map.put("homeInfo",homeInfo);


        List<SysLogoinfoConfigEntity> logoInfo = configMapper.selectList(null);
        map.put("logoInfo",logoInfo.get(0));

        List<Map<String,Object>> menuInfo = userPerms.stream()
                .filter(permission -> String.valueOf(permission.get("pId")).equals("0"))
                .map(permission -> covert(permission, userPerms)).collect(Collectors.toList());
        map.put("menuInfo",menuInfo);
        IpConfigEntity ipConfig = ipConfigService.getIpOne();
        map.put("ipConfig",ipConfig);
        return map;
    }




    /**
     * 将权限转换为带有子级的权限对象
     * 当找不到子级权限的时候map操作不会再递归调用covert
     */
    private Map<String,Object> covert(Map<String,Object> permission, List<Map<String,Object>> permissionList) {
        //Map<String,Object> node = new HashMap<String,Object>();
        //BeanUtils.copyProperties(permission, node);
        List<Map<String,Object>> children = permissionList.stream()
                .filter(subPermission -> String.valueOf(subPermission.get("pId")).equals(String.valueOf(permission.get("id"))))
                .map(subPermission -> covert(subPermission, permissionList)).collect(Collectors.toList());
        if (children.size()>0)
            permission.put("child",children);

        return permission;
    }

    public List<Map<String,Object>> getTermTreeBycompanyId(Long id, Long companyId) {
        QueryWrapper wrapper = new QueryWrapper();
        //a.is_del = 1 AND b.company_id = AND a.pid =
        wrapper.eq("a.is_del",1);
        wrapper.eq("b.company_id",companyId);
        wrapper.eq("a.pid",id);
       return permissionMapper.getTermTreeBycompanyId(wrapper);
    }

    public List<Map<String, Object>> getTermTreeByLoginCompanyId(Long id) {
        Long companyId = ShiroUtils.getLoginUser().getCompanyId();
        return getTermTreeBycompanyId(id,companyId);
    }

    /**
     * 默认给超级管理员添加功能
     * @param perm
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateCustomize(PermissionEntity perm) {
        Long id = perm.getId();
        saveOrUpdate(perm);
        if (null == id){
            RolePermissionEntity entity = new RolePermissionEntity();
            entity.setCompanyId(1l);
            entity.setRoleId(1l);
            entity.setPermitId(perm.getId());
            rolePermissionService.save(entity);
        }
    }
}
