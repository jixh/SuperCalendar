package com.ldf.calendar.model;

/**
 * Created by jktaihe on 1/10/17.
 * blog: blog.jktaihe.com
 */

public class Point {
    public boolean isAnim = false;
    public int x,y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(boolean isAnim, int x, int y) {
        this.isAnim = isAnim;
        this.x = x;
        this.y = y;
    }
}
