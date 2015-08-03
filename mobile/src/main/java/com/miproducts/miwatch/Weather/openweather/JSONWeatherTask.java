package com.miproducts.miwatch.Weather.openweather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.miproducts.miwatch.Database.WeatherLocationDbHelper;
import com.miproducts.miwatch.MiDigitalWatchFaceCompanionConfigActivity;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.SendToDataLayerThread;
import com.miproducts.miwatch.utilities.SettingsManager;
import com.miproducts.miwatch.Container.WeatherLocation;

/**
 * Created by ladam_000 on 8/1/2015.
 */
public class JSONWeatherTask extends AsyncTask<String,Void,String> {

    private WeatherHttpClient httpClient;
    private SettingsManager mSettingsManager;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private WeatherLocation locationToFill;
    private WeatherLocationDbHelper dbHelper;

    public JSONWeatherTask(Context mContext, SettingsManager mSettingsManager, GoogleApiClient mGoogleApiClient){
        Log.d(TAG, "Made");
        this.mSettingsManager = mSettingsManager;
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        httpClient = new WeatherHttpClient();
        locationToFill = null;
    }

    /**
     * Indicated with the locationToFill that we are going to load up this WeatherLocation for the user
     * @param mContext
     * @param mSettingsManager
     * @param mGoogleApiClient
     * @param locationToFill - WeatherLocation
     */
    public JSONWeatherTask(Context mContext, SettingsManager mSettingsManager, GoogleApiClient mGoogleApiClient, WeatherLocation locationToFill){
        Log.d(TAG, "Made");
        this.mSettingsManager = mSettingsManager;
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        this.locationToFill = locationToFill;
        dbHelper = new WeatherLocationDbHelper(mContext);
        httpClient = new WeatherHttpClient();

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
            Log.d(TAG, "NONE was set so lets grab Biddeford.");
            returnedData = String.valueOf(sendHttpRequest(04005));
        }

        //TODO prob save the temperature here. with other properties for a weather class - OR UPDATE THE WeatherLocation
        // send to watch
        sendHandledTemperatureToWatch(returnedData);

        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        if(locationToFill != null){
            // tell Companion to update it's UI
            ((MiDigitalWatchFaceCompanionConfigActivity)mContext).updateUI();
        }
        Log.d(TAG, "onPostExecute");
    }



    private int sendHttpRequest(int zipcode) {

        // get the JSON GEOLOCATION TEMP DETAILS
        String result = httpClient.getWeatherData(zipcode);
        // parse the temp value

        int tempInFah = parseTemp(result);

        // we want to return more than just a temp
        if(locationToFill != null){
            //TODO #@DSDSD!111111
            String city = parseCity(result);

            locationToFill.setCity(city);
            locationToFill.setTemperature(tempInFah);

            // Store into the Database
            dbHelper.addLocation(locationToFill);

        }

        return tempInFah;
    }

    private void log(String y) {
        Log.d(TAG, y);
    }


    private int parseTemp(String result) {
        int indexInt = result.indexOf("temp");
        int begOfTempValue = indexInt+6;
        int endOfTempValue = result.indexOf(",", begOfTempValue);
        // Kelvin version
        String temperatureInCelsius = result.substring(begOfTempValue, endOfTempValue);
        // Celsius version
        int tempInCels = (int) ConverterUtil.convertKelvinToCelsius(Double.parseDouble(temperatureInCelsius));
        // Fernheit
        int tempInFah = ConverterUtil.convertCelsiusToFahrenheit(tempInCels);

        return tempInFah;

    }

    private String parseCity(String result) {
        int indexInt = result.indexOf("name");
        int begOfCityValue = indexInt + 6;
        int endOfCityValue = result.indexOf(",", begOfCityValue);
        String city = result.substring(begOfCityValue, endOfCityValue);
        return city;
    }


    private void sendHandledTemperatureToWatch(String temp) {
        // send out to the dataLayer
        DataMap dataMap = new DataMap();
        // going to continue using the broadcast KEY, it is unique after in DataApi.
        dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, Integer.valueOf(temp));
        // send off to wearable - listener over there will be listening.
        new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap, mGoogleApiClient).start();
    }

}