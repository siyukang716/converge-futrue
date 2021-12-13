package com.cloud.msg.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.msg.entity.MsgBusinessMsgMasterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 小可爱
 * @ClassName: MsgBusinessMsgMasterController
 * @Description: 消息模块, 业务消息
 * @date 2021-10-14
 */
public interface MsgBusinessMsgMasterMapper extends BaseMapper<MsgBusinessMsgMasterEntity> {

    IPage<MsgBusinessMsgMasterEntity> pageSelfMsg(Page p, @Param("ew") QueryWrapper<MsgBusinessMsgMasterEntity> wrapper);
}