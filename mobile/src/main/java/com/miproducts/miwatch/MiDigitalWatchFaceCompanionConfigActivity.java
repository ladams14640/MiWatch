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
import android.os.AsyncTask;
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
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.miproducts.miwatch.Weather.openweather.ConverterUtil;
import com.miproducts.miwatch.Weather.openweather.WeatherHttpClient;
import com.miproducts.miwatch.utilities.Consts;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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

        // notified by ConfigListener that temp is requested. - not used anymore - was for degrees on watch to instigate temop change
        brDegree = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                log("brDegree - Temperature is = " + intent.getIntExtra(Consts.KEY_BROADCAST_DEGREE, 0));
                int temp = intent.getIntExtra(Consts.KEY_BROADCAST_DEGREE, 0);
                DataMap dataMap = new DataMap();
                // going to continue using the broadcast KEY, it is unique after in DataApi.
                dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, temp);
                // send off to wearable - listener over there will be listening.
                svMenu.sendOutDataToWearable(dataMap);
            }
        };

        // lets get the current temperature and send it to wearable
        getTemp();

    }

    private void initLayout() {
        svView = (WatchFaceSurfaceView) findViewById(R.id.surfaceView);
        svMenu = new WatchFaceMenu(this);
    }


    // #################### TEMPERATURE STARTS

    private void getTemp() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute();
    }


    int tempInFah;


    public class JSONWeatherTask extends AsyncTask<String,Void,String> {
        WeatherHttpClient httpClient;
        public JSONWeatherTask(){
            httpClient = new WeatherHttpClient();
            Log.d(TAG, "Made");
        }

        private static final String TAG = "JSONWeatherTask";

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "BackGround");

            String data = String.valueOf(sendHttpRequest());
            return data;
        }



        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Temperature " + tempInFah, Toast.LENGTH_SHORT).show();


            Log.d(TAG, "onPostExecute");
        }
        private int sendHttpRequest() {
            // get the JSON GEOLOCATION TEMP DETAILS
            String result = httpClient.getWeatherData("Biddeford");
            // parse the temp value
            int indexInt = result.indexOf("temp");
            int begOfTempValue = indexInt+6;
            int endOfTempValue = result.indexOf(",", begOfTempValue);
            // Kelvin version
            String temperatureInCelsius = result.substring(begOfTempValue, endOfTempValue);
            // Celsius version
            int tempInCels = (int) ConverterUtil.convertKelvinToCelsius(Double.parseDouble(temperatureInCelsius));
            // Fernheit
            tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);

            // send out to the dataLayer
            DataMap dataMap = new DataMap();
            // going to continue using the broadcast KEY, it is unique after in DataApi.
            dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, tempInFah);
            // send off to wearable - listener over there will be listening.
            new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();
            return tempInFah;
        }

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
            if (mGoogleApiClient != null) {
                log("mGoogle != null!");
                mGoogleApiClient.blockingConnect(5, TimeUnit.SECONDS);

                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                for (Node node : nodes.getNodes()) {

                    // Construct a DataRequest and send over the data layer
                    PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                    putDMR.getDataMap().putAll(dataMap);
                    PutDataRequest request = putDMR.asPutDataRequest();
                    DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient, request).await();
                    if (result.getStatus().isSuccess()) {
                        Log.d(TAG, "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                    } else {
                        // Log an error
                        Log.d(TAG, "ERROR: failed to send DataMap");
                    }
                }
            }else {
                log("mGoogle == null!");
            }
        }
    }



// #################### TEMPERATURE ENDS










    @Override
    protected void onStart() {
        super.onStart();

        initReceivers();
        svView.threadRun(true);
    }


    private void initReceivers() {
        registerReceiver(brDegree, new IntentFilter(Consts.BROADCAST_DEGREE));
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    }













    public Point getViewsPosition(int selectedView){
        return svView.getPositionOfView(selectedView);
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
}
