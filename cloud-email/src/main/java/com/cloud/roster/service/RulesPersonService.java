package com.cloud.roster.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.roster.entity.RulesPersonEntity;
import com.cloud.roster.mapper.RulesPersonMapper;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: RulesPersonController
 * @Description: 排版人员规则关联
 * @author 小可爱
 * @date 2021-10-27
 */
@Service
public class RulesPersonService extends ServiceImpl<RulesPersonMapper, RulesPersonEntity> {

    @Autowired
    private UserServiceImpl userService;


    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdates(Long arrangeRuleId, String id) {
        String[] idArr = id.split(",");
        RulesPersonEntity e = new RulesPersonEntity();
        for (String s : idArr) {
            e = new RulesPersonEntity();
            e.setArrangeRuleId(arrangeRuleId);
            e.setUserId(Long.valueOf(s));
            LambdaUpdateWrapper<RulesPersonEntity> lambda = new UpdateWrapper<RulesPersonEntity>().lambda();
            lambda.eq(RulesPersonEntity::getUserId,Long.valueOf(s))
                    .eq(RulesPersonEntity::getArrangeRuleId,arrangeRuleId);
            saveOrUpdate(e,lambda);
        }
        return true;
    }

    public IPage<RulesPersonEntity> pageSelf(Page p, Long arrangeRuleId) {
        QueryWrapper<RulesPersonEntity> lambda = new QueryWrapper<RulesPersonEntity>();
        lambda.eq("arrange_rule_id",arrangeRuleId);
        IPage<RulesPersonEntity> page = page(p, lambda);
        List<RulesPersonEntity> records = page.getRecords();
        Set<Long> uid = records.stream().map(RulesPersonEntity::getUserId).collect(Collectors.toSet());
        Map<Long, UserEntity> mapUser = userService.getUsersByIdsIn(uid);
        records.forEach(e->{
            e.setUserName(mapUser.get(e.getUserId()).getUsername());
        });
        return page;
    }
}