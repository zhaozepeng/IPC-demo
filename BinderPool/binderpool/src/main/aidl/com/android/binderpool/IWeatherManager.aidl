// IWeatherManager.aidl
package com.android.binderpool;

// Declare any non-default types here with import statements
import com.android.binderpool.Weather;

interface IWeatherManager {
    List<Weather> getWeather();
    void addWeather(in Weather weather);
}
