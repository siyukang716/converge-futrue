package com.cloud.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.util.IStatusMessage;
import com.cloud.shiro.ShiroFilterUtils;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import com.spring.commons.entity.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class UserActionInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(UserActionInterceptor.class);

    @Autowired
    private UserServiceImpl userService;
    private final String kickoutUrl="/index/toLogin"; // 退出后重定向的地址
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("请求到达后台方法之前调用（controller之前）");
        //获取用户信息
        UserEntity user = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user != null && StringUtils.isNotBlank(user.getMobile()) && null != user.getVersion()){
            //获取数据库中的用户数据
            LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserEntity::getMobile,user.getMobile());
            UserEntity dataUser = userService.getOne(wrapper);
            //对比session 中用户的version 和数据库中的是否一致
            if(dataUser != null && null != dataUser.getVersion() && String.valueOf(user.getVersion()).equals(String.valueOf(dataUser.getVersion()))){
                //一样，放行
                return true;
            }else {
                //不一样 退出登录
                SecurityUtils.getSubject().logout();
                isAjaxResponse(request,response);
            }
        }

        return false;
    }

    private void isAjaxResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ajax请求
        /**
         * 判断是否已经踢出
         * 1.如果是Ajax 访问，那么给予json返回值提示。
         * 2.如果是普通请求，直接跳转到登录页
         */
        //判断是不是AJAX 请求
        CommonResult result = new CommonResult();
        if (ShiroFilterUtils.isAjax(request)){
            logger.debug(getClass().getName()+ "，当前用户的信息或权限已变更，重新登录后生效！");
            result.setCode(IStatusMessage.SystemStatus.UPDATE.getCode());
            result.setMessage("您的信息或权限已变更，重新登录后生效");
            ShiroFilterUtils.out(response,result);
        }else{
            // 重定向
            WebUtils.issueRedirect(request, response, kickoutUrl);
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("请求处理之后调用；在视图渲染之前，controller处理之后。");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("整个请求完成之后调用。DispatcherServlet视图渲染完成之后。（主要是用于进行资源清理工作）");
    }
}
