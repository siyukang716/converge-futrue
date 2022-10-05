package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.SysLogoinfoConfigEntity;
import com.cloud.sys.service.SysLogoinfoConfigService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author wjg
 * @ClassName: SysLogoinfoConfigController
 * @Description: TODO(这里用一句话描述这个类的作用)系统logo 配置
 * @date 2021-09-06
 */
@Controller
@Api(value = "系统logo 配置crud功能", tags = "系统logo 配置crud功能")
@RequestMapping(value = "/sys/logo/info/config")
public class SysLogoinfoConfigController {
    @Autowired
    private SysLogoinfoConfigService sysLogoinfoConfigService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/sys_logoinfo_config");
        return model;
    }

    /**
     * 列表查询
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<SysLogoinfoConfigEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<SysLogoinfoConfigEntity> wrapper = new QueryWrapper<SysLogoinfoConfigEntity>();
        IPage<SysLogoinfoConfigEntity> page = sysLogoinfoConfigService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改地址信息
     *
     * @param
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result aOrU(SysLogoinfoConfigEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SysLogoinfoConfigEntity> wrapper = Wrappers.<SysLogoinfoConfigEntity>lambdaUpdate();
        boolean b = sysLogoinfoConfigService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除地址信息
     *
     * @param
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer id) {
        Result result = Result.getInstance();
        boolean b = sysLogoinfoConfigService.removeById(id);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer sysFonfigId) {
        Result result = Result.getInstance();
        SysLogoinfoConfigEntity obj = sysLogoinfoConfigService.getById(sysFonfigId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file) {
        return sysLogoinfoConfigService.uploadProduct(file);
    }
}