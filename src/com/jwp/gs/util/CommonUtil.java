package com.jwp.gs.util;

import java.util.List;

/**
 * 常用工具类
 * @author pangjiawei
 * @date 2017年7月13日 下午5:52:16
 */
public class CommonUtil {

	public static <T> int indexOfListByClass(List<T> list, Class<? extends T> clazz) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getClass().equals(clazz)) {
				return i;
			}
		}
		return -1;
	}
}
