package com.ldf.calendar.interf;

import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Week;

/**
 * Created by ldf on 17/6/2.
 */

public interface OnSelectDateListener {
    void onSelectDate(CalendarDate date,boolean isExist);
    void onSelectOtherMonth(int offset);//点击其它月份日期
}
