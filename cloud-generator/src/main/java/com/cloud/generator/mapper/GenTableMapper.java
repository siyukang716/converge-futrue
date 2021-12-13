package com.cloud.generator.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.generator.entity.GenTableEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *
 * @ClassName: GenTableMapper
 * @Description: TODO(这里用一句话描述这个类的作用)代码生成业务表
 * @author wjg
 * @date 2021-09-14
 */
public interface GenTableMapper extends BaseMapper<GenTableEntity> {

    List<GenTableEntity> selectDbTableListByNames(String[] tableNames);


    IPage<GenTableEntity> selectDbTableList(Page<GenTableEntity> p, QueryWrapper<Object> wrapper);
}