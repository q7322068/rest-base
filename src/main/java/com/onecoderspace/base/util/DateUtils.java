package com.onecoderspace.base.util;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class DateUtils {

	public final static String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
	public final static String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm";
	public final static String DATE_FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_CHINA_DEFAULT = "yyyy年MM月dd日";
	
	/**
	 * 获取指定日期前后num天的日期
	 * @author yangwk
	 * @time 2017年9月14日 上午11:13:18
	 * @param date
	 * @param num 正数 多少天之后的日期  负数 多少天之后的日期
	 * @return
	 */
	public static String getDay(String date,int num){
		return getDay(date, num,DATE_FORMAT_DEFAULT);
	}
	
	/**
	 * 获取指定日期前后num天的日期
	 * @author yangwk
	 * @time 2017年9月14日 上午11:13:18
	 * @param date
	 * @param num 正数 多少天之后的日期  负数 多少天之后的日期
	 * @param format 日期格式
	 * @return
	 */
	public static String getDay(String date,int num,String format){
		long t = parseStringToLong(date);
		return getDay(t, num, DATE_FORMAT_DEFAULT);
	}
	
	/**
	 * 获取指定日期前后num天的日期
	 * @author yangwk
	 * @time 2017年9月14日 上午11:13:18
	 * @param date
	 * @param num 正数 多少天之后的日期  负数 多少天之后的日期
	 * @return
	 */
	public static String getDay(long date,int num){
		return getDay(date, num, DATE_FORMAT_DEFAULT);
	}
	
	/**
	 * 获取指定日期前后num天的日期
	 * @author yangwk
	 * @time 2017年9月14日 上午11:13:18
	 * @param date
	 * @param num 正数 多少天之后的日期  负数 多少天之后的日期
	 * @param format 日期格式
	 * @return
	 */
	public static String getDay(long date,int num,String format){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+num);
		return longToString(calendar.getTimeInMillis(),format);
	}

	/**
	 * 将毫秒时间转换为yyyy-MM-dd格式的时间
	 * @author yangwenkui
	 * @time 2017年10月6日 下午5:56:40
	 * @param time 毫秒数
	 * @return
	 */
	public static String longToString(long time) {
		return longToString(time, DATE_FORMAT_DEFAULT);
	}

	/**
	 * 将毫秒时间转换为指定格式的时间
	 * @author yangwenkui
	 * @time 2017年10月6日 下午5:56:40
	 * @param time 毫秒数
	 * @param format 日期格式
	 * @return
	 */
	public static String longToString(long time, String format) {
		if (StringUtils.isBlank(format)) {
			format = DATE_FORMAT_DEFAULT;
		}
		DateTime dTime = new DateTime(time);
		return dTime.toString(format);
	}

	/**
	 * 获取今天开始的时间
	 * @author yangwenkui
	 * @time 2017年10月6日 下午5:58:18
	 * @return
	 */
	public static Timestamp getTodayStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);
		return new Timestamp(cal.getTimeInMillis());
	}
	
	/**
	 * 获取指定日期开始的当日开始时间
	 * @author yangwenkui
	 * @time 2017年10月6日 下午5:58:33
	 * @param date
	 * @return
	 */
	public static long getDayStartTime(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(parseStringToLong(date));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获取指定日期结束时间
	 * @author yangwenkui
	 * @time 2017年10月6日 下午5:58:58
	 * @param date
	 * @return
	 */
	public static long getDayEndTime(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(parseStringToLong(date));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}

	/**
	 * 获得当前日期
	 */
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd");
	}

	/**
	 * 获得当前时间
	 * @param format 日期格式
	 * @return
	 */
	public static String getCurrentTime(String format) {
		DateTime dTime = new DateTime();
		return dTime.toString(format);
	}

	/**
	 * 将字符串类型的日期转换为毫秒数
	 * @author yangwenkui
	 * @time 2017年10月6日 下午6:00:27
	 * @param dateStr
	 * @return
	 */
	public static long parseStringToLong(String dateStr) {
		dateStr = dateStr.trim();
		if (dateStr.length() == 19 || dateStr.length() == 23) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)),
						Integer.parseInt(dateStr.substring(5, 7)) - 1,
						Integer.parseInt(dateStr.substring(8, 10)),
						Integer.parseInt(dateStr.substring(11, 13)),
						Integer.parseInt(dateStr.substring(14, 16)),
						Integer.parseInt(dateStr.substring(17, 19)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}

		} else if (dateStr.length() == 16) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)),
						Integer.parseInt(dateStr.substring(5, 7)) - 1,
						Integer.parseInt(dateStr.substring(8, 10)),
						Integer.parseInt(dateStr.substring(11, 13)),
						Integer.parseInt(dateStr.substring(14, 16)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}

		} else if (dateStr.length() == 14) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)),
						Integer.parseInt(dateStr.substring(4, 6)) - 1,
						Integer.parseInt(dateStr.substring(6, 8)),
						Integer.parseInt(dateStr.substring(8, 10)),
						Integer.parseInt(dateStr.substring(10, 12)),
						Integer.parseInt(dateStr.substring(12, 14)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else if (dateStr.length() == 10 || dateStr.length() == 11) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)),
						Integer.parseInt(dateStr.substring(5, 7)) - 1,
						Integer.parseInt(dateStr.substring(8, 10)), 0, 0, 0);
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else if (dateStr.length() == 8 ) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)),
						Integer.parseInt(dateStr.substring(4, 6)) - 1,
						Integer.parseInt(dateStr.substring(6, 8)), 0, 0, 0);
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else {
			try {
				return Long.parseLong(dateStr);
			} catch (Exception e) {
				return 0;
			}

		}
	}

	public static void main(String[] args) {
		System.out.println(getDay(System.currentTimeMillis(), -30));
	}
}