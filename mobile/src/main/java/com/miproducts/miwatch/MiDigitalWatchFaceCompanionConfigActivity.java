package com.miproducts.miwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.wearable.companion.WatchFaceCompanion;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by larry on 7/2/15.
 */
public class MiDigitalWatchFaceCompanionConfigActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "ConfigActivity";

    private GoogleApiClient mGoogleApiClient;
    private WatchFaceSurfaceView svView;
    private WatchFaceMenu svMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        initLayout();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

    }

    private void initLayout() {
        svView = (WatchFaceSurfaceView) findViewById(R.id.surfaceView);
        svMenu = new WatchFaceMenu(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        unpauseSurfaceViewThread();
    }

    private void unpauseSurfaceViewThread() {
        if(svView != null){
            svView.unpauseThread();
        }
    }

    private void startSurfaceViewThread() {
        if(svView != null){
            svView.startThread();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        stopSurfaceViewThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseSurfaceViewThread();
    }

    private void pauseSurfaceViewThread() {
        if(svView != null){
            svView.pauseThread();
        }
    }

    private void stopSurfaceViewThread(){
        if(svView != null){
            svView.stopThread();
        }

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

    private void log(String s) {
        Log.d(TAG, s);
        //init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        log("Google onConnected: " + connectionHint);


    }

    @Override
    public void onConnectionSuspended(int cause) {
        log("Google onConnectionSuspended: " + cause);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // The Wearable API is unavailable
            Log.d(TAG, "onConnection failed, API wasn't available.");
        }

    }




    private static final String DATA_ACTIVITY_KEY = "com.miproducts.miwatch";
}
