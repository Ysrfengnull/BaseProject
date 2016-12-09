package com.classic.core.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: 日期操作工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings("unused") public final class DateUtil {

    private DateUtil() { }

    public static final String FORMAT_DATE      = "yyyy-MM-dd";
    public static final String FORMAT_TIME      = "HH:mm:ss";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static int getYear() {
        return getTimeByType(Calendar.YEAR);
    }

    public static int getMonth() {
        return getTimeByType(Calendar.MONTH);
    }

    public static int getDay() {
        return getTimeByType(Calendar.DAY_OF_MONTH);
    }

    public static int getHours() {
        return getTimeByType(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes() {
        return getTimeByType(Calendar.MINUTE);
    }

    public static int getSeconds() {
        return getTimeByType(Calendar.SECOND);
    }

    private static int getTimeByType(int type) {
        return Calendar.getInstance().get(type);
    }

    /**
     * 时间格式化
     */
    public static String formatDate(String format, Long time) {
        return formatDate(new SimpleDateFormat(format, Locale.CHINA), time);
    }

    /**
     * 时间格式化
     */
    public static String formatDate(SimpleDateFormat format, Long time) {
        if (null == time || time <= 0) { return ""; }
        return format.format(new Date(String.valueOf(time).length() == 13 ? time : time * 1000));
    }

    /**
     * 根据月份获得最大天数
     *
     * @param year  年
     * @param month 月
     * @return 最大天数
     */
    public static int getMaxDayByMonth(int year, int month) {
        Calendar time = Calendar.getInstance(); //使用默认时区和语言环境获得一个日历
        //注意：在使用set方法之前，必须先调用clear（），否则很多信息会继承自系统当前的时间
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month); //注意Calendar对象默认一月是为零的
        return time.getActualMaximum(Calendar.DAY_OF_MONTH); //获得本月份的天数
    }

    /**
     * 获取N天前、N天后的 日期
     *
     * @param nowDate   当前日期;
     * @param dayAddNum 正数：N天前，负数：N天后;
     */
    public static Date getAddDay(Date nowDate, int dayAddNum) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, dayAddNum);
        return calendar.getTime();
    }

    /**
     * 获取N天前、N天后的 日期
     *
     * @param nowDate   当前时间戳;
     * @param dayAddNum 正数：N天前，负数：N天后;
     */
    public static Date getAddDay(long nowDate, int dayAddNum) {
        return getAddDay(new Date(nowDate), dayAddNum);
    }

    /**
     * 根据日期获得星期
     */
    public static String getWeek(Date d) {
        final String[] dayNames = { "星期日", "星期一", "星期二", "星期三",
                                    "星期四", "星期五", "星期六" };
        Calendar     calendar   = Calendar.getInstance();
        calendar.setTime(d);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) { dayOfWeek = 0; }
        return (dayNames[dayOfWeek]);
    }

    /**
     * 获取某一周第一天
     */
    public static Date getFirstDayByWeek(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int min     = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //获取周开始基准
        int current = calendar.get(Calendar.DAY_OF_WEEK); //获取当天周内天数
        calendar.add(Calendar.DAY_OF_WEEK, min - current); //当天-基准，获取周开始日期
        return calendar.getTime();
    }

    /**
     * 获取某一周最后一天
     */
    public static Date getLastDayByWeek(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int min     = calendar.getActualMinimum(Calendar.DAY_OF_WEEK);
        int current = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_WEEK, min - current + 6);
        return calendar.getTime();
    }

    /**
     * 返回某月份的第一天
     */
    public static Date getFirstDayByMonth(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 返回某月份的最后一天
     */
    public static Date getLastDayByMonth(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 将秒转换为（ 分：秒 格式）
     *
     * @param time 单位：秒
     */
    public static String getTimeFromInt(int time) {
        if (time <= 0) { return "00:00"; }
        int    seconds = time / 60;
        int    million = time % 60;
        String f       = seconds >= 10 ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
        String m       = million >= 10 ? String.valueOf(million) : "0" + String.valueOf(million);
        return f + ":" + m;
    }

    /**
     * 时间格式化（刚刚、几分钟前、几小时前、昨天、前天、年-月-日）
     */
    public static String getShortTime(long time) {
        String shortString = "";
        if (time == 0) { return shortString; }

        long now      = Calendar.getInstance().getTimeInMillis();
        long datetime = (now - time) / 1000;
        if (datetime > 365 * 24 * 60 * 60) {
            shortString = formatDate(FORMAT_DATE, time);
            //shortString = FORMAT_DATE.format(new Date(time));
        } else if (datetime > 24 * 60 * 60 && ((int) (datetime / (24 * 60 * 60))) == 2) {
            shortString = "前天";
        } else if (datetime > 24 * 60 * 60 && ((int) (datetime / (24 * 60 * 60))) == 1) {
            shortString = "昨天";
        } else if (datetime > 60 * 60) {
            shortString = (int) (datetime / (60 * 60)) + "小时前";
        } else if (datetime > 60) {
            shortString = (int) (datetime / (60)) + "分钟前";
        } else {
            shortString = "刚刚";
        }
        return shortString;
    }

    /**
     * 时间格式化(秒，分，小时，天)
     */
    public static String getShortTime2(long time) {
        String shortString = "";
        if (time == 0) { return shortString; }

        long now      = Calendar.getInstance().getTimeInMillis();
        long datetime = (now - time) / 1000; // 秒
        if (datetime > 24 * 60 * 60) {
            shortString = (int) (datetime / (24 * 60 * 60)) + "天";
        } else if (datetime > 60 * 60) {
            shortString = (int) (datetime / (60 * 60)) + "小时";
        } else if (datetime > 60) {
            shortString = (int) (datetime / (60)) + "分钟";
        } else {
            shortString = (int) datetime + "秒";
        }
        return shortString;
    }
}
