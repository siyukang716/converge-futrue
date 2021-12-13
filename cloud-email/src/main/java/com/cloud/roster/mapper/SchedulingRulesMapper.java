package com.cloud.roster.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.roster.entity.SchedulingRulesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.roster.vo.WorkRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @ClassName: SchedulingRulesController
 * @Description: 排班规则
 * @author 小可爱
 * @date 2021-10-27
 */
public interface SchedulingRulesMapper extends BaseMapper<SchedulingRulesEntity> {

    List<WorkRecordVo> getSchedulingPlan(@Param("ew") QueryWrapper wrapper);

    IPage<WorkRecordVo> getPageSchedulingPlan(Page page ,@Param("ew") QueryWrapper wrapper);
}