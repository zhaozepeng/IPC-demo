package com.android.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description: 天气服务端
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-15
 */
public class WeatherManagerService extends Service{

    //支持并发读写的list
    public CopyOnWriteArrayList<Weather> weathers = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Weather nanshan = new Weather();
        nanshan.cityName = "南山";
        nanshan.temperature = 20.5;
        nanshan.humidity = 45;
        nanshan.weather = Weather.AllWeather.cloudy;

        Weather futian = new Weather();
        futian.cityName = "福田";
        futian.temperature = 21.5;
        futian.humidity = 48;
        futian.weather = Weather.AllWeather.rain;

        weathers.add(nanshan);
        weathers.add(futian);
    }

    private Binder mBinder = new IWeatherManager.Stub() {
        @Override
        public List<Weather> getWeather() throws RemoteException {
            return weathers;
        }

        @Override
        public void addWeather(Weather weather) throws RemoteException {
            weathers.add(weather);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
