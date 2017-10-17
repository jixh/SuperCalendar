package com.ldf.calendar.model;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jktaihe on 17/10/17.
 * blog: blog.jktaihe.com
 */

public class DrawBean {

    public Paint circlePaint,rectPaint;

    public DrawBean(String circleColor,String rectColor){
        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor(circleColor));
        rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor(rectColor));
    }



}
