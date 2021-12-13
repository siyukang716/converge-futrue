package com.cloud.jsw.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: JSWController
 * @Author lenovo
 * @Date: 2021/11/3 17:14
 * @Version 1.0
 */
@Controller
@Api(value = "执法记录仪", tags = "执法记录仪")
@RequestMapping(value = "jsw")
public class JSWController {
    @GetMapping("/tolist")
    @ApiOperation(value="单个视频",notes="")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("jsw/index");
        return model;
    }
    @GetMapping("/toManylist")
    @ApiOperation(value="多视频",notes="")
    public ModelAndView toManylist() {
        ModelAndView model = new ModelAndView();
        model.setViewName("jsw/manyVidao");
        return model;
    }
    @GetMapping("/toFilePUlist")
    @ApiOperation(value="客户端下载文件",notes="")
    public ModelAndView toFilePUlist() {
        ModelAndView model = new ModelAndView();
        model.setViewName("jsw/fileOnPu");
        return model;
    }

    @GetMapping("/tofileOnNru")
    @ApiOperation(value="服务端下载文件",notes="")
    public ModelAndView tofileOnNru() {
        ModelAndView model = new ModelAndView();
        model.setViewName("jsw/fileOnNru");
        return model;
    }
}
