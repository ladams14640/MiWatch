package com.miproducts.miwatch.mods;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.miproducts.miwatch.MiDigitalWatchFace;
import com.miproducts.miwatch.R;
import com.miproducts.miwatch.config.CustomizedMods;

import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.ModPositionFunctions;

/**
 * Created by ladam_000 on 7/19/2015.
 */
public class DateMod extends View implements CustomizedMods{


    private static final int ID = Consts.DATE;
    @Override
    public int getId() {
        return ID;
    }

    private static final String TAG = "DateViews";

    private MiDigitalWatchFace svView;
    private Context mContext;

    private int x, y;

    private Paint pDateOfWeek;
    private Paint pDateOfMonth;
    private Paint pRect;

    private int textSize = 25;

    private int width = textSize * 2;
    private int height = textSize * 2;

    private Rect mSelectRect;
    private int currentColor;
    private boolean isVisible = true;

    private String dayOfMonth, dayOfWeek;

    public int getSize(){
        return textSize;
    }

    public DateMod(Context context, MiDigitalWatchFace svView, String dayOfMonth, String dayOfWeek) {
        super(context);
        this.svView = svView;
        this.mContext = context;
        initPositions();
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        initPaint();
        setRectangle();
    }

    private void initPositions() {
        x = mContext.getWallpaperDesiredMinimumWidth() / 5;
        y = ModPositionFunctions.getTopTimerPosition(mContext.getWallpaperDesiredMinimumWidth());


    }

    private void setRectangle(){
        mSelectRect = new Rect(
                x - (width/2),
                y - (height/2),
                x + (width/2),
                y+ (height/2));
    }


    private void initPaint() {
        Resources resources = getResources();
        pDateOfWeek = new Paint();
        pDateOfWeek.setColor(resources.getColor(R.color.digital_time_blue));
        pDateOfWeek.setTextSize(textSize);
        pDateOfWeek.setTextAlign(Paint.Align.CENTER);



        pDateOfMonth = new Paint();
        pDateOfMonth.setColor(resources.getColor(R.color.white));
        pDateOfMonth.setTextSize(textSize);
        pDateOfMonth.setTextAlign(Paint.Align.CENTER);

        pRect = new Paint();
        pRect.setStyle(Paint.Style.STROKE);
        pRect.setColor(resources.getColor(R.color.digital_time_blue));

        currentColor = getResources().getColor(R.color.digital_time_blue);

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawText(dayOfWeek, x, y - textSize, pDateOfWeek);
        canvas.drawText(dayOfMonth, x, y, pDateOfMonth);
        // canvas.drawRect(mSelectRect, pRect);



    }

    private void log(String s){
        Log.d(TAG, s);
    }

    public DateMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void selectPaint() {
        pRect.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    public void changeSize(int newSize) {
        log("changed Size to " + newSize);
        textSize = newSize;
        refreshValues();
        //refresh rectangle
        setRectangle();
        invalidate();
    }

    private void refreshValues() {
        width = textSize * 2;
        height = textSize * 2;
        pDateOfWeek.setTextSize(textSize);
        pDateOfMonth.setTextSize(textSize);
        selectPaint();
    }
    public void setVisibility(boolean visible){
        isVisible = visible;
    }
    public float getX(){
        log("get X = " + x);
        return  x;
    }
    public float getY(){
        log("get Y =  " + y);
        return y;
    }


    public int getColor() {
        return currentColor;
    }

    public boolean getViewsVisibility() {
        return isVisible;
    }
    public void updateDate(String dayOfMonth, String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
    }
}
