package com.miproducts.miwatch.mods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.hud.HudView;
import com.miproducts.miwatch.utilities.BitmapConverter;

/**
 * Created by larry on 6/29/15.
 */
public class FitnessMod extends ImageView {
    private Context mContext;
    private Bitmap bFitness;
    private Bitmap bResizeFitness;

    Rect locationRect;
    RectF outerSizeRingRect;
    RectF innerSizeRingRect;
    RectF innerInnerSizeRingRect;

    private int width = 100;
    private int height = 100;

    private int outerHeight = height/2;
    private int outerWidth = width/2;


    private int x, y;
    private Paint mPaint;
    private HudView mHudView;

    // delete after
    Paint mPaintTest;

    public FitnessMod(Context context, HudView hudView) {
        super(context);
        mContext = context;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bFitness = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_fitness_white,options);



        this.mHudView = hudView;

        if(!mHudView.isRound()) x = mContext.getWallpaperDesiredMinimumWidth() - width;
        else x = mContext.getWallpaperDesiredMinimumWidth() - (int)(width * 1.40);

        y = mContext.getWallpaperDesiredMinimumWidth() - height;

        bResizeFitness = BitmapConverter.getResizedBitmap(bFitness, width, height);
        locationRect = new Rect(x, y,x+width, y+height);

        outerSizeRingRect = new RectF(x+(outerWidth/2), // 25
                y+(outerHeight/2),  // 25
                x+(int)(width*.75),// 75
                y+(int)(height*.75)); // 75
        innerSizeRingRect = new RectF(x + (int)((outerWidth/2) + 8), y+(int)((outerHeight/2) + 8), x+(int)((width*.75) - 8), y+(int)((height*.75) - 8));
        innerInnerSizeRingRect = new RectF(x + (int)((outerWidth/2) + 14), y+(int)((outerHeight/2) + 14), x+(int)((width*.75) - 14), y+(int)((height*.75) - 14));


        mPaint = new Paint();

        // Settings
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(false);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setStyle(Paint.Style.STROKE);
        //setImageBitmap(bResizeFitness);
        mPaintTest = new Paint();
        mPaintTest.setStyle(Paint.Style.FILL);
        mPaintTest.setColor(getResources().getColor(R.color.blue));
    }


    public boolean touchInside(float x, float y){
        if(!locationRect.contains((int)x,(int)y)) return false;
        else {
            log("touch is inside");
            Intent i = mContext.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.fitness");

            mContext.startActivity(i);
            return true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawBitmap(bResizeFitness, x, y, null);
       // canvas.drawRect(locationRect, mPaintTest);
        canvas.drawArc(outerSizeRingRect,270,300,false, mPaint);
        canvas.drawArc(innerSizeRingRect, 270,240,false, mPaint);
        canvas.drawArc(innerInnerSizeRingRect, 270,180, false, mPaint);
        super.draw(canvas);

    }


    public void log(String s){
        Log.d("FitnessMod", s);
    }
}
