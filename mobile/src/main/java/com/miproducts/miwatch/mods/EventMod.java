package com.miproducts.miwatch.mods;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.WatchFaceSurfaceView;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by ladam_000 on 6/29/2015.
 */
public class EventMod extends View {


    private Context mContext;
    private Paint mTimePaint;

    private Paint mPaintRect;
    private Rect locationRect;

    private WatchFaceSurfaceView svWatchView;
    private float xText, yText;
    private float textSize = 40;
    private float displayedTextSize = textSize*2;
    String mEventInfo = "testing";
    String mEventTitle = "title";
    private int currentColor;
    private boolean isVisible = false;

    public float getSize(){
        return textSize;
    }

    private float digitalRectWidth = 8 * textSize;
    private float digitalRectLHeight = (float) 1.5* displayedTextSize;
    private float yCenterOfRect = digitalRectLHeight/2;

    public EventMod(Context context, WatchFaceSurfaceView svWatchView) {
        super(context);
        this.mContext = context;
        this.svWatchView = svWatchView;

        yText = Consts.yEventPosition;
        xText = Consts.xEventPosition;

        repositionRect();
        initPaint();

    }

    private void repositionRect() {
        locationRect = new Rect(
                (int) xText,
                (int)yText -(int)(digitalRectLHeight/2),
                (int)xText + (int)digitalRectWidth,
                (int)yText + (int)digitalRectLHeight);
    }

    private void initPaint() {
        mTimePaint = new Paint();
        mTimePaint.setTextSize(textSize);
        mTimePaint.setColor(getResources().getColor(R.color.digital_time_blue));
        mTimePaint.setAntiAlias(true);
        mTimePaint.setTextAlign(Paint.Align.LEFT);


        mPaintRect = new Paint();
        mPaintRect.setColor(getResources().getColor(android.R.color.holo_blue_light));
        mPaintRect.setStyle(Paint.Style.STROKE);

        currentColor = getResources().getColor(R.color.digital_time_blue);

    }

    /**
     * I want to slide the Event right off
     *
     *
     * @param event - the event the user did. This will allow us to keep HudView lean and mean :-).
     * @return - nothing really, I am not relying on this boolean return, but I kept it for future
     * possibilities.
    */
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
                //xText = event.getX()-digitalRectWidth/2;
                //yText = event.getY()-digitalRectLHeight/2;
                svWatchView.setSelection(Consts.EVENT, true);
                svWatchView.viewIsDragging(true);

                selectPaint();
                repositionRect();

                return true;

            case MotionEvent.ACTION_MOVE:
                // no adjustments if we are animating
                //log("moving");
                //log("moving the text view");
                xText = event.getX()-digitalRectWidth/2;
                yText = event.getY()-digitalRectLHeight/2;
                repositionRect();
                return true;
            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
                log("xPosition = " + xText);
                log("yPosition = " + yText);
                svWatchView.viewIsDragging(false);

//                svWatchView.setSelection(svWatchView.EVENT, false);
                //unselectPaint();
                repositionRect();
                yMove = 0;
                xMove = 0;
                return true;

            }
            return true;

    }

    private void selectPaint() {
        mPaintRect.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    public void unselectPaint() {
        mPaintRect.setColor(getResources().getColor(R.color.digital_time_blue));

    }




    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(locationRect, mPaintRect);
        canvas.drawText(mEventInfo, xText, yText, mTimePaint);
        canvas.drawText(mEventTitle, xText, yText + textSize, // this its below prior one
                mTimePaint);
        }


    public void changeSize(int newSize) {
        //log("changed Size to " + newSize);
        textSize = newSize;
        refreshValues();
        //refresh rectangle
        repositionRect();
        invalidate();
    }

    private void refreshValues() {
        digitalRectWidth = 8 * textSize;
        digitalRectLHeight = (float) 1.5* displayedTextSize;
        initPaint();
        selectPaint();
    }








    public float getX(){
        log("get X = " + xText);
        return xText;
    }
    public float getY(){

        return yText;
    }







    public void log(String s){
        Log.d("EventMod", s);
    }

    public EventMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EventMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getColor() {
        return currentColor;
    }

    public boolean getViewsVisibility() {
        return isVisible;
    }
}
