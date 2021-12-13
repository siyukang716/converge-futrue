package com.cloud.config;

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

/*
        - @Api()用于类；
        表示标识这个类是swagger的资源
        - @ApiOperation()用于方法；
        表示一个http请求的操作
        - @ApiParam()用于方法，参数，字段说明；
        表示对参数的添加元数据（说明或是否必填等）
        - @ApiModel()用于类
        表示对类进行说明，用于参数用实体类接收
        - @ApiModelProperty()用于方法，字段
        表示对model属性的说明或者数据操作更改
        - @ApiIgnore()用于类，方法，方法参数
        表示这个方法或者类被忽略
        - @ApiImplicitParam() 用于方法
        表示单独的请求参数
        - @ApiImplicitParams() 用于方法，包含多个 @ApiImplicitParam

具体使用举例说明：
@Api()
用于类；表示标识这个类是swagger的资源
        tags–表示说明
        value–也是说明，可以使用tags替代
        但是tags如果有多个值，会生成多个list
*/

@Configuration
//@EnableSwagger2
//@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(Environment environment) {
        //配置在什么环境可以使用swagger
        // 获取项目环境
        Profiles profiles = Profiles.of("dev");

        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启用 swagger   false 不能使用
                //.enable(flag)
                // swagger 分组
                .groupName("系统用户权限")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //为当前包路径  指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.cloud.sys.controller"))
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
                .title("Spring Boot 使用 Swagger2 构建RESTful API")
                //创建人
                //.contact(new Contact("Bryan", "http://blog.bianxh.top/", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }

    @Bean
    public Docket docket1(Environment environment){
        //配置在什么环境可以使用swagger
        // 获取项目环境
        Profiles profiles = Profiles.of("dev");

        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启用 swagger   false 不能使用
                //.enable(flag)
                // swagger 分组
                .groupName("风险管控")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //为当前包路径  指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.cloud.riskcontrol"))
                //过滤什么路径
                .paths(PathSelectors.any())
                .build();
    }



    /*@Bean
    public Docket docket2(Environment environment){
        //配置在什么环境可以使用swagger
        // 获取项目环境
        Profiles profiles = Profiles.of("dev");

        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启用 swagger   false 不能使用
                //.enable(flag)
                // swagger 分组
                .groupName("order")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //为当前包路径  指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.cloud.order"))
                //过滤什么路径
                .paths(PathSelectors.any())
                .build();
    }*/

    @Bean
    public Docket encry(Environment environment) {
        //配置在什么环境可以使用swagger
        // 获取项目环境
        Profiles profiles = Profiles.of("dev");

        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否启用 swagger   false 不能使用
                //.enable(flag)
                // swagger 分组
                .groupName("加密接口")
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //为当前包路径  指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.cloud.springbootencry.web"))
                //过滤什么路径
                .paths(PathSelectors.any())
                .build();
    }



//    @Bean
//    public Docket docket2(){
//        return new Docket(DocumentationType.SWAGGER_2).groupName("BBBB");
//    }
//    @Bean
//    public Docket docket3(){
//        return new Docket(DocumentationType.SWAGGER_2).groupName("CCCC");
//    }

   /* @Bean
    public Docket creatRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.study.springboot.controller"))
                .paths(PathSelectors.any()).build();

    }

    private ApiInfo apiInfo() {
        return  new ApiInfoBuilder()
                .title("springboot利用swagger构建api文档")
                .description("简单优雅的restfun风格，http://blog.csdn.net/saytime")
                .termsOfServiceUrl("http://blog.csdn.net/saytime")
                .version("1.0")
                .build();
    }*/

}
