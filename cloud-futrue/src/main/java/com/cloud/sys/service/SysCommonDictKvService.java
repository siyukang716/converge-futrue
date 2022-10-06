package com.cloud.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.sys.entity.SysCommonDictKvEntity;
import com.cloud.sys.mapper.SysCommonDictKvMapper;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: SysCommonDictKvController
 * @Description: 字典管理
 * @author 豆芽菜
 * @date 2021-10-12
 */
@Service
public class SysCommonDictKvService extends ServiceImpl<SysCommonDictKvMapper, SysCommonDictKvEntity> {
    @Autowired
    private SysCommonDictService dictService;

    public Result getDictMapById(String dictTypes) {
        Result result = Result.getInstance();
        LambdaQueryWrapper<SysCommonDictKvEntity> lambda = new QueryWrapper<SysCommonDictKvEntity>().lambda();
        lambda.select(SysCommonDictKvEntity::getDictChildId,SysCommonDictKvEntity::getDictTag,SysCommonDictKvEntity::getDictId,SysCommonDictKvEntity::getDictCode);
        lambda.eq(SysCommonDictKvEntity :: getIsDel,1);
        lambda.eq(SysCommonDictKvEntity :: getDictStatus,1);
        lambda.in(SysCommonDictKvEntity::getDictId, Arrays.asList(dictTypes.split(",")));
        List<SysCommonDictKvEntity> kvlist = super.list(lambda);
        Map<String, List<SysCommonDictKvEntity>> data = kvlist.stream().collect(Collectors.groupingBy(SysCommonDictKvEntity::getDictId));
        result.setData(data);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
        if (kvlist == null){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
    public Result getDictById(String dictType) {
        Result result = Result.getInstance();
        LambdaQueryWrapper<SysCommonDictKvEntity> lambda = new QueryWrapper<SysCommonDictKvEntity>().lambda();
        lambda.select(SysCommonDictKvEntity::getDictChildId,SysCommonDictKvEntity::getDictTag,SysCommonDictKvEntity::getDictId,SysCommonDictKvEntity::getDictCode);
        lambda.eq(SysCommonDictKvEntity :: getIsDel,1);
        lambda.eq(SysCommonDictKvEntity :: getDictStatus,1);
        lambda.eq(SysCommonDictKvEntity::getDictId, dictType);
        List<SysCommonDictKvEntity> kvlist = super.list(lambda);
        result.setData(kvlist);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
        if (kvlist == null){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    public Result saveOrUpdateLocal(SysCommonDictKvEntity entity) {
        Result result = Result.getInstance();



        if (null == entity.getDictChildId()){
            LambdaUpdateWrapper<SysCommonDictKvEntity> wrapper = Wrappers.<SysCommonDictKvEntity>lambdaUpdate();
            wrapper.eq(SysCommonDictKvEntity::getDictId,entity.getDictId());
            wrapper.eq(SysCommonDictKvEntity::getDictCode,entity.getDictCode());
            List<SysCommonDictKvEntity> list = super.list(wrapper);
            if (list.size() > 0){
                result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
                result.setMessage("字典值重复!!!!!!");
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

    public Result updateStatus(Integer dictStatus, Long dictChildId) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<SysCommonDictKvEntity> wrapper = Wrappers.<SysCommonDictKvEntity>lambdaUpdate();
        wrapper.set(SysCommonDictKvEntity::getDictStatus,dictStatus);
        wrapper.eq(SysCommonDictKvEntity::getDictChildId,dictChildId);
        boolean update = super.update(wrapper);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功!!!!!!");
        if (!update) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }


}