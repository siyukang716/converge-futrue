package com.cloud.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.sys.entity.IpConfigEntity;
import com.cloud.sys.mapper.IpConfigMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: IpConfigController
 * @Description: 系统ip配置
 * @author 豆芽菜
 * @date 2021-11-21
 */
@Service
public class IpConfigService extends ServiceImpl<IpConfigMapper, IpConfigEntity> {

    public IpConfigEntity getIpOne() {
        return CollectionUtils.isNotEmpty(super.list())?super.list().get(0):new IpConfigEntity();
    }
}