package com.ldf.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.ldf.calendar.Const;
import com.ldf.calendar.model.Point;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.interf.OnAdapterSelectListener;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarRenderer;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.Utils;
import com.ldf.calendar.utils.DrawSelectHelper;

public class Calendar extends View {
    /**
     * 日历列数
     */
    private CalendarAttr.CalendayType calendarType;
    private int cellHeight; // 单元格高度
    private int cellWidth; // 单元格宽度

    private OnSelectDateListener onSelectDateListener;    // 单元格点击回调事件
    private Context context;
    private CalendarAttr calendarAttr;
    private CalendarRenderer renderer;

    private OnAdapterSelectListener onAdapterSelectListener;
    private float touchSlop;
    private Paint circlePaint,rectPaint;
    private int radius = 20;
    private int margin = 10;
    private DrawSelectHelper pointHelper;

    public Calendar(Context context, OnSelectDateListener onSelectDateListener,DrawSelectHelper _pointHelper) {
        super(context);
        this.onSelectDateListener = onSelectDateListener;
        pointHelper = _pointHelper;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        touchSlop = Utils.getTouchSlop(context);
        initAttrAndRenderer();

        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#221122"));
        rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#22290091"));
    }

    private void initAttrAndRenderer() {
        calendarAttr = new CalendarAttr();
        calendarAttr.setWeekArrayType(CalendarAttr.WeekArrayType.Monday);
        calendarAttr.setCalendarType(CalendarAttr.CalendayType.MONTH);
        renderer = new CalendarRenderer(this, calendarAttr, context);
        renderer.setOnSelectDateListener(onSelectDateListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderer.draw(canvas);
    }

    private void onDrawSelect(Canvas canvas,Point[] points) {

        canvas.drawRect(points[0].x,points[0].y-cellHeight/2+margin,points[3].x,points[3].y+cellHeight/2-margin,rectPaint);

        canvas.drawCircle(points[0].x,points[0].y,radius,circlePaint);

        canvas.drawCircle(points[3].x,points[3].y,radius,circlePaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        cellHeight = h / Const.TOTAL_ROW;
        cellWidth = w / Const.TOTAL_COL;
        calendarAttr.setCellHeight(cellHeight);
        calendarAttr.setCellWidth(cellWidth);
        renderer.setAttr(calendarAttr);
    }

    private float posX = 0;
    private float posY = 0;

    /*
     * 触摸事件为了确定点击的位置日期
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - posX;
                float disY = event.getY() - posY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (posX / cellWidth);
                    int row = (int) (posY / cellHeight);

                    onAdapterSelectListener.cancelSelectState();

                    renderer.onClickDate(col, row);

                    onAdapterSelectListener.updateSelectState();

                    invalidate();
                }
                break;
        }
        return true;
    }

    public CalendarAttr.CalendayType getCalendarType() {
        return calendarAttr.getCalendarType();
    }

    public void switchCalendarType(CalendarAttr.CalendayType calendarType) {
        calendarAttr.setCalendarType(calendarType);
        renderer.setAttr(calendarAttr);
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void resetSelectedRowIndex() {
        renderer.resetSelectedRowIndex();
    }

    public int getSelectedRowIndex() {
        return renderer.getSelectedRowIndex();
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        renderer.setSelectedRowIndex(selectedRowIndex);
    }

    public void setOnAdapterSelectListener(OnAdapterSelectListener onAdapterSelectListener) {
        this.onAdapterSelectListener = onAdapterSelectListener;
    }

    public void showDate(CalendarDate current) {
        renderer.showDate(current);
    }

    public void updateWeek(int rowCount) {
        renderer.updateWeek(rowCount);
        invalidate();
    }

    public void updateSelectWeek(Week selectWeek,int row) {
        renderer.updateSelectWeek(selectWeek,row);
        invalidate();
    }

    public void update() {
        renderer.update();
    }

    public void cancelSelectState() {
        renderer.cancelSelectState();
    }

    public CalendarDate getSeedDate() {
        return renderer.getSeedDate();
    }

    public void setDayRenderer(IDayRenderer dayRenderer) {
        renderer.setDayRenderer(dayRenderer);
    }
}