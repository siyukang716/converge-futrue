package com.spring.commons.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by lenovo on 2021/3/18.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "payment")
public class Payment implements Serializable {
    @TableId
    private Long id;
    private String serial;
}
