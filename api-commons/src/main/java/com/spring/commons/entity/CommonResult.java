package com.spring.commons.entity;

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
public class CommonResult<T> implements Serializable {
    private Integer code = 200;
    private String message;
    private T data;
    private String serverPort;

    public CommonResult(Integer code, String message){
        this(code,message,null,null);
    }
}
