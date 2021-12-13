package com.generator;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:/com/generator/generator.properties")
class GenerateApplicationTests {

    @Value("${datasource.url}")
    private String dataSourceurl;

    @Value("${datasource.username}")
    private String dataSourcename;

    @Value("${datasource.password}")
    private String dataSourcepassword;

    @Value("${datasource.driver-class-name}")
    private String dataSourcedriver;

    @Value("${datatables.name}")
    private String tables;

    @Value("${package.parent}")
    private String packageParent;

    @Value("${datatables.isNormalize}")
    private boolean isNormalize;

    @Test
    void generateMybatisPlusTest() {

    }
}