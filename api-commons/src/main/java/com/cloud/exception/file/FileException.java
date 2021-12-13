package com.cloud.exception.file;
/**
 * 文件信息异常类
 * 
 * @author
 */
public class FileException extends Exception
{
    private static final long serialVersionUID = 1L;


    public FileException(String code, Throwable cause)
    {
        super( code,  cause);
    }

}
