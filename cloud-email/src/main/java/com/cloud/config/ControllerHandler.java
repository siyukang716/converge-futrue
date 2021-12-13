package com.cloud.config;

import com.alibaba.fastjson.JSON;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author wjg
 * @version 1.0
 * @since 2019/1/4 15:23
 * 全局异常处理
 */
@ControllerAdvice
public class ControllerHandler {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        GenericConversionService genericConversionService = (GenericConversionService) binder.getConversionService();
        if (genericConversionService != null) {
            genericConversionService.addConverter(new DateConverter());
        }
    }



    /**
     * 登录认证异常
     */
    //@ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
//    public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
//        if (isAjaxRequest(request)) {
//            // 输出JSON
//            Map<String,Object> map = new HashMap<>();
//            map.put("code", "-999");
//            map.put("message", "未登录");
//            writeJson(map, response);
//            return null;
//        } else {
//            return "redirect:/system/login";
//        }
//    }

    /**
     * 权限异常
     */
//    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
//    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
//        if (isAjaxRequest(request)) {
//            // 输出JSON
//            Map<String,Object> map = new HashMap<>();
//            map.put("code", "-998");
//            map.put("message", "无权限");
//            writeJson(map, response);
//            return null;
//        } else {
//            return "redirect:/system/403";
//        }
//    }

    /**
     * 输出JSON
     *
     * @param response
     * @author SHANHY
     * @create 2017年4月4日
     */
    private void writeJson(Map<String,Object> map, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 是否是Ajax请求
     *
     * @param request
     * @return
     * @author SHANHY
     * @create 2017年4月4日
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }

    @ExceptionHandler({Exception.class })
    private void exceptionHandle(Exception e, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            e.printStackTrace();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            Result result = Result.getInstance();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作失败!!!!!");
            out.write(JSON.toJSONString(result));

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}

