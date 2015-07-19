package com.miproducts.miwatch.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.SettingsManager;

/**
 * Created by ladam_000 on 7/3/2015.
 */
public class DigitalTimer extends Mods implements CustomizedMods{
    private static final int ID = Consts.DIGITAL_TIMER;
    @Override
    public int getId() {
        return ID;
    }



    private final static String TAG = "DigitalTimer";
    private String digitalTime = "10:00";
    private int digitalSize = Consts.sizeDigitalTime;
    // 2 and 1/2 of the size
    private float digitalRectWidth = (2 * digitalSize) + (digitalSize/2);
    private float digitalRectLHeight = digitalSize;
    // heuristically came to this
    private float yCenterOfRect = (digitalSize/2)+(int)(digitalSize * .20);

    TextView tvDigitalTime;
    private Paint mTimePaint;
    private Rect selectRect;
    private Paint timeRectPaint;
    private WatchFaceSurfaceViewConfig svView;

    private Context mContext;

    private int xPositionForTime;
    private int yPositionForTime;
    private boolean isVisible = true;
    private int currentColor;

    public int getSize(){
        return digitalSize;
    }

    private void init(){
        initPaint();

        // Digital Hour and minutes
        tvDigitalTime = new TextView(getContext());
        tvDigitalTime.setTextColor(getResources().getColor(R.color.digital_time_blue));


        // Time
        xPositionForTime = (getContext().getWallpaperDesiredMinimumWidth() / 2) - (getContext().getWallpaperDesiredMinimumWidth() / 10);

        SettingsManager sm = new SettingsManager(mContext);
        sm.writeToPreferences(SettingsManager.DIGITAL_TIME_X, (int)xPositionForTime);

        yPositionForTime = 115;


        tvDigitalTime.setTextSize(24);
        resetRectPosition();
    }
    public void setVisibility(boolean visible){
        isVisible = visible;
    }
    public float getX(){
        log("get X = " + (xPositionForTime));
        return xPositionForTime;
    }
    public float getY(){
        log("get Y = " + yPositionForTime);
        return yPositionForTime;
    }

    private void resetRectPosition(){
        selectRect = new Rect(
                (int)(xPositionForTime),
                (int)yPositionForTime-(int)yCenterOfRect,
                (int)xPositionForTime+(int)digitalRectWidth,
                (int)yPositionForTime);
    }

    private void initPaint() {
        // Time Paint
        mTimePaint = new Paint();
        mTimePaint.setTextSize(digitalSize);
        mTimePaint.setColor(getResources().getColor(R.color.digital_time_blue));
        mTimePaint.setAntiAlias(true);
       // mTimePaint.setTextAlign(Paint.Align.LEFT);

        // rect paint
        timeRectPaint = new Paint();
        timeRectPaint.setColor(getResources().getColor(R.color.digital_time_blue));
        timeRectPaint.setStyle(Paint.Style.STROKE);

        currentColor = getResources().getColor(R.color.digital_time_blue);


    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //canvas.drawRect(selectRect, timeRectPaint);
        canvas.drawText(digitalTime, xPositionForTime, yPositionForTime, mTimePaint);

    }

    private boolean isTouchingTimeRectangle(float x, float y){
        log("touching digital text view");
        return selectRect.contains((int)x, (int)y);
    }

    // finger location while moving.
    float xMove, yMove;
    boolean isDragging = false;
    public boolean touchInside(MotionEvent event){
        // if we are dragging no need to check if we are within the square, just drag it.
        if(!isDragging) {
            if (!selectRect.contains((int) event.getX(), (int) event.getY())) {
                isDragging = false;
                return false;
            } else {
                isDragging = true;
            }
        }

       // log("touch is inside");

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Tell SurfaceView we are selected, and to make sure all touches go to us now.
               // svView.setSelection(Consts.DIGITAL_TIMER, true);
                // Tell surface View we are dragging.
                //svView.viewIsDragging(true);
                // make sure rectangle turns orange to indicate selection
                selectPaint();
                resetRectPosition();
                return true;

            case MotionEvent.ACTION_MOVE:
               // svView.viewIsDragging(true);
                xPositionForTime = (int)(event.getX()-digitalRectWidth/2);
                // make sure we do not go over the hud
                if((int)(event.getY()-digitalRectLHeight/2) < Consts.yHudPosition){
                    yPositionForTime = (int)(event.getY()-digitalRectLHeight/2);
                }
                resetRectPosition();
                return true;
            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
                log("xTime = " + xPositionForTime);
                log("yTime = " + yPositionForTime);
               // svView.viewIsDragging(false);
                //unlectPaint();
                resetRectPosition();
                yMove = 0;
                xMove = 0;
                return true;

        }
        return true;

    }

    private void selectPaint() {
        timeRectPaint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }
    @Override
    public void unSelectPaint() {
        timeRectPaint.setColor(getResources().getColor(R.color.digital_time_blue));

    }






















    public DigitalTimer(Context context, WatchFaceSurfaceViewConfig svView) {
        super(context, svView);
        this.svView = svView;
        mContext = context;
        init();
    }


    public DigitalTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DigitalTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public DigitalTimer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }
    private void log(String s){
        Log.d(TAG, s);
    }


    public void changeSize(int newSize) {
        //log("changed Size to " + newSize);
        digitalSize = newSize;
        refreshValues();
        //refresh rectangle
        resetRectPosition();
        invalidate();
    }

    private void refreshValues() {
        digitalRectWidth = (2 * digitalSize) + (digitalSize/2);
        digitalRectLHeight = digitalSize;
        yCenterOfRect = (digitalSize/2)+(int)(digitalSize * .20);
        initPaint();
        selectPaint();
    }

    public int getColor() {
        return currentColor;
    }

    @Override
    public boolean getViewsVisibility() {
        return isVisible;
    }
}
