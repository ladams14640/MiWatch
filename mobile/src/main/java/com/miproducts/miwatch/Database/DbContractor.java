package com.miproducts.miwatch.Database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ladam_000 on 8/2/2015.
 */
public final class DbContractor {


    public DbContractor(){}

    // Database Name
    private static final String DATABASE_NAME = "WeatherLocations";

    public static abstract class WeatherLoc implements BaseColumns{
        public static final String TABLE_NAME = "TABLE_LOCATIONS";
        public static final String COLUMN_TEMPERATURE = "TEMPERATURE";
        public static final String COLUMN_CITY = "CITY";
        public static final String COLUMN_ZIPCODE = "ZIPCODE";
        public static final String COLUMN_TIME = "TIME";
        public static final String COLUMN_DESC = "DESCRIPTION";
        public static final String COLUMN_STATE = "STATE";

        //public static final String[] PROJECT_RETRIEVAL = {COLUMN_TEMPERATURE, COLUMN_CITY, COLUMN_ZIPCODE};
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String LONG_TYPE = " LONG";
    //private static final String INTEGER_TYPE = " INTEGER";
    //private static final String PRIMARY_TYPE = " INTEGER PRIMARY KEY";

    private static final String COMMA_SEP = ",";

    /*
        CREATE table_locations (_ID PRIMARY, _TEMP INTEGER, _CITY TEXT, _STATE TEXT, _ZIPCODE INTEGER)
     */


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherLoc.TABLE_NAME + " (" +
                    WeatherLoc.COLUMN_TEMPERATURE + TEXT_TYPE + COMMA_SEP +
                    WeatherLoc.COLUMN_CITY + TEXT_TYPE + COMMA_SEP +
                    WeatherLoc.COLUMN_ZIPCODE + TEXT_TYPE + COMMA_SEP +
                    WeatherLoc.COLUMN_DESC + TEXT_TYPE + COMMA_SEP +
                    WeatherLoc.COLUMN_TIME + LONG_TYPE + COMMA_SEP +
                    WeatherLoc.COLUMN_STATE + TEXT_TYPE + " )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeatherLoc.TABLE_NAME;
}
