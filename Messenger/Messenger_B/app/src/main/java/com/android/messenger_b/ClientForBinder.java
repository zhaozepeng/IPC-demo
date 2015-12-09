package com.android.messenger_b;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.view.View;

import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-08
 */
public class ClientForBinder extends BaseActivity implements View.OnClickListener{
    private ServiceConnection serviceConnection;
    private Messenger serverMessenger;
    private IBinder mBoundService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_binder);
        findViewById(R.id.connect_binder).setOnClickListener(this);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    mBoundService = service;
                    Class clazz = mBoundService.getClass();
                    //如果两个应用程序没有运行在同一个进程中，则无法反射到该函数
                    Method method = clazz.getDeclaredMethod("getServer");
                    Object object = method.invoke(mBoundService);
                    Class messenger = object.getClass();
                    Method add = messenger.getDeclaredMethod("add", int[].class);
                    L.e("1+2+3=" + add.invoke(object, new int[]{1,2,3}));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serverMessenger = null;
            }
        };
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClassName("com.android.messenger_a", "com.android.messenger_a.ServerWithBinder");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverMessenger != null)
            unbindService(serviceConnection);
    }
}
