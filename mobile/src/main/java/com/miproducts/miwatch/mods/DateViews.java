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

import com.miproducts.miwatch.R;
import com.miproducts.miwatch.WatchFaceSurfaceView;
import com.miproducts.miwatch.utilities.Consts;

/**
 * Created by ladam_000 on 7/5/2015.
 */
public class DateViews extends View {
    private static final String TAG = "DateViews";

    private WatchFaceSurfaceView svView;
    private Context mContext;

    private int x, y;
    private String dateOfWeek = "Sun";
    private String dateOfMonth = "05";

    private Paint pDateOfWeek;
    private Paint pDateOfMonth;
    private Paint pRect;

    private int textSize = 50;

    private int width = textSize * 2;
    private int height = textSize * 2;

    private Rect mSelectRect;
    private int currentColor;

    public int getSize(){
        return textSize;
    }

    public DateViews(Context context, WatchFaceSurfaceView svView) {
        super(context);
        this.svView = svView;
        this.mContext = context;
        initPositions();
        initPaint();
        setRectangle();
    }



    // finger location while moving.
    float xMove, yMove;
    boolean isDragging = false;
    public boolean touchInside(MotionEvent event){
        // if we are dragging no need to check if we are within the square, just drag it.
        if(!isDragging) {
            if (!mSelectRect.contains((int) event.getX(), (int) event.getY())) {
                isDragging = false;
                return false;
            } else {
                isDragging = true;
            }
        }

        // we are now dragging and lets move this shit.
       // log("touch is inside");
        // touch has come in

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //log("down");
                //x = (int)(event.getX()-(width/2));
                //y = (int) (event.getY()-(height/2));

                svView.setSelection(Consts.DATE, true);
                svView.viewIsDragging(true);
                selectPaint();
                setRectangle();

                return true;

            case MotionEvent.ACTION_MOVE:
                // no adjustments if we are animating
                //log("moving");
                //log("moving the text view");
                //log("moving the text view");
                x = (int)(event.getX()-(width/2));
                y = (int) (event.getY()-(height/2));
                setRectangle();
                return true;
            // no need to call finger off if we are aniamting, animating, because of ACTION_MOVE
            case MotionEvent.ACTION_UP:
                isDragging = false;
                // unselected
                //unselectPaint();
                //svView.setSelection(svView.DATE, false);
                svView.viewIsDragging(false);

                log("xPosition = " + x);
                log("yPosition = " + y);
                yMove = 0;
                xMove = 0;
                return true;

        }
        return true;

    }




    private void initPositions() {
        x = Consts.xDatePosition;
        y = Consts.yTimePosition;
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
        canvas.drawText(dateOfWeek, x, y, pDateOfWeek);
        canvas.drawText(dateOfMonth, x, y + textSize, pDateOfMonth);
        canvas.drawRect(mSelectRect, pRect);



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

    public void unselectPaint() {
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

    public float getX(){
        log("get X = " + (svView.getCanvasX()- x));
        return (int)(svView.getCanvasX()- x);
    }
    public float getY(){
        return y - svView.getY();
    }


    public int getColor() {
        return currentColor;
    }
}
