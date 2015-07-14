package com.miproducts.miwatch.mods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
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
    Paint mPaint;

    Bitmap bTimer;
    Bitmap bResizedTimer;
    Rect locationRect;
    private int width = 100;
    private int height = 100;
    private int x, y;

    public TimerMod(Context context) {
        super(context);
        log("init");
        this.mContext = context;
        bTimer = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_timer_white);

        x = mContext.getWallpaperDesiredMinimumWidth()/20;

        y = mContext.getWallpaperDesiredMinimumWidth()-20;

        bResizedTimer = BitmapConverter.getResizedBitmap(bTimer, width, height);
        locationRect = new Rect(x, y,x+width, y+height);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(bTimer, x, y, null);
    }

    public TimerMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
