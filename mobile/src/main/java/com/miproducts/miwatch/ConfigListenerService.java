package com.miproducts.miwatch;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;
import com.miproducts.miwatch.Weather.openweather.ConverterUtil;
import com.miproducts.miwatch.Weather.openweather.JSONWeatherTask;
import com.miproducts.miwatch.Weather.openweather.WeatherHttpClient;
import com.miproducts.miwatch.utilities.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ladam_000 on 7/12/2015.
 */
public class ConfigListenerService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    private static final String TAG = "DigitalListenerService";

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

//TODO this will go off wehen the companion sends off some data or when the wearable does.
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        log("onDataChanged");
        //SettingsManager sm = new SettingsManager(getApplicationContext());

            getTemp();

            /*
        DataMap dataMap;
        for (DataEvent event : dataEventBuffer) {
            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                log("DataMap received on watch: " + dataMap);
                log("From Wearable");
                Log.d(TAG, "Is it true? :" + dataMap.getBoolean(Consts.DEGREE_REFRESH));
                    //TODO so wierd I cant have methods here for some reason.

            }

        }
*/

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
            String temperatureInCelsius = result.substring(begOfTempValue, endOfTempValue);
            // temperature in Fahrenheit
            int tempInCels = (int) ConverterUtil.convertKelvinToCelsiusconvertKelvinToCelsius(Double.parseDouble(temperatureInCelsius));
            Log.d(TAG, "Celsius = "+ tempInCels);
            // TODO atm this sends the temperature results to the MainCompanionActivity - we want to
            //TODO change this so we can do it regardless if application on phone is on.
             tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);
            Log.d(TAG, "Fahrenheit = " + tempInFah);

            // send this to the MainCompanionActivity to send it off to wearable. -
            Intent sendDegreesIntent = new Intent(Consts.BROADCAST_DEGREE);
            sendDegreesIntent.putExtra(Consts.KEY_BROADCAST_DEGREE, tempInFah);
            sendBroadcast(sendDegreesIntent);

            return "temp_nothing";
        }

    }






    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    public void log(String s) {
        Log.d("Phone Service Listener", s);
    }






}


