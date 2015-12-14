package com.android.socket;

import android.app.Service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Intent;
import android.os.IBinder;

import com.android.libcore.log.L;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-14
 */
public class SocketServer extends Service{
    private boolean mIsServiceDestroyed = false;
    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket;
            try {
                //监听8688端口
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                L.e(e);
                return;
            }
            while (!mIsServiceDestroyed) {
                try {
                    // 接受客户端请求，并且阻塞直到接收到消息
                    final Socket client = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        while (!mIsServiceDestroyed) {
            String str = in.readLine();
            if (str == null) {
                break;
            }
            L.i("server has received '" + str +"'");
            String message = "server has received your message";
            out.println(message);
        }
        out.close();
        in.close();
        client.close();
    }
}
