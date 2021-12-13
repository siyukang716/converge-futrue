package com.cloud.sys.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.fastdfs.FastDFSClient;
import com.cloud.sys.entity.UploadFileEntity;
import com.cloud.sys.service.UploadFileService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.List;


/**
 * @author
 * @ClassName: UploadFileController
 * @Description: TODO(这里用一句话描述这个类的作用)断点续传
 * @date 2021-09-18
 */
@Controller
@Api(value = "断点续传crud功能", tags = "断点续传crud功能")
@RequestMapping(value = "system/file")
@Slf4j
public class UploadFileController {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private FastDFSClient fastDFSClient;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/uploadfile");
        return model;
    }

    /**
     * 列表查询
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<UploadFileEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<UploadFileEntity> wrapper = new QueryWrapper<UploadFileEntity>();
        IPage<UploadFileEntity> page = uploadFileService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }

    // 设置图片上传路径
    //@Value("${file.basepath}")
    private String basePath = "/upload";


    /**
     * 上传
     *
     * @param file
     * @param suffix
     * @param shardIndex
     * @param shardSize
     * @param shardTotal
     * @param size
     * @param key
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file,
                         String suffix,
                         Long shardIndex,
                         Long shardSize,
                         Long  shardTotal,
                         Long size,
                         String key
    ) throws IOException, InterruptedException {
        log.info("上传文件开始");
        key = SecureUtil.md5(key);
        //文件的名称
        String name = IdUtil.fastUUID().replaceAll("-", "");
        // 获取文件的扩展名
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        //设置图片新的名字
        String fileName = new StringBuffer().append(key).append(".").append(suffix).toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4
        //这个是分片的名字
        String localfileName = new StringBuffer(fileName)
                .append(".")
                .append(shardIndex)
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4.1

        // 以绝对路径保存重名命后的图片
        File targeFile = new File(basePath, localfileName);
        //上传这个图片
        file.transferTo(targeFile);
        //数据库持久化这个数据
        UploadFileEntity file1 = new UploadFileEntity();
        file1.setPath(basePath + localfileName);
        file1.setFileName(name);
        file1.setSuffix(ext);
        file1.setSize(size);
        file1.setCreatedAt(System.currentTimeMillis());
        file1.setUpdatedAt(System.currentTimeMillis());
        file1.setShardIndex(shardIndex);
        file1.setShardSize(shardSize);
        file1.setShardTotal(shardTotal);
        file1.setFileKey(key);
        //插入到数据库中
        //保存的时候 去处理一下 这个逻辑
        uploadFileService.saveFile(file1);
        //判断当前是不是最后一个分页 如果不是就继续等待其他分页 合并分页
        if (shardIndex.equals(shardTotal)) {
            file1.setPath(basePath + fileName);
            this.merge(file1);
        }
        return "上传成功";
    }

    @RequestMapping("/check")
    @ResponseBody
    public Result check(String key) {
        key = SecureUtil.md5(key);
        Result result = Result.getInstance();
        List<UploadFileEntity> check = uploadFileService.check(key);
        //如果这个key存在的话 那么就获取上一个分片去继续上传
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        if (check.size() > 0) {
            result.setData(check.get(0));
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("获取成功!!!!!!");
        }
        return result;
    }


    /**
     * @author fengxinglie
     * 合并分页
     */
    private void merge(UploadFileEntity fileDTO) throws FileNotFoundException, InterruptedException {
        //合并分片开始
        log.info("分片合并开始");
        String path = fileDTO.getPath(); //获取到的路径 没有.1 .2 这样的东西
        //截取视频所在的路径
        path = path.replace(basePath, "");
        Long shardTotal = fileDTO.getShardTotal();
        File newFile = new File(basePath + path);
        FileOutputStream outputStream = new FileOutputStream(newFile, true); // 文件追加写入
        FileInputStream fileInputStream = null; //分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                // 读取第i个分片
                fileInputStream = new FileInputStream(new File(basePath + path + "." + (i + 1))); // course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                log.info("IO流关闭");
            } catch (Exception e) {
                log.error("IO流关闭", e);
            }
        }
        log.info("分片结束了");
        //告诉java虚拟机去回收垃圾 至于什么时候回收 这个取决于 虚拟机的决定
        System.gc();
        //等待100毫秒 等待垃圾回收去 回收完垃圾
        Thread.sleep(100);
        log.info("删除分片开始");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = basePath + path + "." + (i + 1);
            File file = new File(filePath);
            boolean result = file.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("删除分片结束");
    }






    /**
     * 临时文件是否存在，存在是否是完整的文件(是就是秒传)，不是完整的就分片继续上传,不完整告诉前台从哪里开始继续传
     *
     * @param key  文件的唯一标识
     * @param size 总文件大小
     * @return
     */
    @PostMapping("checkKey")
    @ResponseBody
    public Result checkSingleFile(@RequestParam("key") String key, @RequestParam("size") Integer size) {
        //临时单文件
        Result result = Result.getInstance();
        File newFile = new File(basePath + key);
        if (newFile.exists()) {
            if (newFile.length() == (size)) {
                result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
                result.setMessage("秒传成功!!!!!!");
            }
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("秒传成功!!!!!!");
            result.setData(newFile.length());
        } else {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("查询失败,可以添加");
        }
       return result;
    }

    /**
     * 单个临时文件分片上传
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping("singleFile")
    @ResponseBody
    public Result uploadOneFile(MultipartFile file,String key) throws Exception {
        return uploadFileService.uploadOneFile(file,key);
    }




    /**
     * 单个临时文件分片上传
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping("fastUpload")
    @ResponseBody
    public String fastUpload(MultipartFile file) throws Exception {
        String s = fastDFSClient.uploadFile(file);
        return s;
    }



}