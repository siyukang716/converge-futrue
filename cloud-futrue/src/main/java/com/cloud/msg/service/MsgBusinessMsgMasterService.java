package com.cloud.msg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.msg.entity.MsgBusinessMsgMasterEntity;
import com.cloud.msg.entity.MsgBusinessSubEntity;
import com.cloud.msg.mapper.MsgBusinessMsgMasterMapper;
import com.cloud.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @ClassName: MsgBusinessMsgMasterController
 * @Description: 消息模块,业务消息
 * @author 豆芽菜
 * @date 2021-10-14
 */
@Service
public class MsgBusinessMsgMasterService extends ServiceImpl<MsgBusinessMsgMasterMapper, MsgBusinessMsgMasterEntity> {

    @Autowired
    private MsgBusinessSubService subService;
    @Autowired
    private MsgBusinessMsgMasterMapper mapper;

    /**
     * 插入消息   和消息订阅人员
     * @param msg 消息主表
     * @param userIds  消息订阅用户id集合
     * @return
     */
    @Transactional
    public boolean insertMsg(MsgBusinessMsgMasterEntity msg, List<Long> userIds){
        //插入消息主表
        msg.setMsgPostTime(new Date());
        boolean insert = msg.insert();
        if (!insert)return insert;
        Long msgId = msg.getMsgId();
        List<MsgBusinessSubEntity> subs = new ArrayList<>();
        userIds.forEach(uid->{
            MsgBusinessSubEntity subEntity = new MsgBusinessSubEntity();
            subEntity.setMsgId(msgId);
            subEntity.setUserId(uid);
            subs.add(subEntity);
        });
        //插入消息订阅人
        return subService.saveBatch(subs);
    }

    /**
     * 查询当前人员消息列表
     * @param p
     * @return
     */
    public IPage<MsgBusinessMsgMasterEntity> pageSelfMsg(Page p) {
        Long loginUserId = ShiroUtils.getLoginUserId();
        QueryWrapper<MsgBusinessMsgMasterEntity> wrapper = new QueryWrapper<MsgBusinessMsgMasterEntity>();
        wrapper.eq("bs.user_id",loginUserId);
        wrapper.orderByDesc("bmm.msg_post_time");
        IPage<MsgBusinessMsgMasterEntity> page = mapper.pageSelfMsg(p,wrapper);
        return page;
    }
}