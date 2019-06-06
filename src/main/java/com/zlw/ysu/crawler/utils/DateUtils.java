package com.zlw.ysu.crawler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Ranger
 * @create 2019-05-18 15:46
 */
public class DateUtils {

    private static String startDay = "2019-02-25";
    private static String endDay = "2019-07-14";

    public static int getWeek() {
        int days = 0;
        int nowWeek = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
            String nowDate = df.format(new Date());
            int nowDaysBetween = daysBetween(startDay, nowDate) + 1;
            days = daysBetween(startDay, endDay);
            int x = nowDaysBetween % 7;
            if (x == 0) {
                nowWeek = nowDaysBetween / 7;
            } else {
                nowWeek = nowDaysBetween / 7 + 1;
            }

        } catch (ParseException e) {
//            System.out.println("输入的日期不合法，解析日期失败");
            e.printStackTrace();
        }
        return nowWeek;
    }

    /**
     * 获取当前时间是星期几
     *
     * @return
     */
    public static int getWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
            return 7;
        }
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 计算两个String类型日期之间的天数
     *
     * @param startDay
     * @param endDay
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String startDay, String endDay)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDay));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDay));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式返回String类型系统时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());
    }
}
