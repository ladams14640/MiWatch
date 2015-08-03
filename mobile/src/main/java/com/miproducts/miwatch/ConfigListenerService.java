package com.miproducts.miwatch;

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
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.miproducts.miwatch.Weather.openweather.ConverterUtil;
import com.miproducts.miwatch.Weather.openweather.WeatherHttpClient;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.SettingsManager;

import java.util.concurrent.TimeUnit;

/**
 * Responsible for listening for the wearable.
 * Created by ladam_000 on 7/12/2015.
 */
public class ConfigListenerService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener {

    private static final String TAG = "DigitalListenerService";
    private GoogleApiClient mGoogleApi;
    private SettingsManager mSettingsManager;

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
        mSettingsManager = new SettingsManager(getApplicationContext());
        mGoogleApi = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        //TODO if any issues with receiving data this is why. - check and delete toDO
            try{
                getTemp();
            }finally {
                dataEventBuffer.close();
            }
    }

    private void getTemp() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute();
    }

//TODO move all of this over to the method we got. promote DRY.
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
            String zipcode = mSettingsManager.getZipCode();
            String returnedData;
            if(!zipcode.equals("NONE")){
                returnedData = String.valueOf(sendHttpRequest(Integer.valueOf(zipcode)));
            }
            else {
                // use biddo as default for now
                returnedData = String.valueOf(sendHttpRequest(04005));
            }
            return returnedData;
        }



        @Override
        protected void onPostExecute(String result) {


            Log.d(TAG, "onPostExecute");
        }
        private String sendHttpRequest(int zipcode) {
            String result = httpClient.getWeatherData(zipcode);
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

               // NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApi).await();
               // for (Node node : nodes.getNodes()) {

                    // Construct a DataRequest and send over the data layer
                    PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                    putDMR.getDataMap().putAll(dataMap);
                    PutDataRequest request = putDMR.asPutDataRequest();
                    Wearable.DataApi.putDataItem(mGoogleApi, request);
                   /* if (result.getStatus().isSuccess()) {
                        Log.d("WatchFaceMenu", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                    } else {
                        // Log an error
                        Log.d("WatchFaceMenu", "ERROR: failed to send DataMap");
                    }*/
                //}
           }else {
                log("mGoogle == null!");
            }
        }
    }


    public void log(String s) {
        Log.d("Phone Service Listener", s);
    }






}

