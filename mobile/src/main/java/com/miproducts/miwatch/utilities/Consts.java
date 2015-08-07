package com.miproducts.miwatch.utilities;

import android.provider.CalendarContract;

/**
 * Created by Larry on 6/3/2015.
 */
public class Consts {

    // represents the Degree Symbol
    public static final char DEGREE_SIGN = 0x00B0;

    // Heuristic
    public final static int canvasWidth = 600;
    public final static int canvasHeight = 600;

    // TIME
    public static final int xTimePosition = canvasWidth/2;
    public static final int yTimePosition = 142;
    public static final int sizeDigitalTime = 100;
    // EVENT
    public static final int xEventPosition = 57;
    public static final int yEventPosition = (int) (canvasHeight * (.4));

    // HUD
    public static final int yHudPosition = (int) (canvasHeight * (.3));

    // DateViews
    public static final int xDatePosition = 100;
    public static final int yDatePositions = 120;

    /** For selection purposes */
    public static final int NONE = 0;
    public static final int DIGITAL_TIMER = 1;
    public static final int EVENT = 2;
    public static final int FITNESS = 3;
    public static final int DEGREE = 4;
    public static final int DATE = 5;
    public static final int ALARM_TIMER = 6;

    // Path for serializing data.
    /**
     *
     * PATH and KEYS for serializing Property data.
     *
     */
    public final static String PHONE_TO_WEARABLE_PATH = "/wearable_data"; // path from phone to watch
    public final static String WEARABLE_TO_PHONE_PATH = "/wearable_to_phone"; // from watch to phone

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


    public static final String PHONE_TO_WEARABLE_DEGREE = "PHONE_TO_WEARABLE_DEGREE";


    public static final String BROADCAST_DEGREE = "com.miproducts.miwatch.DEGREE";
    public static final String KEY_BROADCAST_DEGREE = "KEY_BROADCAST_DEGREE";

  //  public static final String
  //  public static final String
  //  public static final String
 //   public static final String


}