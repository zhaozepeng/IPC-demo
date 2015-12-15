// IWeatherManager.aidl
package com.android.aidl;
import com.android.aidl.Weather;

// Declare any non-default types here with import statements

interface IWeatherManager {
    List<Weather> getWeather();
    void addWeather(in Weather weather);
}
