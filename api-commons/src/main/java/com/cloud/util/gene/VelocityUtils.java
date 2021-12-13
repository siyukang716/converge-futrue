package com.cloud.util.gene;

import com.cloud.generator.entity.GenTableColumnEntity;
import com.cloud.generator.entity.GenTableEntity;
import com.cloud.util.DateUtils;
import com.cloud.util.StringUtils;
import org.apache.velocity.VelocityContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VelocityUtils {
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = "main/java";

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /**
     * html空间路径
     */
    private static final String TEMPLATES_PATH = "main/resources/templates";

    /**
     * 默认上级菜单，系统工具
     */
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTableEntity genTable, List<GenTableColumnEntity> columns) {
        String packageName = genTable.getPackageName();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());

        velocityContext.put("packageName", packageName);
        velocityContext.put("entityName", genTable.getClassName());
        velocityContext.put("serviceName", genTable.getClassName()+"Service");
        velocityContext.put("controllerName", genTable.getClassName()+"Controller");
        velocityContext.put("mapperName", genTable.getClassName()+"Mapper");
        velocityContext.put("fileName", genTable.getClassName().toLowerCase());//html变量

        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));

        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("date", DateUtils.getDate());
        velocityContext.put("columns", columns);
        velocityContext.put("table", genTable);
        String fieldNames = columns.stream().map(GenTableColumnEntity::getColumnName).collect(Collectors.joining(","));
        velocityContext.put("fieldNames", fieldNames);

        return velocityContext;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory) {
        List<String> templates = new ArrayList<String>();
        templates.add("vm/entity.java.vm");
        templates.add("vm/controller.java.vm");
        templates.add("vm/mapper.java.vm");
        templates.add("vm/mapper.xml.vm");
        templates.add("vm/service.java.vm");


        templates.add("vm/list.html.vm");
        templates.add("vm/form.html.vm");
        templates.add("vm/list.js.vm");

        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTableEntity genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String htmlfileName = genTable.getClassName().toLowerCase();
        String javaPath = StringUtils.replace(packageName, ".", "/");

        if (template.contains("entity.java.vm")) {
            fileName = StringUtils.format("{}/entity/{}Entity.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        }  else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/{}Service.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format("{}/xml/{}Mapper.xml", javaPath, className);
        } else if (template.contains("list.html.vm")) {
            fileName = StringUtils.format("{}/html/{}.html", javaPath, htmlfileName);
        } else if (template.contains("form.html.vm")) {
            fileName = StringUtils.format("{}/html/form/{}Form.html", javaPath, htmlfileName);
        } else if (template.contains("list.js.vm")) {
            fileName = StringUtils.format("{}/js/{}.js", javaPath, htmlfileName);
        }
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        String basePackage = StringUtils.substring(packageName, 0, lastIndex);
        return basePackage;
    }


}