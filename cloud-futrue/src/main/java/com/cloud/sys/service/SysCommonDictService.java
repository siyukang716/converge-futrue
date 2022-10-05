package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.sys.entity.SysCommonDictEntity;
import com.cloud.sys.mapper.SysCommonDictMapper;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: SysCommonDictController
 * @Description: 公共字典
 * @author 小可爱
 * @date 2021-10-12
 */
@Service
public class SysCommonDictService extends ServiceImpl<SysCommonDictMapper, SysCommonDictEntity> {


    /**
     * 根据字典类型(dictTypes) 获取字典主键集合
     * @param dictTypes
     * @return
     */
    public List<Long> getDictidsBydictType(String dictTypes){
        LambdaQueryWrapper<SysCommonDictEntity> lambda = new QueryWrapper<SysCommonDictEntity>().lambda();
        lambda.in(SysCommonDictEntity::getDictType,Arrays.asList(dictTypes.split(",")));
        Collection<SysCommonDictEntity> sysCommonDictEntities = list(lambda);
        return sysCommonDictEntities.stream().map(SysCommonDictEntity::getDictId).collect(Collectors.toList());
    }

    public Result saveOrUpdateLocal(SysCommonDictEntity entity) {
        Result result = Result.getInstance();

        if (null == entity.getDictId()){
            LambdaUpdateWrapper<SysCommonDictEntity> wrapper = Wrappers.<SysCommonDictEntity>lambdaUpdate();
            wrapper.eq(SysCommonDictEntity::getDictType,entity.getDictType());
            List<SysCommonDictEntity> list = super.list(wrapper);
            if (list.size() > 0){
                result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
                result.setMessage("字典类型重复!!!!!!");
                return result;
            }
        }



        boolean b = super.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}