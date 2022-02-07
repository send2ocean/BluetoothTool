package com.fyts.bluetoothtool.other;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Locale.CHINA;

/**
 * Created by Xiaoming on 2016/4/6 11:42.
 * Email:xiaoming_huo@163.com
 * <p>
 * 与时间处理相关的常用功能
 */
public class TimeUtil {

    public static final String NORMAL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String NORMAL_PATTERN2 = "yyyy-MM-dd HH:mm";
    public static final String NORMAL_PATTERN3 = "yyyy-MM-dd";
    public static final String NORMAL_PATTERN4 = "MM月dd日";
    public static final String NORMAL_PATTERN6 = "yyyy年-MM月-dd日";
    public static final String NORMAL_PATTERNS = "yyyy年MM月dd日 HH:mm";
    public static final String NORMAL_PAT = "MM-dd HH:mm";
    public static final String NORMAL_PATTE = "yyyy-MM-dd HH:mm";
    public static final String NORMAL_PATTES = "yyyy年MM月dd日 HH:mm";
    public static final String NORMAL_YEAR = "yyyy年";
    public static final String NORMIAN_DATE = "yyyy/MM/dd HH:mm";
    public static final String NORMIAN_DATES = "yyyy.MM.dd HH:mm";
    public static final String NORMAL_DATE = "yyyy-MM-dd";
    public static final String NORMALDIAN_DATE = "yyyy.MM.dd";
    public static final String PLAINT_DATE = "yyyyMMdd";
    public static final String PLAIN_PATTERN = "yyyyMMddHHmmss";
    public static final String END_TIME = "2099-12-31 00:00";
    public static final String MONTH_DATE = "MM-dd HH:mm";
    public static final String MONTH_DATE_STyLE1 = "MM/dd";
    public static final String MONTH_DATES = "MM-dd";
    public static final String MONTH_TIME = "HH:mm";
    public static final String MONTH_TIMES = "HHH:mmMin";
    public static final long day = 1000 * 60 * 60 * 24;

    private TimeUtil() {
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurrentTime(String pattern) {
        return getTime(NORMAL_PATTERN);
    }

    /**
     * 根据格式提取当前时间字符串
     *
     * @param pattern 时间格式
     * @return
     */
    public static String getTime(String pattern) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(date);
    }

