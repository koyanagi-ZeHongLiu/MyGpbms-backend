package com.example.gpbms.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 职责：<br/>
 * Md5加密工具类 <br/>
 * @author ccy1024
 * @date 2017年1月8日 上午7:33:01
 */
public class Md5Utils {
    
    /**
     * Md5加密
     */
    public static String md5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("没有提供Md5加密算法");
        }
    }
}
