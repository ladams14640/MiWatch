package com.miproducts.miwatch.mods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.hud.HudView;
import com.miproducts.miwatch.utilities.BitmapConverter;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by ladam_000 on 6/28/2015.
 */
public class TimerMod extends View{
    private Context mContext;
    Paint mPaintOutline;
    Paint mPaintQuarterCircles;

    Rect locationRect;
    RectF outerSizeRingRect;

    private int width = 100;
    private int height = 100;
    private int halfWidth = width/2;
    private int halfHeight = height/2;

    private int x, y;
    private HudView mHudView;

    public TimerMod(Context context, HudView hudView) {
        super(context);
        log("init");
        this.mContext = context;

        this.mHudView = hudView;

        if(!mHudView.isRound()) x = mContext.getWallpaperDesiredMinimumWidth()/20;
        else x = mContext.getWallpaperDesiredMinimumWidth()/8;

        y = mContext.getWallpaperDesiredMinimumHeight()-height;

        locationRect = new Rect(x, y,x+width, y+height);

        mPaintOutline = new Paint();
        mPaintOutline.setAntiAlias(true);
        mPaintOutline.setStrokeWidth(3);
        mPaintOutline.setColor(getResources().getColor(R.color.white));
        mPaintOutline.setStyle(Paint.Style.STROKE);

        mPaintQuarterCircles = new Paint();
        mPaintQuarterCircles.setAntiAlias(true);
        mPaintQuarterCircles.setStrokeWidth(3);
        mPaintQuarterCircles.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintQuarterCircles.setColor(getResources().getColor(R.color.white));

        outerSizeRingRect = new RectF(x+(int)(halfWidth/2), // 25
                y+(int)(halfHeight/2),  // 25
                x+(int)(width*.75),// 75
                y+(int)(height*.75)); // 75
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(outerSizeRingRect, 270, 300, false, mPaintOutline);
        //TODO refactor the sizes n positions.
        int circleSize = 1;
        // right
        canvas.drawCircle(x + (int) (width * .75)-5, y+halfHeight,circleSize,mPaintQuarterCircles);
        // bot
        canvas.drawCircle(x + halfWidth, y+(int)(height*.75)-5,circleSize,mPaintQuarterCircles);
        // left
        canvas.drawCircle(x + (halfWidth/2) + 5, y+halfHeight,circleSize,mPaintQuarterCircles);
        // top
        canvas.drawCircle(x + (int)(halfWidth), y +(int)(halfHeight/2) + 5, circleSize, mPaintQuarterCircles);

        super.draw(canvas);
    }

    public boolean touchInside(float x, float y){
        if(!locationRect.contains((int)x,(int)y)) return false;
        else {
            log("touch is inside");
            Intent k = mContext.getPackageManager().getLaunchIntentForPackage("com.miproducts.mitimer");
            mContext.startActivity(k);
            return true;
        }
    }
    public void log(String s){
        Log.d("TimerMod", s);
    }

}
