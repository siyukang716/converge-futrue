package com.cloud.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.cloud.generator.entity.GenTableColumnEntity;
import com.cloud.generator.entity.GenTableEntity;
import com.cloud.generator.mapper.GenTableColumnMapper;
import com.cloud.generator.mapper.GenTableMapper;
import com.cloud.util.gene.GenUtils;
import com.cloud.util.gene.VelocityInitializer;
import com.cloud.util.gene.VelocityUtils;
import com.generator.CodeGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wjg
 * @ClassName: GenTableEntityService
 * @Description: 代码生成业务表
 * @date 2021-09-14
 */
@Service
public class GenTableService extends ServiceImpl<GenTableMapper, GenTableEntity> {
    @Autowired
    private GenTableMapper mapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;
    @Autowired
    private GenUtils genUtils;
    @Autowired
    private CodeGenerator codeGenerator;


    public List<GenTableEntity> selectDbTableListByNames(String tables) {
        String[] tableNames = tables.split(",");
        return mapper.selectDbTableListByNames(tableNames);
    }

    public void importGenTable(List<GenTableEntity> tableList, Long operName) {
        for (GenTableEntity table : tableList) {
            String tableName = table.getTableName();

            genUtils.initTable(table, operName + "");
            int row = mapper.insert(table);
            if (row > 0) {
                // 保存列信息
                List<GenTableColumnEntity> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                for (GenTableColumnEntity column : genTableColumns) {
                    genUtils.initColumnField(column, table);
                    genTableColumnMapper.insert(column);
                }
            }
        }
    }

    /**
     * 批量生成代码
     *
     * @param tableNames 表数组
     * @return 数据
     */
    public byte[] downloadCode(String[] tableNames) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


    /**
     * 查询表信息并生成代码
     */
    public void generatorCode(String tableName, ZipOutputStream zip) throws Exception {
        // 查询表信息
        LambdaQueryWrapper<GenTableEntity> lambda = new QueryWrapper<GenTableEntity>().lambda();
        lambda.eq(GenTableEntity::getTableId, tableName);
        GenTableEntity table = mapper.selectOne(lambda);

        LambdaQueryWrapper<GenTableColumnEntity> columnlambda = new QueryWrapper<GenTableColumnEntity>().lambda();
        columnlambda.eq(GenTableColumnEntity::getTableId, table.getTableId());
        columnlambda.orderByAsc(GenTableColumnEntity::getSort);
        List<GenTableColumnEntity> columns = genTableColumnMapper.selectList(columnlambda);


        VelocityInitializer.initVelocity();
        //codeGenerator.teate();
        VelocityContext context = VelocityUtils.prepareContext(table, columns);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());

        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }

    }

    /**
     * //Map<String, Object> objectMap = new HashMap<>(); String templatePath = ""; String outputFile ="";
     * //writer(objectMap,templatePath,outputFile);
     *
     * @param objectMap
     * @param templatePath
     * @param outputFile
     * @throws Exception
     */
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {

        if (!StringUtils.isEmpty(templatePath)) {
            Template template = Velocity.getTemplate(templatePath, ConstVal.UTF8);
            FileOutputStream fos = new FileOutputStream(outputFile);
            Throwable var6 = null;

            try {
                OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
                Throwable var8 = null;

                try {
                    BufferedWriter writer = new BufferedWriter(ow);
                    Throwable var10 = null;

                    try {
                        template.merge(new VelocityContext(objectMap), writer);
                    } catch (Throwable var54) {
                        var10 = var54;
                        throw var54;
                    } finally {
                        if (writer != null) {
                            if (var10 != null) {
                                try {
                                    writer.close();
                                } catch (Throwable var53) {
                                    var10.addSuppressed(var53);
                                }
                            } else {
                                writer.close();
                            }
                        }

                    }
                } catch (Throwable var56) {
                    var8 = var56;
                    throw var56;
                } finally {
                    if (ow != null) {
                        if (var8 != null) {
                            try {
                                ow.close();
                            } catch (Throwable var52) {
                                var8.addSuppressed(var52);
                            }
                        } else {
                            ow.close();
                        }
                    }

                }
            } catch (Throwable var58) {
                var6 = var58;
                throw var58;
            } finally {
                if (fos != null) {
                    if (var6 != null) {
                        try {
                            fos.close();
                        } catch (Throwable var51) {
                            var6.addSuppressed(var51);
                        }
                    } else {
                        fos.close();
                    }
                }

            }

            log.debug("模板:" + templatePath + ";  文件:" + outputFile);
        }
    }

    public IPage<GenTableEntity> selectDbTableList(Page<GenTableEntity> p) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>();

        return mapper.selectDbTableList(p, wrapper);
    }

    public Map<String, String> previewCode(Long tableId) {

        Map<String, String> dataMap = new LinkedHashMap<>();
        // 查询表信息
        LambdaQueryWrapper<GenTableEntity> lambda = new QueryWrapper<GenTableEntity>().lambda();
        lambda.eq(GenTableEntity::getTableId, tableId);
        GenTableEntity table = mapper.selectOne(lambda);

        LambdaQueryWrapper<GenTableColumnEntity> columnlambda = new QueryWrapper<GenTableColumnEntity>().lambda();
        columnlambda.eq(GenTableColumnEntity::getTableId, table.getTableId());
        columnlambda.orderByAsc(GenTableColumnEntity::getSort);
        List<GenTableColumnEntity> columns = genTableColumnMapper.selectList(columnlambda);
        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table, columns);
        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    public boolean delById(Long tableId) {
        mapper.deleteById(tableId);
        LambdaQueryWrapper<GenTableColumnEntity> lambda = new QueryWrapper<GenTableColumnEntity>().lambda();
        lambda.eq(GenTableColumnEntity::getTableId, tableId);
        genTableColumnMapper.delete(lambda);
        return true;
    }
}