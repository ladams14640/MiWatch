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
import com.miproducts.miwatch.utilities.SettingsManager;

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
    private EditText etSize, etZipCode;
    private ImageButton ibColor;
    private CheckBox cbVisible;
    private TextView tvSelectedView;

    // currently selected view
    private int selectedView;

    // Need the activity to set changes to the individual children contained in WatchFaceSurfaceView
    MiDigitalWatchFaceCompanionConfigActivity mActivity;

    // Mack Daddy Handler
    MenuPackageUtility mMenuPackageUtility;

    // Preferences
    SettingsManager mSettingsManager;

    public WatchFaceMenu(MiDigitalWatchFaceCompanionConfigActivity miDigitalWatchFaceCompanionConfigActivity){
        mActivity = miDigitalWatchFaceCompanionConfigActivity;
        tvSelectedView = (TextView) mActivity.findViewById(R.id.tvSelectedMod);
        mMenuPackageUtility = new MenuPackageUtility(mActivity);
        mSettingsManager = new SettingsManager(mActivity);

        initEtSize();
        initIbColor();
        initCbVisible();
        initZipCode();
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
                            // change the look of the view's sizes.
                            int newValue = Integer.parseInt(etSize.getText().toString());
                            mActivity.ChangeViewSize(newValue, selectedView);

                            // Create a DataMap object and send it to the data layer
                            DataMap dataMap = new DataMap();
                            // store all of the view's properties in the datamap before sending it off.
                            //mMenuPackageUtility.handleAllPackaging(dataMap);

                            // Requires a new thread to avoid blocking the UI
                            //new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();
                            // TODO probably need to check the zipcode with a registry of zipcodes to make sure its right.
                            // save zipcode
                            if(Integer.parseInt(etZipCode.getText().toString()) != 0){
                                mSettingsManager.saveZipcode(Integer.parseInt(etZipCode.getText().toString()));
                            }else {
                                Toast.makeText(mActivity, "Can't Save the Zipcode", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                }

                return false;
            }
        });
    }


    private void initZipCode(){
        etZipCode = (EditText) mActivity.findViewById(R.id.etZipcode);

        int savedZipCode = mSettingsManager.getZipCode();
        //if(savedZipCode != 0){
            etZipCode.setText(Integer.toString(savedZipCode));
       //}
        etZipCode.setOnKeyListener(new View.OnKeyListener() {
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

                            // TODO probably need to check the zipcode with a registry of zipcodes to make sure its right.
                            // save zipcode
                            if(Integer.parseInt(etZipCode.getText().toString()) != 0){
                                mSettingsManager.saveZipcode(Integer.parseInt(etZipCode.getText().toString()));
                            }else {
                                Toast.makeText(mActivity, "Can't Save the Zipcode", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                }

                return false;
            }
        });
    }


    /**
     * Called by MainCompanionActivity
     * @param dataMap
     */
    public void sendOutDataToWearable(DataMap dataMap){
        //Requires a new thread to avoid blocking the UI
        new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();
    }

    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }
    }



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
}
