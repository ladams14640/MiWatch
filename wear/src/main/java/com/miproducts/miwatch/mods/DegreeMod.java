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

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.BitmapConverter;
import com.miproducts.miwatch.utilities.Consts;

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

    Paint mPaint1;

    public DegreeMod(Context context) {
        super(context);

        this.mContext = context;

        x = mContext.getWallpaperDesiredMinimumWidth()/2;
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
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        //canvas.drawRect(locationRect,mPaint1);
        canvas.drawText("72"+ Consts.DEGREE,x-halfTextSize,y-30,mPaint);

    }

    public boolean touchInside(float x, float y){
        if(!locationRect.contains((int)x,(int)y)) return false;
        else {
            log("touch is inside");

            return true;
        }
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
}
