package com.miproducts.miwatch.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.miproducts.miwatch.R;

/**
 * Created by larry on 7/31/15.
 */
public class EventView extends View implements ConfigViews{
    private final static String TAG = "EventView";
    private WatchFaceSurfaceViewConfig svView;


    private float leftBorder;
    private float topBorder;
    private float rightBorder;
    private float botBorder;

    private float width = 300;
    private float height = 100;

    private Paint rectPaint;

    private Rect rectBorder;

    public EventView(Context context, WatchFaceSurfaceViewConfig svView, float leftBorder, float topBorder) {
        super(context);
        this.svView = svView;
        this.leftBorder = leftBorder;
        this.topBorder = topBorder;
        this.rightBorder = leftBorder + width;
        this.botBorder = topBorder + height;

        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setColor(getResources().getColor(R.color.green_select));

        drawRectBorder();
    }

    // Draw the rect that surrounds the Mod
    private void drawRectBorder(){
        rectBorder = new Rect(
                (int)(leftBorder),
                (int)topBorder,
                (int)rightBorder,
                (int)botBorder);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rectBorder, rectPaint);


        super.draw(canvas);
    }

    public void log(String msg) {
        Log.d(TAG, msg);
    }


    public EventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EventView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
