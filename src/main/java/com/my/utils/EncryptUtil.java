package com.my.utils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * 加密工具
 * @author whm
 * @date 2018/7/27
 */
public class EncryptUtil {

    private static char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static void main(String[] args) throws Exception {
        String input = "123456";
        System.out.println("MD5加密" + "\n"
                + "明文：" + input + "\n"
                + "无盐密文：" + encryptWithoutSalt(input, "MD5"));
        System.out.println("带盐密文：" + encryptWithSalt(input, salt(), "MD5"));
    }

    /**
     * 不加盐加密
     * @param inputStr	输入明文
     * @param algorithm	算法类型【MD5，SHA】
     * @return
     */
    public static String encryptWithoutSalt(String inputStr, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //哈希计算,转换输出
            return byte2HexStr(md.digest(inputStr.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    /**
     * 加盐加密
     * @param inputStr	输入明文
     * @param salt			盐
     * @param algorithm	算法类型【MD5，SHA】
     * @return
     */
    public static String encryptWithSalt(String inputStr, String salt, String algorithm) {
        try {
            //申明使用算法
            MessageDigest md = MessageDigest.getInstance(algorithm);

            //加盐，输入加盐
            String inputWithSalt = inputStr + salt;
            //哈希计算,转换输出
            String hashResult = byte2HexStr(md.digest(inputWithSalt.getBytes()));

            /*将salt存储到hash值中，用于登陆验证密码时使用相同的盐*/
            char[] cs = new char[48];
            for (int i = 0; i < 48; i += 3) {
                cs[i] = hashResult.charAt(i / 3 * 2);
                //输出带盐，存储盐到hash值中;每两个hash字符中间插入一个盐字符
                cs[i + 1] = salt.charAt(i / 3);
                cs[i + 2] = hashResult.charAt(i / 3 * 2 + 1);
            }
            hashResult = new String(cs);
            return hashResult;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    /**
     * 自定义简单生成盐，是一个随机生成的长度为16的字符串，每一个字符是随机的十六进制字符
     * @return
     */
    public static String salt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < sb.capacity(); i++) {
            sb.append(hex[random.nextInt(16)]);
        }
        return sb.toString();
    }

    /**
     * 将字节数组转换成十六进制字符串
     * @param bytes
     * @return
     */
    private static String byte2HexStr(byte[] bytes) {
        int len = bytes.length;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < len; i++) {
            byte byte0 = bytes[i];
            result.append(hex[byte0 >>> 4 & 0xf]);
            result.append(hex[byte0 & 0xf]);
        }
        return result.toString();
    }
}
