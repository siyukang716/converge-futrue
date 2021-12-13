package com.cloud.sys.service;

import com.cloud.sys.entity.CustomerInfEntity;
import com.cloud.sys.mapper.CustomerInfMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @ClassName: CustomerInfEntityService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wjg
 * @date 2021-08-05
 */
@Service
public class CustomerInfService extends ServiceImpl<CustomerInfMapper, CustomerInfEntity> {
    @Autowired
    private CustomerInfMapper customerInfMapper;

}