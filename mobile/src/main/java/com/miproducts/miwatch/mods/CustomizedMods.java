package com.miproducts.miwatch.mods;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.miproducts.miwatch.WatchFaceSurfaceView;

/**
 * Created by ladam_000 on 7/11/2015.
 */
public class CustomizedMods extends View {

    private static final String TAG = "CustomizedMods";
    private float xPos, yPos;
    private Rect rLocationRect;
    Paint pMain;
    Paint pRect;

    private int size = 75;

    // heuristically came to this
    private float rectWidth = (2 * size) + (size/2);
    private float rectHeight = size;


    private int currentColor;
    private WatchFaceSurfaceView svView;

    public float getSize(){
        return size;
    }

    public float getX(){
        log("get X = " + (xPos));
        return xPos;
    }
    public float getY(){
        log("get Y = " + yPos);
        return yPos;
    }



    public CustomizedMods(Context context) {
        super(context);
    }



























































    private void log(String s){
        Log.d(TAG, s);}

    public CustomizedMods(Context context, AttributeSet attrs) {      super(context, attrs);   }

    public CustomizedMods(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);  }

    public CustomizedMods(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);    }
}
