package com.miproducts.miwatch.mods;

/**
 * Created by larry on 7/6/15.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.WatchFaceSurfaceView;
import com.miproducts.miwatch.utilities.BitmapConverter;
import com.miproducts.miwatch.utilities.Consts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;


/**
 * Created by ladam_000 on 6/28/2015.
 */
public class TimerView extends View {
    private Context mContext;
    Paint mPaint;

    Bitmap bTimer;
    Bitmap bResizedTimer;
    Rect locationRect;
    private int width = 200;
    private int height = 200;
    private int x, y;
    private boolean isSelected = false;
    private WatchFaceSurfaceView svView;
    private int currentColor;
    private boolean isVisible = false;

    public int getSize(){
        return width;
    }


    public TimerView(Context context, WatchFaceSurfaceView svView) {
        super(context);
      //  log("init");
        this.mContext = context;
        this.svView = svView;
        bTimer = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_timer_white);

        x = Consts.canvasWidth / 20;
        y = Consts.canvasHeight - height;

        bResizedTimer = BitmapConverter.getResizedBitmap(bTimer, width, height);
        repositionRect();

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.digital_time_blue));

        currentColor = getResources().getColor(R.color.white);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(bResizedTimer, x, y, null);
        canvas.drawRect(locationRect, mPaint);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void repositionRect() {
        locationRect = new Rect(
                (int) x,
                (int)y,
                (int)x + (int)width,
                (int)y + (int)height);
    }

    // finger location while moving.
    float xMove, yMove;
    boolean isDragging = false;
    public boolean touchInside(MotionEvent event){
        // if we are dragging no need to check if we are within the square, just drag it.
        if(!isDragging) {
            if (!locationRect.contains((int) event.getX(), (int) event.getY())) {
                isDragging = false;
                return false;
            } else {
                isDragging = true;
            }
        }

        // we are now dragging and lets move this shit.
        //log("touch is inside");
        // touch has come in

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //log("down");
               // x = (int)(event.getX()-(width/2));
              //  y = (int) (event.getY()-(height/2));
                svView.setSelection(Consts.ALARM_TIMER, true);
                svView.viewIsDragging(true);
                // selected
                selectPaint();
                repositionRect();

                return true;

            case MotionEvent.ACTION_MOVE:
                // no adjustments if we are animating
                //log("moving");
                //log("moving the text view");
                x = (int)(event.getX()-(width/2));
                y = (int) (event.getY()-(height/2));
                repositionRect();
                return true;
            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
                log("xPosition = " + x);
                log("yPosition = " + y);
                svView.viewIsDragging(false);
                //svView.setSelection(svView.ALARM_TIMER, false);
                // unselected
                //unselectPaint();
                repositionRect();
                yMove = 0;
                xMove = 0;
                return true;

        }
        return true;

    }

    private void selectPaint() {
        mPaint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    public void unselectPaint() {
        mPaint.setColor(getResources().getColor(R.color.digital_time_blue));

    }

    public void log(String s){
        Log.d("TimerMod", s);
    }
    public void changeSize(int newSize) {
        //log("changed Size to " + newSize);
        width = newSize;
        height = newSize;
        refreshValues();
        //refresh rectangle
        repositionRect();
        invalidate();
    }

    private void refreshValues() {
        bResizedTimer = BitmapConverter.getResizedBitmap(bTimer, width, height);

        initPaint();
        selectPaint();
    }

    public int getColor() {
        return currentColor;
    }

    public float getX(){
        log("get X = " + (int)(svView.getCanvasX()-x));
        return (int)(svView.getCanvasX()-x);
    }
    public float getY(){
        return y- svView.getX();
    }

    public boolean getViewVisibility() {
        return isVisible;
    }

    public boolean getViewsVisibility() {
        return isVisible;
    }
}
