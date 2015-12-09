package com.android.messenger_b;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;

import java.text.SimpleDateFormat;

/**
 * Description: 客户端的进程，用来和服务器沟通
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-08
 */
public class ClientForHandler extends BaseActivity implements View.OnClickListener{

    private Button connect_handler;
    private TextView tv_handler;
    private Button connect_binder;
    private TextView tv_binder;

    private ServiceConnection serviceConnection;
    private Messenger serverMessenger;
    private Messenger messenger;

    private boolean hasBindService = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_handler);
        connect_handler = (Button) findViewById(R.id.connect_handler);
        connect_handler.setOnClickListener(this);
        tv_handler = (TextView) findViewById(R.id.tv_handler);
        connect_binder = (Button) findViewById(R.id.connect_binder);
        connect_binder.setOnClickListener(this);
        tv_binder = (TextView) findViewById(R.id.tv_binder);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serverMessenger = new Messenger(service);
                communicate();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serverMessenger = null;
            }
        };

        messenger = new Messenger(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                L.i("i have received '" + msg.getData().getString("message") + "'");
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("message", "OK, bye bye~");
                message.setData(bundle);
                L.i("i have send '" + message.getData().getString("message") + "'");
                message.what = 2;
                if (serverMessenger != null){
                    try {
                        serverMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void communicate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Message message = Message.obtain();
        Bundle msg = new Bundle();
        msg.putString("message", "i have send handler a message at " + simpleDateFormat.format(System.currentTimeMillis()));
        message.setData(msg);
        L.i("i have send '" + message.getData().getString("message") + "'");
        message.what = 1;
        message.replyTo = messenger;
        if (serverMessenger != null){
            try {
                serverMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connect_handler:
                if (!hasBindService) {
                    Intent intent = new Intent();
                    intent.setClassName("com.android.messenger_a", "com.android.messenger_a.ServerWithHandler");
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                    hasBindService = true;
                }else{
                    if (serverMessenger == null){
                        return;
                    }
                    communicate();
                }
                break;
            case R.id.connect_binder:
                startActivity(new Intent(this, ClientForBinder.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverMessenger != null)
            unbindService(serviceConnection);
    }
}
