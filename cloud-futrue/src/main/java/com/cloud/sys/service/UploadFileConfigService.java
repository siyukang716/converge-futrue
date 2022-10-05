package com.cloud.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.constant.Constants;
import com.cloud.sys.entity.UploadFileConfigEntity;
import com.cloud.sys.mapper.UploadFileConfigMapper;
import com.cloud.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: UploadFileConfigController
 * @Description: 文件上传路径配置
 * @author 小可爱
 * @date 2021-09-23
 */
@Service
public class UploadFileConfigService extends ServiceImpl<UploadFileConfigMapper, UploadFileConfigEntity> {

    @Autowired
    private RedisUtil redisUtil;


    public boolean saveOrUpdate(UploadFileConfigEntity entity) {
        redisUtil.hset(Constants.SYS_FILE_FONFIG,entity.getBusinessType(),entity);
        return super.saveOrUpdate(entity);
    }
}