package com.generator;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: CodeGenerator
 * @Author lenovo
 * @Date: 2021/8/4 14:54
 * @Version 1.0
 */

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
@Component
public class CodeGenerator {
    private String dataUrl = "jdbc:mysql://127.0.0.1:3306/my_study?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
    private String dataUsername = "root";
    private String dataPassword = "root";
    //private String needTable = "customer_login,customer_inf,customer_level_inf,customer_addr,customer_point_log,customer_balance_log,customer_login_log";  //需要生成的表
    //private String needTable = "brand_info,product_category,supplier_info,product_info,product_pic_info,product_comment";
    private String needTable = "gen_table,gen_table_column";
    private String parentPackage = "com.cloud.generator";
    private String entityPackage= "entity";
    private String mapperPackage= "mapper";
    private String xmlPackage= "xml";
    private String servicePackage= "service";
    private String webPackage= "controller";









    @Test
    public void teate() {

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir("C://Users//lenovo//Desktop");//代码生成存放位置
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setOpen(false);
        gc.setAuthor("wjg");//此处填写代码生成人姓名
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        //gc.setServiceImplName("%sService");
        gc.setServiceImplName("%sService");
        // gc.setControllerName("%sAction");
        gc.setEntityName("%sEntity");
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(dataUrl);
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");//("com.mysql.jdbc.Driver");
        dsc.setUsername(dataUsername);
        dsc.setPassword(dataPassword);
        /*dsc.setTypeConvert(new SqlServerTypeConvert(){
        // 自定义数据库表字段类型转换【可选】
        @Override
        public DbColumnType processTypeConvert(String fieldType) {
        if (fieldType.contains("tinyint") ) {
        return DbColumnType.BLOB;
        }
        // 注意！！processTypeConvert 存在默认类型转换，
        //如果不是你要的效果请自定义返回、非如下直接返回。
        return super.processTypeConvert(fieldType);
        }
        });*/
        mpg.setDataSource(dsc);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setTablePrefix("sys_");// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略

        /**
         * 第一步
         * 代码生成需要改动这里，将待生成的表名替换到下面
         *
         * 2017.11.2
         */
        strategy.setInclude(needTable.split(","));// 需要生成的表
        /**
         * 第二步
         * 去掉表名前缀
         *
         * 2017.11.2
         */
        //strategy.setTablePrefix(new String[]{"t_"});// 此处可以修改为您的表前缀
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        strategy.setSuperEntityClass("com.mayi.test.base.BaseEntity");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(
                new String[]{});
        // 自定义 mapper 父类
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.mapper.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceImplClass("com.mayi.test.base.BaseService");
        // 自定义 controller 父类
        strategy.setSuperControllerClass("com.mayi.test.base.BaseController");
        //逻辑删除属性名称
        strategy.setLogicDeleteFieldName("is_deleted");
        List<TableFill> tableFillList = CollUtil.newArrayList();
        TableFill fill = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        tableFillList.add(fill);

        fill = new TableFill("create_time", FieldFill.INSERT);
        tableFillList.add(fill);

        strategy.setTableFillList(tableFillList);

        // 自定义 service 实现类父类
        //strategy.setSuperServiceImplClass("org.ibase4j.core.base.BaseService");
        // 自定义 controller 父类
        //strategy.setSuperControllerClass("org.ibase4j.core.base.AbstractController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        mpg.setStrategy(strategy);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackage);//此处填写包名
        /**
         * 第三步
         * 按模块调整代码放置目录
         *
         * 2017.11.2
         */
        pc.setEntity(entityPackage);
        pc.setMapper(mapperPackage);
        pc.setXml(xmlPackage);
        pc.setServiceImpl(servicePackage);
        pc.setController(webPackage);
        mpg.setPackageInfo(pc);
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        //        InjectionConfig cfg = new InjectionConfig() {
        //            public void initMap() {
        //            Map<String, Object> map = new HashMap<String, Object>();
        //            map.put("providerClass", "ISysProvider");
        //            map.put("providerClassPackage", "org.ibase4j.provider.ISysProvider");
        //            this.setMap(map);
        //            }
        //        };
        //        mpg.setCfg(cfg);
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setEntity("templates/entity.java.vm");
        tc.setMapper("templates/mapper.java.vm");
        tc.setXml("templates/mapper.xml.vm");
        tc.setServiceImpl("templates/service.java.vm");
        tc.setController("templates/controller.java.vm");

        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
        System.out.println("代码生成完毕");
    }




}

