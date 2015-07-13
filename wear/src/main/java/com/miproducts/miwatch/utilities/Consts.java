package com.miproducts.miwatch.utilities;

import android.provider.CalendarContract;

/**
 * Created by Larry on 6/3/2015.
 */
public class Consts {


    public static final char DEGREE = 0x00B0;
    public static final int NORMAL_ALPHA = 255;

    public static final int MSG_UPDATE_TIME = 0;
    public static final int MSG_LOAD_MEETINGS = 1;

    public static final long NORMAL_UPDATE_RATE_MS = 500;


    public static final String KEY_CONFIG_REQUIRE_INTERVAL = "RequireInterval";
    public static final String KEY_CONFIG_TEMPERATURE_SCALE = "TemperatureScale";
    public static final String KEY_WEATHER_CONDITION = "Condition";
    public static final String KEY_WEATHER_SUNRISE = "Sunrise";
    public static final String KEY_WEATHER_SUNSET = "Sunset";
    public static final String KEY_CONFIG_THEME = "Theme";
    public static final String KEY_CONFIG_TIME_UNIT = "TimeUnit";
    public static final String KEY_WEATHER_TEMPERATURE = "Temperature";
    public static final String KEY_WEATHER_UPDATE_TIME = "Update_Time";
    public static final String PATH_CONFIG = "/DigitalWatchFaceService/Config/";
    public static final String PATH_WEATHER_INFO = "/WeatherWatchFace/WeatherInfo";
    public static final String PATH_WEATHER_REQUIRE = "/WeatherService/Require";
    public static final String COLON_STRING = ":";
    public static final String PACKAGE_NAME = Consts.class.getPackage().getName();
    public static final int ZIP_CODE = 04005;


    public final static int ID_FITNESS_HUD = 0;
    public final static int ID_TIMER_HUD = 1;
    public final static int ID_DEGREE_HUD = 3;
    public final static int ID_HUD = 4;
    public final static int ID_CALENDAR_HUD = 5;

    // For Calendar Query
    public static final String[] PROJECTION = {
            CalendarContract.Events._ID,                            // 0
            CalendarContract.Events.TITLE,                          // 1
            CalendarContract.Events.DESCRIPTION,                    //2
            CalendarContract.Events.DTSTART,                        //3
            CalendarContract.Events.DTEND,                          //4
    };

    // The indices for the projection array above.
    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_TITLE = 1;
    public static final int PROJECTION_DESCRIPTION = 2;
    public static final int PROJECTION_START = 3;
    public static final int PROJECTION_END = 4;

    // calendar.getTime().split(). these are the indices for that String array.
    // calendar[0] = Wed
    // calendar[1] = Dec
    // Calendar[2] == 31
    // Calendar[3] == military time
    // Calendar[4] == EST
    // calendar[5] == 2015

    public final static int CALENDAR_DAY_OF_WEEK = 0;
    public final static int CALENDAR_MONTH_TIME = 1;
    public final static int CALENDAR_DAY_OF_MONTH = 2;
    public final static int CALENDAR_MILITARY_TIME = 3;
    public final static int CALENDAR_YR_TIME = 5;


    /**
     *
     * PATH and KEYS for serializing Property data.
     *
     */
    public final static String WEARABLE_DATA_PATH = "/wearable_data";

    public static final String DIGITAL_TIMER_POS_API = "DIGITAL_TIMER_POS_API"; // x,y array of the digital time
    public static final String DIGITAL_TIMER_COLOR_API = "DIGITAL_TIMER_COLOR_API"; // color of time
    public static final String DIGITAL_SIZE_API = "DIGITAL_SIZE_API"; // size of time
    public static final String DIGITAL_VISIBLE_API = "DIGITAL_VISIBLE_API"; // visibility of time

    public static final String EVENT_POS_API = "EVENT_POS_API";
    public static final String EVENT_COLOR_API = "EVENT_COLOR_API";
    public static final String EVENT_SIZE_API = "EVENT_SIZE_API";
    public static final String EVENT_VISIBLE_API = "EVENT_VISIBLE_API";


    public static final String FITNESS_POS_API = "FITNESS_POS_API";
    public static final String FITNESS_COLOR_API = "FITNESS_COLOR_API";
    public static final String FITNESS_SIZE_API = "FITNESS_SIZE_API";
    public static final String FITNESS_VISIBLE_API = "FITNESS_VISIBLE_API";

    public static final String DEGREE_POS_API = "DEGREE_POS_API";
    public static final String DEGREE_COLOR_API = "DEGREE_COLOR_API";
    public static final String DEGREE_SIZE_API = "DEGREE_SIZE_API";
    public static final String DEGREE_VISIBLE_API = "DEGREE_VISIBLE_API";

    public static final String DATE_POS_API = "DATE_POS_API";
    public static final String DATE_COLOR_API = "DATE_COLOR_API";
    public static final String DATE_SIZE_API = "DATE_SIZE_API";
    public static final String DATE_VISIBLE_API = "DATE_VISIBLE_API";


    public static final String ALARM_COLOR_API = "ALARM_COLOR_API";
    public static final String ALARM_POS_API = "ALARM_POS_API";
    public static final String ALARM_SIZE_API = "ALARM_SIZE_API";
    public static final String ALARM_VISIBLE_API = "ALARM_VISIBLE_API";

    public static final String DEGREE_REFRESH = "DEGREE_REFRESH";

}