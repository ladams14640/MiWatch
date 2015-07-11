package com.miproducts.miwatch;

import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.utilities.Consts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by larry on 7/6/15.
 */
public class WatchFaceMenu  {
    private static final String TAG = "WatchFaceMenu";

    private EditText etSize;
    private ImageButton ibColor;
    private CheckBox cbVisible;
    private TextView tvSelectedView;

    private int selectedView;
    MiDigitalWatchFaceCompanionConfigActivity mActivity;

    public WatchFaceMenu(MiDigitalWatchFaceCompanionConfigActivity miDigitalWatchFaceCompanionConfigActivity){
        mActivity = miDigitalWatchFaceCompanionConfigActivity;
        etSize = (EditText) mActivity.findViewById(R.id.etSize);
        cbVisible = (CheckBox) mActivity.findViewById(R.id.cbVisibile);
        tvSelectedView = (TextView) mActivity.findViewById(R.id.tvSelectedMod);

        initEtSize();
       initIbColor();
    }

    private void initIbColor() {
        ibColor = (ImageButton) mActivity.findViewById(R.id.imageView);
        //TODO button for color dialog.
    }

    private void initEtSize() {
        etSize.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // wasnt up button wasnt the enter key
                if(event.getAction() != KeyEvent.ACTION_UP)
                    return false;
                if(event.getKeyCode() != KeyEvent.KEYCODE_ENTER)
                    return false;


                if(event.getAction() == KeyEvent.ACTION_UP){
                    if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        //TODO link this to setting a size
                        // TODO but first we need some kind of uniformed system.
                        log("enter pressed");
                        if(etSize.getText().toString() != ""){
                            int newValue = Integer.parseInt(etSize.getText().toString());
                            mActivity.ChangeViewSize(newValue, selectedView);

                            // Create a data map and put data in it

                            String WEARABLE_DATA_PATH = "/wearable_data";

                            // Create a DataMap object and send it to the data layer
                            DataMap dataMap = new DataMap();

                            //TODO 1.2 this is where we will be setting up our positions, color, and the
                            //TODO 2.2 rest.

                            handleDigitalTimeRetrieval(dataMap); // stores DigitalTime positions
                            handleDateRetrieval(dataMap);
                            handleAlarmRetrieval(dataMap);
                            handleDegreeRetrieval(dataMap);
                            handleFitnessRetrieval(dataMap);
                            handleEventRetrieval(dataMap);
                            //Requires a new thread to avoid blocking the UI
                            new SendToDataLayerThread(WEARABLE_DATA_PATH, dataMap).start();
                            log("surface starts at " + mActivity.getSurfaceX());
                        }
                    }

                }

