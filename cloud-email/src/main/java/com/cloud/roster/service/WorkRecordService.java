package com.cloud.roster.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.roster.entity.WorkRecordEntity;
import com.cloud.roster.mapper.WorkRecordMapper;
import com.cloud.roster.vo.WorkRecordVo;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import com.cloud.util.DateUtils;
import com.cloud.util.PageDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: WorkRecordController
 * @Description: 排班记录
 * @author 小可爱
 * @date 2021-10-27
 */
@Service
public class WorkRecordService extends ServiceImpl<WorkRecordMapper, WorkRecordEntity> {
    @Autowired
    private SchedulingRulesService schedulingRulesService;
    @Autowired
    private UserServiceImpl userService;

    /**
     * 生成排班记录
     * @return
     */
    @Transactional
    public boolean generateShiftPlan() {
        //查询需要排班人员及规则
        List<WorkRecordVo> schedulingPlan = schedulingRulesService.getSchedulingPlan();
        //获取当前年月日
        String date = DateUtils.getDate();

        schedulingPlan.forEach(e->{
            WorkRecordEntity entity = new WorkRecordEntity();
            entity.setUserId(e.getUserId());
            entity.setArrangeRuleId(e.getArrangeRuleId());
            String time = date + " " +e.getWorkStartTime();
            Date workStartTime = DateUtils.parseDate(time);
            entity.setWorkSTime(workStartTime);
            DateTime workETime = DateUtil.offsetHour(workStartTime, e.getWorkingHours());
            entity.setWorkETime(workETime);
            entity.setWorkType(1);
            entity.setWorkDate(workStartTime);
            entity.insert();
        });
        return true;
    }

    /**
     * 查询排班记录
     * @param p
     * @return
     */
    public PageDataResult getPageList(Page p) {

        PageDataResult pdr = new PageDataResult();
        LambdaQueryWrapper<WorkRecordEntity> wrapper = new QueryWrapper<WorkRecordEntity>().lambda();
        wrapper.orderByDesc(WorkRecordEntity::getInsertTime);
        IPage<WorkRecordEntity> page = page(p,wrapper);
        List<WorkRecordEntity> records = page.getRecords();
        //获取用户信息
        Set<Long> uids = records.stream().map(WorkRecordEntity::getUserId).collect(Collectors.toSet());
        uids.addAll(records.stream().map(WorkRecordEntity::getInsertUid).collect(Collectors.toSet()));
        Map<Long, UserEntity> users = userService.getUsersByIdsIn(uids);
        records.forEach(e->{
            e.setUserName(users.get(e.getUserId()).getUsername());
        });
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }
}