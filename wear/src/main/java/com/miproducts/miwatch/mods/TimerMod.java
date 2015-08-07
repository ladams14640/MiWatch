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
    private HudView mHudView;

    public TimerMod(Context context, HudView hudView) {
        super(context);
        log("init");
        this.mContext = context;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        bTimer = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_timer_white,options);
        this.mHudView = hudView;

        if(!mHudView.isRound()) x = mContext.getWallpaperDesiredMinimumWidth()/20;
        else x = mContext.getWallpaperDesiredMinimumWidth()/8;

        y = mContext.getWallpaperDesiredMinimumHeight()-height;

        locationRect = new Rect(x, y,x+width, y+height);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(false);
        mPaint.setDither(true);

        //TODO testing this filter, havent seen if it works 7/15/15. will check out tonight.
        mPaint.setFlags(Paint.FILTER_BITMAP_FLAG);

        bResizedTimer = BitmapConverter.getResizedBitmap(bTimer, width, height);

/*
        bResizedTimer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float ratioX = width / (float) bTimer.getWidth();
        float ratioY = height / (float) bTimer.getHeight();
        float middleX = width / 2.0f;
        float middleY = height / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(bResizedTimer);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bTimer, middleX - bTimer.getWidth() / 2, middleY - bTimer.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG))
   */
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(bResizedTimer, x, y, mPaint);
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
