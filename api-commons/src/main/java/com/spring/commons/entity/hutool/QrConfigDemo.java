package com.spring.commons.entity.hutool;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.script.ScriptUtil;
import cn.hutool.system.SystemUtil;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import java.awt.*;

/**
 *
 * @Description: QrConfigDemo
 * @Author lenovo
 * @Date: 2021/5/19 10:27
 * @Version 1.0
 */
public class QrConfigDemo {

    public static void main(String[] args) {
        QrConfig config = new QrConfig(300, 300);
// 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
// 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN.getRGB());
// 设置背景色（灰色）
        config.setBackColor(Color.GRAY.getRGB());
// 生成二维码到文件，也可以到流
        // 高纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        config.setImg("e:/111.jpg");

        //QrCodeUtil.generate("http://www.baidu.com/", config, FileUtil.file("e:/qrcode.jpg"));



    }
    @Test
    public void test1(){
        ScriptUtil.eval("print('Script test!');");
    }

    @Test
    public void test2(){
        SystemUtil.getUserInfo();
    }


    @Test
    public void test3(){
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100,4,0);
        //图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write("d:/line.png");
        //输出code
        Console.log(lineCaptcha.getCode());
        //验证图形验证码的有效性，返回boolean值
        boolean verify = lineCaptcha.verify("1234");
        Console.log(verify);
        //重新生成验证码
//        lineCaptcha.createCode();
//        lineCaptcha.write("e:/line.png");
//        //新的验证码
//        Console.log(lineCaptcha.getCode());
//        //验证图形验证码的有效性，返回boolean值
//        Console.log(lineCaptcha.verify("1234"));
    }
}
