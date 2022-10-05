package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.CompPermEntity;
import com.cloud.sys.service.CompPermService;
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

/**
 * @author 小可爱
 * @ClassName: CompPermController
 * @Description: TODO(这里用一句话描述这个类的作用)公司权限
 * @date 2021-11-16
 */
@Controller
@Api(value = "公司权限crud功能", tags = "公司权限")
@RequestMapping(value = "sys/comp/perm")
public class CompPermController {
    @Autowired
    private CompPermService compPermService;
    /**
     @GetMapping("/tolist") public ModelAndView toList() {
     ModelAndView model = new ModelAndView();
     model.setViewName("");
     return model;
     }
     */
    /**
     * 列表查询
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询", httpMethod = "GET")
    public PageDataResult getPageList(Page p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<CompPermEntity> wrapper = new QueryWrapper<CompPermEntity>();
        IPage<CompPermEntity> page = compPermService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改
     * 批量
     * @param
     * @return
     */
    @PostMapping("/aOrUS")
    @ResponseBody
    @ApiOperation(value = "批量新增修改接口", notes = "批量新增修改接口")
    public Result aOrU(String perids,Long companyId) {
        Result result = Result.getInstance();
        boolean b = compPermService.saveOrUpdate(perids,companyId);
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
     *
     * @param
     * @return
     */
    @GetMapping("/dels")
    @ResponseBody
    @ApiOperation(value = "删除接口", notes = "删除接口")
    public Result dels(String perids,Long companyId) {
        Result result = Result.getInstance();
        boolean b = compPermService.removeByIds(perids,companyId);
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
    public Result getById(Long compPermId) {
        Result result = Result.getInstance();
        CompPermEntity obj = compPermService.getById(compPermId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}