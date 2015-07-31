package com.miproducts.miwatch.config;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ladam_000 on 7/16/2015.
 */
public class Mods extends View {
    public Mods(Context context, WatchFaceSurfaceViewConfig svView) {
        super(context);
    }

    public Mods(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Mods(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Mods(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean touchInside(MotionEvent event){return false;}

    public void unSelectPaint(){}

    public int getId(){return 0;}
    private void log(String msg){}
}
