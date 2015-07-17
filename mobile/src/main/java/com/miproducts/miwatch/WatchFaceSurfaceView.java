package com.miproducts.miwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miproducts.miwatch.mods.DateViews;
import com.miproducts.miwatch.mods.DegreeMod;
import com.miproducts.miwatch.mods.DigitalTimer;
import com.miproducts.miwatch.mods.EventMod;
import com.miproducts.miwatch.mods.FitnessMod;
import com.miproducts.miwatch.mods.Mods;
import com.miproducts.miwatch.mods.TimerView;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.GetViewProperties;

/**
 * Created by larry on 7/2/15.
 */
public class WatchFaceSurfaceView extends SurfaceView implements View.OnTouchListener {
    private final static String TAG = "WatchFaceSurfaceView";

    // thread and holder
    private SurfaceHolder surfaceHolder;
    private SurfaceThread stThread;

    // mods
    private DigitalTimer mDigitalTimer;
    private EventMod mEventMod;
    private FitnessMod mFitnessMod;
    private DegreeMod mDegreeMod;
    private DateViews mDateViews;
    private TimerView mTimerView;

    private GetViewProperties mGetProperties;

    private int currentlySelectedView = Consts.NONE;

    // are we dragging a view
    private boolean isViewDragging = false;

    private int width = 600, height = 600;

    private Mods[] lViews = new Mods[6];

    public WatchFaceSurfaceView(Context context) {
        super(context);
       init();
    }

    private void init() {
       // log("init");

        stThread = new SurfaceThread(this);

        setOnTouchListener(this);
        surfaceHolder = getHolder();

        initMods();
        mGetProperties = new GetViewProperties(
                    mDigitalTimer,
                    mEventMod,
                    mFitnessMod,
                    mDegreeMod,
                    mDateViews,
                    mTimerView);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                stThread.passHolder(holder);
                //  log("surfaecHolder callback surface created");
                //stThread.setRunning(true);
                //stThread.start(); // handle this inside the thread now.

            }

            /**
             * Everytime This surfaceChange happens it's after the screen pops up and is visible it seems.
             * So whether from scratch or from unpause this method is called only when surface becomes visible.
             * Does not get called when it hides.
             * @param holder
             * @param format
             * @param width
             * @param height
             */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                log("changed");
                // incase we come back from a paused state.
                //if (isShowing)

                //stThread.passHolder(holder);

                stThread.setRunning(true);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stThread.setRunning(false);
            }
        });

    }


    public float getCanvasWidth(){
        return width;
    }

    public float getCanvasHeight(){
        return height;
    }

    private void initMods() {
        mDigitalTimer = new DigitalTimer(getContext(), this);
        mEventMod = new EventMod(getContext(), this);
        mFitnessMod = new FitnessMod(getContext(), this);
        mDegreeMod = new DegreeMod(getContext(), this);
        mDateViews = new DateViews(getContext(), this);
        mTimerView = new TimerView(getContext(), this);

        lViews[0] = mDigitalTimer;
        lViews[1] = mEventMod;
        lViews[2] = mFitnessMod;
        lViews[3] = mDegreeMod;
        lViews[4] = mDateViews;
        lViews[5] = mTimerView;

    }

    protected void drawSomething(Canvas canvas) {
            if(canvas != null) {
                canvas.drawColor(Color.BLACK);
                for(int i = 0; i < lViews.length; i++){
                    lViews[i].draw(canvas);
                }
            }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       // log("touched me.");
        // we are dragging
        if(isViewDragging){
            // we have selected something, handle the movement according.
            switch(currentlySelectedView){
                // essentially initial press will go here.
                case Consts.NONE:
                    for(int i = 0; i < lViews.length; i++){
                        lViews[i].touchInside(event);
                    }

                    break;
                case Consts.DIGITAL_TIMER:
                    mDigitalTimer.touchInside(event);
                    break;
                case Consts.FITNESS:
                    mFitnessMod.touchInside(event);
                    break;
                case Consts.EVENT:
                    mEventMod.touchInside(event);
                    break;
                case Consts.DATE:
                    mDateViews.touchInside(event);
                    break;
                case Consts.ALARM_TIMER:
                    mTimerView.touchInside(event);
                    break;
                case Consts.DEGREE:
                    mDegreeMod.touchInside(event);
                    break;

            }
        }
        // we weren't dragging anything, but something could be still selected
        else {

            for(int i = 0; i < lViews.length; i++){
                lViews[i].touchInside(event);
            }
        }
        return true;
    }

    private void resetViewsToUnselected() {
        for(int i = 0; i < lViews.length; i++){
                lViews[i].unSelectPaint();
        }
    }

    private void resetViewsButCurrent(int selection) {
        for(int i = 0; i < lViews.length; i++){
            if(lViews[i].getId() != selection){
                log("View with ID was not the selected one.: " + lViews[i].getId());
                lViews[i].unSelectPaint();
            }
        }

    }


    public float getCanvasX(){
     //   log("getCanvasX = " + ((int) getCanvasWidth() - getX()));
        return getCanvasWidth() - getX();
    }


    public WatchFaceSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WatchFaceSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public WatchFaceSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void log(String s) {
        Log.d(TAG, s);
        //init();
    }

    /**
     * Called by the Individual Views when one has been selected.
     *
     * @param selection - Constant that decides what view is selected
     * @param b - whether it is selected or not
     */
    public void setSelection(int selection, boolean b) {
        if(b == false){
            resetViewsButCurrent(selection);
            currentlySelectedView = Consts.NONE;
            // Tell the MainActivity, so he can adjust the Menu's values
            ((MiDigitalWatchFaceCompanionConfigActivity) getContext()).setMenuSelection(Consts.NONE);
        }else {
            // Tell the MainActivity, so he can adjust the Menu's values
            currentlySelectedView = selection;
            resetViewsButCurrent(selection);
            ((MiDigitalWatchFaceCompanionConfigActivity) getContext()).setMenuSelection(currentlySelectedView);
        }
    }


    /**
     * Tell us if we are dragging a view or if one is just selected.
     * @param b
     */
    public void viewIsDragging(boolean b) {
        isViewDragging = b;
    }

    // action on view
    public void changeViewSize(int newSize, int selectedView){ mGetProperties.changeViewSize(newSize, selectedView);}
    public void setViewsVisibility(int selectedView, boolean isChecked) { mGetProperties.changeViewsVisibility(selectedView, isChecked); }

    // Get Properties from the individual Views
    public boolean getVisibilityOfView(int selectedView) {return mGetProperties.getVisibilityOfView(selectedView);}
    public Point getPositionOfView(int selectedView){return mGetProperties.getPositionOfView(selectedView);}
    public int getColorOfView(int selectedView){return mGetProperties.getColorOfView(selectedView);}
    public float getSizeOfView(int selectedView) { return mGetProperties.getSizeOfView(selectedView);}


    //TODO will this fix it? Thread igivng me trouble when i pause the damn application.
    public void threadRun(boolean b) {
        stThread.setRunning(b);
    }


}
