// IWeatherManager.aidl
package com.android.aidl;

// Declare any non-default types here with import statements
import com.android.aidl.Weather;

interface IWeatherManager {
    List<Weather> getWeather();
    void addWeather(in Weather weather);
}
