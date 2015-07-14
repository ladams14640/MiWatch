package com.miproducts.miwatch.mods;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.MiDigitalWatchFace;
import com.miproducts.miwatch.container.Event;
import com.miproducts.miwatch.hud.HudView;
import com.miproducts.miwatch.utilities.BitmapConverter;
import com.miproducts.miwatch.utilities.ConverterUtil;
import com.miproducts.miwatch.utilities.LoadMeetingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ladam_000 on 6/29/2015.
 */
public class EventMod extends View {

    private final int CALENDAR_SWIPE_THRESHOLD = -50;


    private int mIndex;
    private int mEventIndex;
    LoadMeetingTask mLoadMeetingTask;


    private Context mContext;
    private Paint mPaint;
    private Rect locationRect;
    private int width;
    private int height = 100;
    private int x, y;
    private HudView mHudView;
    private float xText;
    private int textSize = 24;
    private int textHalfSize = textSize/2;
    // keep track if we are animating
    private boolean isAnimating = false;


    String mEventInfo = "";
    String mEventTitle = "";
    String mEventDesc = "";

    private int eventIndex;
    private boolean isTaskRunning = false;


    public void setEventIndex(int index){
        eventIndex = index;
    }

    public EventMod(Context context, HudView mHud) {
        super(context);
        this.mContext = context;
        this.mHudView = mHud;
        x = mContext.getWallpaperDesiredMinimumWidth()/10;
        y = (int)mHudView.getTopOfHud()+10;
        width = mContext.getWallpaperDesiredMinimumWidth()-50;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = width;
        params.height = y+height;
        setLayoutParams(params);


        locationRect = new Rect(x, y,x+width, y+height);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setAntiAlias(true);
        mPaint.setDither(false);
        mPaint.setTextAlign(Paint.Align.LEFT);
        xText = x;

       // initASyncTask();

    }
    public void initASyncTask(){
        mLoadMeetingTask = null;
        mLoadMeetingTask = new LoadMeetingTask(mContext, EventMod.this, mHudView);
        mLoadMeetingTask.execute();
    }
    /**
     * I want to slide the Event right off
     *
     *
     * @param event - the event the user did. This will allow us to keep HudView lean and mean :-).
     * @return - nothing really, I am not relying on this boolean return, but I kept it for future
     * possibilities.
    */
    // finger location when initial press.
    float xDown;
    // finger location while moving.
    float xMove, xOffsetTouch;
    boolean isDragging = false;
    public boolean touchInside(MotionEvent event){
        if(!locationRect.contains((int)event.getX(),(int)event.getY())) return false;
        else {
            log("touch is inside");
            // touch has come in

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    log("down");
                    // no need to move view if animating.
                    if(!isAnimating)
                        xDown = event.getX();

                    return true;

                case MotionEvent.ACTION_MOVE:
                    // no adjustments if we are animating
                    if(!isAnimating) {
                        log("moving");
                        xMove = event.getX();
                        xOffsetTouch = xMove - xDown;
                        float deltaX = Math.abs(xOffsetTouch);
                        isDragging = true;
                        xText = (int) xOffsetTouch;
                        log("position of xText == " + xText);
                        mHudView.invalidate();
                        if(xText < CALENDAR_SWIPE_THRESHOLD){
                            isAnimating = true;
                            fingerOff();
                        }
                    }
                    return true;
                // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
                case MotionEvent.ACTION_UP:
                    if(!isAnimating)
                        fingerOff();
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    if(!isAnimating)
                        fingerOff();
                    return true;
            }




            return true;
        }
    }

    private void sendConfigurationChange() {


    }


    private void fingerOff() {
        log("finger off and I was dragging = " + isDragging);
        if(isDragging){
            isDragging = false;
            if(xText < CALENDAR_SWIPE_THRESHOLD ){
                Log.d("Threshold", "met");
                animateChangeInEvent();
                return;

            } else {
                //TODO make a setup for going the other way too. that way we can grab events forward and backwards
                Log.d("threshold", "not met");
                isDragging = false;
                xDown = 0;
                xMove = 0;

                x = mContext.getWallpaperDesiredMinimumWidth()/10;
                y = (int)mHudView.getTopOfHud()+10;
                xText= x;

                mHudView.invalidate();
                return;
            }

        }
        x = mContext.getWallpaperDesiredMinimumWidth()/10;
        y = (int)mHudView.getTopOfHud()+10;
        xText= x;


    }

    private void animateChangeInEvent() {

        ValueAnimator animator = ValueAnimator.ofFloat(mContext.getWallpaperDesiredMinimumWidth(), x);
        log("Event Index we are using is " + mEventIndex);
        // It will take 1000ms for the animator to go from the width of the canvas to the
        // original position.
        animator.setDuration(500); // .5 second
        isAnimating = true;
        // Callback that executes on animation steps.
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            boolean alreadySetEvent = false;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                //Log.d("ValueAnimator", "value=" + value);
                if(!alreadySetEvent) {
                    alreadySetEvent = true;
                    log("current Event Size = " + mHudView.getEventSize());
                    if (mHudView.getEventSize() > eventIndex + 1) {
                        log("next Event");
                        eventIndex += 1;
                        setNextEvent(eventIndex);
                    }
                    // we have reached out limit and we want now to go back
                    // to original position
                    else {
                        log("first event");
                        eventIndex = 0;
                        setNextEvent(eventIndex);
                    }
                }
                xText = value;
                mHudView.invalidate();
                // we have finished.
                if (value <= x) {
                    log("animation done");
                    isDragging = false;
                    xDown = 0;
                    xMove = 0;
                    xText = x;
                    isAnimating = false;
                    mHudView.invalidate();
                }
            }


        });
        log("animation start");
        animator.start();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //canvas.drawRect(locationRect, mPaint);
        canvas.drawText(mEventInfo, xText, y + textSize, mPaint);
        canvas.drawText(mEventTitle, xText, y + (textSize*2), mPaint);
    //        canvas.drawText(mEventDesc, xText, y + (textSize * 3), mPaint);
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

    private void cancelLoadMeetingTask() {
        if (mLoadMeetingTask != null) {
            mLoadMeetingTask.cancel(true);
        }
    }
    public void justAdded() {
        //log("mLoadMeetingTask status = " + mLoadMeetingTask.getStatus().toString());
        // lets fetch new events if we got any
        xText = x;
    }
    public int setNextEvent(int index) {
        log("setNextEvent");
        isTaskRunning = false;
        if(mHudView.getEventSize() > index){// calendar[0] = Wed

            String dayOfMonth = mHudView.getEvent(index).dayOfMonth;
            String time = ConverterUtil.normalizeTime(mHudView.getEvent(index).time);
            String title = mHudView.getEvent(index).title;
            String description = mHudView.getEvent(index).desc;
            String month = mHudView.getEvent(index).month;

            //tvTitleRow.setText(month + " " + dayOfMonth + "  " + time);
            mEventInfo = (month + " " + dayOfMonth + "  " + time);
            mEventTitle = title;
            mEventDesc =  description;

            mEventIndex = index;
            Log.d("initCalendarHud", "events larger than index and it is " + mEventIndex);

        }
        // nothing has changed but the index that has been passed through is larger than
        // the events size so we go back to 0
        else if (mHudView.getEventSize() > 0) {
            Log.d("initCalendarHud", "events smaller than index and greater than 0");

            String dayOfMonth = mHudView.getEvent(0).dayOfMonth;
            String time = ConverterUtil.normalizeTime(mHudView.getEvent(0).time);
            String title = mHudView.getEvent(0).title;
            String description = mHudView.getEvent(0).desc;
            String month = mHudView.getEvent(0).month;

            //tvTitleRow.setText(month + " " + dayOfMonth + "  " + time);
            mEventInfo = (month + " " + dayOfMonth + "  " + time);
            mEventTitle = title;
            mEventDesc =  description;

            mEventIndex = 0;
        }
        log("mEvents.size = " + mHudView.getEventSize());
        log("return " + mEventIndex);
        mHudView.invalidate();
        return mEventIndex;

    }

    public void cancelTasks() {
        mLoadMeetingTask.cancel(true);
    }

    public void LoadingMeetingsDone() {

    }

    public int getEventsSize() {
        return mHudView.getEventSize();
    }





}
