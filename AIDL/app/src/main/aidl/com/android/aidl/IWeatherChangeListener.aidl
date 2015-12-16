// IWeatherChangeListener.aidl
package com.android.aidl;
import com.android.aidl.Weather;

// Declare any non-default types here with import statements

interface IWeatherChangeListener {
    void onWeatherChange(in Weather newWeather);
}
