package com.cloud.common.fastdfs;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: FastDFSClient
 * @Author lenovo
 * @Date: 2021/11/20 13:32
 * @Version 1.0
 */
@Component
public class FastDFSClient {

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) {
        try {
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
            return storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @param in
     * @param suffixName
     * @return
     */
    public String uploadFile(InputStream in, String suffixName) {
        try {
            StorePath storePath = storageClient.uploadFile(in, in.available(), suffixName, null);
            return storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param fileUrl
     * @return
     */
    public boolean deleteFile(String fileUrl) {
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }



}
