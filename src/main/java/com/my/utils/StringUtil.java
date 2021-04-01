package com.my.utils;

import com.google.common.collect.Maps;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static final char QUOTE_ENCODE[] = "&quot;".toCharArray();

	private static final char AMP_ENCODE[] = "&amp;".toCharArray();

	private static final char LT_ENCODE[] = "&lt;".toCharArray();

	private static final char GT_ENCODE[] = "&gt;".toCharArray();

	private static MessageDigest digest = null;

	private static Random randGen = new Random();

	private static char numbersAndLetters[] = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	/**
	 * 给生成唯一字符串使用
	 */
	@SuppressWarnings("unused")
	private static String[] strArray = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "!", "@", "#", "$", "%", "^", "&", "(", ")" };

	public StringUtil() {
	}

	/**
	 * 搜索text中的repl, 从第m位开始，返回repl确切位置
	 *
	 * @param text
	 * @param repl
	 * @return -1不包含 其他包含
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */

	public static int ishave(String text, String repl, int m) {
		if (text == null || repl == null || "".equals(repl)) {
			return -1;
		}
		return text.indexOf(repl, m);
	}

	/**
	 * 搜索text中的repl,返回其第一次索引
	 *
	 * @param text
	 * @param repl
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static int ishavefirst(String text, String repl) {
		if (text == null || repl == null || "".equals(repl)) {
			return -1;
		}
		return text.indexOf(repl);
	}

	public static final String replaceIgnoreCase(String line, String oldString, String newString) {
		if (line == null){
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String replaceIgnoreCase(String line, String oldString, String newString, int count[]) {
		if (line == null){
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 0;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 将特殊字符 转义成html格式
	 *
	 * @param in
	 * @return String
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final String escapeHTMLTags(String in) {
		if (in == null){
			return null;
		}
		int i = 0;
		int last = 0;
		char input[] = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>'){
				if (ch == '<') {
					if (i > last){
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '>') {
					if (i > last){
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(GT_ENCODE);
				}
			}
		}

		if (last == 0){
			return in;
		}
		if (i > last){
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	/**
	 * 过滤html 标签
	 *
	 * @param s
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String clearHTMLTags(String s) {
		boolean flag = true;
		StringBuffer sb = new StringBuffer();
		if (s == null || s.length() == 0){
			return "";
		}
		String srcStr = replaceIgnoreCase(s, "&nbsp;", "");
		srcStr = replaceIgnoreCase(srcStr, "\n", "");
		srcStr = replaceIgnoreCase(srcStr, "\t", "");
		for (int i = 0; i < srcStr.length(); i++) {
			char c = srcStr.charAt(i);
			if (c == '>') {
				flag = true;
			} else {
				if (c == '<'){
					flag = false;
				}
				if (c != ' ' && c != '\u3000' && i < srcStr.length() && flag){
					sb.append(c);
				}
			}
		}

		String desStr = sb.toString();
		return desStr;
	}

	/**
	 * MD5 加密
	 *
	 * @param data
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final synchronized String hash(String data) {
		if (digest == null){
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");
				nsae.printStackTrace();
			}
		}
		digest.update(data.getBytes());
		return encodeHex(digest.digest());
	}

	/**
	 * byte数字转换16进制字符串
	 *
	 * @param bytes
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final String encodeHex(byte bytes[]) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16){
				buf.append("0");
			}
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

	/**
	 * 16进制字符串转换byte数组
	 * @param hex
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final byte[] decodeHex(String hex) {
		char chars[] = hex.toCharArray();
		byte bytes[] = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			byte newByte = 0;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = newByte;
			byteCount++;
		}

		return bytes;
	}

	/**
	 * char类型转换byte字节
	 *
	 * @param ch
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case 48: // '0'
			return 0;

		case 49: // '1'
			return 1;

		case 50: // '2'
			return 2;

		case 51: // '3'
			return 3;

		case 52: // '4'
			return 4;

		case 53: // '5'
			return 5;

		case 54: // '6'
			return 6;

		case 55: // '7'
			return 7;

		case 56: // '8'
			return 8;

		case 57: // '9'
			return 9;

		case 97: // 'a'
			return 10;

		case 98: // 'b'
			return 11;

		case 99: // 'c'
			return 12;

		case 100: // 'd'
			return 13;

		case 101: // 'e'
			return 14;

		case 102: // 'f'
			return 15;
		}
		return 0;
	}

	/**
	 * 字符串Base64加密
	 *
	 * @param data
	 * @return String
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String encodeBase64(String data) {
		return encodeBase64(data.getBytes());
	}

	/**
	 * 字节数组Base64加密
	 *
	 * @param data
	 * @return String
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String encodeBase64(byte data[]) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
		for (int i = 0; i < len; i++) {
			int c = data[i] >> 2 & 0x3f;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
			c = data[i] << 4 & 0x3f;
			if (++i < len)
				c |= data[i] >> 4 & 0xf;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
			if (i < len) {
				c = data[i] << 2 & 0x3f;
				if (++i < len)
					c |= data[i] >> 6 & 0x3;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
			} else {
				i++;
				ret.append('=');
			}
			if (i < len) {
				c = data[i] & 0x3f;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
			} else {
				ret.append('=');
			}
		}

		return ret.toString();
	}

	/**
	 * 字符串Base64解密
	 *
	 * @param data
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String decodeBase64(String data) {
		return decodeBase64(data.getBytes());
	}

	/**
	 * 字节数组Base64解密
	 *
	 * @param data
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String decodeBase64(byte data[]) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; i++) {
			int c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(data[i]);
			i++;
			int c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(data[i]);
			c = c << 2 | c1 >> 4 & 0x3;
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (61 == c)
					break;
				c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf((char) c);
				c1 = c1 << 4 & 0xf0 | c >> 2 & 0xf;
				ret.append((char) c1);
			}
			if (++i >= len)
				continue;
			c1 = data[i];
			if (61 == c1)
				break;
			c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf((char) c1);
			c = c << 6 & 0xc0 | c1;
			ret.append((char) c);
		}

		return ret.toString();
	}

	private static final String replace(String line, String oldString, String newString) {
		if (line == null)
			return null;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 字符串中 特殊符号 替换为空
	 *
	 * @param text
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final String[] toLowerCaseWordArray(String text) {
		if (text == null || text.length() == 0)
			return new String[0];
		ArrayList wordList = new ArrayList();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = 0;
		for (int end = boundary.next(); end != -1; end = boundary.next()) {
			String tmp = text.substring(start, end).trim();
			tmp = replace(tmp, "+", "");
			tmp = replace(tmp, "/", "");
			tmp = replace(tmp, "\\", "");
			tmp = replace(tmp, "#", "");
			tmp = replace(tmp, "*", "");
			tmp = replace(tmp, ")", "");
			tmp = replace(tmp, "(", "");
			tmp = replace(tmp, "&", "");
			if (tmp.length() > 0)
				wordList.add(tmp);
			start = end;
		}

		return (String[]) wordList.toArray(new String[wordList.size()]);
	}

	/**
	 * 生成固定长度 数字字母 的随机数
	 *
	 * @param length
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final String randomString(int length) {
		if (length < 1)
			return null;
		char randBuffer[] = new char[length];
		for (int i = 0; i < randBuffer.length; i++)
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];

		return new String(randBuffer);
	}

	/**
	 *
	 * @Title: getRandomPwd @Description:
	 * String 返回类型 @throws
	 */
	public static String getRandom6Pwd() {
		return String.valueOf((1 + Math.random()) * 1000000).substring(1, 7);
	}

	/**
	 * 将字符串中特殊字符转义成xml格式
	 *
	 * @param string
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final String escapeForXML(String string) {
		if (string == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>')
				if (ch == '<') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '&') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(AMP_ENCODE);
				} else if (ch == '"') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(QUOTE_ENCODE);
				}
		}

		if (last == 0)
			return string;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	/**
	 * 将xml转义字符 转换为正常字符串
	 *
	 * @param string
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static final String unescapeFromXML(String string) {
		string = replace(string, "&lt;", "<");
		string = replace(string, "&gt;", ">");
		string = replace(string, "&quot;", "\"");
		return replace(string, "&amp;", "&");
	}

	/**
	 * 字符串格式转换 ISO8859_1 转为 GBK
	 *
	 * @param strvalue
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String toChinese(String strvalue) {
		try {
			if (strvalue == null) {
				return null;
			} else {
				strvalue = new String(strvalue.getBytes("GBK"), "ISO8859_1");
				return strvalue;
			}
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 过滤字符串中包含SQL注入的关键字
	 *
	 * @param content
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static String inspect(String content) {
		// String content=str;
		if (content == null || content.trim().equals("")) {
			return "";
		}
		String filter[] = new String[] { "'", " and", "and ", " exec", "exec ", " insert", "insert ", " select", "select ", " delete", "delete ", " update", "update ", " count", "count ", "*", "%", " chr", "chr ", " mid", "mid ", " master", "master ", " truncate", "truncate ", " char", "char ",
				" declare", "declare ", ";", " or", "or ", "-", "+", "," };
		for (int i = 0; i < filter.length; i++) {
			if (filter[i].trim().equals("")) {
				continue;
			}
			content = content.replace(filter[i], "");
		}
		return content;
	}

	/**
	 * 验证手机号码
	 *
	 * @param mobiles
	 *            手机号码(单个)
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 根据url返回字符串
	 *
	 * @param url
	 *            网络url
	 * @return
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	@SuppressWarnings("static-access")
	public String getStu(String url) {
		URL urlmy = null;
		try {
			urlmy = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) urlmy.openConnection();
			con.setFollowRedirects(true);
			con.setInstanceFollowRedirects(false);
			con.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader br;
		StringBuffer sb = new StringBuffer("");
		try {
			br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s.toString() + "\r\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 图片压缩
	 *
	 * @param inpath
	 *            待压缩图片路径
	 * @param outpath
	 *            压缩后图片路径
	 * @param height
	 *            压缩高度
	 * @param width
	 *            压缩宽度
	 * @return 压缩后图片路径
	 * @throws Exception
	 * @author: LJ
	 * @Createtime: Sep 24, 2012
	 */
	@SuppressWarnings("static-access")
	public static Icon getFixedBoundIcon(String inpath, String outpath, int height, int width) throws Exception {
		double Ratio = 0.0;
		// 缩放比例
		File F = new File(inpath);
		if (!F.isFile())
			throw new Exception(F + " is not image file error in getFixedBoundIcon!");
		Icon ret = new ImageIcon(inpath);
		BufferedImage Bi = ImageIO.read(F);
		if ((Bi.getHeight() > height) || (Bi.getWidth() > width)) {
			if (Bi.getHeight() > Bi.getWidth()) {
				Ratio = (new Integer(height)).doubleValue() / Bi.getHeight();
			} else {
				Ratio = (new Integer(width)).doubleValue() / Bi.getWidth();
			}
			int lastLength = inpath.lastIndexOf(".");
			String subFilePath = outpath.substring(0, lastLength);
			String fileType = inpath.substring(lastLength);
			File zoomFile = new File(subFilePath + fileType);
			Image Itemp = Bi.getScaledInstance(width, height, Bi.SCALE_SMOOTH);
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), null);
			Itemp = op.filter(Bi, null);
			try {
				ImageIO.write((BufferedImage) Itemp, "jpg", zoomFile);
				ret = new ImageIcon(zoomFile.getPath());
				System.err.println("图片压缩后路径为 " + outpath);
			} catch (Exception ex) {
				System.out.println("######## here error : " + ex);
			}
		}
		return ret;
	}

	@SuppressWarnings("unused")
	public static void main(String args[]) throws UnsupportedEncodingException {

		String sql = "#dev,111111111111,111,111,1!";
		byte[] bs = sql.getBytes();
		// 用新的字符编码生成字符串
		// new String(bs, newCharset);
		try {
			System.out.println(getFixedBoundIcon("e:/3.jpg", "e:/1.jpg", 100, 500));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String toSqlParam(String value) {
		String rv = "";
		if (value != null) {
			String[] vs = value.split(",");
			if (vs.length > 0) {
				for (String v : vs) {
					rv += "'" + v + "',";
				}
				if (rv.length() > 0) {
					rv = rv.substring(0, rv.length() - 1);
				}
			}
		}
		return rv;

	}

	/**
	 * @MethodName: isEmpty
	 * @Description: 判断一个值是否为无效值 null及字符串""会被对待为无效值
	 * @author: tengfeiyu
	 * @date 2014-12-10 上午09:22:55
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value) {
		if ("".equals(checkObjForStr(value))) {
			return true;
		}
		return false;
	}

	/**
	 * 检查对象转换成数字
	 *
	 * @param value
	 * @return
	 */
	public static String checkObjForNum(Object value) {
		if (value == null || value.toString().trim().equals("")) {
			value = "0";
		}
		return value.toString().trim();
	}

	/**
	 * 检查对象转换成字符
	 *
	 * @param value
	 * @return
	 */
	public static String checkObjForStr(Object value) {
		if (value == null || value.toString().trim().equals("")) {
			value = "";
		}
		return value.toString().replaceAll("\\n", "").replaceAll("\\r", "").trim().replaceAll(" ", "").replaceAll("  ", "");
	}

	/**
	 * 去除字符串中的横线
	 *
	 * @param str
	 * @return
	 */
	public static String deleteHorizontal(String str) {
		String s = str.replaceAll("-", "");
		return s;
	}

	/**
	 * 给字符串左边补0
	 *
	 * @param str
	 *            要进行补0操作的字符串
	 * @param length
	 *            要补到的位数
	 * @return 补充完的字符串 如字符串st="789",length=10,则方法返回为"0000000789"
	 */
	public static String supplementZero(String str, int length) {
		int len = length - str.length();
		StringBuffer zero = new StringBuffer("");
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				zero.append("0");
			}
		}
		return zero.append(str).toString();
	}

	/**
	 * 给字符串左边补0
	 *
	 * @param obj
	 *            要进行补0操作的对象
	 * @param length
	 *            要补到的位数
	 * @return 补充完的字符串 如字符串st="789",length=10,则方法返回为"0000000789"
	 */
	public static String supplementZero(Object obj, int length) {
		String str = checkObjForStr(obj);
		int len = length - str.length();
		StringBuffer zero = new StringBuffer("");
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				zero.append("0");
			}
		}
		return zero.append(str).toString();
	}

	/**
	 * @MethodName: replaceDateStr
	 * @Description: 快速替换日期字符串中的"-"和"："
	 * @author: tengfeiyu
	 * @date 2015-1-14 下午04:16:15
	 * @param dateStr
	 * @return
	 */
	public static String replaceDateStr(String dateStr) {
		if (dateStr != null) {
			return dateStr.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
		}
		return null;
	}

	/**
	 *
	 * @Title: separatorChar @Description:
	 * resource @return 设定文件 @return String 返回类型 @throws
	 */
	public static String separatorChar(String resource) {// 对\\ 和 / 处理 window
		// 和liunx 不同
		if (File.separatorChar == '/') {
			resource = StringUtil.replace(resource, "\\", "/");
		} else {
			resource = StringUtil.replace(resource, "/", "\\");
		}
		return resource;
	}

	/**
	 * 对字符串处理:将指定位置到指定位置的字符以星号代替
	 *
	 * @param content
	 *            传入的字符串
	 * @param begin
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return
	 */
	public static String getStarString(String content, int begin, int end) {

		if (begin >= content.length() || begin < 0) {
			return content;
		}
		if (end >= content.length() || end < 0) {
			return content;
		}
		if (begin >= end) {
			return content;
		}
		String starStr = "";
		for (int i = begin; i < end; i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, begin) + starStr + content.substring(end, content.length());

	}

	/**
	 * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
	 *
	 * @param content
	 *            传入的字符串
	 * @param frontNum
	 *            保留前面字符的位数
	 * @param endNum
	 *            保留后面字符的位数
	 * @return 带星号的字符串
	 */

	public static String getStarString2(String content, int frontNum, int endNum) {

		if (frontNum >= content.length() || frontNum < 0) {
			return content;
		}
		if (endNum >= content.length() || endNum < 0) {
			return content;
		}
		if (frontNum + endNum >= content.length()) {
			return content;
		}
		String starStr = "";
		for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, frontNum) + starStr + content.substring(content.length() - endNum, content.length());

	}
	/**
	 * 文字转码
	 * @param str
	 * @return
	 */
	public static String encodeStr(String str) {
		if(str==null){
			return null;
		}else{
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
	}

	public static String notNull(Object object){
		try{
			String resultStr = (String) object;
			if(null == resultStr){
				resultStr = "";
			}
			return resultStr;
		}catch (Exception e){
			return "";
		}
	}

	//获取request的请求参数和值并拼接
	public static Map<String,String> getRequestParamsVal(HttpServletRequest request) {
		Map<String,String> map = Maps.newTreeMap();
		Enumeration parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = (String) parameterNames.nextElement();
			String parameterValue = request.getParameter(paramName);
            map.put(paramName,parameterValue);
		}
		return map;
	}


	//获取request的请求IP
	public static String getRequestIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}


}
