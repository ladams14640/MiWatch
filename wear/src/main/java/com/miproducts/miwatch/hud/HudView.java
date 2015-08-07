package com.miproducts.miwatch.hud;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.wearable.watchface.WatchFaceService;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.wearable.DataMap;
import com.miproducts.miwatch.MiDigitalWatchFace;
import com.miproducts.miwatch.R;
import com.miproducts.miwatch.container.Event;
import com.miproducts.miwatch.mods.DegreeMod;
import com.miproducts.miwatch.mods.EventMod;
import com.miproducts.miwatch.mods.FitnessMod;
import com.miproducts.miwatch.mods.TimerMod;
import com.miproducts.miwatch.utilities.Consts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 6/26/15.
 */
public class HudView extends ViewGroup implements View.OnTouchListener{

    private Context mContext;
    private WindowManager.LayoutParams params;
    private float x= 0,y = 125,w = 200,h=200;
    Rect mRect;
    MiDigitalWatchFace.Engine mEngine;
    /* All the booleans for the mods */
    boolean isFitness = true, isDegree = true, isEvents = true, isTimer = true;
    Paint mTestPaint;

    // MODS
    TimerMod mTimerMod;
    FitnessMod mFitnessMod;
    DegreeMod mDegreeMod;
    EventMod mEventMod;
    private List<Event> mEvents;
    private boolean isRound = false;

    public HudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public HudView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;

    }

    public HudView(Context context, boolean isRound, MiDigitalWatchFace.Engine engine) {
        super(context);

        mContext = context;
        setOnTouchListener(this);
        mEngine = engine;


        w = mEngine.getWidth();
        h = mEngine.getHeight();

        mRect= new Rect((int)x,(int)y,(int)w,(int)h);

        this.isRound = isRound;

        initPaint();

        mEvents = new ArrayList<Event>();

        initMods();

    }

    public float getSurfaceWidth(){
        return w;
    }

    private void initPaint() {
        mTestPaint = new Paint();
        mTestPaint.setColor(getResources().getColor(R.color.digital_time_blue));
        mTestPaint.setAlpha(100);
    }

    private void initMods() {
        if(isTimer){
            mTimerMod = new TimerMod(mContext,this);
        }
        if(isFitness){
            mFitnessMod = new FitnessMod(mContext,this);
        }
        if(isDegree){
            mDegreeMod = new DegreeMod(mContext, this);
        }
        if(isEvents){
            mEventMod = new EventMod(mContext, this);
        }
    }

    public boolean isRound(){
        return isRound;
    }
    // since we will be manually calling it, no need to have a onDraw, just call draw from MiDigital-
    // WatchFace
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //log("Draw");

        //canvas.drawRect(x, y, w, h, mTestPaint);
        mEventMod.draw(canvas);
        mDegreeMod.draw(canvas);

        //canvas.scale(2,2); // 2 views below use images and seems to temp fix
        mFitnessMod.draw(canvas);
        mTimerMod.draw(canvas);
    }



    /**
     * Call this before getParams() is called.
     */
    public void setParams(){
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = 200;
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle("Load Average");
    }

    /**
     * Make sure we called setParams() first.
     * Call this so we can add the HudView to the WatchfaceService
     * @return WindowManager.LayoutParams for this HudView with WindowManager.addView(this, this.getParams())
     */
    public WindowManager.LayoutParams getParams(){
        return params;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //log("touched");
        // we're inside the Hud View
        if(mRect.contains((int)event.getX(), (int) event.getY())){
            switch(event.getAction() ){
                case MotionEvent.ACTION_DOWN:
                    // check if touch was inside
                    mEventMod.touchInside(event);
                    mTimerMod.touchInside(event.getX(), event.getY());
                    mFitnessMod.touchInside(event.getX(), event.getY());
                    mDegreeMod.touchInside(event.getX(), event.getY());

                    return false;
                case MotionEvent.ACTION_MOVE:
                    mEventMod.touchInside(event);

                    return false;
                case MotionEvent.ACTION_CANCEL:
                    mEventMod.touchInside(event);
                    return false;
                case MotionEvent.ACTION_UP:
                    mEventMod.touchInside(event);
                    return false;
            }
            //mEventMod.touchInside(event);

            log("touch on inside");
            return false;
        }else {
            log("touch on the outside");
            mEventMod.cancelTasks();
            mEngine.removeHudView();
            return false;
        }

    }

    public float getTopOfHud(){
        return y;
    }


    @Override
    public void invalidate() {
        super.invalidate();
        mEngine.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
    public void log(String s){
        Log.d("HudView", s);
    }

    public void justAdded() {
        if(isEvents){
            mEventMod.justAdded();
        }

        if(isDegree){
            mDegreeMod.resetTemp();
        }
    }
    public void resetTemp(){
        if(isDegree){
            mDegreeMod.resetTemp();
        }
    }

    public boolean isEventModActive(){
        return isEvents;
    }
    public WatchFaceService.Engine getEngine() {
        return mEngine;
    }

    public void initEventSyncTask() {
        mEventMod.initASyncTask();

    }

    public Event getEvent(int index){
        return mEvents.get(index);
    }

    public int getEventSize(){

        return mEvents.size();
    }
    public void addEvents(List<Event> mEvents) {
        this.mEvents = mEvents;
    }

    /**
     * Called by DegreeMod, will refresh their stuff.
     * @param dataMap - new DataMap with a oscillating boolean value, to stimulate a change in the
     *                MainCompanionActivity's Listener.
     */
    public void refreshDegrees(DataMap dataMap) {
        log("refreshDegrees dataMap: " + dataMap);
        mEngine.refreshDegrees(dataMap);
    }

    public void cancelRefreshDegree() {
        if(isDegree){
            mDegreeMod.cancelDisplayRefresh();
        }
    }
}
