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

}
