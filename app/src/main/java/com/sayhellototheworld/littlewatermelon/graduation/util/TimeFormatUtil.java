package com.sayhellototheworld.littlewatermelon.graduation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 123 on 2017/9/25.
 */

public class TimeFormatUtil {

    public static Date bmobDateToDate(String bmobDate){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = null;
        try {
            date = sdf.parse(bmobDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateToStringTime(Date date){
        String time;
        Calendar calendarDate = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        calendarDate.setTime(date);
        int year = calendarDate.get(Calendar.YEAR);
        int month = calendarDate.get(Calendar.MONTH) + 1;
        int day = calendarDate.get(Calendar.DAY_OF_MONTH);
        String mo = month + "";
        String da = day + "";
        if (day < 10){
            da = "0" + day;
        }
        if (month < 10){
            mo = "0" + month;
        }
        if (calendarDate.get(Calendar.YEAR) != calendarNow.get(Calendar.YEAR)){
            time = year + "-" + mo + "-" + da;
        }else {
            time =mo + "-" + da;
        }
        return time;
    }

    public static String DateToRealTime(Date date){
        String time;
        Calendar calendarDate = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        calendarDate.setTime(date);
        int year = calendarDate.get(Calendar.YEAR);
        int month = calendarDate.get(Calendar.MONTH) + 1;
        int day = calendarDate.get(Calendar.DAY_OF_MONTH);
        int hour = calendarDate.get(Calendar.HOUR_OF_DAY);
        int minute = calendarDate.get(Calendar.MINUTE);
        String mo = month + "";
        String ho = hour + "";
        String mi = minute + "";
        String da = day + "";
        if (day < 10){
            da = "0" + day;
        }
        if (month < 10){
            mo = "0" + month;
        }
        if (hour < 10){
            ho = "0" + hour;
        }
        if (minute < 10){
            mi = "0" + minute;
        }
        if (calendarDate.get(Calendar.YEAR) != calendarNow.get(Calendar.YEAR)){
            time = year + "年" + mo + "月" + da + "日  " + ho + ":" + mi;
        }else {
            time =mo + "月" + da + "日  " + ho + ":" + mi;
        }
        return time;
    }

    /**
     *
     * @param date1
     * @param date2
     * @return 如果 date1 在 date2 之后返回true，反之返回false
     */
    public static boolean compareDate(Date date1,Date date2){
        if (date1.after(date2)){
            return true;
        }
        return false;
    }

}
