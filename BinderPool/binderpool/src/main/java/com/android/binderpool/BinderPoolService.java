package com.android.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description: binder连接池Server
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-17
 */
public class BinderPoolService extends Service{
    public static final int CODE_WEATHER = 1;
    public static final int CODE_COMPUTER = 2;

    private IBinderPoolManager iBinderPoolManager;

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
        iBinderPoolManager = new IBinderPoolManager.Stub(){
            @Override
            public IBinder queryCode(int code) throws RemoteException {
                switch (code){
                    case CODE_WEATHER:
                        return new IWeatherManager.Stub(){

                            @Override
                            public List<Weather> getWeather() throws RemoteException {
                                return weathers;
                            }

                            @Override
                            public void addWeather(Weather weather) throws RemoteException {
                                weathers.add(weather);
                            }
                        };
                    case CODE_COMPUTER:
                        return new IComputerManager.Stub() {
                            @Override
                            public double computeAverageTemperature(List<Weather> weathers) throws RemoteException {
                                double sum = 0;
                                for (int i=0; i<weathers.size(); i++){
                                    sum += weathers.get(i).temperature;
                                }
                                return sum/weathers.size();
                            }
                        };
                    default:
                        return null;
                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinderPoolManager.asBinder();
    }
}
