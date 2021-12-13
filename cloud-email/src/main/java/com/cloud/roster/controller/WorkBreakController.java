package com.cloud.roster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.roster.entity.WorkBreakEntity;
import com.cloud.roster.service.WorkBreakService;
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
 * @ClassName: WorkBreakController
 * @Description: TODO(这里用一句话描述这个类的作用)工作休息规则
 * @author 小可爱
 * @date 2021-10-27
 */
@Controller
@Api(value = "工作休息规则crud功能",tags = "工作休息规则")
@RequestMapping(value ="roster/work/break")
public class WorkBreakController  {
    @Autowired
    private WorkBreakService workBreakService;

    @GetMapping("/tolist/{arrangeRuleId}/{postStatus}")
    @ApiOperation(value = "跳转休息规则页面",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "规则主键", paramType = "query", required = true),
            @ApiImplicitParam(name = "postStatus", value = "发布状态", paramType = "query", required = true)
    })
    public ModelAndView toList(@PathVariable("arrangeRuleId")Long arrangeRuleId,@PathVariable("postStatus")Integer postStatus) {
        ModelAndView model = new ModelAndView();
        model.setViewName("roster/workbreak");
        model.addObject("arrangeRuleId",arrangeRuleId);
        model.addObject("postStatus",postStatus);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrangeRuleId", value = "规则主键", paramType = "query", required = true)
    })
    public PageDataResult getPageList(Page p,Long arrangeRuleId) {
        PageDataResult pdr = new PageDataResult();
        LambdaQueryWrapper<WorkBreakEntity> lambda = new QueryWrapper<WorkBreakEntity>().lambda();
        lambda.eq(WorkBreakEntity::getArrangeRuleId,arrangeRuleId);
        IPage<WorkBreakEntity> page = workBreakService.page(p,lambda);
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
    public Result aOrU(WorkBreakEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<WorkBreakEntity> wrapper = Wrappers.<WorkBreakEntity>lambdaUpdate();
        boolean b = workBreakService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
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
    public Result del(Long workDetailsId) {
        Result result = Result.getInstance();
        boolean b = workBreakService.removeById(workDetailsId);
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
    public Result getById(Long workDetailsId) {
        Result result = Result.getInstance();
        WorkBreakEntity obj = workBreakService.getById(workDetailsId);
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