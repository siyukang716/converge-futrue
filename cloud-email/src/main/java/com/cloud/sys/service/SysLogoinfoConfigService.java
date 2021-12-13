package com.cloud.sys.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.properties.config.YamlPropertySourceFactory;
import com.cloud.sys.entity.SysLogoinfoConfigEntity;
import com.cloud.sys.mapper.SysLogoinfoConfigMapper;
import com.cloud.util.IStatusMessage;
import com.cloud.util.MultipartFileToFile;
import com.cloud.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @ClassName: SysLogoinfoConfigEntityService
 * @Description: 系统logo 配置
 * @author wjg
 * @date 2021-09-06
 */
@Service
@Slf4j
@PropertySource(value = "classpath:fileConfig/productConfig.yml",factory = YamlPropertySourceFactory.class)
public class SysLogoinfoConfigService extends ServiceImpl<SysLogoinfoConfigMapper, SysLogoinfoConfigEntity> {


    @Value("${sys.config.path}")
    private String srcPath;
    @Value("${product.info.ip}")
    private String ip;

    public Result uploadProduct(MultipartFile file) {
        Result result = Result.getInstance();
        if (file.isEmpty()) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("上传失败，请选择文件");
            return result;
        }
        String fileName = getFileName(file.getOriginalFilename()); //获取文件后缀名


        String filePath = srcPath+File.separator; //根据业务id 组装文件路径
        if (!FileUtil.isDirectory(filePath))
            FileUtil.mkdir(filePath);
        String local = filePath + fileName;//组装文件名
        File dest = new File(local);
        try {
            FileUtil.copy(MultipartFileToFile.multipartFileToFile(file),dest,true);
            log.info("上传成功!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("获取成功!!!!!!");
            Map map = new HashMap<String,String >();
            map.put("url",ip+srcPath+"/"+fileName);
            map.put("picUrl",srcPath+"/"+fileName);
            result.setData(map);
        } catch (Exception e) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("上传失败,请重试!");
            log.error(e.toString(), e);
        }
        return result;
    }
    /**
     * 获取文件名称
     * @return
     */
    public String getFileName(String filename){
        return IdUtil.objectId()+"."+FileUtil.getSuffix(filename);
    }
}