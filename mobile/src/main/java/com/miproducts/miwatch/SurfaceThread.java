package com.miproducts.miwatch;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by larry on 7/2/15.
 */
public class SurfaceThread extends Thread{
    private final static String TAG = "SurfaceThread";
    WatchFaceSurfaceView svView;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private SurfaceHolder mHolder;
    Canvas canvas;
    public SurfaceThread(WatchFaceSurfaceView svView){
        this.svView = svView;
    }
    // was crashing without knowing when we initially started.
    private boolean hasStarted = false;

    public void setRunning(boolean run){
            isRunning = run;

            if(!hasStarted){
                start();
                hasStarted = true;
            }


    }


    // keep track of canvas when it is locked and unlocked.
    @Override
    public void run(){
        while(isRunning) {
            canvas = null;
            if (mHolder != null){
                try {
                    canvas = mHolder.lockCanvas();
                    synchronized (mHolder) {
                        svView.drawSomething(canvas);
                    }
                } finally {
                    if (canvas != null)
                        svView.getHolder().unlockCanvasAndPost(canvas);
                }
        }
            try{
                sleep(30);
            }catch(InterruptedException e){
                e.printStackTrace();

          }
        }
    }
    private void log(String s) {
        Log.d(TAG, s);
        //init();
    }


    public void passHolder(SurfaceHolder holder) {
        mHolder = holder;
    }
}
