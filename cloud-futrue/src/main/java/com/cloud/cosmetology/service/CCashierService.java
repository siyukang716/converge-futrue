package com.cloud.cosmetology.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.cosmetology.entity.CCashierEntity;
import com.cloud.cosmetology.mapper.CCashierMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Result aOrU(CCashierEntity entity) {
        Result result = Result.getInstance();
        int insert = cashierMapper.insert(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!");
        if (insert<=0) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}