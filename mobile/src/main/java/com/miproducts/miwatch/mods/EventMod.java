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
public class EventMod extends Mods implements CustomizedMods{
    private final static int ID = Consts.EVENT;

    private Context mContext;
    private Paint mTimePaint;

    private Paint mPaintRect;
    private Rect locationRect;

    private WatchFaceSurfaceView svWatchView;
    private float xText, yText;
    private int textSize = 40;
    String mEventInfo = "Info stuff";
    String mEventTitle = "Title stuff";
    private int currentColor;
    private boolean isVisible = true;

    public int getSize(){
        return textSize;
    }

    private float digitalRectWidth = ((textSize*4));
    private float digitalRectLHeight = (float) ((textSize*2));


    public EventMod(Context context, WatchFaceSurfaceView svWatchView) {
        super(context, svWatchView);
        this.mContext = context;
        this.svWatchView = svWatchView;

        yText = Consts.yEventPosition + textSize;
        xText = Consts.xEventPosition;

        repositionRect();
        initPaint();

    }

    private void repositionRect() {
        locationRect = new Rect(
                (int) xText,
                (int)yText -(int)(digitalRectLHeight/4),
                (int)xText + (int)digitalRectWidth,
                (int)yText + (int)digitalRectLHeight);
    }

    private void initPaint() {
        mTimePaint = new Paint();
        mTimePaint.setTextSize(textSize);
        mTimePaint.setColor(getResources().getColor(R.color.white));
        mTimePaint.setAntiAlias(true);
        mTimePaint.setTextAlign(Paint.Align.LEFT);


        mPaintRect = new Paint();
        mPaintRect.setColor(getResources().getColor(android.R.color.holo_blue_light));
        mPaintRect.setStyle(Paint.Style.STROKE);

        currentColor = mTimePaint.getColor();

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

                svWatchView.setSelection(Consts.EVENT, true);
                svWatchView.viewIsDragging(true);

                selectPaint();
                repositionRect();

                return true;

            case MotionEvent.ACTION_MOVE:

                xText = event.getX()-digitalRectWidth/2;
                // Make sure the View doesn't leave the hud's perimeter. - heuristic values
                if((int) (event.getY()-(int)(digitalRectLHeight)) > Consts.yHudPosition){
                    yText = event.getY()-digitalRectLHeight/2;
                }
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

    @Override
    public void unSelectPaint() {
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
        digitalRectWidth = (textSize*4);
        digitalRectLHeight = (float) ((textSize*2));
        initPaint();
        selectPaint();
    }

    public float getX(){
        log("get X = " + xText);
        return xText;
    }
    public float getY(){
        log("get Y = " + yText);
        return yText;
    }

    public void setVisibility(boolean visible){
        isVisible = visible;
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

    @Override
    public int getId() {
        return ID;
    }
}
