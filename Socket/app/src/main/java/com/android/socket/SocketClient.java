package com.android.socket;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-14
 */
public class SocketClient extends BaseActivity implements OnClickListener{
    private Button mSendButton;
    private EditText mMessageEditText;
    private PrintWriter mPrintWriter;
    private Socket mClientSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        mSendButton = (Button) findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
        mMessageEditText = (EditText) findViewById(R.id.msg);
        Intent service = new Intent(this, SocketServer.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                //选择和服务器相同的端口8688
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                SystemClock.sleep(1000);
            }
        }
        try {
            // 接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!isFinishing()) {
                String msg = br.readLine();
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    L.i("client has received '" + msg + "' at " + time);
                }
            }
            mPrintWriter.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent service = new Intent(this, SocketServer.class);
        stopService(service);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mSendButton) {
            final String msg = mMessageEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                //像服务器发送信息
                L.i("client has send '" + msg + "' at " + formatDateTime(System.currentTimeMillis()));
                mPrintWriter.println(msg);
                mMessageEditText.setText("");
            }
        }
    }

    private String formatDateTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }
}
