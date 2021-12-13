package com.cloud.generator.mapper;

import com.cloud.generator.entity.GenTableColumnEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @ClassName: GenTableColumnMapper
 * @Description: TODO(这里用一句话描述这个类的作用)代码生成业务表字段
 * @author wjg
 * @date 2021-09-14
 */
public interface GenTableColumnMapper extends BaseMapper<GenTableColumnEntity> {

    List<GenTableColumnEntity> selectDbTableColumnsByName(@Param("tableName") String tableName);
}