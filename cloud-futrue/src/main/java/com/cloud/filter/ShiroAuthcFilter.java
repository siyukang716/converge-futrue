package com.cloud.filter;

import com.alibaba.fastjson.JSON;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 登入过滤器
 */
@Slf4j
public class ShiroAuthcFilter extends FormAuthenticationFilter {

    /**
     * ajax请求，无session
     *  默认：会重定向至LoginUrl，如果LoginUrl返回的是页面，则影响用户体验
     *
     *  修改：转发至：ajaxSessionError ，然后进行response处理
     */
    private String ajaxSessionErrorForwardUrl;

    /**
     * 如果没有设置，则返回LoginUrl
     * @return
     */
    public String getAjaxSessionErrorForwardUrl() {
        if (this.ajaxSessionErrorForwardUrl==null){
            this.ajaxSessionErrorForwardUrl = getLoginUrl();
        }
        return ajaxSessionErrorForwardUrl;
    }

    public void setAjaxSessionErrorForwardUrl(String ajaxSessionErrorForwardUrl) {
        this.ajaxSessionErrorForwardUrl = ajaxSessionErrorForwardUrl;
    }

    /**
     * 转发至ajaxSessionErrorForwardUrl
     * @param request
     * @param response
     * @throws Exception
     */
    public void forwardToAjaxSessionError(ServletRequest request, ServletResponse response)throws Exception {
        WebUtils.toHttp(request).getRequestDispatcher(getAjaxSessionErrorForwardUrl()).forward(request,response);
    }

    /**
     * 保存，然后转发，方便controller获取returnUrl并发送至前端，交由前端控制处理如何跳转
     * @param request
     * @param response
     * @throws Exception
     */
    public void savedAndForwardToAjaxSessionError(ServletRequest request, ServletResponse response)throws Exception{
        this.saveRequest(request);
        this.forwardToAjaxSessionError(request,response);
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            /**
             * ajax 请求无session或者session失效
             */
            if (isAjax(request)){
                response.setCharacterEncoding("UTF-8");//设置编码
                response.setContentType("application/json");//设置返回类型
                //this.savedAndForwardToAjaxSessionError(request,response);
                Result result = Result.getInstance();
                result.setStatus(IStatusMessage.SystemStatus.LOGIN_EXPIRED.getCode());
                result.setMessage("登录过期,请重新登录");
                response.getWriter().print(JSON.toJSONString(result));
                log.info("ajax:---------------------session  过期-----------------------------------");
            }else {
                this.saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }

    /**
     * 判断是否是ajax请求
     * @param request
     * @return
     */
    private boolean isAjax(ServletRequest request) {
        String header = WebUtils.toHttp(request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return true;
        }
        return false;
    }

}