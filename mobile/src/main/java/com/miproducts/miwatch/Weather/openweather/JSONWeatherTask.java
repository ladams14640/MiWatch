package com.miproducts.miwatch.Weather.openweather;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//TODO check over the work when requested by the watch
//TODO update weather if we do currently have the zipcode in the database.

/**
 * Created by ladam_000 on 8/1/2015.
 */
public class JSONWeatherTask extends AsyncTask<String,Void,String> {
    // max length of a integer to check against
    private static final int MAX_INTEGER_LENGTH = 11;
    // error indicate it is not a proper zipcode
    private static final String ERROR_ZIP = "ZIP_ISSUE";
    // error indicate we are not online
    private static final String ERROR_ONLINE = "ONLINE_ISSUE";
    // error we use if we didnt get a result back from trying to reach out to the weather online.
    private static final int ERROR_URL = -2000;

    private WeatherHttpClient httpClient;
    private SettingsManager mSettingsManager;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private WeatherLocation locationToFill;
    private WeatherLocationDbHelper dbHelper;
    private boolean fromActivity = false;

    /**
     * From ConfigServiceListener
     * @param mContext
     * @param mSettingsManager
     * @param mGoogleApiClient
     */
    public JSONWeatherTask(Context mContext, SettingsManager mSettingsManager, GoogleApiClient mGoogleApiClient){
        Log.d(TAG, "Made");
        this.mSettingsManager = mSettingsManager;
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        httpClient = new WeatherHttpClient();
        locationToFill = null;

        // indicate we are from a Service and not an Activity (our app is not present on the screen.)
        fromActivity = false;
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

        // indicate we are from Activity not Service - our app is showing.
        fromActivity = true;

    }
    private static final String TAG = "JSONWeatherTask";
    //mSettingsManager.saveZipcode(String.valueOf(etZipCode.getText().toString()));
    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "BackGround");
        String zipcode;
        //TODO setup boolean clean this shit up!
        if(fromActivity){
            // fresh zipcode, not one from the preferences.
            zipcode = locationToFill.getZipcode();
        }
        else {
            zipcode = mSettingsManager.getZipCode();
        }

        // store data
        String returnedData;

        // check if we are even online
        if(isOnline()){
            // check to see if this is the user's first time in the app
            if(!zipcode.equals(SettingsManager.NOTHING_SAVED))
            {
                // not a integer if it exceeds MAX_INTER_LENGTH
                if(zipcode.length() >= MAX_INTEGER_LENGTH)
                    return ERROR_ZIP;
                returnedData = String.valueOf(sendHttpRequest(Integer.valueOf(zipcode)));
            }
            else {
                Log.d(TAG, "NONE - must be first time was set so lets grab Biddeford.");
                // lets save biddos since first time
                mSettingsManager.saveZipcode("04005");
                returnedData = String.valueOf(sendHttpRequest(04005));
            }
            // onlt send out the temperature if the result back was not an error
            if(Integer.valueOf(returnedData) != (ERROR_URL)){
                // send to watch
                sendHandledTemperatureToWatch(returnedData);
            }else {
                return ERROR_ZIP; //TODO Constance these out.
            }

        }else {
            return ERROR_ONLINE;
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
            if(result.equals(ERROR_ZIP)){
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
        /**
         *
         *
         * fine
         * {"coord":{"lon":-70.44,"lat":43.5},
         * "weather":
         *       [
         *          {   "id":803,"main":"Clouds",
         *              "description":"broken clouds",
         *              "icon":"04n"}],
         *              "base":"cmc stations",
         *              "main":{
         *              "temp":298.16,
         *              "pressure":1008,
         *              "humidity":54,
         *              "temp_min":295.35,
         *              "temp_max":299.15},
         *              "wind":{"speed":4.1,"deg":240},
         *              "clouds":{"all":75},"
         *              dt":1438648183,
         *              "sys":{"type":1,"id":1366,
         *              "message":0.0117,
         *              "country":"US",
         *              "sunrise":1438680854,
         *              "sunset":1438732847},
         *              "id":4977222,
         *              "name":"Saco",
         *              "cod":200
         *              }
         *
         *          // raining
         *              {
         *              "coord":
         *              {"lon":-71.13,"lat":44.05},
         *              "weather":[
         *              {"id":500,
         *              "main":"Rain",
         *              "description":"light rain",
         *              "icon":"10n"}],
         *              "base":"cmc stations","main":{"temp":291.28,"pressure":1008,"humidity":93,"temp_min":289.26,"temp_max":292.15},
         *              "wind":{"speed":1.53,"deg":224},
         *              "rain":{"1h":0.38},
         *              "clouds":{"all":90},"
         *              dt":1438648952,"sys":{"type":1,"id":1355,"message":0.0041,"country":"US","sunrise":1438680935,"sunset":1438733096},"id":5090347,"name":"North Conway","cod":200}
         *
         * description
         *  light rain - sun and rain
         *  overcast clouds - cloud
         *  broken clouds - sun cloud
         *  scattered clouds = clouds sun
         *  sky is clear - sun
         *  moderate rain - sun rain
         *  few clouds - sun clouds
         *  moderate rain - rain
         * Thunderstorm - cloud - but soon to be cloud and lightning
         *
         * bar harbor
         * {"coord":{"lon":-68.2,"lat":44.39},
         * "weather":[{"id":804,
         *          "main":"Clouds",
         *          "description":"overcast clouds","icon":"04n"}
         *          ],
         *          "base":"cmc stations","main":{"temp":294.15,"pressure":1009,"humidity":100,"temp_min":294.15,"temp_max":294.15},"wind":{"speed":5.1,"deg":210},"clouds":{"all":90},"dt":1438648500,"sys":{"type":1,"id":1349,"message":0.0093,"country":"US","sunrise":1438680177,"sunset":1438732447},"id":4957320,"name":"Bar Harbor","cod":200}
         *
         *
         */
        // weather
        // temp
        // parse the temp value
        if(resultIsValid(result)){

            int tempInFah = parseTemp(result);

            // we want to return more than just a temp
            if(fromActivity){

                //TODO reformat here
                String city = parseCity(result);

                locationToFill.setCity(city);
                locationToFill.setTemperature(tempInFah);

                // make sure we don't already have a copy of this location - compare zipcodes.
                if(!dbHelper.doesLocationExist(locationToFill))
                    dbHelper.addLocation(locationToFill);// Store into the Database
                // update the database because we already have this zipcode
                else {dbHelper.updateTemperature(locationToFill);}
                // save the new zipcode.
                mSettingsManager.saveZipcode(locationToFill.getZipcode());
                try{
                    JSONObject resultObject = new JSONObject(result);
                    JSONArray jsonArrayWeather = resultObject.getJSONArray("weather");
                    JSONObject c = jsonArrayWeather.getJSONObject(0);
                    //JSONObject jsonObjectDescript = jsonArrayWeather.getJSONArray("description");
                    log("weather pulled out " + c.getString("description"));
                    //TODO WE GOT the description of the weather - lets setup our db to receive it
                    //todo lets get some constants for this stuff.



                }catch(JSONException e){
                    log("issue with json = " + e.getMessage());
                }



            }
            // we caem from Config
            else {
                // send out to the dataLayer
                DataMap dataMap = new DataMap();
                // going to continue using the broadcast KEY, it is unique after in DataApi.
                dataMap.putInt(Consts.KEY_BROADCAST_DEGREE, tempInFah);
                // send off to wearable - listener over there will be listening.
                new SendToDataLayerThread(Consts.PHONE_TO_WEARABLE_PATH, dataMap, mGoogleApiClient).start();

                // send this to the MainCompanionActivity to send it off to wearable. -
                Intent sendDegreesIntent = new Intent(Consts.BROADCAST_DEGREE);
                sendDegreesIntent.putExtra(Consts.KEY_BROADCAST_DEGREE, tempInFah);
                mContext.sendBroadcast(sendDegreesIntent);
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