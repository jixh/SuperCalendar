package com.ldf.calendar;

import android.content.Context;

import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.interf.OnSelectDateListener;

/**
 * Created by jktaihe on 19/11/17.
 * blog: blog.jktaihe.com
 */

public class CalendarManager {


    private CalendarViewAdapter calendarAdapter = null;

    public CalendarManager(Context context, OnSelectDateListener onSelectDateListener, IDayRenderer iDayRenderer){

        initListener();

        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                iDayRenderer);

        initMonthPager();
    }

    private static void initMonthPager() {

    }

    private static void initListener() {

    }

}
