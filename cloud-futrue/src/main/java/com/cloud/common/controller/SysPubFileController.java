package com.cloud.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.common.entity.SysPubFileEntity;
import com.cloud.common.service.SysPubFileService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
                                                    
/**
 *
 * @ClassName: SysPubFileController
 * @Description: TODO(这里用一句话描述这个类的作用)公共文件
 * @author 豆芽菜
 * @date 2021-10-13
 */
@Controller
@Api(value = "公共文件crud功能",tags = "公共文件")
@RequestMapping(value ="sys/pub/file")
public class SysPubFileController  {
    @Autowired
    private SysPubFileService sysPubFileService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/syspubfile");
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
        QueryWrapper<SysPubFileEntity> wrapper = new QueryWrapper<SysPubFileEntity>();
        IPage<SysPubFileEntity> page = sysPubFileService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改地址信息
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(MultipartFile file,SysPubFileEntity entity) throws Exception {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SysPubFileEntity> wrapper = Wrappers.<SysPubFileEntity>lambdaUpdate();
        result = sysPubFileService.savePubFile(file,entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");

        return result;
    }

    /**
     * 删除地址信息
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long upfileId) {
        Result result = Result.getInstance();
        boolean b = sysPubFileService.removeById(upfileId);
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
    public Result getById(Long upfileId) {
        Result result = Result.getInstance();
        SysPubFileEntity obj = sysPubFileService.getById(upfileId);
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