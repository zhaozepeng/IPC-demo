package com.android.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.android.libcore_ui.activity.BaseActivity;

import java.util.List;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-15
 */
public class ClientActivity extends BaseActivity implements View.OnClickListener{

    private ServiceConnection serviceConnection = null;
    private IWeatherManager weatherManager;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                weatherManager = IWeatherManager.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                weatherManager = null;
            }
        };
        Intent intent = new Intent(this, WeatherManagerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_query){
            try {
                List<Weather> weathers = weatherManager.getWeather();
                StringBuilder sb = new StringBuilder();
                for (Weather weather : weathers){
                    sb.append(weather.cityName).append("\n");
                    sb.append("humidity:").append(weather.humidity)
                        .append("temperature").append(weather.temperature)
                        .append("weather").append(weather.weather).append("\n");
                }
                tv_content.setText(sb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else if (v.getId() == R.id.btn_add){
            Weather weather = new Weather();
            weather.weather = Weather.AllWeather.cloudy;
            weather.humidity = 25.5;
            weather.temperature = 19.5;
            weather.cityName = "罗湖";
            try {
                weatherManager.addWeather(weather);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
