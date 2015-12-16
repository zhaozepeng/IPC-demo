package com.android.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.android.libcore.log.L;

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
    public RemoteCallbackList<IWeatherChangeListener> listeners = new RemoteCallbackList<>();

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
            L.i("server returns all of the weathers");
            return weathers;
        }

        @Override
        public void addWeather(Weather weather) throws RemoteException {
            weathers.add(weather);
            L.i("server add new Weather:" + weather.cityName);

            int N = listeners.beginBroadcast();
            for (int i=0; i<N; i++){
                IWeatherChangeListener listener = listeners.getBroadcastItem(i);
                listener.onWeatherChange(weather);
            }
            L.i("server notify the listener that weathers have been changed");
            listeners.finishBroadcast();
        }

        @Override
        public void addListener(IWeatherChangeListener listener) throws RemoteException {
            L.i("server adding listener");
            listeners.register(listener);
        }

        @Override
        public void removeListener(IWeatherChangeListener listener) throws RemoteException {
            L.i("server removing listener");
            listeners.unregister(listener);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int permission = checkCallingPermission("com.android.permission.WRITEWEATHERPERMISSION");
            //检测客户端是否声明权限
            if (permission == PackageManager.PERMISSION_DENIED){
                L.e("permission denied");
                return false;
            }
            L.i("permission granted");

            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0){
                String packageName = packages[0];
                if (!packageName.startsWith("com.android")){
                    L.e("package name not accept");
                    return false;
                }
                L.i("package name accept");
            }
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
