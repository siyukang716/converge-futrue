package com.cloud.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.log.entity.SysOperLogEntity;
import com.cloud.log.service.SysOperLogService;
import com.cloud.util.PageDataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
                                                                    
/**
 *
 * @ClassName: SysOperLogController
 * @Description: TODO(这里用一句话描述这个类的作用)操作日志记录
 * @author 小可爱
 * @date 2021-11-09
 */
@Controller
@Api(value = "操作日志记录crud功能",tags = "操作日志记录")
@RequestMapping(value ="sys/oper/log")
public class SysOperLogController  {
    @Autowired
    private SysOperLogService sysOperLogService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("log/");
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
        QueryWrapper<SysOperLogEntity> wrapper = new QueryWrapper<SysOperLogEntity>();
        IPage<SysOperLogEntity> page = sysOperLogService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }
}