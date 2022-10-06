package com.cloud.config.autoload;

import cn.hutool.core.util.IdUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.cloud.constant.Constants;
import com.cloud.sys.entity.UploadFileConfigEntity;
import com.cloud.sys.service.UploadFileConfigService;
import com.cloud.util.DateUtils;
import com.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 初始化上传文件配置
 * @Author lenovo
 * @Date: 2021/9/23 9:33
 * @Version 1.0
 */
@Component
@Order(3)
@Slf4j
public class AutoFilePathConfig implements CommandLineRunner {
    @Autowired
    private UploadFileConfigService uploadFileConfigService;
    @Autowired
    private  RedisUtil redisUtil;


    @Override
    public void run(String... args) throws Exception {
        List<UploadFileConfigEntity> list = uploadFileConfigService.list();
        Map<String, UploadFileConfigEntity> collect = list.stream().collect(Collectors.toMap(UploadFileConfigEntity::getBusinessType, d -> d, (k1, k2) -> k1));
        log.info("===================文件上传配置初始化成功");
        redisUtil.hmset(Constants.SYS_FILE_FONFIG,collect);
    }

    /**
     * 根据业务key  获取上传路径以及访问路径
     * @param key
     * @return
     */
    public  Map<String,String> getFilePathById(String key){
        UploadFileConfigEntity o = (UploadFileConfigEntity) redisUtil.hget(Constants.SYS_FILE_FONFIG, key);
        if (o==null)
            throw new RuntimeException("请配置上传路径");
        OsInfo osInfo = SystemUtil.getOsInfo();
        String date = DateUtils.dateTime();
        //String fileName = IdUtil.objectId();
        Map<String,String> map = new HashMap<>();
        if (osInfo.isWindows()){
            map.put("path",o.getWindowsPath()+"/"+date+"/");
            map.put("call",o.getWindowsCall()+ "/"+date+"/");
        }else {
            map.put("path",o.getLinuxPath()+ IdUtil.objectId()+"/"+date+"/");
            map.put("call",o.getLinuxCall()+"/"+date+"/");
        }
        log.info("业务id:"+key+"文件路径:"+map.toString());
        return map;
    }
}
