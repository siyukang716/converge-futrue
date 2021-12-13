package com.cloud.roster.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.roster.entity.SchedulingRulesEntity;
import com.cloud.roster.mapper.SchedulingRulesMapper;
import com.cloud.roster.vo.WorkRecordVo;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: SchedulingRulesController
 * @Description: 排班规则
 * @author 小可爱
 * @date 2021-10-27
 */
@Service
public class SchedulingRulesService extends ServiceImpl<SchedulingRulesMapper, SchedulingRulesEntity> {

    @Autowired
    private SchedulingRulesMapper mapper;
    @Autowired
    private UserServiceImpl userService;
    /**
     * 更加排班规则和排班人员表,查询需要排班的人员
     * @return
     */
    public List<WorkRecordVo> getSchedulingPlan(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("rsr.is_roster",2);
        List<WorkRecordVo> list = mapper.getSchedulingPlan(wrapper);
        return list;
    }

    /**
     * 获取出计划排班人员,分页查询
     * @return
     */
    public IPage<WorkRecordVo> getPageSchedulingPlan(Page p){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("rsr.is_roster",2);
        IPage<WorkRecordVo>  list = mapper.getPageSchedulingPlan(p,wrapper);
        List<WorkRecordVo> records = list.getRecords();
        if(records.size() == 0) return list;
        Set<Long> uids = records.stream().map(WorkRecordVo::getUserId).collect(Collectors.toSet());
        Map<Long, UserEntity> users = userService.getUsersByIdsIn(uids);
        records.forEach(d->{
            d.setUserName(users.get(d.getUserId()).getUsername());
        });
        return list;
    }
}