                return false;
            }
        });
    }



    private void handleDigitalTimeRetrieval(DataMap dataMap) {
        // get location
        Point digitPoint = mActivity.getViewsPosition(Consts.DIGITAL_TIMER);
        List<Integer> digitalPointsArray = new ArrayList<Integer>();
        digitalPointsArray.add(digitPoint.x);
        digitalPointsArray.add(digitPoint.y);

        for(int i = 0; i < digitalPointsArray.size(); i++){
            log("digitalPointsArray we got from the getViewsPosition is = " + digitalPointsArray.get(i));
        }
        //TODO grab a color
        // get color
        int colorChoice = mActivity.getSelectedViewsColor(Consts.DIGITAL_TIMER);
        dataMap.putInt(Consts.DIGITAL_TIMER_API_COLOR, colorChoice);

        // Pack the values
        dataMap.putIntegerArrayList(Consts.DIGITAL_TIMER_API,
                (ArrayList<Integer>) digitalPointsArray);
    }

    private void handleDateRetrieval(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.DATE);
        List<Integer> DatePointsArray = new ArrayList<Integer>();
        DatePointsArray.add(point.x);
        DatePointsArray.add(point.y);

        for(int i = 0; i < DatePointsArray.size(); i++){
            log("DATE we got from the getViewsPosition is = " + DatePointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.DATE_API,
                (ArrayList<Integer>) DatePointsArray);

    }


    private void handleFitnessRetrieval(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.FITNESS);
        List<Integer> FitnessPointsArray = new ArrayList<Integer>();
        FitnessPointsArray.add(point.x);
        FitnessPointsArray.add(point.y);

        for(int i = 0; i < FitnessPointsArray.size(); i++){
            log("Fitness we got from the getViewsPosition is = " + FitnessPointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.FITNESS_API,
                (ArrayList<Integer>) FitnessPointsArray);

    }

    private void handleEventRetrieval(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.EVENT);
        List<Integer> EventPointsArray = new ArrayList<Integer>();
        EventPointsArray.add(point.x);
        EventPointsArray.add(point.y);

        for(int i = 0; i < EventPointsArray.size(); i++){
            log("Event we got from the getViewsPosition is = " + EventPointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.EVENT_API,
                (ArrayList<Integer>) EventPointsArray);

    }

    private void handleAlarmRetrieval(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.ALARM_TIMER);
        List<Integer> TimerPointsArray = new ArrayList<Integer>();
        TimerPointsArray.add(point.x);
        TimerPointsArray.add(point.y);

        for(int i = 0; i < TimerPointsArray.size(); i++){
            log("Alarm we got from the getViewsPosition is = " + TimerPointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.ALARM_API,
                (ArrayList<Integer>) TimerPointsArray);

    }

    private void handleDegreeRetrieval(DataMap dataMap) {
        Point point = mActivity.getViewsPosition(Consts.DEGREE);
        List<Integer> DegreePointsArray = new ArrayList<Integer>();
        DegreePointsArray.add(point.x);
        DegreePointsArray.add(point.y);

        for(int i = 0; i < DegreePointsArray.size(); i++){
            log("Degree we got from the getViewsPosition is = " + DegreePointsArray.get(i));
        }
        dataMap.putIntegerArrayList(Consts.DEGREE_API,
                (ArrayList<Integer>) DegreePointsArray);

    }





    private static final String COUNT_KEY = "com.miproducts.miwatch";

    private void log(String s) {
        Log.d(TAG, s);
    }


    /**
     * This is where we will tie the menu in with the surfaceView.
     * When the user chooses another view, he will effectively change which view's properties he wants
     * to alter
     * @param viewNumber - the Consts.VIEW_NUMBER that we set from the view being touched, which in
     *                  return, notifies the SurfaceView, who notifies the MainActivity who calls this
     *                   method.
     */
    public void setSelectedView(int viewNumber){
        selectedView = viewNumber;
        // Display Which view is selected.
        switch(viewNumber){
            case Consts.NONE:
                tvSelectedView.setText("");
                break;
            case Consts.DIGITAL_TIMER:
                tvSelectedView.setText("Timer View");
                break;
            case Consts.FITNESS:
                tvSelectedView.setText("Fitness View");
                break;
            case Consts.EVENT:
                tvSelectedView.setText("Event View");
                break;
            case Consts.DATE:
                tvSelectedView.setText("Date View");
                break;
            case Consts.ALARM_TIMER:
                tvSelectedView.setText("Alarm View");
                break;
            case Consts.DEGREE:
                tvSelectedView.setText("Degree View");
                break;
        }

        // Display the values associated with that view's properties (color, size, and soon visibility).
        switchValues(viewNumber);

    }

    /**
     * This is where we grab the values from the views that were selected
     * ATM we want the size and the color of the view. With those values we set the menu's display
     *  etSize;
     *  ibColor;
     *  cbVisible;
     * From what was previously saved.
     * @param viewNumber - the view we have recently selected
     */
    private void switchValues(int viewNumber) {
        etSize.setText(Integer.toString((int) mActivity.getSizeOfView(viewNumber)));
        ibColor.setBackgroundColor(mActivity.getSelectedViewsColor(viewNumber));
        //TODO get the visibility property of the view
        //TODO we should check if the dateView had been selected and display additional options
        //TODO - maybe how it is displayed and more control over the dayOfWeek or dayOfMonth since
        //TODO - we are trying to control only one of those atm.
    }

    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mActivity.getApiClient()).await();
            for (Node node : nodes.getNodes()) {

                // Construct a DataRequest and send over the data layer
                PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                putDMR.getDataMap().putAll(dataMap);
                PutDataRequest request = putDMR.asPutDataRequest();
                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mActivity.getApiClient(),request).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("myTag", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v("myTag", "ERROR: failed to send DataMap");
                }
            }
        }
    }
}
