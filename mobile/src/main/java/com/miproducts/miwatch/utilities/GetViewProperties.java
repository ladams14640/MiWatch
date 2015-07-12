package com.miproducts.miwatch.utilities;

import android.graphics.Point;
import android.util.Log;

import com.miproducts.miwatch.mods.DateViews;
import com.miproducts.miwatch.mods.DegreeMod;
import com.miproducts.miwatch.mods.DigitalTimer;
import com.miproducts.miwatch.mods.EventMod;
import com.miproducts.miwatch.mods.FitnessMod;
import com.miproducts.miwatch.mods.TimerView;

/**
 * Created by ladam_000 on 7/11/2015.
 */
public class GetViewProperties {


    // mods
    private DigitalTimer mDigitalTimer;
    private EventMod mEventMod;
    private FitnessMod mFitnessMod;
    private DegreeMod mDegreeMod;
    private DateViews mDateViews;
    private TimerView mTimerView;


    public GetViewProperties(DigitalTimer mDigitalTimer, EventMod mEventMod,
                             FitnessMod mFitnessMod, DegreeMod mDegreeMod, DateViews mDateViews, TimerView mTimerView) {

        this.mDigitalTimer = mDigitalTimer;
        this.mEventMod =  mEventMod;
        this.mFitnessMod = mFitnessMod;
        this.mDegreeMod = mDegreeMod;
        this.mDateViews = mDateViews;
        this.mTimerView = mTimerView;
    }

    public float getSizeOfView(int selectedView) {
        switch(selectedView) {
            case Consts.DIGITAL_TIMER:
                return mDigitalTimer.getSize();
            case Consts.FITNESS:
                return mFitnessMod.getSize();
            case Consts.EVENT:
                return mEventMod.getSize();
            case Consts.DATE:
                return mDateViews.getSize();
            case Consts.ALARM_TIMER:
                return mTimerView.getSize();
            case Consts.DEGREE:
                return mDegreeMod.getSize();
        }
        return 0;
    }



    public int getColorOfView(int selectedView){
        switch(selectedView) {
            case Consts.NONE:
                break;
            case Consts.DIGITAL_TIMER:
                return mDigitalTimer.getColor();
            case Consts.FITNESS:
                return mFitnessMod.getColor();
            case Consts.EVENT:
                return mEventMod.getColor();
            case Consts.DATE:
                return mDateViews.getColor();
            case Consts.ALARM_TIMER:
                return mTimerView.getColor();
            case Consts.DEGREE:
                return mDegreeMod.getColor();
            default:
                return 0;
        }
        return 0;
    }

    public Point getPositionOfView(int selectedView){
        Point pointOfLocation = new Point();
        switch(selectedView){
            case Consts.NONE:
                break;
            case Consts.DIGITAL_TIMER:
                pointOfLocation.set((int) mDigitalTimer.getX(), (int) mDigitalTimer.getY());
                break;
            case Consts.FITNESS:
                pointOfLocation.set((int) mFitnessMod.getX(), (int) mFitnessMod.getY());
                break;
            case Consts.EVENT:
                pointOfLocation.set((int) mEventMod.getX(), (int) mEventMod.getY());
                break;
            case Consts.DATE:
                pointOfLocation.set((int) mDateViews.getX(), (int) mDateViews.getY());
                break;
            case Consts.ALARM_TIMER:
                pointOfLocation.set((int) mTimerView.getX(), (int) mTimerView.getY());
                break;
            case Consts.DEGREE:
                pointOfLocation.set((int)mDegreeMod.getX(),(int) mDegreeMod.getY());
                break;
        }
        return pointOfLocation;
    }

    public boolean getVisibilityOfView(int selectedView) {
        switch (selectedView) {
            case Consts.NONE:
                return false;
            case Consts.DIGITAL_TIMER:
                return mTimerView.getViewsVisibility();
            case Consts.FITNESS:
                return mFitnessMod.getViewsVisibility();
            case Consts.EVENT:
                return mEventMod.getViewsVisibility();
            case Consts.DATE:
                return mDateViews.getViewsVisibility();
            case Consts.ALARM_TIMER:
                return mTimerView.getViewsVisibility();
            case Consts.DEGREE:
                return mDegreeMod.getViewsVisibility();
        }
        return false;
    }
    /**
     * Tell the appropriate view to change it's size
     * @param newSize - new size for the view
     * @param selectedView - the view that we want to manipulate.
     */
    public void changeViewSize(int newSize, int selectedView) {
        log("changeViewSize, new size = " + newSize);
        switch(selectedView) {
            case Consts.NONE:
                break;
            case Consts.DIGITAL_TIMER:
                mDigitalTimer.changeSize(newSize);
                break;
            case Consts.FITNESS:
                mFitnessMod.changeSize(newSize);
                break;
            case Consts.EVENT:
                mEventMod.changeSize(newSize);
                break;
            case Consts.DATE:
                mDateViews.changeSize(newSize);
                break;
            case Consts.ALARM_TIMER:
                mTimerView.changeSize(newSize);
                break;
            case Consts.DEGREE:
                mDegreeMod.changeSize(newSize);
                break;
        }
    }














private void log(String s){    Log.d("GetViewProperties", s);}





}
