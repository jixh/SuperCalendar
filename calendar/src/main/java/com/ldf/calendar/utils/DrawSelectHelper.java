package com.ldf.calendar.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import com.ldf.calendar.model.Point;

/**
 * Created by jktaihe on 1/10/17.
 * blog: blog.jktaihe.com
 */

public class DrawSelectHelper {

    private Point[] points = new Point[4];

    private int w, h,margin = 10, radius;

    private Paint circlePaint,rectPaint;

    public void setPoints(int cellWidth, int cellHeight) {
        w = cellWidth;
        h = cellHeight;
        radius = cellHeight/2;

        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#221122"));
        rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#22290091"));
    }

    public void refreshPoints(int col, int row) {

        Log.e("DrawSelectHelper","col = "+col+",row="+row);

        int tempH = row * h + h / 2;

        Point pointStart = new Point(w / 2, tempH);

        Point pointCenter = new Point(3 * w + w / 2, tempH);

        Point pointSelect = new Point((col - 1) * w + w / 2, tempH);

        Point pointEnd = new Point(6 * w + w / 2, tempH);

        points[0] = pointStart;
        points[1] = pointCenter;
        points[2] = pointSelect;
        points[3] = pointEnd;
    }

    public void onDrawSelect(Canvas canvas) {

        Log.e("DrawSelectHelper","onDrawSelect");

        canvas.drawRect(points[0].x,points[0].y-h/2+margin,points[3].x,points[3].y+h/2-margin,rectPaint);

        canvas.drawCircle(points[0].x,points[0].y,radius,circlePaint);

        canvas.drawCircle(points[3].x,points[3].y,radius,circlePaint);

    }
}
