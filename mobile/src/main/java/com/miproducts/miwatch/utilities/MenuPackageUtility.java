package com.miproducts.miwatch.utilities;

import android.graphics.Point;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.miproducts.miwatch.MiDigitalWatchFaceCompanionConfigActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class is used by the WatchFaceMenu.
 * It will Package up all of the Properties for the Mods and get them ready to be taken
 * off with a Thread to the node for the MiDigitalWatchFaceConfigListenerService for the watchface.
 * Created by ladam_000 on 7/11/2015.
 */
public class MenuPackageUtility {
    private final static String TAG = "MenuPackageUtility";



    private MiDigitalWatchFaceCompanionConfigActivity mActivity;

    public MenuPackageUtility(MiDigitalWatchFaceCompanionConfigActivity mActivity) {
        this.mActivity = mActivity;
    }


    public void handleAllPackaging(DataMap dataMap) {
        handleDigitalPackage(dataMap);
        handleDatePackage(dataMap);
        handleAlarmPackage(dataMap);
        handleDegreePackage(dataMap);
        handleFitnessPackage(dataMap);
        handleEventPackage(dataMap);
    }


    public void handleDigitalPackage(DataMap dataMap) {
        // get location for the position pack
        Point digitPoint = mActivity.getViewsPosition(Consts.DIGITAL_TIMER);
        List<Integer> digitalPointsArray = new ArrayList<Integer>();
        digitalPointsArray.add(digitPoint.x);
        digitalPointsArray.add(digitPoint.y);

        //for(int i = 0; i < digitalPointsArray.size(); i++){
         //log("digitalPointsArray we got from the getViewsPosition is = " + digitalPointsArray.get(i));
         //}

        /* Pack color color values */
        int colorChoice = mActivity.getSelectedViewsColor(Consts.DIGITAL_TIMER);
        dataMap.putInt(Consts.DIGITAL_TIMER_COLOR_API, colorChoice);

        // Pack the position values
        dataMap.putIntegerArrayList(Consts.DIGITAL_TIMER_POS_API,
                (ArrayList<Integer>) digitalPointsArray);

        // Pack the visibility
        boolean visible = mActivity.getViewsVisibility(Consts.DIGITAL_TIMER);
        // Pack the size


    }

    public void handleDatePackage(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.DATE);
        List<Integer> DatePointsArray = new ArrayList<Integer>();
        DatePointsArray.add(point.x);
        DatePointsArray.add(point.y);

        //for(int i = 0; i < DatePointsArray.size(); i++){
        //    log("DATE we got from the getViewsPosition is = " + DatePointsArray.get(i));
        // }

        //

        /*Pack positions*/
        dataMap.putIntegerArrayList(Consts.DATE_POS_API,
                (ArrayList<Integer>) DatePointsArray);
        /* Pack color color values */
        int colorChoice = mActivity.getSelectedViewsColor(Consts.DATE);
        dataMap.putInt(Consts.DATE_COLOR_API, colorChoice);
    }


    public void handleFitnessPackage(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.FITNESS);
        List<Integer> FitnessPointsArray = new ArrayList<Integer>();
        FitnessPointsArray.add(point.x);
        FitnessPointsArray.add(point.y);

        //for(int i = 0; i < FitnessPointsArray.size(); i++){
         //   log("Fitness we got from the getViewsPosition is = " + FitnessPointsArray.get(i));
        //}

        /*Positions*/
        dataMap.putIntegerArrayList(Consts.FITNESS_POS_API,
                (ArrayList<Integer>) FitnessPointsArray);

        /* Pack color color values */
        int colorChoice = mActivity.getSelectedViewsColor(Consts.FITNESS);
        dataMap.putInt(Consts.FITNESS_COLOR_API, colorChoice);
    }

    public void handleEventPackage(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.EVENT);
        List<Integer> EventPointsArray = new ArrayList<Integer>();
        EventPointsArray.add(point.x);
        EventPointsArray.add(point.y);

        //for(int i = 0; i < EventPointsArray.size(); i++){
         //   log("Event we got from the getViewsPosition is = " + EventPointsArray.get(i));
        //}

        dataMap.putIntegerArrayList(Consts.EVENT_POS_API,
                (ArrayList<Integer>) EventPointsArray);

        /* Pack color color values */
        int colorChoice = mActivity.getSelectedViewsColor(Consts.EVENT);
        dataMap.putInt(Consts.EVENT_COLOR_API, colorChoice);

    }

    public void handleAlarmPackage(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.ALARM_TIMER);
        List<Integer> TimerPointsArray = new ArrayList<Integer>();
        TimerPointsArray.add(point.x);
        TimerPointsArray.add(point.y);

        for(int i = 0; i < TimerPointsArray.size(); i++){
            log("Alarm we got from the getViewsPosition is = " + TimerPointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.ALARM_POS_API,
                (ArrayList<Integer>) TimerPointsArray);

    }

    public void handleDegreePackage(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.DEGREE);
        List<Integer> DegreePointsArray = new ArrayList<Integer>();
        DegreePointsArray.add(point.x);
        DegreePointsArray.add(point.y);

        for(int i = 0; i < DegreePointsArray.size(); i++){
            log("Degree we got from the getViewsPosition is = " + DegreePointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.DEGREE_POS_API,
                (ArrayList<Integer>) DegreePointsArray);

    }



    private void log (String msg){
        Log.d(TAG, msg);
    }


}
