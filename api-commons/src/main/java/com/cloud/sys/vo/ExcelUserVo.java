package com.cloud.sys.vo;

import com.cloud.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @Description: ExcelUserVo   导入用户实体类
 * @Author lenovo
 * @Date: 2021/11/22 12:52
 * @Version 1.0
 */
@Data
public class ExcelUserVo {
    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String name;
    /**
     * 性别
     */
    @Excel(name = "性别",combo = {"男","女"})
    private String sex;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private Integer age;

    /**
     * 职务/岗位
     */
    @Excel(name = "职务/岗位")
    private String position;

    /**
     * 电话
     */
    @Excel(name = "电话")
    private String tel;

    /**
     * 入职时间
     */
    @Excel(name = "入职时间")
    private Date entryTime;

    /**
     * 组织结构
     */
    @Excel(name = "部门",field="deptCombo" )
    private String dept;


}
