package com.miproducts.miwatch;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.wearable.companion.WatchFaceCompanion;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.utilities.Consts;

import java.util.Calendar;

/**
 * Created by larry on 7/2/15.
 */
public class MiDigitalWatchFaceCompanionConfigActivity extends Activity {
    private static final String TAG = "ConfigActivity";

    private GoogleApiClient mGoogleApiClient;
    private WatchFaceSurfaceView svView;
    private WatchFaceMenu svMenu;
    private BroadcastReceiver brDegree;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        initLayout();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();


        brDegree = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                log("Temperature is = " + intent.getIntExtra(Consts.KEY_BROADCAST_DEGREE, 0));
                int temp = intent.getIntExtra(Consts.KEY_BROADCAST_DEGREE, 0);
                DataMap dataMap = new DataMap();
                // going to continue using the broadcast KEY, it is unique after in DataApi.
                dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, temp);
                // send off to wearable - listener over there will be listening.
                svMenu.sendOutDataToWearable(dataMap);
            }
        };
    }

    private void initLayout() {
        svView = (WatchFaceSurfaceView) findViewById(R.id.surfaceView);
        svMenu = new WatchFaceMenu(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mGoogleApiClient != null && !mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }

        initReceivers();
        svView.threadRun(true);
    }


    private void initReceivers() {

        registerReceiver(brDegree, new IntentFilter(Consts.BROADCAST_DEGREE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        unregisterReceiver(brDegree);
    }

    @Override
    protected void onPause() {
        svView.threadRun(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        setAlarmFetchForOnce();

    }




    private void displayNoConnectedDeviceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String messageText = "No Device";
        String okText = "No Device Connected";
        builder.setMessage(messageText)
                .setCancelable(false)
                .setPositiveButton(okText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




    public float getSurfaceX(){
        return svView.getCanvasX();
    }



    private void setAlarmFetchForOnce() {
        // lets setup the alarm to run and post the degrees
        Intent intent = new Intent(MiDigitalWatchFaceCompanionConfigActivity.this, AlarmReceiverForTemperature.class);
        intent.putExtra(Consts.KEY_ALARM_REPEAT, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MiDigitalWatchFaceCompanionConfigActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)MiDigitalWatchFaceCompanionConfigActivity.this.getSystemService(Context.ALARM_SERVICE);

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 3);

        alarmManager.set(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), pendingIntent);
        Toast.makeText(MiDigitalWatchFaceCompanionConfigActivity.this, "Start Alarm", Toast.LENGTH_LONG).show();
        Log.i("DISPLAY ALL", "ALARM SET UP");
    }








    public Point getViewsPosition(int selectedView){
        return svView.getPositionOfView(selectedView);
    }





    public GoogleApiClient getApiClient(){
        return mGoogleApiClient;
    }

    /**
     * Called by WatchFaceSurfaceView, when an individual View tells the Surface View it has been
     * selected. It notifies the Activity that a new choice has gone through and to let the menu know.
     * @param none
     */
    public void setMenuSelection(int none) {
        svMenu.setSelectedView(none);
    }

    /**
     * called by WatchFaceMenu, when the user chooses a view, the menu needs to know the view's
     * properties
     * @param viewNumber - COnstant representing the view.
     * @return return the size of the view.
     */
    public float getSizeOfView(int viewNumber) {
        return svView.getSizeOfView(viewNumber);

    }

    /**
     * Change the size of the View to the new size, we must call a method in the surfaceView
     * @param newSize - the new size of the view
     * @param selectedView - the view that is being manipulated, ex Consts.EVENTS
     */
    public void ChangeViewSize(int newSize, int selectedView) {
        log("changeViewSize new size = " + newSize);
        log("new selected View = " + selectedView);
        svView.changeViewSize(newSize,selectedView);
    }

    /**
     * Called by the WatchFaceMenu, when the user chooses a view, the menu needs to know the view's
     * properties.
     * @param selectedView - COnstant representing the view.
     * @return - returns the color of the view.
     */
    public int getSelectedViewsColor(int selectedView){
        return svView.getColorOfView(selectedView);
    }

    /**
     * Called by WatchFaceMenu
     * @param selectedView - Constant rep of view
     * @return
     */
    public boolean getViewsVisibility(int selectedView) {
        return svView.getVisibilityOfView(selectedView);
    }

    /**
     * Called by WatchFaceMenu
     * @param selectedView - Constant reference to the view
     * @return
     */
    public int getViewsSize(int selectedView) {
        return (int) svView.getSizeOfView(selectedView);
    }
    public void setViewsVisible(int selectedView, boolean isChecked) {
        svView.setViewsVisibility(selectedView, isChecked);
    }






    private void log(String s) {
        Log.d(TAG, s);
        //init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }
}
