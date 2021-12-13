package com.cloud.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Description: PasswordTest
 * @Author lenovo
 * @Date: 2021/11/8 16:08
 * @Version 1.0
 */
public class PasswordEncryption {
    public static void main(String[] args) {
        String hashAlgorithmName="SHA1";//加密算法（与配置文件中的一致）
        String credentials="applets";//密码
        ByteSource salt= ByteSource.Util.bytes("");//盐值
//		ByteSource salt=null;//盐值
        int hashIterations=1024;//加密次数（与配置文件中的一致）
//        System.out.println(new SimpleHash(hashAlgorithmName, credentials,salt, 1));

        hashAlgorithmName="MD5";

        System.out.println(new SimpleHash(hashAlgorithmName, "123", salt, 1));

    }

    public static String getMD5Password(String credentials,String salt){

        return new SimpleHash("MD5", credentials, salt, 1).toString();
    }
}
