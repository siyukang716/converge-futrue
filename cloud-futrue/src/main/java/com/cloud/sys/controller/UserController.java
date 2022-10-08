package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.cosmetology.entity.CUserEntity;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 用户操作
 */
@Controller
@RequestMapping("/user")
@Api(value = "用户crud功能",tags = "用户crud功能")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/list")
    public String toList() {
        return "auth/userList";
    }

    /**
     * @api {get} /index/:userName
     * @apiDescription  这只是一个测试的接口描述
     * @apiName index
     * @apiParam {String} userName 用户的姓名
     * @apiParam {Number} userAge 用户的年龄
     * @apiParamExample {json} Request-Example:
     * {
     *     "userName":"caojing",
     *     "userAge":12
     * }
     * @apiGroup index
     * @apiError userNotFound  The <code>id</code>
     * @apiSampleRequest /index
     */
    @RequestMapping("/index")
    @ResponseBody
    @CrossOrigin
    public String index(@RequestParam("userName") String userName, @RequestParam("userAge") int userAge) {
        return "index userName is " + userName + "userAge " + userAge;
    }


    /**
     * 分页查询用户列表
     * @param p
     * @param user
     * @return
     */
    @GetMapping("/getPageList")
    @ResponseBody
    @ApiOperation(value = "用户列表的分页查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<UserEntity> p, UserEntity user,Long deptid) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
            wrapper.like(!StringUtils.isNullOrEmpty(user.getUsername()),"user.username",user.getUsername());
            wrapper.like(!StringUtils.isNullOrEmpty(user.getMobile()),"user.mobile",user.getMobile());
            wrapper.eq(deptid != null && deptid > 0,"sud.dept_id",deptid);
            wrapper.apply(user.getInsertTime() != null ,"date_format(user.insert_time,'%Y-%m-%d') = date_format({0},'%Y-%m-%d')",user.getInsertTime());
            wrapper.eq("user.company_id", ShiroUtils.getLoginUser().getCompanyId());
            Page<UserEntity> list = userService.listUsers(p,wrapper);
            //pdr.setTotals((int) page.getTotal());
            pdr.setTotals((int) list.getTotal());
            //pdr.setList(page.getRecords());
            pdr.setList(list.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }

        return pdr;
    }
    /**
     * 操作人员列表全量下拉
     * @param entity
     * @return
     */
    @GetMapping("/getUserList")
    @ResponseBody
    @ApiOperation(value = "操作人员列表全量下拉",httpMethod = "GET")
    public Result getUserList(UserEntity entity) {
        Result result = Result.getInstance();
        LambdaQueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>().lambda();
        wrapper.orderByDesc(UserEntity::getUpdateTime);
        List<UserEntity> prodList = userService.list(wrapper);
        result.setData(prodList);
        return result;
    }



    /**
     * 设置用户是否离职
     * @return ok/fail
     */
    @PostMapping("/setJobUser")
    @ResponseBody
    public Result  setJobUser(UserEntity user){
        Result result = Result.getInstance();
        userService.updateById(user);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
        return result;
    }
    /**
     * 设置用户[新增或更新]
     * @return ok/fail
     */
    @PostMapping("/setUser")
    @ResponseBody
    public Result setUser(UserEntity user) {
        return userService.setUser(user);
    }
    /**
     * 删除用户
     * @return ok/fail
     */
    @PostMapping("/deleteUser")
    @ResponseBody
    @RequiresPermissions("userInfo:del")
    //@ApiOperation(value = "删除用户",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_VALUE)
    public String delUser(Long id){
        try {
            boolean b = userService.removeById(id);
            return  "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "操作异常";
        }
    }
    @PostMapping("/recoverUser")
    @ResponseBody
    public String recoverUser(Long id){
        try {
            userService.recoverUser(id);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "操作异常";
        }
    }

    /**
     * 获取用户
     */
    @GetMapping("/getUser")
    @ResponseBody
    public Result getUser(Long id){
        Result<UserEntity> result = Result.getInstance();
        try {
            UserEntity user = userService.getById(id);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }







    @GetMapping("/importTemplate")
    @ApiOperation(value = "下载人员导入模板", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public void importTemplate(HttpServletResponse response) throws Exception {
        userService.importTemplate(response);
    }


    @PostMapping("/importData")
    @ResponseBody
    @ApiImplicitParams(@ApiImplicitParam(name = "file", value = "导入承包商文件", required = true))
    @ApiOperation(value = "导入承包商文件", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result importData(MultipartFile file) throws Exception {
        Result result = Result.getInstance();

        String message = userService.importUser(file);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }


}
