package com.ldf.calendar;

import android.text.TextUtils;

import com.ldf.calendar.model.WeekDate;
import com.ldf.calendar.view.Day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jktaihe on 17/10/17.
 * blog: blog.jktaihe.com
 */

public class DateUtils {

    private static SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isExpire(Day day){

        WeekDate weekDate = getWeek(day.getDate().toString());

        return compareDate(getToday(),weekDate.endDay) < 0;
    }


    public static WeekDate getWeek(String date){

        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(SDF_DAY.parse(date));
        } catch (ParseException e) {
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

        if (TextUtils.equals(beginDate,endDate))return true;

        try {
            if (SDF_DAY.parse(endDate).getTime() == SDF_DAY.parse(beginDate).getTime()) return  true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;


    }
}
