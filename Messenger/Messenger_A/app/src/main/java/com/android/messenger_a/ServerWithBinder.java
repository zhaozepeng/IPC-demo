package com.android.messenger_a;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Description: 服务器端service，用来接受client端发送过来的对象
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-08
 */
public class ServerWithBinder extends Service{
    private InnerBinder binder;

    private class InnerBinder extends Binder {
        public ServerWithBinder getServer(){
            return ServerWithBinder.this;
        }
    }

    public int add(int... ints){
        int sum = 0;
        for (int temp : ints){
            sum += temp;
        }
        return sum;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new InnerBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new InnerBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
