package com.cloud.common.properties;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.cloud.common.properties.config.YamlPropertySourceFactory;
import com.cloud.util.IStatusMessage;
import com.cloud.util.MultipartFileToFile;
import com.cloud.util.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: ProductFileProperties
 * @Author lenovo
 * @Date: 2021/8/17 15:04
 * @Version 1.0
 */
//@ConfigurationProperties(prefix = "demo")  //指定前缀
//@PropertySource(value = "classpath:config.properties")
//PropertySource  默认支持 properties 和xml   使用yml时需要自己实现yml处理类
@PropertySource(value = "classpath:fileConfig/productConfig.yml",factory = YamlPropertySourceFactory.class)
@Data
@Component
@Slf4j
public class ProductFileProperties {

    @Autowired
    private Environment environment;
    public void a(){
        environment.getProperty("");
    }
    @Value("${product.info.ip}")
    private String ip;
    @Value("${product.info.srcPath}")
    private String srcPath;

    /**
     * 获取文件名称
     * @return
     */
    public String getFileName(String filename){
        return IdUtil.objectId()+"."+FileUtil.getSuffix(filename);
    }

    public Result uploadProduct(MultipartFile file,Integer businessId) {
        Result result = Result.getInstance();
        if (file.isEmpty()) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("上传失败，请选择文件");
            return result;
        }
        String fileName = getFileName(file.getOriginalFilename()); //获取文件后缀名


        String filePath = srcPath+businessId+File.separator; //根据业务id 组装文件路径
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
            map.put("url",ip+srcPath+businessId+"/"+fileName);
            map.put("picUrl",srcPath+businessId+"/"+fileName);
            result.setData(map);
        } catch (Exception e) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("上传失败,请重试!");
            log.error(e.toString(), e);
        }
        return result;
    }
}
