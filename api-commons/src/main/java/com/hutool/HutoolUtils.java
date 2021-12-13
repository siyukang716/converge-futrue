package com.hutool;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @Description: HutoolUtils
 * @Author lenovo
 * @Date: 2021/8/29 11:12
 * @Version 1.0
 */
public class HutoolUtils {
    private final static Snowflake snowflake = IdUtil.getSnowflake();

    public static Long getSnowflakeId() {
        return snowflake.nextId();
    }

    @Test
    public void encty() {
//        String testStr = "加密文件";
//
//        AES aes = SecureUtil.aes();
//        byte[] encrypt = aes.encrypt(testStr);//加密
//        System.out.println("加密:"+encrypt);
//        byte[] decrypt = aes.decrypt(encrypt);//解密
//        System.out.println("解密:"+decrypt);

        String content = "test中文";

//随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        System.out.println("哈哈哈哈哈w".getBytes(StandardCharsets.UTF_8));
//构建
        AES aes = SecureUtil.aes("哈哈哈哈哈w".getBytes(StandardCharsets.UTF_8));

//加密
        byte[] encrypt = aes.encrypt(content);
//解密
        byte[] decrypt = aes.decrypt(encrypt);
        System.out.println("-------------------------");
        System.out.println(encrypt);
        System.out.println(decrypt);
        System.out.println(new String(encrypt));
        System.out.println(new String(decrypt));
//构建
        DES des = SecureUtil.des(key);
//加密为16进制表示
        String encryptHex = des.encryptHex(content);
//解密为原字符串
        String decryptStr = des.decryptStr(encryptHex);
        System.out.println(encryptHex);
        System.out.println(decryptStr);

    }


}
