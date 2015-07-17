package com.miproducts.miwatch.mods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.WatchFaceSurfaceView;
import com.miproducts.miwatch.utilities.BitmapConverter;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by larry on 6/29/15.
 */
public class FitnessMod extends View implements CustomizedMods {
    private Context mContext;
    private WatchFaceSurfaceView svView;
    private Bitmap bFitness;
    private Bitmap bResizeFitness;

    Rect locationRect;
    private int width = 200;
    private int height = 200;
    private float x, y;
    private Paint mPaint;
    private Paint pRect;
    private int currentColor;
    private boolean isVisible = true;


    public int getSize(){
        return width;
    }


    public FitnessMod(Context context, WatchFaceSurfaceView svView) {
        super(context);
        mContext = context;
        this.svView = svView;
        bFitness = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_fitness_white);

        x = Consts.canvasWidth - width;
        y = Consts.canvasHeight - height;

        bResizeFitness = BitmapConverter.getResizedBitmap(bFitness, (int)width, (int)height);
        initPaint();
        repositionRect();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        pRect = new Paint();
        pRect.setColor(getResources().getColor(R.color.digital_time_blue));
        pRect.setStyle(Paint.Style.STROKE);

        currentColor = getResources().getColor(R.color.white);

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
                svView.setSelection(Consts.FITNESS, true);
                svView.viewIsDragging(true);

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
                svView.viewIsDragging(true);
                return true;
            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
                log("xPosition = " + x);
                log("yPosition = " + y);
                svView.viewIsDragging(false);

                //svView.setSelection(svView.FITNESS, false);
                //unselectPaint();
                repositionRect();
                yMove = 0;
                xMove = 0;
                return true;

        }
        return true;

    }
    private void selectPaint() {
        pRect.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    public void unSelectPaint() {
        pRect.setColor(getResources().getColor(R.color.digital_time_blue));

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(locationRect, pRect);
        canvas.drawBitmap(bResizeFitness, x, y, mPaint);

    }
    public void changeSize(int newSize) {
        //log("changed Size to " + newSize);
        width =  newSize;
        height = newSize;
        refreshValues();
        //refresh rectangle
        repositionRect();
        invalidate();
    }

    private void refreshValues() {
        bResizeFitness = BitmapConverter.getResizedBitmap(bFitness, (int)width, (int)height);
        initPaint();
        selectPaint();
    }
    public FitnessMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitnessMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FitnessMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    public void log(String s){
        Log.d("FitnessMod", s);
    }

    public int getColor() {
        return currentColor;
    }
    public void setVisibility(boolean visible){
        isVisible = visible;
    }

    public float getX(){
        log("get X = " + x);
        return x;
    }
    public float getY(){
        log("get Y = "  + y);
        return y;
    }


    public boolean getViewsVisibility() {
        return isVisible;
    }
}
