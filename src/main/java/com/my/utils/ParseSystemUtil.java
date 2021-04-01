package com.my.utils;

import java.util.Calendar;

/**
 * 进制转换工具类
 *
 */
public class ParseSystemUtil {

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    //获取高四位
    public static int getHeight4(char data){
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    //获取低四位
    public static int getLow4(char data){
        int low;
        low = (data & 0x0f);
        return low;
    }

    //获取高6位
    public static int getHeight6(char data){
        int height;
        height = (data & 0xc0);
        return height;
    }

    //获取低2位
    public static int getLow2(char data){
        int low;
        low = (data & 0x3f);
        return low;
    }

    //获取高四位
    public static int getHeight4(byte data){
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    //获取低四位
    public static int getLow4(byte data){
        int low;
        low = (data & 0x0f);
        return low;
    }

    //获取高2位
    public static int getHeight6(byte data){
        int height;
        height = (data & 0xc0);
        return height;
    }

    //获取低6位
    public static int getLow2(byte data){
        int low;
        low = (data & 0x3f);
        return low;
    }

    public static void main(String[] args){

        Calendar c = Calendar.getInstance();
        int year = Integer.parseInt((c.get(Calendar.YEAR)+"").substring(3));
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        int sum = year + month + day + hour + minute + second;

        byte[] bytes = new byte[10];
        bytes[0] = (byte)90;
        bytes[1] = (byte)84;
        bytes[2] = (byte)61;
        bytes[3] = (byte)year;
        bytes[4] = (byte)month;
        bytes[5] = (byte)day;
        bytes[6] = (byte)hour;
        bytes[7] = (byte)minute;
        bytes[8] = (byte)second;
        bytes[9] = (byte)sum;

        System.out.println(parseByte2HexStr(bytes));
    }
}