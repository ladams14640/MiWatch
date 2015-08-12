package com.miproducts.miwatch.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.utilities.Consts;
    //TODO July 31 2015 - Continue with the last 3 mods, 
/**
 *
 * We will be using this Mod to customize which Mod is being displayed to the user and where.
 * This will be like a place holder in the configuration screen, that will allow the user to do 2
 * things.
 * 1. Change the color scheme of watchface
 * 2. change the individual Mod showing / change which mod is showing where.
 *
 * Created by ladam_000 on 7/19/2015.
 */
public class PickingMod extends Mods implements CustomizedMods {

    // rect paint
    Paint rectPaint;
    // border of the Mod
    private Rect rectBorder;
    // the rectangle border input.
    private float leftBorder, topBorder, rightBorder, botBorder;
    private int width = 60, height = 60;
    private int ID = 0;




    // incase an ID of timer is passed
    DigitalTimer mDigitalTimer;
    // incase an ID of date is passed
   // DateViews mDate;
    // incase an ID of Event is passed
    EventView mEvent;

    /**
     *
     * @param context - Context
     * @param svView - SurfaceView
     * @param leftBorder - left side
     * @param topBorder - right side
     * @param initialID - ID
     */
    public PickingMod(Context context, WatchFaceSurfaceViewConfig svView,
                      float leftBorder, float topBorder, int initialID) {
        super(context, svView);

        // set the rect perimeter values.
        this.leftBorder = leftBorder;
        this.topBorder = topBorder;
        this.rightBorder = leftBorder + width;
        this.botBorder = topBorder + height;

        this.ID = initialID;

        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setColor(getResources().getColor(R.color.green_select));

        if(ID == Consts.DIGITAL_TIMER){
            mDigitalTimer = new DigitalTimer(getContext(),svView, leftBorder, topBorder);
        }
        else if(ID == Consts.DATE){
          //  mDate = new DateViews(getContext(),svView);
        }
        else if(ID == Consts.EVENT){
            mEvent = new EventView(getContext(), svView, leftBorder, topBorder);
        }

        drawRectBorder();
    }


    // Draw the rect that surrounds the Mod
    private void drawRectBorder(){

        rectBorder = new Rect(
                (int)(leftBorder),
                (int)topBorder,
                (int)rightBorder,
                (int)botBorder);
    }


    @Override
    public void draw(Canvas canvas) {


        // special case for digital time
        if(ID == Consts.DIGITAL_TIMER){
            mDigitalTimer.draw(canvas);
        }else if(ID == Consts.EVENT){
            // no rect, it will have a special rectangle
            mEvent.draw(canvas);
        }
        // normal case, lets draw the rectangle and have the mod draw itself.
        else {
            canvas.drawRect(rectBorder, rectPaint);

        }

        super.draw(canvas);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void changeSize(int newSize) {

    }

    @Override
    public void setVisibility(boolean visible) {

    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public boolean getViewsVisibility() {
        return false;
    }
    public PickingMod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PickingMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PickingMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
