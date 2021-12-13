package com;


import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication
//扫描mybatis  mapper 包路径
//@MapperScan(basePackages = {"com.cloud.order.mapper","com.cloud.sys.mapper","com.cloud.contractor.mapper",
//        "com.cloud.product.mapper","com.cloud.mapper","com.cloud.generator.mapper","com.cloud.activiti.mapper"
//        ,"com.cloud.common.mapper","com.cloud.msg.mapper","com.cloud.education.mapper","com.cloud.riskcontrol.mapper","com.cloud.work.mapper",
//       "com.cloud.roster.mapper"})
@MapperScan("com.cloud.**.mapper")
//@EnableTransactionManagement(proxyTargetClass=true)
//开启定时任务注解
@EnableScheduling
//开启异步调用方法
//@EnableAsync
///Swagger 接口生成器
@EnableSwagger2
@SpringBootApplication(exclude={
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
})
//openFegin
@EnableFeignClients
//@EnableEurekaClient
@EnableTransactionManagement
public class EmailMain  extends SpringBootServletInitializer {
    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EmailMain.class);
    }*/
    public static void main(String[] args) {

        SpringApplication.run(EmailMain.class,args);

    }


    @Value("${server.http.port}")
    private int httpPort;
    @Value("${server.port}")
    private int httpsPort;
    //下面是2.0的配置，1.x请搜索对应的设置
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    private Connector createHTTPConnector() {
        Connector connector = new  Connector("org.apache.coyote.http11.Http11NioProtocol");
        //同时启用http（8080）、https（8443）两个端口
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(httpPort);
        connector.setRedirectPort(httpsPort);
        return connector;
    }

}
