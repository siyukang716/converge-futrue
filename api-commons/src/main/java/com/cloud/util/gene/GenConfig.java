package com.cloud.util.gene;


import com.cloud.util.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 * 
 * @author
 */
@Component
//@ConfigurationProperties(prefix = "gen")
@PropertySource(value = { "classpath:generator.yml" },factory = YamlPropertySourceFactory.class)
@Data
public class GenConfig
{
    /** 作者 */
    @Value("${gen.author}")
    public String author;

    /** 生成包路径 */
    @Value("${gen.packageName}")
    public String packageName;

    /** 自动去除表前缀，默认是false */
    @Value("${gen.autoRemovePre}")
    public boolean autoRemovePre;

    /** 表前缀(类名不会包含表前缀) */
    @Value("${gen.tablePrefix}")
    public String tablePrefix;


}
