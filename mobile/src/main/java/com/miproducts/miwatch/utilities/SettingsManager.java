package com.miproducts.miwatch.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by larry on 7/13/15.
 */

public class SettingsManager {
    public final static String SHARED_PREFERENCES_NAME = "GOOGLEAPICLIENT_PREFERENCE";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private static final String KEY_ZIPCODE = "KEY_ZIPCODE";
    public static final String NOTHING_SAVED = "NONE";

    private static final String KEY_TOWN = "KEY_TOWN";
    private static final String KEY_STATE = "KEY_STATE";

    //TODO not sure if i am even going to do that one.
    // keep track if we have started the app before - determine to load the db with the JSON
    private static final String KEY_FIRST_TIME_RUNNING_APP = "KEY_FIRST_TIME_RUNNING_APP";


    public SettingsManager(Context applicationContext) {
        mPrefs = applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }



    public void writeToPreferences(String key, int value){
        mEditor.putInt(key,value);
        mEditor.apply();
        mEditor.commit();
    }


    public int getIntFromPreferences(String key){
        return mPrefs.getInt(key, 0);
    }




    public void saveZipcode(String zipcode){
        mEditor.putString(KEY_ZIPCODE, zipcode);
        mEditor.apply();
        mEditor.commit();
    }
    public String getZipCode(){
        return mPrefs.getString(KEY_ZIPCODE, NOTHING_SAVED);
    }

    public void saveState(String state){
        mEditor.putString(KEY_STATE, state);
        mEditor.apply();
        mEditor.commit();
    }
    public String getState(){
        return mPrefs.getString(KEY_STATE, NOTHING_SAVED);
    }

    public void saveTown(String town){
        mEditor.putString(KEY_TOWN, town);
        mEditor.apply();
        mEditor.commit();
    }
    public String getTown(){
        return mPrefs.getString(KEY_TOWN, NOTHING_SAVED);
    }









    /* Keep track if this is the users first time running app*/
    public Boolean getIsUsersFirstTimeRunningApp() {
        return mPrefs.getBoolean(KEY_FIRST_TIME_RUNNING_APP, true);
    }
    public void setIsUsersFirstTimeRunningApp(boolean isSet){
        mEditor.putBoolean(KEY_FIRST_TIME_RUNNING_APP, isSet);
        mEditor.apply();
        mEditor.commit();
    }

}
