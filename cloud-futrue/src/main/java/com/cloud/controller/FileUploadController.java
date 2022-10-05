package com.cloud.controller;

import com.cloud.common.properties.ProductFileProperties;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: FileUploadController
 * @Author lenovo
 * @Date: 2021/8/17 14:59
 * @Version 1.0
 */
@Controller
@Slf4j
@Api(value = "上传文件功能", tags = "ProductPicInfoController")
@RequestMapping(value = "/file/upload/info")
public class FileUploadController {

    @Autowired
    private ProductFileProperties properties;

    @ResponseBody
    @RequestMapping("/get")
    public String toList(Integer productId) {
        ModelAndView model = new ModelAndView();
        model.addObject("productId",productId);
        model.setViewName("product/productpicinfo");
        return properties.getIp();
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file,Integer businessId, HttpServletRequest request) {
       return properties.uploadProduct(file,businessId);
    }



    @PostMapping("/multiUpload")
    @ResponseBody
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = "/Users/itinypocket/workspace/temp/";
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "上传第" + (i++) + "个文件失败";
            }
            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                log.info("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                log.error(e.toString(), e);
                return "上传第" + (i++) + "个文件失败";
            }
        }

        return "上传成功";

    }




    /**
     * 多文件
     * */
    @PostMapping("/uploads")
    @ResponseBody
    Object uploads(@RequestParam("files") MultipartFile [] files, HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        map.put("status",0);
        List<String> filenames=new ArrayList<>();
        for (MultipartFile file: files
        ) {
            String ext = file.getOriginalFilename().split("\\.")[1];
            String fileName = file.getOriginalFilename();
            //fileName = UUID.randomUUID().toString()+"."+ext; //对文件名称重命名

            try {
                //FileUtil.uploadFile(file.getBytes(), uploadPath, fileName);
                filenames.add(fileName);
            } catch (Exception e) {
                map.put("status",-1);
                map.put("message",e.getMessage());
                return  map;

            }
        }

        map.put("filename",filenames);
        return map;
    }
}
