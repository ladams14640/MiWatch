package com.miproducts.miwatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.MenuPackageUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by larry on 7/6/15.
 */
public class WatchFaceMenu  {
    private static final String TAG = "WatchFaceMenu";

    // Menu Stuff
    private EditText etSize;
    private ImageButton ibColor;
    private CheckBox cbVisible;
    private TextView tvSelectedView;

    // currently selected view
    private int selectedView;

    // Need the activity to set changes to the individual children contained in WatchFaceSurfaceView
    MiDigitalWatchFaceCompanionConfigActivity mActivity;

    // Mack Daddy Handler
    MenuPackageUtility mMenuPackageUtility;

    public WatchFaceMenu(MiDigitalWatchFaceCompanionConfigActivity miDigitalWatchFaceCompanionConfigActivity){
        mActivity = miDigitalWatchFaceCompanionConfigActivity;
        tvSelectedView = (TextView) mActivity.findViewById(R.id.tvSelectedMod);
        mMenuPackageUtility = new MenuPackageUtility(mActivity);
        initEtSize();
        initIbColor();
        initCbVisible();
    }

    private void initCbVisible() {
        cbVisible = (CheckBox) mActivity.findViewById(R.id.cbVisibile);
        cbVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mActivity.setViewsVisible(selectedView, isChecked);
            }
        });
    }

    private void initIbColor() {
        ibColor = (ImageButton) mActivity.findViewById(R.id.imageView);
        //TODO button for color dialog.
    }

    private boolean oscillate = false;
    private void initEtSize() {
        etSize = (EditText) mActivity.findViewById(R.id.etSize);
        /**
         * We do TWO things here!
         * 1. Set the new size for the view (applies user's choice in size to the selected view.
         * 2. sends all of the data to the Node for the wearable(only if the data actually has changed).
         */
        etSize.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // wasnt up button wasnt the enter key
                if (event.getAction() != KeyEvent.ACTION_UP)
                    return false;
                if (event.getKeyCode() != KeyEvent.KEYCODE_ENTER)
                    return false;


                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                        // log("enter pressed");
                        if (etSize.getText().toString() != "") {

                            int newValue = Integer.parseInt(etSize.getText().toString());
                            mActivity.ChangeViewSize(newValue, selectedView);

                            // Create a DataMap object and send it to the data layer
                            DataMap dataMap = new DataMap();
                            // TODO need to do this atm, we set it to true by watch, false by this
                            // TODO this way we can see changes and  track changes for testing
                            // TODO will need a solution for reseting the value back to false.

                            //TODO no need to set that now - we are
                            // temp to reset for testing
                            //oscillate = !oscillate;
                            //dataMap.putBoolean("DUMMY TO MAKE SURE ITS ALWAYS FRESH", oscillate);

                            // send out all the user's choices to the node. To be picked up by the watch on it's node.
                            mMenuPackageUtility.handleAllPackaging(dataMap);

                            //Requires a new thread to avoid blocking the UI
                            new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();
                            //log("surface starts at " + mActivity.getSurfaceX());


                            setAlarmToFetchDegreesIn30();
                        }
                    }

                }

                return false;
            }
        });
    }
    // fetch degrees in 30 mins by setting an Alarm to our BroadcastREceiver, AlarmReceiverForTemperature
    private void setAlarmToFetchDegreesIn30() {
        // lets setup the alarm to run and post the degrees
        Intent intent = new Intent(mActivity, AlarmReceiverForTemperature.class);
        intent.putExtra(Consts.KEY_ALARM_REPEAT, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)mActivity.getSystemService(Context.ALARM_SERVICE);

        Calendar instance = Calendar.getInstance();

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 300000, 300000, pendingIntent);

    }


    /**
     * Called by MainCompanionActivity
     * @param dataMap
     */
    public void sendOutDataToWearable(DataMap dataMap){
        //Requires a new thread to avoid blocking the UI
        new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();
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
        cbVisible.setChecked(mActivity.getViewsVisibility(viewNumber));
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
                    Log.d("WatchFaceMenu", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.d("WatchFaceMenu", "ERROR: failed to send DataMap");
                }
            }
        }
    }
}
