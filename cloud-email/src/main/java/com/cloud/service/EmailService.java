package com.cloud.service;

import com.spring.commons.entity.CommonResult;
import com.spring.commons.entity.ToEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public CommonResult commonEmail(ToEmail toEmail) {
        CommonResult result = new CommonResult();
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo(toEmail.getTos());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(toEmail.getContent());
        try {
            mailSender.send(message);
            toEmail.getTos();
            result.setMessage("发送普通邮件成功");
            return result;
        } catch (MailException e) {
            e.printStackTrace();
            result.setMessage("普通邮件方失败");
            return result;
        }
    }


    public CommonResult htmlEmail(ToEmail toEmail){
        CommonResult result = new CommonResult();
        try {
            //创建一个MINE消息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            //谁发
            minehelper.setFrom(from);
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件主题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            minehelper.setText(toEmail.getContent(), true);
            mailSender.send(message);
            result.setMessage(toEmail.getTos() + toEmail.getContent()+ "HTML邮件成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("HTML邮件失败");
            return result;
        }
    }
    public CommonResult staticEmail(ToEmail toEmail, MultipartFile multipartFile, String resId) {
        CommonResult result = new CommonResult();
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(from);
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            //邮件内容拼接
            String content =
                    "<html><body><img width='250px' src=\'cid:" + resId + "\'>" + toEmail.getContent()
                            + "</body></html>";
            helper.setText(content, true);
            //蒋 multpartfile 转为file
            File multipartFileToFile = MultipartFileToFile(multipartFile);
            FileSystemResource res = new FileSystemResource(multipartFileToFile);

            //添加内联资源，一个id对应一个资源，最终通过id来找到该资源
            helper.addInline(resId, res);
            mailSender.send(message);
            result.setMessage(toEmail.getTos() + toEmail.getContent()+ "嵌入静态资源的邮件已经发送");
            return result;
        } catch (MessagingException e) {
            result.setMessage("嵌入静态资源的邮件发送失败");
            return result;
        }
    }

    private File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public CommonResult enclosureEmail(ToEmail toEmail, MultipartFile multipartFile) {
        CommonResult result = new CommonResult();
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(from);
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            helper.setText(toEmail.getContent(), true);
            File multipartFileToFile = MultipartFileToFile(multipartFile);
            FileSystemResource file = new FileSystemResource(multipartFileToFile);
            String filename = file.getFilename();
            //添加附件
            helper.addAttachment(filename, file);
            mailSender.send(message);
            result.setMessage("附件邮件成功");
            return result;
        } catch (MessagingException e) {
            e.printStackTrace();
            result.setMessage("附件邮件发送失败" + e.getMessage());
            return result;
        }
    }

}
