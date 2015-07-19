package com.miproducts.miwatch.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.miproducts.miwatch.mods.*;
import com.miproducts.miwatch.mods.DegreeMod;
import com.miproducts.miwatch.mods.FitnessMod;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by ladam_000 on 7/19/2015.
 */
public class WatchFaceSurfaceViewConfig extends SurfaceView {
    private final static String TAG = "WatchFaceSurfaceView";

    // thread and holder
    private SurfaceHolder surfaceHolder;
    private SurfaceThread stThread;

    // mods
    private DateViews mDateViews;
    //private HudView mHudView;
    private DigitalTimer mDigitalTimer;

    private int width = 0, height = 600;



    public WatchFaceSurfaceViewConfig(Context context) {
        super(context);
        init();

    }

    private void init() {
        log("init");

        stThread = new SurfaceThread(this);

        surfaceHolder =  getHolder();

        initMods();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                log("surface created.");
                //if it is the first time the thread starts
                stThread = new SurfaceThread(stThread.getSvView());
                stThread.passHolder(surfaceHolder);
                stThread.setRunning(true);
                stThread.start();  // Start a new thread
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


    }


    public float getCanvasWidth(){
        return width;
    }
    public float getCanvasHeight(){
        return height;
    }
    private void initMods() {
       // mHudView = new HudView(getContext(),this);
        mDateViews = new DateViews(getContext(), this);
        mDigitalTimer = new DigitalTimer(getContext(), this);


    }

    protected void drawSomething(Canvas canvas) {

        if(canvas != null) {
            if(width == 0){
                width = canvas.getWidth();
                height = canvas.getHeight();
            }
            // background
            canvas.drawColor(Color.BLACK);

            // Hud
            //mHudView.draw(canvas);
            mDateViews.draw(canvas);
            mDigitalTimer.draw(canvas);
        }

    }





    public float getCanvasX(){
        //   log("getCanvasX = " + ((int) getCanvasWidth() - getX()));
        return getCanvasWidth() - getX();
    }


    public WatchFaceSurfaceViewConfig(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WatchFaceSurfaceViewConfig(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public WatchFaceSurfaceViewConfig(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void log(String s) {
        Log.d(TAG, s);
        //init();
    }



    // pause thread from the MainCompanionActivity
    public void threadRun(boolean b) {
        stThread.setRunning(b);
    }


}