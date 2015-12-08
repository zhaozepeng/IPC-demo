package com.android.messenger_a;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.android.libcore.log.L;

/**
 * Description: 服务器端service，用来接受client端发送过来的对象
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-08
 */
public class ServerWithHandler extends Service{
    Messenger messenger = null;

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    L.i("i receive" + msg.getData().getString("message"));
                    Messenger client = msg.replyTo;

                    //回应客户端
                    if (client != null){
                        Message reply = Message.obtain();
                        Bundle message = new Bundle();
                        message.putString("message", "i have received your message");
                        L.i("i have received your message");
                        reply.setData(message);
                        try {
                            client.send(reply);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    L.i("i receive" + msg.getData().getString("message"));
                    L.i("client has disconnect this connection, bye~");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(new MessengerHandler());
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
