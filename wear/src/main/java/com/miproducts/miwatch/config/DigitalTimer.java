package com.miproducts.miwatch.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Used by PickingMod - it is a special Mod and handles everything like the border n such.
 * Created by ladam_000 on 7/3/2015.
 */
public class DigitalTimer extends View {
    private static final int ID = Consts.DIGITAL_TIMER;
    private Paint rectPaint;
    private Paint timePaint;
    private float rightBorder, leftBorder, topBorder, botBorder;
    private Rect rectBorder;


    public DigitalTimer(Context context, WatchFaceSurfaceViewConfig svView, float leftBorder, float topBorder) {
        super(context);
        this.leftBorder = leftBorder;
        this.topBorder = topBorder;

        setStuff();

        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setColor(getResources().getColor(R.color.green_select));

        timePaint = new Paint();
        timePaint.setColor(getResources().getColor(R.color.digital_time_blue));
        timePaint.setTextSize(Consts.sizeDigitalTime);
        drawRectBorder();
        }
    // we handle the PickingMod differently if we gave const. digitalTimer ID.
    private void setStuff() {
            int digitalSize = Consts.sizeDigitalTime;
            // 2 and 1/2 of the size
            float digitalRectWidth = (2 * digitalSize) + (digitalSize/2);
            //float digitalRectLHeight = digitalSize;
            // heuristically came to this
            float yCenterOfRect = (digitalSize/2)+(int)(digitalSize * .20);

            this.rightBorder = (int) (leftBorder + digitalRectWidth);
            this.botBorder = topBorder;
            this.topBorder = (int)(topBorder-yCenterOfRect);
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
        if(canvas != null){
            canvas.drawRect(rectBorder, rectPaint);
            canvas.drawText("10:00", leftBorder, botBorder, timePaint);
        }
        super.draw(canvas);
    }

    public DigitalTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DigitalTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DigitalTimer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
