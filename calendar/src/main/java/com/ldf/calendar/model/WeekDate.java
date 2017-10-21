package com.ldf.calendar.model;

import com.ldf.calendar.DateUtils;

import java.io.Serializable;

/**
 * Created by jktaihe on 17/10/17.
 * blog: blog.jktaihe.com
 */

public class WeekDate implements Serializable {

    public String startDay;
    public String endDay;

    public WeekDate(String startDay, String endDay) {
        this.startDay = DateUtils.getFormatDate(startDay);
        this.endDay =  DateUtils.getFormatDate(endDay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeekDate weekDate = (WeekDate) o;

        if (!startDay.equals(weekDate.startDay)) return false;
        return endDay.equals(weekDate.endDay);
    }

    @Override
    public int hashCode() {
        int result = startDay.hashCode();
        result = 31 * result + endDay.hashCode();
        return result;
    }
}
