package com.miproducts.miwatch.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.miproducts.miwatch.Container.WeatherLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ladam_000 on 8/2/2015.
 */
public class WeatherLocationDbHelper  extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weather_location";
    private static final String TAG = "DbHelper";


    public WeatherLocationDbHelper(Context context){
       super(context, DATABASE_NAME , null, DATABASE_VERSION);
   }


    public void addLocation(WeatherLocation wLocation){
        // grab a copy of the database.
        SQLiteDatabase database = this.getWritableDatabase();

        // lets stuff values for the row
        ContentValues insertableRow = new ContentValues();
        // ZIPCODE
        insertableRow.put(DbContractor.WeatherLoc.COLUMN_ZIPCODE, String.valueOf(wLocation.getZipcode()));
        // CITY
        insertableRow.put(DbContractor.WeatherLoc.COLUMN_CITY, wLocation.getCity());
        // TEMP
        insertableRow.put(DbContractor.WeatherLoc.COLUMN_TEMPERATURE, String.valueOf(wLocation.getTemperature()));

        float id = database.insert(DbContractor.WeatherLoc.TABLE_NAME, null, insertableRow);
        Log.d("DBHelper", "id of added item = " + id);
        database.close();

    }

    // Getting All Contacts
    public List<WeatherLocation> getAllWeatherLocations() {
        log("getAllWeatherLocations");
        List<WeatherLocation> weatherLocations = new ArrayList<WeatherLocation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DbContractor.WeatherLoc.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WeatherLocation weatherLocation = new WeatherLocation();
                // weather
                weatherLocation.setTemperature(Integer.parseInt(cursor.getString(0)));
                log("temp " + cursor.getString(2));
                // city
                weatherLocation.setCity(cursor.getString(1));
                log("city " + cursor.getString(1));
                // zipcode
                weatherLocation.setZipcode(cursor.getString(2));
                log("zipcode " + cursor.getString(0));
                // Adding Weather Location to list
                weatherLocations.add(weatherLocation);
            } while (cursor.moveToNext());
        }

        // return contact list
        return weatherLocations;
    }


    public boolean doesLocationExist(WeatherLocation wLocation){
        List<WeatherLocation> allLocations = getAllWeatherLocations();
        for(int i = 0; i < allLocations.size(); i++){
            if(wLocation.getZipcode().equals(allLocations.get(i).getZipcode())){
                return true;
            }
        }
        return false;
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContractor.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DbContractor.WeatherLoc.TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void updateTemperature(WeatherLocation locationToFill) {
        log("updateTemperature");
        SQLiteDatabase db = this.getWritableDatabase();

        List<WeatherLocation> weatherLocations = new ArrayList<WeatherLocation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DbContractor.WeatherLoc.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WeatherLocation weatherLocation = new WeatherLocation();
                // weather
                weatherLocation.setTemperature(Integer.parseInt(cursor.getString(0)));
                log("temp " + cursor.getString(2));
                // city
                weatherLocation.setCity(cursor.getString(1));
                log("city " + cursor.getString(1));
                // zipcode
                weatherLocation.setZipcode(cursor.getString(2));
                log("zipcode " + cursor.getString(0));
                // Adding Weather Location to list
                weatherLocations.add(weatherLocation);
            } while (cursor.moveToNext());
        }

        if(weatherLocations != null && weatherLocations.size() > 0){
            for(WeatherLocation loc : weatherLocations){
                // find the entry that has the same zipcode.
                if(loc.getZipcode().equals(locationToFill.getZipcode())){
                    // build up new value
                    ContentValues cv = new ContentValues();
                    cv.put(DbContractor.WeatherLoc.COLUMN_TEMPERATURE,
                            locationToFill.getTemperature());

                    //String sql = "UPDATE "+ DbContractor.WeatherLoc.TABLE_NAME + " SET " + DbContractor.WeatherLoc.COLUMN_TEMPERATURE + " = '" + locationToFill.getTemperature() + " WHERE " + DbContractor.WeatherLoc.COLUMN_ZIPCODE + " = " + locationToFill.getZipcode();

                    int result = db.update(DbContractor.WeatherLoc.TABLE_NAME,
                            cv,
                            DbContractor.WeatherLoc.COLUMN_ZIPCODE + " = ?",
                            new String[] {locationToFill.getZipcode()});

                    log("updated db, # rows effected = " + result);

                }
            }
        }
        db.close();
        cursor.close();

    }
}
