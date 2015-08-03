package com.miproducts.miwatch.Weather.openweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.miproducts.miwatch.Database.WeatherLocationDbHelper;
import com.miproducts.miwatch.MiDigitalWatchFaceCompanionConfigActivity;
import com.miproducts.miwatch.utilities.Consts;
import com.miproducts.miwatch.utilities.SendToDataLayerThread;
import com.miproducts.miwatch.utilities.SettingsManager;
import com.miproducts.miwatch.Container.WeatherLocation;
//TODO done LETS MAKE SURE WE CHECK TO SEE IF ITS A ZIPCODE.
//TODO done LETS MAKE SURE WE ALSO CHECK IF WE GOT INTERNET ABILITY.

//TODO check over the work when requested by the watch
//TODO update weather if we do currently have the zipcode in the database.

/**
 * Created by ladam_000 on 8/1/2015.
 */
public class JSONWeatherTask extends AsyncTask<String,Void,String> {
    private static final int ERROR_URL = -2000;
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
     * The Weather Location indicates this came from the Activity not the ConfigListenerService and
     * that the Activity will eventually need it's UI updated - because it will have a new item in it's
     * listview.
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
        if(isOnline()){
            if(!zipcode.equals("NONE")){
                // not a integer
                if(zipcode.length() >= 11) // integer can only be 10 digits max
                    return "ZIP_ISSUE";
                returnedData = String.valueOf(sendHttpRequest(Integer.valueOf(zipcode)));
            }
            else {
                Log.d(TAG, "NONE was set so lets grab Biddeford.");
                returnedData = String.valueOf(sendHttpRequest(04005));
            }
            // onlt send out the temperature if the result back was not an error
            if(Integer.valueOf(returnedData) != (ERROR_URL)){
                // send to watch
                sendHandledTemperatureToWatch(returnedData);
            }else {
                return "ZIP_ISSUE";
            }

        }else {
            return "ONLINE_ISSUE";
        }

        return null;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onPostExecute(String result) {
        // by default result should return a null - unless there was an issue.
        if(result != null){
            //TODO make these constant.s
            if(result.equals("ZIP_ISSUE")){
                Toast.makeText(mContext, "Not proper zipcode!", Toast.LENGTH_LONG).show();

                return;
            }else if(result.equals("ONLINE_ISSUE")){
                Toast.makeText(mContext, "Not online!", Toast.LENGTH_LONG).show();

            }

        }
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
        if(resultIsValid(result)){ // make sure we got back a proper value
            int tempInFah = parseTemp(result);

            // we want to return more than just a temp
            if(locationToFill != null){
                //TODO #@DSDSD!111111
                String city = parseCity(result);

                locationToFill.setCity(city);
                locationToFill.setTemperature(tempInFah);
                // make sure we don't already have a copy of this location - compare zipcodes.
                if(!dbHelper.doesLocationExist(locationToFill))
                    dbHelper.addLocation(locationToFill);// Store into the Database
            }

            return tempInFah;
        }else {
            return ERROR_URL;
        }

    }

    private boolean resultIsValid(String result) {
        log("check validity " + !result.contains("Not found city"));
        //TODO check validity
        // we will get not found city if this was not a accurate zipcode.
        return !result.contains("Not found city");

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

        int indexInt = result.indexOf("name") + 1; // +1 to avoid the "
        int begOfCityValue = indexInt + 6;
        int endOfCityValue = result.indexOf(",", begOfCityValue);
        String city = result.substring(begOfCityValue, endOfCityValue-1); // -1 to avoid "
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