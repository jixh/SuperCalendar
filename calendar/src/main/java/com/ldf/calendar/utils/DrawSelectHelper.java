package com.ldf.calendar.utils;

import android.graphics.Canvas;
import android.util.Log;

import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.model.DrawBean;
import com.ldf.calendar.model.Point;

/**
 * Created by jktaihe on 1/10/17.
 * blog: blog.jktaihe.com
 */

public class DrawSelectHelper {

    private static DrawSelectHelper instance;

    private DrawSelectHelper() {
    }

    public static DrawSelectHelper getInstance() {
        if (instance == null) instance = new DrawSelectHelper();
        return instance;
    }

    private static final float MAX = 5;

    private boolean isAnim = false;

    private int during = Float.valueOf(MAX).intValue();

    private Point[] points = new Point[4];

    private int w, h, margin = 10, radius;

    private static DrawBean selectDraw = new DrawBean("#2eb872", "#332eb872");

    private static DrawBean expireDraw = new DrawBean("#cbd2db", "#33cbd2db");


    public void onDrawSelect(Canvas canvas, int cellWidth, int cellHeight, int col, int row, boolean isExpire, boolean isAnim) {

        setPoints(cellWidth, cellHeight);

        refreshPoints(col, row);

        draw(canvas, isExpire ? expireDraw : selectDraw, isAnim ? during : Float.valueOf(MAX).intValue());
        Log.e("drawxx", "onDrawSelect=" + during + ",isAnim=" + isAnim);
        if (isAnim)
            anim();

    }

    private void anim() {
        if (during == MAX) {
            setAnim(false);
        } else {
            during++;
        }
    }

    private void draw(Canvas canvas, DrawBean drawBean, int during) {
        canvas.drawRect(points[0].x + ((points[2].x - points[0].x) / MAX * (MAX - during)),
                points[0].y - h / 2 + margin,
                points[3].x + ((points[2].x - points[3].x) / MAX * (MAX - during)),
                points[3].y + h / 2 - margin,
                drawBean.rectPaint);

        canvas.drawCircle(points[0].x + ((points[2].x - points[0].x) / MAX * (MAX - during)), points[0].y, radius - margin, drawBean.circlePaint);

        canvas.drawCircle(points[3].x + ((points[2].x - points[3].x) / MAX * (MAX - during)), points[3].y, radius - margin, drawBean.circlePaint);
    }

    private void setPoints(int cellWidth, int cellHeight) {
        w = cellWidth;
        h = cellHeight;
        radius = cellHeight / 2;
    }

    private void refreshPoints(int col, int row) {

        int tempH = row * h + h / 2;

        Point pointStart = new Point(w / 2, tempH);

        Point pointCenter = new Point(3 * w + w / 2, tempH);

        Point pointSelect = new Point(col * w + w / 2, tempH);

        Point pointEnd = new Point(6 * w + w / 2, tempH);

        points[0] = pointStart;
        points[1] = pointCenter;
        points[2] = pointSelect;
        points[3] = pointEnd;
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {

        isAnim = anim;

        if (isAnim) {
            during = 0;
        } else {
            during = Float.valueOf(MAX).intValue();
        }

    }
}
