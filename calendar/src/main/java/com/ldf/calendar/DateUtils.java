package com.ldf.calendar;

import android.text.TextUtils;

import com.ldf.calendar.model.WeekDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jktaihe on 17/10/17.
 * blog: blog.jktaihe.com
 */

public class DateUtils {

    private static SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isExpire(String day){

        if (DateUtils.equalDate(day,Utils.pressedStateStartDate))
            return true;

        WeekDate weekDate = getWeek(day);
        return compareDate(getToday(),weekDate.endDay) < 0;
    }




    public static WeekDate getWeek(String date){

        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(SDF_DAY.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);

        // 所在周开始日期
        String data1 = SDF_DAY.format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        String data2 = SDF_DAY.format(cal.getTime());

        return new WeekDate(data1 , data2);
    }


    public static String getToday() {
        return  SDF_DAY.format(System.currentTimeMillis());
    }

    public static int compareDate(String beginDate, String endDate){
        int w = 0;

        if(!TextUtils.isEmpty(beginDate) && !TextUtils.isEmpty(endDate)){

            if (TextUtils.equals(beginDate,endDate))return 0;

            try {
                w = SDF_DAY.parse(endDate).getTime() > SDF_DAY.parse(beginDate).getTime()
                        ? 1	: -1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return w;
    }

    public static boolean isDefaultDateFormat(String date){
        try {
            SDF_DAY.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean equalDate(String beginDate,String endDate){

        if (TextUtils.isEmpty(beginDate)|| TextUtils.isEmpty(endDate))return false;

        if (TextUtils.equals(beginDate,endDate))return true;

        try {
            if (SDF_DAY.parse(endDate).getTime() == SDF_DAY.parse(beginDate).getTime()) return  true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;


    }

    public static boolean lessThanToday(String date){
        WeekDate weekDate = DateUtils.getWeek(DateUtils.getToday());
        return compareDate(weekDate.startDay,date) < 0;
    }

    public static int diffMonth(String date){

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(SDF_DAY.parse(date));
            c2.setTime(SDF_DAY.parse(getToday()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int month = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12;
        int month2 = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);

        return  month + month2;
    }


    public static boolean isNotMinMonth(String date){
        return diffMonth(date) > -2;
    }


    public static boolean isNotMaxMonth(String date){
        return diffMonth(date) < 12;
    }

    public static String getDate(String date, int duringDay){
        String result = "";
        try {
            Date d = SDF_DAY.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DAY_OF_MONTH,duringDay);
            result = SDF_DAY.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFormatDate(String date){
        String d = date;

        try {
            d = SDF_DAY.format(SDF_DAY.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

}
