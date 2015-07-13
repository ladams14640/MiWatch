package com.miproducts.miwatch.Weather;

import android.content.Context;

public interface IWeatherApi {
    WeatherInfo getCurrentWeatherInfo(double lon, double lat);
}
