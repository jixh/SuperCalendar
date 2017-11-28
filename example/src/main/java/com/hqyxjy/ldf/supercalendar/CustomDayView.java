package com.hqyxjy.ldf.supercalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.State;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.utils.DrawSelectHelper;
import com.ldf.calendar.view.DayView;

/**
 * Created by ldf on 17/6/26.
 */

public class CustomDayView extends DayView {

    public static final int COLOR_CURRENT = Color.parseColor("#3c4350");
    public static final int COLOR_NEXT_PAST = Color.parseColor("#bcc0c8");
    public static final int selectColor = Color.parseColor("#FFFFFF");

    private TextView dateTv;
    private ImageView marker;
    private View selectedBackground;
    private View todayBackground;
    private final CalendarDate today = new CalendarDate();

    /**
     * 构造器
     *
     * @param context 上下文
     * @param layoutResource 自定义DayView的layout资源
     */
    public CustomDayView(Context context, int layoutResource) {
        super(context, layoutResource);
        dateTv = (TextView) findViewById(R.id.date);
        marker = (ImageView) findViewById(R.id.maker);
        selectedBackground = findViewById(R.id.selected_background);
        todayBackground = findViewById(R.id.today_background);
    }

    @Override
    public void refreshContent(boolean isAnim) {
        renderSelect(day.getState());
        renderToday(day.getDate());
//        renderMarker(day.getDate(), day.getState());
        super.refreshContent(isAnim);
    }

    private void renderMarker(CalendarDate date, State state) {
        if (Utils.loadMarkData().containsKey(date.toString())) {
            if (state == State.SELECT || date.toString().equals(today.toString())) {
                marker.setVisibility(GONE);
            } else {
                marker.setVisibility(VISIBLE);
                if (Utils.loadMarkData().get(date.toString()).equals("0")) {
                    marker.setEnabled(true);
                } else {
                    marker.setEnabled(false);
                }
            }
        } else {
            marker.setVisibility(GONE);
        }
    }

    private void renderSelect(State state) {

        if (state == State.EXPIRE ||state == State.SD_START || state == State.SD_END||state == State.SELECT_START || state == State.SELECT_END ) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(selectColor);
        }
        else if (state == State.SELECT_DATE_START ||state == State.SELECT_DATE_END ) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(DrawSelectHelper.getInstance().isAnim()?COLOR_CURRENT:selectColor);
        }
        else if (state == State.SELECT) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(COLOR_CURRENT);
        }
        else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(COLOR_NEXT_PAST);
        } else {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(COLOR_CURRENT);
        }
    }

    private void renderToday(CalendarDate date) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText("今");
                todayBackground.setVisibility(VISIBLE);
            } else {
                dateTv.setText(date.day + "");
                todayBackground.setVisibility(GONE);
            }
        }
    }

    @Override
    public IDayRenderer copy() {
        return new CustomDayView(context, layoutResource);
    }
}
