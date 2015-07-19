package com.miproducts.miwatch.config;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by ladam_000 on 7/5/2015.
 */
public class DateViews extends Mods implements CustomizedMods{
    private static final int ID = Consts.DATE;
    @Override
    public int getId() {
        return ID;
    }

    private static final String TAG = "DateViews";

    private WatchFaceSurfaceViewConfig svView;
    private Context mContext;

    private int x, y;
    private String dateOfWeek = "Sun";
    private String dateOfMonth = "05";

    private Paint pDateOfWeek;
    private Paint pDateOfMonth;
    private Paint pRect;

    private int textSize = 25;

    private int width = textSize * 2;
    private int height = textSize * 2;

    private Rect mSelectRect;
    private int currentColor;
    private boolean isVisible = true;


    public int getSize(){
        return textSize;
    }

    public DateViews(Context context, WatchFaceSurfaceViewConfig svView) {
        super(context, svView);
        this.svView = svView;
        this.mContext = context;
        initPositions();
        initPaint();
        setRectangle();
    }

    private void initPositions() {
        x = getContext().getWallpaperDesiredMinimumWidth() / 5;
        y = 115;


    }

    private void setRectangle(){
        mSelectRect = new Rect(
                x - (width/2),
                y - (height/2),
                x + (width/2),
                y+ (height/2));
    }


    // finger location while moving.
    float xMove, yMove;
    boolean isDragging = false;

    public boolean touchInside(MotionEvent event){
        // if we are dragging no need to check if we are within the square, just drag it.
        if(!isDragging) {
            if (!mSelectRect.contains((int) event.getX(), (int) event.getY())){
                isDragging = false;
                return false;
            } else {
                isDragging = true;
            }
        }

        // we are now dragging and lets move this shit.
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
              //  svView.setSelection(Consts.DATE, true);
               // svView.viewIsDragging(true);
                selectPaint();
                setRectangle();

                return true;

            case MotionEvent.ACTION_MOVE:
                 x = (int)(event.getX()-(width/2));
                // make sure we do not go over the hud - 6 is a heuristic number I came to.
                if((int) (event.getY()-(height/6)) < Consts.yHudPosition){
                    y = (int) (event.getY()-(height/2));
                }
                setRectangle();
                return true;

            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
               // svView.viewIsDragging(false);

                log("xPosition = " + x);
                log("yPosition = " + y);
                yMove = 0;
                xMove = 0;
                return true;

        }
        return true;

    }


    private void initPaint() {
        Resources resources = getResources();
        pDateOfWeek = new Paint();
        pDateOfWeek.setColor(resources.getColor(R.color.digital_time_blue));
        pDateOfWeek.setTextSize(resources.getDimension(R.dimen.digital_text_size));
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
        canvas.drawText(dateOfWeek, x, y, pDateOfWeek);
        canvas.drawText(dateOfMonth, x, y + textSize, pDateOfMonth);
       // canvas.drawRect(mSelectRect, pRect);



    }

    private void log(String s){
        Log.d(TAG, s);
    }

    public DateViews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DateViews(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void selectPaint() {
        pRect.setColor(getResources().getColor(android.R.color.holo_orange_dark));
    }
    @Override
    public void unSelectPaint() {
        pRect.setColor(getResources().getColor(R.color.digital_time_blue));

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
}