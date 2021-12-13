package com.cloud.roster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Description: 接口文档配置
 * @Author lenovo
 * @Date: 2021/10/29 17:57
 * @Version 1.0
 */
@Configuration
public class RosterSwaggerConfig {


    @Bean
    public Docket createRosterRestApi(Environment environment) {
        //配置在什么环境可以使用swagger
        // 获取项目环境
        Profiles profiles = Profiles.of("dev");

        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启用 swagger   false 不能使用
                //.enable(flag)
                // swagger 分组
                .groupName("自动排班")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //为当前包路径  指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.cloud.roster"))
                //过滤什么路径
                .paths(PathSelectors.any())
                .build();

//        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
    }




    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        //ApiInfo apiInfo = new ApiInfo();

        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 使用 Swagger2 构建自动排班  RESTful API")
                //创建人
                //.contact(new Contact("Bryan", "http://blog.bianxh.top/", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }


}
