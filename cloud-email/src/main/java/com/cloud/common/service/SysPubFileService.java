package com.cloud.common.service;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.common.entity.SysPubFileEntity;
import com.cloud.common.mapper.SysPubFileMapper;
import com.cloud.fastdfs.FastDFSClient;
import com.cloud.sys.service.UploadFileService;
import com.cloud.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @ClassName: SysPubFileController
 * @Description: 公共文件
 * @author 小可爱
 * @date 2021-10-13
 */
@Service
public class SysPubFileService extends ServiceImpl<SysPubFileMapper, SysPubFileEntity> {

    @Autowired
    private UploadFileService uploadFileService;

    @Value("${upload.file.way}")
    private boolean way;
    @Autowired
    private FastDFSClient fastDFSClient;

    @Transactional
    public Result savePubFile(MultipartFile file, SysPubFileEntity entity) throws Exception {
        Result result = Result.getInstance();
        String url = "";
        if (way){
            url  = fastDFSClient.uploadFile(file);
        }else {
            result =   uploadFileService.uploadOneFile(file,entity.getTablePrimarykey());
            url = (String)result.getData();
        }

        String fileName = file.getOriginalFilename();
        String extName = FileUtil.extName(fileName);
        entity.setUpfileType(extName);
        String name = FileUtil.getPrefix(fileName);
        entity.setUpfileName(name);
        entity.setUpfileAddress(url);
        entity.insert();
        result.setData(entity);
        return result;
    }
}