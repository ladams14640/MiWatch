package com.miproducts.miwatch.mods;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.wearable.DataMap;
import com.miproducts.miwatch.R;
import com.miproducts.miwatch.hud.HudView;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.SettingsManager;

/**
 * Created by larry on 6/29/15.
 */
public class DegreeMod extends View {

    private Context mContext;
    Paint mPaint;
    Rect locationRect;
    // setting these heuristically.
    private int textSize = 50;
    private int width = textSize;
    private int height = textSize + 25;
    private int halfTextSize = textSize/2;

    private int x, y;
    private boolean bRefresh = false;

    Paint mPaint1;
    private HudView mHudView;
    private String temp = "72";

    private SettingsManager sm;
    private boolean refresh = true;


    public DegreeMod(Context context, HudView mHudView) {
        super(context);

        this.mContext = context;
        this.mHudView = mHudView;
        if(!mHudView.isRound())x = mContext.getWallpaperDesiredMinimumWidth()/2;
        else x = mContext.getWallpaperDesiredMinimumWidth()/2 - (halfTextSize/2);

        y = mContext.getWallpaperDesiredMinimumWidth();
        locationRect = new Rect(x-halfTextSize, y-height,x+width, y);
        mPaint = new Paint();
        mPaint.setDither(false);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(getResources().getColor(R.color.white));

        // testing purposes delete after
        mPaint1 = new Paint();
        mPaint1.setDither(false);
        mPaint1.setAntiAlias(true);
        mPaint1.setTextSize(textSize);
        mPaint1.setColor(getResources().getColor(R.color.green));

        // set the current temperature - not sure if this will work.
        sm = new SettingsManager(mContext);
        int numTemp = sm.getIntFromPreferences(Consts.DEGREE_REFRESH);
        if(numTemp != 0)
            temp = Integer.toString(numTemp);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        //canvas.drawRect(locationRect,mPaint1);
        canvas.drawText(temp+ Consts.DEGREE_SIGN,x-halfTextSize,y-30,mPaint);

    }

    public boolean touchInside(float x, float y){
        if(!locationRect.contains((int)x,(int)y)) return false;
        else {
            log("touch is inside");
            // Create a DataMap object and send it to the data layer
            DataMap dataMap = new DataMap();
            refreshDegrees(dataMap);
            return true;
        }
    }
    // Lets tell HudView to send out a message to the dataLayer
    private void refreshDegrees(DataMap dataMap) {
        refresh = true;
        if(bRefresh == true){
            bRefresh = false;
        }else if(bRefresh == false){
            bRefresh = true;
        }
        //bRefresh = !bRefresh;
        log("refresh = " + bRefresh);
        /*Pack true to refresh degrees */
        dataMap.putBoolean(Consts.DEGREE_REFRESH,
                bRefresh);
        mHudView.refreshDegrees(dataMap);
    }

    public DegreeMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DegreeMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DegreeMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void log(String s){
        Log.d("DegreeMod", s);
    }

    /**
     *
     * Called by HudView to refresh
     * we were crtashing when we tried to update and the hud wasn't displayed, the method below reset
     * coupled wirth the boolean Allows us to avboid that.
     */
    public void resetTemp() {
        log("resetTemp");
       // if(refresh == true){
            int numTemp = sm.getIntFromPreferences(Consts.DEGREE_REFRESH);
            if(numTemp != 0)
                temp = Integer.toString(numTemp);
            invalidate();
      //  }
    }

    public void cancelDisplayRefresh() {
        refresh = false;
    }
}
