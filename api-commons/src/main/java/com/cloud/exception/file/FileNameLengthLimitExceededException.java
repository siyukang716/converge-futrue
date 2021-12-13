package com.cloud.exception.file;

/**
 * 文件名称超长限制异常类
 * 
 * @author
 */
public class FileNameLengthLimitExceededException extends Exception
{
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength)
    {
        super("upload.filename.exceed.length", new Exception("文件名称超长限制:"+defaultFileNameLength));
    }
}
