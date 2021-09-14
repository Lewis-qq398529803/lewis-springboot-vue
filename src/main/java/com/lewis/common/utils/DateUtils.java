package com.lewis.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author taozi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static String YYYY = "yyyy";

	public static String YYYY_MM = "yyyy-MM";

	public static String YYYY_MM_DD = "yyyy-MM-dd";

	public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	private static String[] parsePatterns = {
			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
			"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 获取当前时间戳
	 *
	 * @return 时间戳
	 */
	public static long current() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前Date型日期
	 *
	 * @return Date() 当前日期
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取 yyyy-MM-dd
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getDate() {
		return dateTimeNow(YYYY_MM_DD);
	}

	/**
	 * 获取 yyyy-MM-dd HH:mm:ss
	 *
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getTime() {
		return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取 yyyyMMddHHmmss
	 *
	 * @return yyyyMMddHHmmss
	 */
	public static String dateTimeNow() {
		return dateTimeNow(YYYYMMDDHHMMSS);
	}

	public static String dateTimeNow(final String format) {
		return parseDateToStr(format, new Date());
	}

	public static String dateTime(final Date date) {
		return parseDateToStr(YYYY_MM_DD, date);
	}

	public static String parseDateToStr(final String format, final Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	public static Date dateTime(final String format, final String ts) {
		try {
			return new SimpleDateFormat(format).parse(ts);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 日期路径 即年/月/日 如2018/08/08
	 *
	 * @return yyyy/MM/dd
	 */
	public static String datePath() {
		Date now = new Date();
		return DateFormatUtils.format(now, "yyyy/MM/dd");
	}

	/**
	 * 日期路径 即年月日 如20180808
	 *
	 * @return yyyyMMdd
	 */
	public static String dateTime() {
		Date now = new Date();
		return DateFormatUtils.format(now, "yyyyMMdd");
	}

	/**
	 * 日期型字符串转化为日期格式
	 *
	 * @param str
	 * @return Date
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime   当前时间
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return boolean
	 */
	public static boolean isIn(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		return date.after(begin) && date.before(end);
	}

	/**
	 * 获取服务器启动时间
	 *
	 * @return Date
	 */
	public static Date getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}

	/**
	 * 计算两个时间差
	 *
	 * @param endDate
	 * @param nowDate
	 * @return day + "天" + hour + "小时" + min + "分钟"
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}
}
