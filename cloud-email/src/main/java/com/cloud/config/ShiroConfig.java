package com.cloud.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.cloud.config.shiro.*;
import com.cloud.filter.KickoutSessionControlFilter;
import com.cloud.filter.ShiroAuthcFilter;
import com.cloud.shiro.RetryLimitHashedCredentialsMatcher;
import com.cloud.shiro.ShiroRealm;
import com.cloud.shiro.ShiroRealmWX;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.*;

@Configuration
//@EnableTransactionManagement
public class ShiroConfig {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * 1，配置shiro 安全管理器接口 securityManage
     * 2，shiro 连接约束配置 filterChainDefinitionns
     *
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        //shiroFilterFactoryBean 对象
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        logger.debug("-----------------Shiro拦截器工厂类注入开始");
        //必须设置 SecurityManager,Shiro的核心安全接口
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //指定要求登录时的连接
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面toLogin
        shiroFilterFactoryBean.setLoginUrl("/");
        //登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/home");
        //未授权时跳转的界面  该配置无效，并不会进行页面跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");



        //添加kickout 认证  （同一个用户多设备登录限制）
        HashMap<String,Filter> filters = new HashMap<>();
        //hashMap.put("kickout",kickoutSessionFilter());
        filters.put("kickout",kickoutSessionControlFilter());
        filters.put("authc", new ShiroAuthcFilter());
        shiroFilterFactoryBean.setFilters(filters);



        //拦截器. filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断，因为前端模板采用了thymeleaf，这里不能直接使用 ("/static/**", "anon")来配置匿名访问，必须配置到每个静态目录
        //配置退出过滤器，具体的退出代码shiro已经实现
        filterChainDefinitionMap.put("/logout","logout");
        //配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/user/userList","user");
        filterChainDefinitionMap.put("/","user");



        // 配置不会被拦截的链接 从上向下顺序判断
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/doc.html","anon");
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/user/sendMsg", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/applets/entrance/code2Session", "anon");
        filterChainDefinitionMap.put("/applets/entrance/login", "anon");
        filterChainDefinitionMap.put("/applets/entrance/register", "anon");
        //filterChainDefinitionMap.put("/home", "anon");
        filterChainDefinitionMap.put("/wx/attest", "anon");
        filterChainDefinitionMap.put("/user/unlockAccount", "anon");

        //filterChainDefinitionMap.put("/user/deleteUser", "authc,perms[userInfo:del]");
//		//add操作，该用户必须有【addOperation】权限
////		filterChainDefinitionMap.put("/add", "perms[addOperation]");
//
//		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
        //其他资源都需要认证  authc 表示需要认证才能进行访问
        filterChainDefinitionMap.put("/**", "authc");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.debug("-----------------Shiro拦截器工厂类注入成功");

        return  shiroFilterFactoryBean;
    }

    /**
     * 自定义的 ModularRealmAuthenticator
     **//*
    @Bean
    public ModularRealmAuthenticator authenticator(){
        ModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        return authenticator;
    }*/

    /**
     * shiro 安全管理器设置 realm  认证 和ehcache 缓存管理
     * 认证策略实际上是AuthenticationStrategy这个接口，它有三个实现：
     * (1) FirstSuccessfulStrategy:只要有一个Realm验证成功即可，只返回第一个Realm身份验证成功的认证
     * 信息，其他的忽略。
     *
     * (2) AtLeastOneSuccessfulStrategy:只要有一个Realm验证成功即可，和FirstSuccessfulStrategy不同，将
     * 返回所有Realm身份校验成功的认证信息。
     * (3) AllSuccessfulStrategy:所有Realm验证成功才算成功，且返回所有Realm身份认证成功的认证信息，如
     * 果有一个失败就失败了。
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();


        //新增部分多realm配置
        ModularRealmAuthenticator modularRealmAuthenticator = new CustomModularRealmAuthenticator();
        //至少成功一个都算认证通过得
        modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        securityManager.setAuthenticator(modularRealmAuthenticator);
        List<Realm> realms = new ArrayList<>();

        realms.add(shiroRealm());
        realms.add(shiroRealmWX());
        securityManager.setRealms(realms);




        //设置 realm
        //securityManager.setRealm(shiroRealm());
        //注入 ehcache  缓存管理器
        //securityManager.setCacheManager(ehCacheManager());

        //配置redis缓存
        securityManager.setCacheManager(rediscacheManager());


        //注入session管理器;
        securityManager.setSessionManager(sessionManager());
        //注入Cookie记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * shiro缓存管理器;
     * 需要添加到securityManager中
     * @return
     */
    @Bean
    public RedisCacheManager rediscacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("username");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }


    /**
     * ehcache缓存管理器；shiro整合ehcache：
     * 通过安全管理器：securityManager
     * 单例的cache防止热部署重启失败
     * @return EhCacheManager
     */
