package com.cloud.roster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.roster.entity.SchedulingRulesEntity;
import com.cloud.roster.service.SchedulingRulesService;
import com.cloud.roster.vo.WorkRecordVo;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * @ClassName: SchedulingRulesController
 * @Description: TODO(这里用一句话描述这个类的作用)排班规则
 * @author 小可爱
 * @date 2021-10-27
 */
@Controller
@Api(value = "排班规则crud功能",tags = "排班规则")
@RequestMapping(value ="roster/scheduling/rules")
public class SchedulingRulesController  {
    @Autowired
    private SchedulingRulesService schedulingRulesService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("roster/schedulingrules");
        return model;
    }

    @GetMapping("/toPageSchedulingPlan")
    public ModelAndView toPageSchedulingPlan() {
        ModelAndView model = new ModelAndView();
        model.setViewName("roster/workrecordplan");
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
        LambdaQueryWrapper<SchedulingRulesEntity> lambda = new QueryWrapper<SchedulingRulesEntity>().lambda();
        lambda.orderByDesc(SchedulingRulesEntity::getInsertTime);
        IPage<SchedulingRulesEntity> page = schedulingRulesService.page(p,lambda);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 获取需要排班人员记录
     * @param p
     * @return
     */
    @GetMapping("/getPageSchedulingPlan")
    @ResponseBody
    @ApiOperation(value = "根据规则获取需要排班人员记录",httpMethod = "GET")
    public PageDataResult getPageSchedulingPlan(Page p) {
        PageDataResult pdr = new PageDataResult();
        IPage<WorkRecordVo> page = schedulingRulesService.getPageSchedulingPlan(p);
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
    public Result aOrU(SchedulingRulesEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SchedulingRulesEntity> wrapper = Wrappers.<SchedulingRulesEntity>lambdaUpdate();
        boolean b = schedulingRulesService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 发布
     * @param
     * @return
     */
    @GetMapping("/publish")
    @ResponseBody
    @ApiOperation(value = "发布排班规则",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "主键", paramType = "query", required = true)
    })
    public Result publish(Long arrangeRuleId) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SchedulingRulesEntity> lambda = new UpdateWrapper<SchedulingRulesEntity>().lambda();
        lambda.set(SchedulingRulesEntity::getPostStatus,2);
        lambda.eq(SchedulingRulesEntity::getArrangeRuleId,arrangeRuleId);
        boolean b = schedulingRulesService.update(lambda);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("发布成功!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 修改排班状态
     * @param arrangeRuleId
     * @return
     */
    @GetMapping("/editRoster")
    @ResponseBody
    @ApiOperation(value = "修改排班状态", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "主键", paramType = "query", required = true),
            @ApiImplicitParam(name = "isRoster", value = "排班 2  不排班 1", paramType = "query", required = true)
    })
    public Result editRoster(Long arrangeRuleId,Integer isRoster) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SchedulingRulesEntity> lambda = new UpdateWrapper<SchedulingRulesEntity>().lambda();
        lambda.set(SchedulingRulesEntity::getIsRoster, isRoster);
        lambda.eq(SchedulingRulesEntity::getArrangeRuleId, arrangeRuleId);
        boolean b = schedulingRulesService.update(lambda);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("修改排班成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @GetMapping("/getById")
    @ResponseBody
    public Result getById(Long arrangeRuleId) {
        Result result = Result.getInstance();
        SchedulingRulesEntity obj = schedulingRulesService.getById(arrangeRuleId);
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