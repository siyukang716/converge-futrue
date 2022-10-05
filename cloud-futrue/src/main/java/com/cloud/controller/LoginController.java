package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cloud.shiro.RetryLimitHashedCredentialsMatcher;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: LoginController
 * @Author lenovo
 * @Date: 2021/8/19 10:29
 * @Version 1.0
 */
@Controller
@RequestMapping("/user")
@Api(value = "登录", tags = "用户登录接口")
@Slf4j
public class LoginController {


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RetryLimitHashedCredentialsMatcher matcher;

    /**
     * 解除admin 用户的限制登录
     * 写死的 方便测试
     *
     * @return
     */
    @GetMapping("/unlockAccount")
    @ApiOperation(value = "用户解锁", httpMethod = "GET")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户登录账号", paramType = "query", required = true)
    })
    public Result unlockAccount(String username) {
        log.info("===========================解锁============================================");
        return matcher.unlockAccount(username);
    }

    @RequestMapping(value = "changePwd")
    @ResponseBody
    public Result changePwd(String pwd, String new_pwd) {
        Result result = Result.getInstance();
        String md5Pwd = DigestUtils.md5Hex(pwd);
        UserEntity loginUser = ShiroUtils.getLoginUser();
        if (md5Pwd.equals(loginUser.getPassword())) {
            LambdaUpdateWrapper<UserEntity> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(UserEntity::getPassword, DigestUtils.md5Hex(new_pwd)).eq(UserEntity::getId, loginUser.getId());
            boolean b = userService.update(wrapper);
            if (b)
                result.setMessage("修改成功!!!!!!!!!");
            else
                result.setMessage("修改失败!!!!!!!!!");
        } else {
            result.setMessage("原密码错误!!!!!!!!!");
        }
        return result;
    }


    /**
     * 登录【使用shiro中自带的HashedCredentialsMatcher结合ehcache（记录输错次数）配置进行密码输错次数限制】
     * </br>缺陷是，无法友好的在后台提供解锁用户的功能，当然，可以直接提供一种解锁操作，清除ehcache缓存即可，不记录在用户表中；
     * </br>
     *
     * @param user
     * @param rememberMe
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(HttpServletRequest request, UserEntity user,
                        @RequestParam(value = "rememberMe", required = false) boolean rememberMe) {
        Result result = Result.getInstance();
        log.debug("用户登录，请求参数=user:" + user + "，是否记住我：" + rememberMe);
        /*if (!validatorRequestParam(user, result)) {
            log.debug("用户登录，结果=responseResult:" + result);
            return result;
        }*/

        // 校验验证码
        /*if(!existUser.getMcode().equals(user.getSmsCode())){ //不等
         responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
         responseResult.setMessage("短信验证码输入有误");
         log.debug("用户登录，结果=responseResult:"+responseResult);
         return responseResult;
        } //1分钟
        long beginTime =existUser.getSendTime().getTime();
        long endTime = new Date().getTime();
        if(((endTime-beginTime)-60000>0)){
             responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
             responseResult.setMessage("短信验证码超时");
             log.debug("用户登录，结果=responseResult:"+responseResult);
             return responseResult;
        }*/
        // 用户登录

        try {
            // 1、 封装用户名、密码、是否记住我到token令牌对象 [支持记住我]
            AuthenticationToken token = new UsernamePasswordToken(user.getMobile(), user.getPassword(), rememberMe);
            //AuthenticationToken token = new UsernamePasswordToken(user.getMobile(), user.getPassword(),rememberMe);
            // 2、 Subject调用login
            Subject subject = SecurityUtils.getSubject();
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.debug("用户登录，用户验证开始！user=" + user.getMobile());
            subject.login(token);
            log.info("用户登录，用户验证通过！user=" + user.getMobile());
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());

        } catch (Exception e) {
            //登录失败从request中获取shiro处理的异常信息   shiroLoginfailure 就是shiro异常类名全称
            String shiroLoginfailure = (String) request.getAttribute("shiroLoginfailure");
            result.setMessage("登录错误");
            if (e instanceof UnknownAccountException)
                result.setMessage("用户名不存在");
            if (e instanceof IncorrectCredentialsException)
                result.setMessage("密码错误");
            if (e instanceof LockedAccountException)
                result.setMessage("账号已被锁定,请联系管理员");
            log.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + user.getMobile(), e);
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return result;


    }

    /**
     * 登录【使用redis和mysql实现，用户密码输错次数限制，和锁定解锁用户的功能//TODO】
     * 该实现后续会提供！TODO
     *
     * @param user
     * @param rememberMe
     * @return
     */
    @RequestMapping(value = "logina", method = RequestMethod.POST)
    @ResponseBody
    public Result logina(UserEntity user,
                         @RequestParam(value = "rememberMe", required = false) boolean rememberMe) {
        log.debug("用户登录，请求参数=user:" + user + "，是否记住我：" + rememberMe);
        Result result = Result.getInstance();
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        if (null == user) {
            result.setStatus(IStatusMessage.SystemStatus.PARAM_ERROR
                    .getCode());
            result.setMessage("请求参数有误，请您稍后再试");
            log.debug("用户登录，结果=responseResult:" + result);
            return result;
        }
        /*if (!validatorRequestParam(user, result)) {
            log.debug("用户登录，结果=responseResult:" + result);
            return result;
        }*/
        // 用户是否存在
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getMobile, user.getMobile());
        UserEntity existUser = this.userService.getOne(wrapper, true);
        if (existUser == null) {
            result.setMessage("该用户不存在，请您联系管理员");
            log.debug("用户登录，结果=responseResult:" + result);
            return result;
        } else {
            // 校验验证码
			/*if(!existUser.getMcode().equals(user.getSmsCode())){ //不等
			 responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
			 responseResult.setMessage("短信验证码输入有误");
			 log.debug("用户登录，结果=responseResult:"+responseResult);
			 return responseResult;
			} //1分钟
			long beginTime =existUser.getSendTime().getTime();
			long endTime = new Date().getTime();
			if(((endTime-beginTime)-60000>0)){
				 responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
				 responseResult.setMessage("短信验证码超时");
				 log.debug("用户登录，结果=responseResult:"+responseResult);
				 return responseResult;
			}*/
        }
        // 是否锁定
        boolean flag = false;
        // 用户登录
        try {
            // 1、 封装用户名和密码到token令牌对象 [支持记住我]
            AuthenticationToken token = new UsernamePasswordToken(
                    user.getMobile(), DigestUtils.md5Hex(user.getPassword()),
                    rememberMe);
            // 2、 Subject调用login
            Subject subject = SecurityUtils.getSubject();
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.debug("用户登录，用户验证开始！user=" + user.getMobile());
            subject.login(token);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS
                    .getCode());
            log.info("用户登录，用户验证通过！user=" + user.getMobile());
        } catch (UnknownAccountException uae) {
            log.error("用户登录，用户验证未通过：未知用户！user=" + user.getMobile(), uae);
            result.setMessage("该用户不存在，请您联系管理员");
        } catch (IncorrectCredentialsException ice) {
            // 获取输错次数
            log.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user=" + user.getMobile(),
                    ice);
            result.setMessage("用户名或密码不正确");
        } catch (LockedAccountException lae) {
            log.error("用户登录，用户验证未通过：账户已锁定！user=" + user.getMobile(), lae);
            result.setMessage("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            log.error(
                    "用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user=.getMobile()" + user, eae);
            result.setMessage("用户名或密码错误次数大于5次,账户已锁定，2分钟后可再次登录或联系管理员解锁");
            // 这里结合了，另一种密码输错限制的实现，基于redis或mysql的实现；也可以直接使用RetryLimitHashedCredentialsMatcher限制5次
            flag = true;
        } /*catch (DisabledAccountException sae){
		 log.error("用户登录，用户验证未通过：帐号已经禁止登录！user=" +
		 user.getMobile(),sae);
		 responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
		 responseResult.setMessage("帐号已经禁止登录");
		}*/ catch (AuthenticationException ae) {
            // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            log.error("用户登录，用户验证未通过：认证异常，异常信息如下！user=" + user.getMobile(),
                    ae);
            result.setMessage("用户名或密码不正确");
        } catch (Exception e) {
            log.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + user.getMobile(), e);
            result.setMessage("用户登录失败，请您稍后再试");
        }
        if (flag) {
            // 已经输错6次了，将进行锁定！【也可以使用redis记录密码输错次数，然后进行锁定//TODO】
            int num = 1;//this.userService.setUserLockNum(existUser.getId(), 1);
            if (num < 1) {
                log.info("用户登录，用户名或密码错误次数大于5次,账户锁定失败！user="
                        + user.getMobile());
            }
        }
        log.debug("用户登录，user=" + user.getMobile() + ",登录结果=responseResult:"
                + result);
        return result;
    }
}
