package com.ldf.calendar.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.ldf.calendar.model.Point;

/**
 * Created by jktaihe on 1/10/17.
 * blog: blog.jktaihe.com
 */

public class DrawSelectHelper {

    public static final float MAX = 10;

    private boolean isShow = true;

    private boolean isAnim = false;

    private int during;

    private Point[] points = new Point[4];

    private int w, h,margin = 10, radius;

    private Paint circlePaint,rectPaint;



    public void setPoints(int cellWidth, int cellHeight) {
        w = cellWidth;
        h = cellHeight;
        radius = cellHeight/2;

        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#27221122"));
        rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#22290091"));
    }

    public void refreshPoints(int col, int row) {

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

    public void onDrawSelect(Canvas canvas) {

        if (!isShow)return;

        anim(canvas);

    }

    private void anim(Canvas canvas) {
        canvas.drawRect(points[0].x + ((points[2].x - points[0].x)/MAX * (MAX -during)),
                points[0].y-h/2+margin,
                points[3].x+ ((points[2].x - points[3].x)/MAX * (MAX -during)),
                points[3].y+h/2-margin,
                rectPaint);

        canvas.drawCircle(points[0].x+((points[2].x - points[0].x)/MAX * (MAX -during)),points[0].y,radius,circlePaint);

        canvas.drawCircle(points[3].x+((points[2].x - points[3].x)/MAX * (MAX -during)),points[3].y,radius,circlePaint);


        if (during >= MAX){
            setAnim(false);
        }else {
            during++;
        }
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {

        isAnim = anim;

        if (isAnim){
            during = 0;
        }else{
            during = Float.valueOf(MAX).intValue();
        }

    }
}
