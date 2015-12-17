// IComputerManager.aidl
package com.android.binderpool;

import com.android.binderpool.Weather;

interface IComputerManager {
    double computeAverageTemperature(in List<Weather> weathers);
}
