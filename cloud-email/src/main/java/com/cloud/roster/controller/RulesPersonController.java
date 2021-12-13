package com.cloud.roster.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.roster.entity.RulesPersonEntity;
import com.cloud.roster.service.RulesPersonService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
                                
/**
 *
 * @ClassName: RulesPersonController
 * @Description: TODO(这里用一句话描述这个类的作用)排版人员规则关联
 * @author 小可爱
 * @date 2021-10-27
 */
@Controller
@Api(value = "排版人员规则关联crud功能",tags = "排版人员规则关联")
@RequestMapping(value ="roster/rules/person")
public class RulesPersonController  {
    @Autowired
    private RulesPersonService rulesPersonService;

    @GetMapping("/tolist/{arrangeRuleId}")
    @ApiOperation(value = "跳转到本规则上班人员列表页",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "主键", paramType = "query", required = true)
    })
    public ModelAndView toList(@PathVariable("arrangeRuleId") Long arrangeRuleId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("roster/rulesperson");
        model.addObject("arrangeRuleId",arrangeRuleId);
        return model;
    }

    /**
     * 查询某上班规则人员列表
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "查询某上班规则人员列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "规则主键", paramType = "query", required = true)
    })
    public PageDataResult getPageList(Page p,Long arrangeRuleId) {
        PageDataResult pdr = new PageDataResult();
        IPage<RulesPersonEntity> page = rulesPersonService.pageSelf(p,arrangeRuleId);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 批量增加或修改
     * @param
     * @return
     */
    @PostMapping("/aOrUs")
    @ResponseBody
    @ApiOperation(value = "批量添加对应规则排班人员",notes = "",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "规则id", paramType = "query", required = true),
            @ApiImplicitParam(name = "ids", value = "人员id  多个人员用,拼接", paramType = "query", required = true)
    })
    public Result aOrUs(Long arrangeRuleId ,String ids) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<RulesPersonEntity> wrapper = Wrappers.<RulesPersonEntity>lambdaUpdate();
        boolean b = rulesPersonService.saveOrUpdates(arrangeRuleId,ids);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
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
    public Result del(Long rulesPersonId) {
        Result result = Result.getInstance();
        boolean b = rulesPersonService.removeById(rulesPersonId);
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
    public Result getById(Long rulesPersonId) {
        Result result = Result.getInstance();
        RulesPersonEntity obj = rulesPersonService.getById(rulesPersonId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null){
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}