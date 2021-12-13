package com.cloud.roster.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.roster.entity.WorkRecordEntity;
import com.cloud.roster.service.WorkRecordService;
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
 * @ClassName: WorkRecordController
 * @Description: TODO(这里用一句话描述这个类的作用)排班记录
 * @author 小可爱
 * @date 2021-10-27
 */
@Controller
@Api(value = "排班记录crud功能",tags = "排班记录")
@RequestMapping(value ="roster/work/record")
public class WorkRecordController  {
    @Autowired
    private WorkRecordService workRecordService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("roster/workrecord");
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
        return workRecordService.getPageList(p);
    }


    /**
     * 增加或修改
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(WorkRecordEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<WorkRecordEntity> wrapper = Wrappers.<WorkRecordEntity>lambdaUpdate();
        boolean b = workRecordService.saveOrUpdate(entity);
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
    public Result del(Long workId) {
        Result result = Result.getInstance();
        boolean b = workRecordService.removeById(workId);
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
    public Result getById(Long workId) {
        Result result = Result.getInstance();
        WorkRecordEntity obj = workRecordService.getById(workId);
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
     * 生成排班记录
     * @return
     */
    @PostMapping("/generateShiftPlan")
    @ResponseBody
    @ApiOperation(value = "生成排班记录",httpMethod = "POST")
    public Result generateShiftPlan() {
        Result result = Result.getInstance();
        boolean b = workRecordService.generateShiftPlan();
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("排班成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }



}