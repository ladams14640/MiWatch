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
import com.miproducts.miwatch.utilities.ModPositionFunctions;

/**
 * Created by ladam_000 on 7/19/2015.
 */
public class WatchFaceSurfaceViewConfig extends SurfaceView {
    private final static String TAG = "WatchFaceSurfaceView";

    // thread and holder
    private SurfaceHolder surfaceHolder;
    private SurfaceThread stThread;

    private Context mContext;

    // mods
    //private HudView mHudView;
    //private DigitalTimer mDigitalTimer;

    private int width = 0, height = 600;

    private PickingMod mDigitalTimer;
    private PickingMod mDate;
    private PickingMod mAlarm;
    private PickingMod mEvent;
    private PickingMod mFitness;
    private PickingMod mDegrees;

    public WatchFaceSurfaceViewConfig(Context context) {
        super(context);
        mContext = context;

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
          }
    //TODO we need to create a color choice some how.
    protected void drawSomething(Canvas canvas) {

        if(canvas != null) {
            if(width == 0){
                width = canvas.getWidth()-40;
                height = canvas.getHeight();

                mDigitalTimer = new PickingMod(getContext(),this,
                        // left wall
                        ModPositionFunctions.getLeftTimerPosition(width),
                        // top wall
                        ModPositionFunctions.getTopTimerPosition(height),
                        // ID
                        Consts.DIGITAL_TIMER);

                mDate = new PickingMod(getContext(), this,
                        ModPositionFunctions.getLeftDateConfigPosition(width),
                        ModPositionFunctions.getTopDateConfigPosition(height),
                        Consts.DATE);

                mEvent = new PickingMod(getContext(), this,
                        ModPositionFunctions.getLeftEventConfigPosition(width),
                        ModPositionFunctions.getTopEventConfigPosition(height),
                        Consts.EVENT);
            }{
                // background
                canvas.drawColor(Color.BLACK);

                // Hud
                //mHudView.draw(canvas);
                mDigitalTimer.draw(canvas);
                mDate.draw(canvas);
                mEvent.draw(canvas);
            }
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
