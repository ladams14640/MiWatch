package com.miproducts.miwatch.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.miproducts.miwatch.MiDigitalWatchFace;
import com.miproducts.miwatch.R;

/**
 * Created by larry on 7/8/15.
 */
public class SettingsManager {
    public final static String SHARED_PREFERENCES_NAME = "GOOGLEAPICLIENT_PREFERENCE";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    public SettingsManager(Context applicationContext) {
        mPrefs = applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }



    public void writeToPreferences(String key, int value){
        mEditor.putInt(key,value);
        mEditor.apply();
        mEditor.commit();
    }

    public void writeToPreferences(String key, String value){
        mEditor.putString(key, value);
        mEditor.apply();
        mEditor.commit();
    }

    public void writeToPreferences(String key, boolean value){
        mEditor.putBoolean(key, value);
        mEditor.apply();
        mEditor.commit();
    }

    public int getIntFromPreferences(String key){
        return mPrefs.getInt(key, 0);
    }

    public String getStringFromPreferences(String key){
        return mPrefs.getString(key, "null");
    }

    public boolean getBoolFromPreferences(String key){
        return mPrefs.getBoolean(key, false);
    }


    public static final String DIGITAL_TIME_X = "DIGITAL_TIME_X";
}
