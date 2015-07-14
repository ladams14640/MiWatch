package com.miproducts.miwatch.mods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.hud.HudView;
import com.miproducts.miwatch.utilities.BitmapConverter;
import com.miproducts.miwatch.utilities.ConverterUtil;

/**
 * Created by larry on 6/29/15.
 */
public class FitnessMod extends ImageView {
    private Context mContext;
    private Bitmap bFitness;
    private Bitmap bResizeFitness;

    Rect locationRect;
    private int width = 100;
    private int height = 100;
    private int x, y;
    private Paint mPaint;
    private HudView mHudView;


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
        mPaint = new Paint();

        // Settings
        mPaint.setAntiAlias(false);
        mPaint.setFilterBitmap(false);
        mPaint.setDither(true);
        //setImageBitmap(bResizeFitness);
    }


    public boolean touchInside(float x, float y){
        if(!locationRect.contains((int)x,(int)y)) return false;
        else {
            log("touch is inside");
           // Intent i = mContext.getPackageManager().getLaunchIntentForPackage("com.asus.wellness");
            Intent i = mContext.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.fitness");

            mContext.startActivity(i);
            return true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bResizeFitness, x, y, null);
        super.draw(canvas);

    }


    public void log(String s){
        Log.d("FitnessMod", s);
    }
}
