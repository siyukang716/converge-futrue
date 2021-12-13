package com.spring.commons.entity.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import org.junit.Test;

import java.io.IOException;

/**
 * @Description: FtpDemo
 * @Author lenovo
 * @Date: 2021/5/11 9:56
 * @Version 1.0
 */
public class FtpDemo {
    @Test
    public void tet() throws IOException {
        //匿名登录（无需帐号密码的FTP服务器）
        //Ftp ftp = new Ftp("192.168.8.150");
        Ftp ftp = new Ftp("192.168.8.151",21,"root","123456");
        //进入远程目录
        ftp.cd("/mywork");
        //上传本地文件
        ftp.upload("/mywork/", FileUtil.file("e:/spring.txt"));
        //下载远程文件
        //ftp.download("/opt/upload", "test.jpg", FileUtil.file("e:/test2.jpg"));
        System.out.println("--------上传成功--------");
        //关闭连接
        ftp.close();

//        String phone = DesensitizedUtil.mobilePhone("18049531999");
//        System.out.println(phone);


//        String str = PinyinUtil.getPinyin("你好");
//        System.out.println(str);
//        String str2 = PinyinUtil.getPinyin("你好", " | ");
//        System.out.println(str2);




    }


}
