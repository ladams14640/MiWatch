package com.miproducts.miwatch.mods;

import android.view.MotionEvent;

/**
 * Created by ladam_000 on 7/16/2015.
 */
public interface CustomizedMods  {
    int getSize();
    boolean touchInside(MotionEvent event);
    void unSelectPaint();
    float getX();
    float getY();
    void changeSize(int newSize);
    void setVisibility(boolean visible);
    int getColor();
    boolean getViewsVisibility();
}
