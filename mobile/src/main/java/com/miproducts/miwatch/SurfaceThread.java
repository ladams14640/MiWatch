package com.miproducts.miwatch;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by larry on 7/2/15.
 */
public class SurfaceThread extends Thread{
    private final static String TAG = "SurfaceThread";
    WatchFaceSurfaceView svView;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public SurfaceThread(WatchFaceSurfaceView svView){
        this.svView = svView;
    }

    public void setRunning(boolean run){
        isRunning = run;
    }

    public void setPaused(boolean pause){
        isPaused = pause;
        if(isPaused){
            if(isRunning)
                notify();

        }
    }

    @Override
    public void run(){
        while(isRunning){
            //Log.d("SurfaceThread", "running");
            Canvas canvas = svView.getHolder().lockCanvas();
            synchronized ((svView.getHolder())){
                while(isPaused){
                    try{
                        wait();
                    }catch(InterruptedException e){
                        log("issue with wait e.message = " + e.getMessage());
                    }
                }
                svView.drawSomething(canvas);
            }
            svView.getHolder().unlockCanvasAndPost(canvas);
        }
        try{
            sleep(30);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    private void log(String s) {
        Log.d(TAG, s);
        //init();
    }

    public boolean isRunning(){
        return isRunning;
    }
}
