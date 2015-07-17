package com.miproducts.miwatch.mods;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.WatchFaceSurfaceView;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by larry on 6/29/15.
 */
public class DegreeMod extends View implements CustomizedMods{

    private Context mContext;
    private WatchFaceSurfaceView svView;
    Paint mPaint;
    Rect locationRect;
    // setting these heuristically.
    private int textSize = 75;
    private int width = textSize;
    private int height = textSize + 25;
    private boolean isVisible = true;

    public int getSize(){
        return textSize;
    }

    private int x, y;

    Paint mPaint1;

    // current color
    private int currentColor;

    public DegreeMod(Context context, WatchFaceSurfaceView svView) {
        super(context);

        this.mContext = context;
        this.svView = svView;

        x = Consts.canvasWidth/2;
        y = Consts.canvasWidth-height;


        setRectangle();
        initPaint();

    }

    private void setRectangle() {
        locationRect = new Rect(
                x-(width/2),
                y-height,
                x+ (width + (width/2)),
                y+(height/2));
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setDither(false);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(getResources().getColor(R.color.white));

        currentColor = getResources().getColor(R.color.white);

        mPaint1 = new Paint();
        mPaint1.setDither(false);
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(getResources().getColor(R.color.digital_time_blue));
        mPaint1.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawRect(locationRect, mPaint1);
        // place in center
        canvas.drawText("72"+ Consts.DEGREE_SIGN, x, y, mPaint);
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
        //"touch is inside");
        // touch has come in

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //log("down");
                //x = (int)(event.getX()-(width/2));
                //y = (int) (event.getY()-(height/2));
                svView.setSelection(Consts.DEGREE, true);
                svView.viewIsDragging(true);

                selectPaint();
                setRectangle();

                return true;

            case MotionEvent.ACTION_MOVE:
                // no adjustments if we are animating
                //log("moving");
                //log("moving the text view");
                x = (int)(event.getX()-(width/2));
                y = (int) (event.getY()-(height/2));
                setRectangle();
                return true;
            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
                log("xPosition = " + x);
                log("yPosition = " + y);
                svView.viewIsDragging(false);

                // svView.setSelection(svView.DEGREE, false);
                //unselectPaint();
                setRectangle();
                yMove = 0;
                xMove = 0;
                return true;

        }
        return true;

    }

    private void selectPaint() {
        mPaint1.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    public void unSelectPaint() {
        mPaint1.setColor(getResources().getColor(R.color.digital_time_blue));

    }



    public void changeSize(int newSize) {
        //log("changed Size to " + newSize);
        textSize = newSize;
        refreshValues();
        //refresh rectangle
        setRectangle();
        invalidate();
    }

    private void refreshValues() {
        width = (int)(textSize * (1.5));
        height = (int)(textSize * (1.5));
        initPaint();
        selectPaint();
    }
    public void setVisibility(boolean visible){
        isVisible = visible;
    }
    public float getX(){
        log("get X = " + (x));
        return (x);
    }
    public float getY(){
        log("get Y = " + (y));
        return y;
    }





    public DegreeMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DegreeMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DegreeMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void log(String s){
        Log.d("DegreeMod", s);
    }

    public int getColor() {
        return currentColor;
    }
    public boolean getViewsVisibility() {
        return isVisible;
    }
}
