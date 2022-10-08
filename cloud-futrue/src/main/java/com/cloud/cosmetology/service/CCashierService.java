package com.cloud.cosmetology.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.cosmetology.entity.CCashierEntity;
import com.cloud.cosmetology.mapper.CCashierMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.util.PageDataResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 * @ClassName: CCashierController
 * @Description: 美容院-收银管理
 * @author 豆芽菜
 * @date 2022-10-06
 */
@Service
public class CCashierService extends ServiceImpl<CCashierMapper, CCashierEntity> {
    @Resource
    private CCashierMapper cashierMapper;

    public PageDataResult getPageList(Page p, CCashierEntity cashierEntity) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<CCashierEntity> wrapper = new QueryWrapper<CCashierEntity>();
        wrapper.eq(Objects.nonNull(cashierEntity.getStaffId()),"cc.staff_id",cashierEntity.getStaffId());
        IPage<CCashierEntity> page = cashierMapper.getPageList(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }
}