    /**
     * 默认时间
     *
     * @param s 传入的时间
     * @return
     */
    public static String getDefTime(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s;
        }
        return getTime(NORMAL_PATTERN);
    }

    /**
     * 转换自己想要的日期格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String getTime(String time, String pattern) {
        Date date = null;
        if (!TextUtils.isEmpty(time)) {
            date = parse(time, NORMAL_PATTERN);
        }
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(date);
    }

    //转换时间
    public static String getDateToString(long milSecond, String pattern) {
        String normalTime;
        //格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(milSecond);
        normalTime = simpleDateFormat.format(date);
        return normalTime;
    }

    /**
     * 转换自己想要的日期格式
     */
    public static String getTime(String time, String pattern1, String pattern2) {
        Date date = null;
        if (!TextUtils.isEmpty(time)) {
            date = parse(time, pattern1);
        }
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern2, CHINA);
        return dateFormat.format(date);
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern, CHINA);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把 date 转成字符串
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(NORMAL_PATTERN, CHINA);
        return dateFormat.format(date);
    }

    /**
     * 获取 当前时间  前后几天的时间
     *
     * @param pattern
     * @param day     当前时间的正负值
     * @return
     */
    public static String getTime(String pattern, int day) {
        Date date = new Date();
        long endTime = day * 24L * 3600L * 1000L + date.getTime();
        Date end = new Date(endTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(end);
    }

    /**
     * 获取 指定时间  前后几天的时间
     *
     * @param pattern
     * @param day     当前时间的正负值
     * @return
     */
    public static String getTimeLater(String starTime, String pattern, int day) {
        Date date = parse(starTime, pattern);
        long endTime = day * 24L * 3600L * 1000L + date.getTime();
        Date end = new Date(endTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CHINA);
        return dateFormat.format(end);
    }

    public static String getEndTime(int day) {
        return getTime(NORMAL_PATTERN, day);
    }

    public static String getEndDate(int day) {
        return getTime(NORMAL_DATE, day);
    }

    //获取当前的年份
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance(CHINA);
        return calendar.get(Calendar.YEAR);
    }

    //比较2个时间的长短
    public static long getTimeCha(String start, String end, String pattern) {
        Date startDate = TimeUtil.parse(start, pattern);
        Date endDate = TimeUtil.parse(end, pattern);
        if (startDate != null && endDate != null) {
            return endDate.getTime() - startDate.getTime();
        }
        return 0;
    }

    //比较2个时间的长短
    public static long getTimeCha(String start, String end) {
        return getTimeCha(start, end, TimeUtil.NORMAL_PATTERN);
    }

    public static long dateToMillisecond(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NORMAL_PATTERN);//24小时制
        long time = 0;
        try {
            time = simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static int getDay(String time, String time2, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(time);
            date2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 获取时间 天:时:分;秒
     *
     * @return
     */
    public static String getTimeShort(long time) {
        int d = 1000 * 60 * 60 * 24;//3600
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long day = time / d;
        long l = time % d;
        long hour = l / h;
        long min = l % h / m;
        long sec = l % h % m / 1000;
        if (day > 0) {
            return day + "天" + hour + "时" + min + "分" + sec + "秒";
        }
        if (hour > 0) {
            return hour + "时" + min + "分" + sec + "秒";
        }
        if (min > 0) {
            return min + "分" + sec + "秒";
        }
        return sec + "秒";
    }

    /**
     * 获取时间 小时:分;秒
     *
     * @return
     */
    public static String getTimeAll(long time) {
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long hour = time / h;
        long min = time % h / m;
        long sec = time % h % m / 1000;
        if (hour > 9) {
            if (min > 9) {
                if (sec > 9) {
                    return hour + "H " + min + "Min";
                } else {
                    return hour + "H " + min + "Min";
                }
            } else {
                if (sec > 9) {
                    return hour + "H " + "0" + min + "Min";
                } else {
                    return hour + "H " + "0" + min + "Min";
                }
            }
        } else {
            if (min > 9) {
                if (sec > 9) {
                    return "0" + hour + "H " + min + "Min";
                } else {
                    return "0" + hour + "H " + min + "Min";
                }
            } else {
                if (sec > 9) {
                    return "0" + hour + "H " + "0" + min + "Min";
                } else {
                    return "0" + hour + "H " + "0" + min + "Min";
                }
            }
        }
    }

    /**
     * 获取时间 天：小时:分;秒  的集合
     *
     * @return
     */
    public static List<String> getTimeList(long time) {
        List<String> str = new ArrayList<>();
        int d = 1000 * 60 * 60 * 24;//3600
        int h = 1000 * 60 * 60;//3600
        int m = 1000 * 60;
        long day = time / d;
        long l = time % d;
        long hour = l / h;
        long min = l % h / m;
        long sec = l % h % m / 1000;
        str.add(day + "");
        if (hour > 9) {
            str.add(hour + "");
        } else {
            str.add("0" + hour);
        }
        if (min > 9) {
            str.add(min + "");
        } else {
            str.add("0" + min);
        }
        if (sec > 9) {
            str.add(sec + "");
        } else {
            str.add("0" + sec);
        }
        return str;
    }

    public static String getJiTime(long time) {
        long min = time / 60000;
        long sec = time % 60000 / 1000;
        StringBuilder s = new StringBuilder();
        if (min > 0) {
            if (min >= 10) {
                s.append(min);
            } else {
                s.append("0");
                s.append(min);
            }
        } else {
            s.append("00");
        }
        s.append(":");
        if (sec > 0) {
            if (sec >= 10) {
                s.append(sec);
            } else {
                s.append("0");
                s.append(sec);
            }
        } else {
            s.append("00");
        }
        if (TextUtils.isEmpty(s.toString())) {
            s.append("00:00");
        }
        return s.toString();
    }
}
