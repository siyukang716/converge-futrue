package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.IpConfigEntity;
import com.cloud.sys.service.IpConfigService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @ClassName: IpConfigController
 * @Description: TODO(这里用一句话描述这个类的作用)系统ip配置
 * @author 豆芽菜
 * @date 2021-11-21
 */
@Controller
@Api(value = "系统ip配置crud功能",tags = "系统ip配置")
@RequestMapping(value ="sys/ip/config")
public class IpConfigController  {
    @Autowired
    private IpConfigService ipConfigService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/ipconfig");
        return model;
    }

    /**
     * 列表查询
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询",httpMethod = "GET")
    public PageDataResult getPageList(Page p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<IpConfigEntity> wrapper = new QueryWrapper<IpConfigEntity>();
        IPage<IpConfigEntity> page = ipConfigService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    @ApiOperation(value = "新增修改接口", notes = "新增修改接口")
    public Result aOrU(IpConfigEntity entity) {
        Result result = Result.getInstance();
        boolean b = ipConfigService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    @ApiOperation(value = "删除接口", notes = "删除接口")
    public Result del(Long ipId) {
        Result result = Result.getInstance();
        boolean b = ipConfigService.removeById(ipId);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation(value = "根据主键查询对象接口", notes = "根据主键查询对象接口")
    public Result getById(Long ipId) {
        Result result = Result.getInstance();
        IpConfigEntity obj = ipConfigService.getById(ipId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null){
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 查询ip表数据
     * @param p
     * @return
     */
    @GetMapping("/getIpOne")
    @ResponseBody
    @ApiOperation(value = "列表查询",httpMethod = "GET")
    public Result getIpOne(Page p) {
        Result result = Result.getInstance();
        IpConfigEntity obj = ipConfigService.getIpOne();
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        return result;
    }

}