package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 小可爱
 * @ClassName: DictSeq
 * @Description: 字典序列
 * @date 2021-11-21
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_seq")
@ApiModel("字典序列")
public class DictSeqEntity extends Model<DictSeqEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;


    /**
     * 序列号
     */
    @TableField("serial_number")
    @ApiModelProperty(value = "序列号")
    private Integer serialNumber;


    /**
     * 业务类型
     */
    @TableField("business_type")
    @ApiModelProperty(value = "业务类型")
    private String businessType;


}