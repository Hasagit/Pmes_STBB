package com.ruiduoyi.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import io.github.jp1017.serialport.SerialPort;

public class SerialPortService extends Service {
    private SerialPort serialPort;
    private InputStream in;
    private OutputStream out;


    public SerialPortService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            serialPort=new SerialPort(new File("/dev/ttyUSB10"),9600,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        in=serialPort.getInputStream();
        out=serialPort.getOutputStream();

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int size;
                    try {
                        byte[] buffer = new byte[12];
                        byte[] cardId=new byte[4];
                        if (in == null) return;
                        size = in.read(buffer);
                        if (size > 0) {
                            for (int i=7;i<11;i++){
                                cardId[i-7]=buffer[i];
                            }
                            //onDataReceived(buffer, size);
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=binary(cardId,10);
                            handler.sendMessage(msg);
                            Log.e("read",binary(cardId,10));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        thread.start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    String num=String.valueOf(msg.obj);
                    Intent intent=new Intent();
                    intent.putExtra("num",num);
                    intent.setAction("SerialPortNum");
                    getApplicationContext().sendBroadcast(intent);
                    break;
            }
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
