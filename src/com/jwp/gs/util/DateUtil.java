package com.jwp.gs.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * @author pangjiawei
 *
 */
public class DateUtil {
	
	/** 一天有多少秒 */
	public final static int DAY_SECONDS = 24 * 60 * 60;
	/** 一周有多少秒 */
	public final static int WEEK_SECONDS = 7 * 24 * 60 * 60;
	/** 日期格式 */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式 */
	public final static String TIME_PATTERN = "HH:mm:ss";
	/** 日期时间格式 */
	public final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** TimeStamp格式 */
	public final static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss:SSS";
	/** 系统时间偏移 */
	public static long SYSTEM_TIME_OFFSET = 0;
	
	
	/**
	 * 获取当前游戏时间-毫秒数
	 * @return
	 */
	public static long getCurrentTimeMilis(){
		return System.currentTimeMillis() + SYSTEM_TIME_OFFSET;
	}
	
	/**
	 * 获取当前游戏时间-日期
	 * @return
	 */
	public static Date getCurrentTimeDate(){
		return getCurrentTimeCalendar().getTime();
	}
	
	/**
	 * 获取当前游戏时间-日历
	 * @return
	 */
	public static Calendar getCurrentTimeCalendar(){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getCurrentTimeMilis());
		return cal;
	}
	
	/**
	 * 判断两个毫秒数是否在同一天
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean isSameDay(long t1,long t2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(t1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(t2);
		
		return isSameDay(cal1, cal2);
	}
	
	/**
	 * 判断两个Data对象是否在同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false ;
		}
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
	}

	/**
	 * 判断两个Calendar对象是否在同一天
	 * @param cal1
	 * @param cal2
	 * @return
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
				cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}
	
	/**
	 * 格式化日期，保留日期部分yyyy-MM-dd
	 * @see #getDateTimeStr(Date, String)
	 */
	public static String getDateStr(Date date) {
		return getDateTimeStr(date, DATE_PATTERN);
	}
	
	/**
	 * 格式化日期, 保留时间部分HH:mm:ss
	 * @see #getDateTimeStr(Date, String)
	 */
	public static String getTimeStr(Date date) {
		return getDateTimeStr(date, TIME_PATTERN);
	}
	
	/**
	 * 格式化日期，格式yyyy-MM-dd HH:mm:ss
	 * @see #getDateTimeStr(Date, String)
	 * @param date
	 * @return
	 */
	public static String getDateTimeStr(Date date) {
		return getDateTimeStr(date, DATETIME_PATTERN);
	}
	
	/**
	 * 格式化日期
	 * <p>如果格式为null或空字符串，则使用默认格式yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @param pattern
	 * @return 如果Date对象不存在则返回null，如果格式错误返回null并抛出异常，否则返回格式化后的时间信息字符串
	 */
	public static String getDateTimeStr(Date date, String pattern) {
		if(date == null) {
			return null;
		}
		if(StringUtil.isNullOrNone(pattern)) {
			pattern = DATETIME_PATTERN;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将给定的时间字符串转换成今天对应时间的Date对象，格式为HH:mm:ss
	 * @see #str2Date(String, String)
	 * @param str
	 * @return
	 */
	public static Date strTime2NowDateTime(String str) {
		String time = getDateStr(new Date()) + " " + str;
		return str2Date(time, DATETIME_PATTERN);
	}
	
	/**
	 * 将给定的日期时间字符串转换成Date对象，格式为yyyy-MM-dd HH:mm:ss
	 * @see #str2Date(String, String)
	 * @param str
	 * @return
	 */
	public static Date str2DateTime(String str) {
		return str2Date(str, DATETIME_PATTERN);
	}
	
	/**
	 * 将字符串转成日期格式
	 * <p>如果格式为null或空字符串，则使用默认格式yyyy-MM-dd HH:mm:ss
	 * @param str
	 * @param pattern
	 * @return 如果str为空则返回null，如果格式错误返回null并抛出异常，否则返回转换后的Date对象
	 */
	public static Date str2Date(String str, String pattern) {
		Date date = null;
		if (StringUtil.isNullOrNone(str))
			return null;

		if (StringUtil.isNullOrNone(pattern)) {
			pattern = DATETIME_PATTERN;
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 判断给定的第一个时间是否在第二个时间之前
	 * @param one 第一个时间,Date对象
	 * @param two 第二个时间,Date对象
	 * @return 如果两个时间任意一个为null，则返回false，否则返回比较结果
	 */
	public static boolean beforeDateTime(Date one, Date two) {
		if(one == null || two == null) {
			return false;
		}
		return one.before(two);
	}
	
	/**
	 * 判断给定的第一个时间是否在第二个时间之前,字符串格式yyyy-MM-dd HH:mm:ss
	 * @param one 第一个时间，String对象
	 * @param two 第二个时间，String对象
	 * @see #beforeDateTime(Date, Date)
	 * @return
	 */
	public static boolean beforeDateTime(String one, String two) {
		return beforeDateTime(str2DateTime(one), str2DateTime(two));
	}
	
	/**
	 * 判断给定的第一个时间是否在第二个时间之前(不考虑日期),字符串格式HH:mm:ss
	 * @param one 第一个时间，String对象
	 * @param two 第二个时间，String对象
	 * @see #beforeDateTime(String, String)
	 * @return
	 */
	public static boolean beforeTime(String one, String two) {
		Date date = new Date();
		one = getDateStr(date) + " " + one;
		two = getDateStr(date) + " " + two;
		return beforeDateTime(one, two);
	}
	
	/**
	 * 判断给定时间是否在当前时间之前
	 * @param date 给定之间Date对象
	 * @see #beforeDateTime(Date, Date)
	 * @return
	 */
	public static boolean beforeNow(Date date) {
		return beforeDateTime(date, new Date());		
	}
	
	/**
	 * 判断给定的第一个时间是否在第二个时间之后
	 * @param one 第一个时间,Date对象
	 * @param two 第二个时间,Date对象
	 * @return 如果两个时间任意一个为null，则返回false，否则返回比较结果
	 */
	public static boolean afterDateTime(Date one, Date two) {
		if(one == null || two == null) {
			return false;
		}
		return one.after(two);
	}
	
	/**
	 * 判断给定的第一个时间是否在第二个时间之后,字符串格式yyyy-MM-dd HH:mm:ss
	 * @param one 第一个时间，String对象
	 * @param two 第二个时间，String对象
	 * @see #afterDateTime(Date, Date)
	 * @return
	 */
	public static boolean afterDateTime(String one, String two) {
		return afterDateTime(str2DateTime(one), str2DateTime(two));
	}
	
	/**
	 * 判断给定的第一个时间是否在第二个时间之后(不考虑日期),字符串格式HH:mm:ss
	 * @param one 第一个时间，String对象
	 * @param two 第二个时间，String对象
	 * @see #afterDateTime(String, String)
	 * @return
	 */
	public static boolean afterTime(String one, String two) {
		Date date = new Date();
		one = getDateStr(date) + " " + one;
		two = getDateStr(date) + " " + two;
		return afterDateTime(one, two);
	}
	
	/**
	 * 判断给定时间是否在当前时间之后
	 * @param date 给定之间Date对象
	 * @see #afterDateTime(Date, Date)
	 * @return
	 */
	public static boolean afterNow(Date date) {
		return afterDateTime(date, new Date());		
	}
	
	/**
	 * 判断当前时间是否在给定的两个时间以内
	 * @param one 第一个时间，Date对象
	 * @param two 第二个时间，Date对象
	 * @return 如果两个时间任意一个为null，则返回false，否则返回比较结果
	 */
	public static boolean betweenDateTime(Date one, Date two) {
		if(one == null || two == null) {
			return false;
		}
		Date date = new Date();
		return date.before(two) && date.after(one);
	}
	
	/**
	 * 判断当前时间是否在给定的两个时间以内,字符串格式yyyy-MM-dd HH:mm:ss
	 * @param one 第一个时间，String对象
	 * @param two 第二个时间，String对象
	 * @see #betweenDateTime(Date, Date)
	 * @return
	 */
	public static boolean betweenDateTime(String one, String two) {
		return betweenDateTime(str2DateTime(one), str2DateTime(two));
	}
	
	/**
	 * 判断当前日期是否在给定的两个日期以内，字符串格式yyyy-MM-dd
	 * @param one 第一个日期，String对象
	 * @param two 第二个日期，String对象
	 * @return
	 */
	public static boolean betweenDate(String one, String two) {
		one = getDateStr(str2DateTime(one + " 00:00:00"));//这样处理是因为后面要比较字符串，必须保证格式完全一致
		two = getDateStr(str2DateTime(two + " 00:00:00"));
		if(one.equals(two) && one.equals(getDateStr(new Date()))) {
			return true;
		}
		return betweenDateTime(one + " 00:00:00", two + " 23:59:59");
	}
	
	/**
	 * 判断当前时间是否在给定的两个时间以内(不考虑日期)，字符串格式HH:mm:ss
	 * @param one 第一个时间，String对象
	 * @param two 第二个时间，String对象
	 * @return
	 */
	public static boolean betweenTime(String one, String two) {
		return betweenDateTime(strTime2NowDateTime(one), strTime2NowDateTime(two));
	}
	
	/**
	 * 判断指定时间是否是当月的最后一天
	 * @param time
	 * @return
	 */
	public static boolean isLastDayOfMonth(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int oldMonth = cal.get(Calendar.MONTH);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		int newMonth = cal.get(Calendar.MONTH);
		return !(oldMonth == newMonth);
	}
	
	
	
	public static void main(String[] args) {
		try {
//			System.out.println(new Date());
//			System.out.println(new Timestamp(new Date().getTime()));
//			System.out.println(new SimpleDateFormat("eeeeee").format(new Date()));
//			System.out.println(getDateTime(new Date(), "eee"));
//			String tm = "11:22:33";
//			System.out.println(strTime2NowDateTime(tm));
//			String a = "2014-2-28 14:9:13";
//			System.out.println(str2DateTime(a));
//			String b = "2014-2-28 15:36:12";
			String a = "14:49:12";
			String b = "14:50:29";
//			System.out.println(afterNow(str2DateTime(a)));
//			System.out.println(beforeNow(str2DateTime(a)));
//			String a = "2014-2-29";
//			String b = "2014-2-29";
			System.out.println(betweenTime(a, b));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
