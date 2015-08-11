package com.miproducts.miwatch.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.miproducts.miwatch.Container.WeatherLocation;
import com.miproducts.miwatch.utilities.SettingsManager;

import java.security.Policy;
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

//TODO lets try nothing, i mean we are already setting to nothing up further in the chain, lets just save it such.
   public void addLocation(WeatherLocation wLocation){
        log("addLocation");
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
        // DESC
        insertableRow.put(DbContractor.WeatherLoc.COLUMN_DESC, wLocation.getDesc());
        // TIME_STAMP
        insertableRow.put(DbContractor.WeatherLoc.COLUMN_TIME, wLocation.getTime_stamp());
        // STATE
        insertableRow.put(DbContractor.WeatherLoc.COLUMN_STATE, wLocation.getState());
        log("addLocation state = " + wLocation.getState());

        float id = database.insert(DbContractor.WeatherLoc.TABLE_NAME, null, insertableRow);
        Log.d("DBHelper", "id of added item = " + id);
        database.close();

    }

    // Getting All Contacts
    //TODO lets grab desc and timestamp

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
                log("temp " + cursor.getString(0));
                // city
                weatherLocation.setCity(cursor.getString(1));
                log("city " + cursor.getString(1));
                // zipcode
                weatherLocation.setZipcode(cursor.getString(2));
                log("zipcode " + cursor.getString(2));
                // desc
                weatherLocation.setDesc(cursor.getString(3));
                log("desc " + cursor.getString(3));
                // time_stamp
                weatherLocation.setTime_stamp(cursor.getLong(4));
                log("milliseconds " + cursor.getLong(4));

                // state
                weatherLocation.setState(cursor.getString(5));
                log("state2 " + cursor.getString(5));


                // Adding Weather Location to list
                weatherLocations.add(weatherLocation);
            } while (cursor.moveToNext());
        }
        log("done with getAllWeatherLocations");
        // return contact list
        return weatherLocations;
    }


    public boolean doesLocationExist(WeatherLocation wLocation){
        log("doesLocationExist");
        List<WeatherLocation> allLocations = getAllWeatherLocations();
        for(int i = 0; i < allLocations.size(); i++){
            // already have that zipcode
            if(wLocation.getZipcode().equals(allLocations.get(i).getZipcode()) &&
                    // Dont want to bother if they have the NOTHING flag.
                    !wLocation.getZipcode().equals(SettingsManager.NOTHING_SAVED)){
                log("zipcode = " + wLocation.getZipcode() + " = " + allLocations.get(i).getZipcode());
                log("already have that city and state = " + wLocation.getState() + " " + wLocation.getCity());
                log("already have that city and state = " + allLocations.get(i).getState() + " " + allLocations.get(i).getCity());
                return true;
            }
            // already have that city and town
            else if(wLocation.getState().equals(allLocations.get(i).getState()) &&
                    wLocation.getCity().equals(allLocations.get(i).getCity())){
                log("already have that city and state = " + wLocation.getState() + " " + wLocation.getCity());
                log("already have that city and state = " + allLocations.get(i).getState() + " " + allLocations.get(i).getCity());
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


    //TODO letsm ake sure we update the time stamp and description

    /**
     *
     * @param locationToFill - location should be stuffed with what it needs.
     * @param isTown - whether we came from town or zipcode
     */
    public void updateTemperatureAndTime(WeatherLocation locationToFill, boolean isTown) {
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
                log("temp " + cursor.getString(0));
                // city
                weatherLocation.setCity(cursor.getString(1));
                log("city " + cursor.getString(1));

                // from zipcode
                if(!isTown){
                    // zipcode
                    weatherLocation.setZipcode(cursor.getString(2));
                    log("zipcode " + cursor.getString(2));
                    // desc
                    weatherLocation.setDesc(cursor.getString(3));
                    // time_stamp
                    weatherLocation.setTime_stamp(cursor.getLong(4));

                }
                // from town and state not zipcode
                else {
                    // desc
                    weatherLocation.setDesc(cursor.getString(3));
                    // time_stamp
                    weatherLocation.setTime_stamp(cursor.getLong(4));

                    //state
                    weatherLocation.setState(cursor.getString(5));
                    log("state3 " + cursor.getString(5));
                }

                // Adding Weather Location to list
                weatherLocations.add(weatherLocation);
            } while (cursor.moveToNext());
        }

        //TODO lets not iterate over this seperately when we are done.
        if(weatherLocations != null && weatherLocations.size() > 0){
            for(WeatherLocation loc : weatherLocations) {
                // we set this location - when we instantiated this dbHelper.
                if (!isTown) {
                    log("isFromTown = " + isTown);
                    if (loc.getZipcode().equals(locationToFill.getZipcode())) {
                        log("got a match");
                        // build up new value
                        ContentValues cv = new ContentValues();
                        // save temp
                        cv.put(DbContractor.WeatherLoc.COLUMN_TEMPERATURE,
                                locationToFill.getTemperature());
                        // save time
                        cv.put(DbContractor.WeatherLoc.COLUMN_TIME, locationToFill.getTime_stamp());
                        // save desc
                        cv.put(DbContractor.WeatherLoc.COLUMN_DESC, locationToFill.getDesc());

                        //String sql = "UPDATE "+ DbContractor.WeatherLoc.TABLE_NAME + " SET " + DbContractor.WeatherLoc.COLUMN_TEMPERATURE + " = '" + locationToFill.getTemperature() + " WHERE " + DbContractor.WeatherLoc.COLUMN_ZIPCODE + " = " + locationToFill.getZipcode();

                        int result = db.update(DbContractor.WeatherLoc.TABLE_NAME,
                                cv,
                                DbContractor.WeatherLoc.COLUMN_ZIPCODE + " = ?",
                                new String[]{locationToFill.getZipcode()});

                        log("updated db, # rows effected = " + result);

                    }
                }
                //TODO duplicate code - lets DRY it out
                // from town and state
                else {
                    log("isFromTown = " + isTown);
                    // we already had it.
                    if(loc.getCity().equals(locationToFill.getCity()) && loc.getState().equals(locationToFill.getState())){
                        log("got a match");
                        // build up new value
                        ContentValues cv = new ContentValues();
                        // save temp
                        cv.put(DbContractor.WeatherLoc.COLUMN_TEMPERATURE,
                                locationToFill.getTemperature());
                        // save time
                        cv.put(DbContractor.WeatherLoc.COLUMN_TIME, locationToFill.getTime_stamp());
                        // save desc
                        cv.put(DbContractor.WeatherLoc.COLUMN_DESC, locationToFill.getDesc());

                        //String sql = "UPDATE "+ DbContractor.WeatherLoc.TABLE_NAME + " SET " + DbContractor.WeatherLoc.COLUMN_TEMPERATURE + " = '" + locationToFill.getTemperature() + " WHERE " + DbContractor.WeatherLoc.COLUMN_ZIPCODE + " = " + locationToFill.getZipcode();
                        //TODO need to test.
                        int result = db.update(DbContractor.WeatherLoc.TABLE_NAME,
                                cv,
                                DbContractor.WeatherLoc.COLUMN_STATE + " = ? AND " + DbContractor.WeatherLoc.COLUMN_CITY + " = ?",
                                new String[]{locationToFill.getState(), locationToFill.getCity()});

                        log("updated db, # rows effected = " + result);
                    }
                }
            }

        }
        db.close();
        cursor.close();

    }




}
