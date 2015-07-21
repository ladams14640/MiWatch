package com.miproducts.miwatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.miproducts.miwatch.Weather.openweather.ConverterUtil;
import com.miproducts.miwatch.Weather.openweather.WeatherHttpClient;
import com.miproducts.miwatch.utilities.Consts;

import java.util.concurrent.TimeUnit;

/**
 * Created by ladam_000 on 7/12/2015.
 */
public class ConfigListenerService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener {

    private static final String TAG = "DigitalListenerService";
    private GoogleApiClient mGoogleApi;


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: " + bundle);

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended: " + cause);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed: " + result);

    }
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        log("onDataChanged");
        mGoogleApi = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
            getTemp();
    }

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

            String data = sendHttpRequest();
            return data;
        }



        @Override
        protected void onPostExecute(String result) {


            Log.d(TAG, "onPostExecute");
        }
        private String sendHttpRequest() {
            String result = httpClient.getWeatherData("Biddeford");
            int indexInt = result.indexOf("temp");
            int begOfTempValue = indexInt+6;
            int endOfTempValue = result.indexOf(",", begOfTempValue);
            // temp comes in kelvin
            String temperatureInCelsius = result.substring(begOfTempValue, endOfTempValue);

            // temp in celsius
            int tempInCels = (int) ConverterUtil.convertKelvinToCelsius(Double.parseDouble(temperatureInCelsius));
            Log.d(TAG, "Celsius = "+ tempInCels);

            // temp in fehreinheit
             tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);
            Log.d(TAG, "Fahrenheit = " + tempInFah);

            /**/
            // send out to the dataLayer
            DataMap dataMap = new DataMap();
            // going to continue using the broadcast KEY, it is unique after in DataApi.
            dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, tempInFah);
            // send off to wearable - listener over there will be listening.
            new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap).start();


            // send this to the MainCompanionActivity to send it off to wearable. -
            Intent sendDegreesIntent = new Intent(Consts.BROADCAST_DEGREE);
            sendDegreesIntent.putExtra(Consts.KEY_BROADCAST_DEGREE, tempInFah);
            sendBroadcast(sendDegreesIntent);




            return "temp_nothing";
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
            if (mGoogleApi != null) {
                log("mGoogle != null!");
                mGoogleApi.blockingConnect(5, TimeUnit.SECONDS);

                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApi).await();
                for (Node node : nodes.getNodes()) {

                    // Construct a DataRequest and send over the data layer
                    PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                    putDMR.getDataMap().putAll(dataMap);
                    PutDataRequest request = putDMR.asPutDataRequest();
                    DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApi, request).await();
                    if (result.getStatus().isSuccess()) {
                        Log.d("WatchFaceMenu", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                    } else {
                        // Log an error
                        Log.d("WatchFaceMenu", "ERROR: failed to send DataMap");
                    }
                }
            }else {
                log("mGoogle == null!");
            }
        }
    }


    public void log(String s) {
        Log.d("Phone Service Listener", s);
    }






}