//    @Bean
//    public EhCacheManager ehCacheManager() {
//        logger.debug("=====shiro整合ehcache缓存：ShiroConfiguration.getEhCacheManager()");
//        EhCacheManager ehcache = new EhCacheManager();
//        CacheManager cacheManager = CacheManager.getCacheManager("shiro");
//        if(cacheManager == null){
//            try {
//
//                cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:config/ehcache.xml"));
//
//            } catch (CacheException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ehcache.setCacheManager(cacheManager);
//        return ehcache;
//    }





    /**
     * 配置Shiro生命周期处理器
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }





    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        return redisManager;
    }
    /**
     * 身份认证realm; (账号密码校验；权限等)
     *
     * @return
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();



        shiroRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        shiroRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthorizationCacheName("authorizationCache");


        //使用自定义的CredentialsMatcher进行密码校验和输错次数限制
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    @Bean
    public ShiroRealmWX shiroRealmWX() {
        ShiroRealmWX shiroRealm = new ShiroRealmWX();



        shiroRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        shiroRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthorizationCacheName("authorizationCache");


        //使用自定义的CredentialsMatcher进行密码校验和输错次数限制
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcherSHA());
        return shiroRealm;
    }

    /**
     * 配置 凭证匹配器 ,加密算法为 SHA1
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcherSHA(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("SHA1");
        //设置加密次数，比如两次，相当于SHA1(SHA1())
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }
    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
     *
     * @return
     */
    //@Bean("credentialsMatcher")
    @Bean
    public RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher() {
        //RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager());
        RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        //加密算法的名称
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
        //配置加密的次数
        hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列两次，相当于 // md5(md5(""));
        //是否存储为16进制
        //hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }



    /**
     * 设置记住我cookie过期时间
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     * @return
     */
    @Bean
    public SimpleCookie remeberMeCookie(){
        logger.debug("记住我，设置cookie过期时间！");
        //cookie名称;对应前端的checkbox的name = rememberMe
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }



    /**
     * 配置cookie记住我管理器
     * cookie管理对象;记住我功能,rememberMe管理器
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        logger.debug("配置cookie记住我管理器！");
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }
    /**
     * FormAuthenticationFilter 过滤器 过滤记住我
     * @return
     */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }







    /**
     * @描述：ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     * @创建人：wyait
     * @创建时间：2017年12月21日 下午1:52:59
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }



    /**
     *
     * @描述：sessionManager添加session缓存操作DAO
     * @创建人：wyait
     * @创建时间：2018年4月24日 下午8:13:52
     * @return
     */
//    @Bean
//    public SessionManager sessionManager() {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//
//        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
//        //配置监听
//        listeners.add(sessionListener());
//        sessionManager.setSessionListeners(listeners);
//        sessionManager.setSessionIdCookie(sessionIdCookie());
//        sessionManager.setSessionDAO(sessionDAO());
//        sessionManager.setCacheManager(ehCacheManager());
//
//        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
//        sessionManager.setGlobalSessionTimeout(1800000);
//        //是否开启删除无效的session对象  默认为true
//        sessionManager.setDeleteInvalidSessions(true);
//        //是否开启定时调度器进行检测过期session 默认为true
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
//        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
//        //暂时设置为 5秒 用来测试
//        sessionManager.setSessionValidationInterval(3600000);
//
//        //取消url 后面的 JSESSIONID
//        sessionManager.setSessionIdUrlRewritingEnabled(false);
//
//        return sessionManager;
//    }
    /**
     * 配置会话管理器，设定会话超时及保存
     * @return
     */
//    @Bean("sessionManager")
    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        //sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(rediscacheManager());
        sessionManager.setSessionFactory(sessionFactory());


        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(1800000 * 10);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(3600000 *10);
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;

    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        //simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }



    /**
     *
     * @描述：kickoutSessionFilter同一个用户多设备登录限制
     * @创建人：wyait
     * @创建时间：2018年4月24日 下午8:14:28
     * @return
     */
//    public KickoutSessionFilter kickoutSessionFilter(){
//        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
//        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
//        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
//        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
//        kickoutSessionFilter.setCacheManager(ehCacheManager());
//        //用于根据会话ID，获取会话进行踢出操作的；
//        kickoutSessionFilter.setSessionManager(sessionManager());
//        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
//        kickoutSessionFilter.setKickoutAfter(false);
//        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
//        kickoutSessionFilter.setMaxSession(1);
//        //被踢出后重定向到的地址；
//        kickoutSessionFilter.setKickoutUrl("/toLogin?kickout=1");
//        return kickoutSessionFilter;
//    }

    /**
     * 并发登录控制
     * @return
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //kickoutSessionControlFilter.setRedisManager(redisManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(3);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/login");
        return kickoutSessionControlFilter;
    }


    /**
     *
     * @描述：开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * </br>Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
     * </br>不使用注解的话，可以注释掉这两个配置
     * @创建人：wyait
     * @创建时间：2018年5月21日 下午6:07:56
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    /**
     * 开启Shiro的注解
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }



    /**
     * 解决： 无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized") 无效
     * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
     * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
     * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
     * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver=new SimpleMappingExceptionResolver();
        Properties properties=new Properties();
        //这里的 /unauthorized 是页面，不是访问的路径
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException","/unauthorized");
        properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","/unauthorized");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }





    /**
     * 让某个实例的某个方法的返回值注入为Bean的实例
     * Spring静态注入
     * @return
     */
    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(){
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }
    @Bean("sessionFactory")
    public ShiroSessionFactory sessionFactory(){
        ShiroSessionFactory sessionFactory = new ShiroSessionFactory();
        return sessionFactory;
    }





    /**
     * 配置session监听
     * @return
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }
    /**
     * 配置会话ID生成器
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }
    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
//    @Bean
//    public SessionDAO sessionDAO() {
//        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
//        //使用ehCacheManager
//        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
//        //设置session缓存的名字 默认为 shiro-activeSessionCache
//        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
//        //sessionId生成器
//        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
//        return enterpriseCacheSessionDAO;
//    }


    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDAO.setExpire(1810 *10);
        return redisSessionDAO;
    }

}
