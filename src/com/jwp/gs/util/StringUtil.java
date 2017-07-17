package com.jwp.gs.util;

/**
 * 字符串工具类
 * @author pangjiawei
 *
 */
public class StringUtil {

	/**
	 * 判断给定字符串是否为空
	 * <p>判断标准：
	 * <p>1.如果字符串为null返回true
	 * <p>2.如果字符串值为null返回true
	 * <p>3.如果字符串由空格组成返回true
	 * <p>4.如果字符串为"null"返回true
	 * @param str 给定字符串
	 * @return
	 */
	public static boolean isNullOrNone(String str) {
		return str == null || str.isEmpty() || str.trim().isEmpty() || str.equals("null");
	}
	
	
	/**
	 * 分割字符串返回int数组
	 * @param src
	 * @param separator
	 * @return
	 */
	public static int[]	split(String src, String separator) {
		int[] array = null;
		String[] strs = src.split(separator);
		array = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			array[i] = Integer.valueOf(strs[i]);
		}
		return array;
	}
	
	/**
	 * 判断给定字符串是否是仅由数字组成
	 * @param value
	 * @return
	 */
	public static boolean isNumberStr(String value){
		if (value == null) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c < '0' || c > '9'){
				return false;
			}
		}
		return true;
	}
}
