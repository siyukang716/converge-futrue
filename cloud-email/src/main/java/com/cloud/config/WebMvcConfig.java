package com.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.List;

/**
 * 自定义静态资源映射路径和静态资源存放路径
 */
//@EnableWebMvc
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加静态资源--过滤swagger-api (开源的在线API文档)
     * 添加自定义静态资源映射路径和静态资源存放路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html","doc.html")//
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    /**
     * 类型转换器 long 交互精度问题
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //super.configureMessageConverters(converters);
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        // 如果存在fastJson的转换器，将其移除
//        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof FastJsonHttpMessageConverter);

//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)));
//        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)));
//        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)));
//        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)));
//        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
//        objectMapper.registerModule(javaTimeModule);
        /**
         * 序列化换成json时，将所有的long变成string
         * 因为js中的数字类型不能包含所有的java long 值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);


        // 需要重新加入jackson的转换器，该处的objectMapper已经在配置中注册了
        //converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
        converters.add(0,jackson2HttpMessageConverter);
        /**
         * 修改StringHttpMessageConverter默认配置
         * @param converters
         */
        //converters.add(responseBodyStringConverter());


    }


//    @Bean
//    public Converter<String, LocalDateTime> localDateTimeConvert() {
//        return new Converter<String, LocalDateTime>() {
//            @Override
//            public LocalDateTime convert(String source) {
//                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                LocalDateTime dateTime = null;
//                try {
//                    //2020-01-01 00:00:00
//                    switch (source.length()){
//                        case 10:
//                            log.debug("传过来的是日期格式：{}",source);
//                            source=source+" 00:00:00";
//                            break;
//                        case 13:
//                            log.debug("传过来的是日期 小时格式：{}",source);
//                            source=source+":00:00";
//                            break;
//                        case 16:
//                            log.debug("传过来的是日期 小时:分钟格式：{}",source);
//                            source=source+":00";
//                            break;
//                    }
//                    dateTime = LocalDateTime.parse(source, df);
//                } catch (Exception e) {
//                    log.error(e.getMessage(),e);
//                }
//                return dateTime;
//            }
//        };
//    }


    @Bean
    public ResourceUrlProvider resourceUrlProvider(){
        ResourceUrlProvider resourceUrlProvider = new ResourceUrlProvider();
        return resourceUrlProvider;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //registry.addInterceptor(new ResourceUrlProviderExposingInterceptor(resourceUrlProvider())).addPathPatterns("/**");

    }

}
