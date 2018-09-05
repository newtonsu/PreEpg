package com.starcor.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @desc 日期工具类
 * @author lj
 * @date 2018/3/25 22:35
 */
public class DateUtils {

    public static int TIME_END_SECOND = 86399000;//23:59:59 毫秒数

    /**
     * 根据秒数获取时间,格式为hhss
     * @param sencond
     * @return
     */
    public static String getTime(Long sencond){
        String hour = "" + sencond/3600;
        String min = "" + (sencond%3600)/60;
        hour = hour.length() == 1 ? ("0" + hour) : hour;
        min = min.length() == 1 ? ("0" + min) : min;
        return hour + min;
    }

    /**
     * 格式化毫秒数为指定格式的字符串
     * @param mills
     * @param pattern
     * @return
     */
    public static String formatDate(Long mills,String pattern){
        Date date = new Date(mills);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取当前日期字符串--格式为yyyyMMddd
     * @param numAfterCurDate
     * @return
     */
    public static String getCurrentDateStr(Integer numAfterCurDate){
        return formatDate(getCurrentDateMills(numAfterCurDate),"yyyyMMdd");
    }

    /**
     * 获取指定当前日期之后日期的毫秒数(日期格式为yyyy-MM-dd,即改日期不包含时分秒)
     * @param numAfterCurDate
     * @return
     */
    public static Long getCurrentDateMills(Integer numAfterCurDate){
        if(numAfterCurDate == null){
            numAfterCurDate = 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,numAfterCurDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long mills = null;
        try {
            String source = year + "-" + month + "-" + day;
            mills = sdf.parse(source).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mills;
    }

}
