package com.cloud.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.generator.entity.GenTableEntity;
import com.cloud.generator.service.GenTableService;
import com.cloud.shiro.ShiroUtils;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import com.cloud.util.text.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author wjg
 * @ClassName: GenTableController
 * @Description: TODO(这里用一句话描述这个类的作用)代码生成业务表
 * @date 2021-09-14
 */
@Controller
@Api(value = "代码生成业务表crud功能", tags = "")
@RequestMapping(value = "/gen/Table")
public class GenTableController {
    @Autowired
    private GenTableService genTableService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("generator/genTable");
        return model;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(String tableId) {
        ModelAndView model = new ModelAndView();
        model.addObject("tableId", tableId);
        model.setViewName("generator/edit");
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
    public PageDataResult getPageList(Page<GenTableEntity> p) {
        PageDataResult pdr = new PageDataResult();
        LambdaQueryWrapper<GenTableEntity> wrapper = new QueryWrapper<GenTableEntity>().lambda();
        wrapper.orderByDesc(GenTableEntity::getCreateTime);
        IPage<GenTableEntity> page = genTableService.page(p, wrapper);
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
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(GenTableEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<GenTableEntity> wrapper = Wrappers.<GenTableEntity>lambdaUpdate();
        boolean b = genTableService.saveOrUpdate(entity);
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
    public Result del(Long tableId) {
        Result result = Result.getInstance();
        boolean b = genTableService.delById(tableId);
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
    public Result getById(Integer tableId) {
        Result result = Result.getInstance();
        GenTableEntity obj = genTableService.getById(tableId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
    @GetMapping("/importTableSave")
    @ResponseBody
    public Result importTableSave(String tableNames) {
        Result result = Result.getInstance();
        // 查询表信息
        List<GenTableEntity> tableList = genTableService.selectDbTableListByNames(tableNames);
        Long loginUserId = ShiroUtils.getLoginUserId();
        genTableService.importGenTable(tableList, loginUserId);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("导入成功!!!!!!");
//        if (obj == null) {
//            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
//            result.setMessage("操作异常，请您稍后再试");
//        }
        return result;
    }


    /**
     * 批量生成代码
     */

    @GetMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws Exception {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"genCode.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 查询数据库列表
     */
    @GetMapping("/db/list")
    @ResponseBody
    public PageDataResult dataList(Page<GenTableEntity> p) {
        PageDataResult pdr = new PageDataResult();
        IPage<GenTableEntity> page = genTableService.selectDbTableList(p);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    @ResponseBody
    public Result preview(@PathVariable("tableId") Long tableId) throws IOException {
        Result result = Result.getInstance();
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        result.setData(dataMap);
        return result;
    }
}