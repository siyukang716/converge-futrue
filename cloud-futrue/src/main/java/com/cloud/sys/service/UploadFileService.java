package com.cloud.sys.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.config.autoload.AutoFilePathConfig;
import com.cloud.sys.entity.UploadFileEntity;
import com.cloud.sys.mapper.UploadFileMapper;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import com.cloud.util.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: UploadFileController
 * @Description: 断点续传
 * @author
 * @date 2021-09-18
 */
@Service
public class UploadFileService extends ServiceImpl<UploadFileMapper, UploadFileEntity> {



    @Autowired
    private UploadFileMapper fileMapper;
    @Autowired
    AutoFilePathConfig autoFilePathConfig;

    //保存文件
    public boolean saveFile(UploadFileEntity file1){
        //根据 数据库的 文件标识来查询 当前视频 是否存在
        LambdaQueryWrapper<UploadFileEntity> lambda = new QueryWrapper<UploadFileEntity>().lambda();
        lambda.eq(UploadFileEntity::getFileKey,file1.getFileKey());
        List<UploadFileEntity> fileDTOS = fileMapper.selectList(lambda);
        //如果存在就话就修改
        if(fileDTOS.size()!=0){
            //根据key来修改
            LambdaQueryWrapper<UploadFileEntity> lambda1 = new QueryWrapper<UploadFileEntity>().lambda();
            lambda1.eq(UploadFileEntity::getFileKey,file1.getFileKey());
            fileMapper.update(file1,lambda1);
        }else
        {
            //不存在就添加
            fileMapper.insert(file1);
        }
        return false;
    }

    //检查文件
    public List<UploadFileEntity> check(String key){
        LambdaQueryWrapper<UploadFileEntity> lambda = new QueryWrapper<UploadFileEntity>().lambda();
        lambda.eq(UploadFileEntity::getFileKey,key);
        List<UploadFileEntity> dtos = fileMapper.selectList(lambda);
        return dtos;
    }

    public Result uploadOneFile(MultipartFile file, String key) throws Exception {
        Result result = Result.getInstance();
        File upfile = FileUtils.multipartFileToFile(file);
        String extName = FileUtil.extName(upfile);
        Map<String, String> fileConfig = autoFilePathConfig.getFilePathById(key);
        String fileName = IdUtil.objectId();
        String path = fileConfig.get("path");
        if (!FileUtil.isDirectory(path)){//判断文件夹是否存在
            FileUtil.mkdir(fileConfig.get("path"));//不存在  创建
        }
        path += fileName+"."+extName;
        FileUtil.copy(upfile,new File(path),true);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("文件上传成功!!!!!!");
        result.setData(fileConfig.get("call")+fileName+"."+extName);
        FileUtil.del(upfile.getAbsolutePath());
        return result;
    }
}