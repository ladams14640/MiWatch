package com.miproducts.miwatch.utilities;

/**
 * Created by larry on 7/13/15.
 */

import android.content.SharedPreferences;
import android.content.Context;

import com.miproducts.miwatch.config.MiDigitalUtil;

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
    // public



    public void setMainColor(String defaultColor){
        mEditor.putString(MiDigitalUtil.KEY_MAIN_COLOR, defaultColor);
        mEditor.apply();
        mEditor.commit();
    }

    public String getMainColor(){
        return mPrefs.getString(MiDigitalUtil.KEY_MAIN_COLOR, "nothing_set");
    }

    public void setHudRemove(boolean hudRemove){
        mEditor.putBoolean(MiDigitalUtil.KEY_HUD_ACTION, hudRemove);
        mEditor.apply();
        mEditor.commit();
    }
    public Boolean getHudRemove(){
        return mPrefs.getBoolean(MiDigitalUtil.KEY_HUD_ACTION, false);

    }

}
