package com.cloud.cosmetology.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.cosmetology.entity.CCashierEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @ClassName: CCashierController
 * @Description: 美容院-收银管理
 * @author 豆芽菜
 * @date 2022-10-06
 */
public interface CCashierMapper extends BaseMapper<CCashierEntity> {

    IPage<CCashierEntity> getPageList(Page p,@Param(Constants.WRAPPER) QueryWrapper<CCashierEntity> wrapper);
}