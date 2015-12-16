// IWeatherManager.aidl
package com.android.aidl;

// Declare any non-default types here with import statements
import com.android.aidl.Weather;
import com.android.aidl.IWeatherChangeListener;

interface IWeatherManager {
    List<Weather> getWeather();
    void addWeather(in Weather weather);
    void addListener(in IWeatherChangeListener listener);
    void removeListener(in IWeatherChangeListener listener);
}
