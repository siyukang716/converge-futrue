package com.cloud.deemo;

import com.cloud.service.EmailService;
import com.spring.commons.entity.ToEmail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTest {

    @Autowired
    private EmailService emailService;
    @Test
    public void testHtml() throws Exception {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h1>康，晚上几点下班呀  O(∩_∩)O哈哈~</h1>\n" +
                "</body>\n" +
                "</html>";
        emailService.htmlEmail(new ToEmail(new String[]{"2693258139@qq.com","1312050204@qq.com","753339491@qq.com"},"Html邮件",content));
    }

}
