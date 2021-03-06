package com.ruiduoyi.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Gpio;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.ImageView;

import com.glongtech.gpio.GpioEvent;
import com.ruiduoyi.R;
import com.ruiduoyi.model.AppDataBase;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GpioService extends Service {
    private int i=0;
    private static GpioEvent event_gpio;
    private AppDataBase dataBase;
    private SharedPreferences sharedPreferences;
    private String mac,jtbh;
    private Timer timer_gpio;
    public GpioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("gpio_service","onCreate");
        initData();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("gpio_service","onStartCommand");
        if (intent!=null){
            jtbh=intent.getStringExtra("jtbh");
            mac=intent.getStringExtra("mac");
        }else {
            jtbh=sharedPreferences.getString("jtbh","");
            mac=sharedPreferences.getString("mac","");
            if (sharedPreferences.getString("service_ip","").equals("")){
                NetHelper.URL="http://"+getString(R.string.service_ip)+":8080/Service1.asmx";
            }else {
                NetHelper.URL="http://"+sharedPreferences.getString("service_ip","")+":8080/Service1.asmx";
            }
        }
        Log.w("starCommand",jtbh+"   "+mac);
        //initGpio();
        //initData();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData(){
        dataBase=new AppDataBase(this);
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        initGpio();
        updateGpio();
    }



    public void initGpio(){
        int g1=Gpio.SetGpioInput("gpio1");
        int g2=Gpio.SetGpioInput("gpio2");
        int g3=Gpio.SetGpioInput("gpio3");
        int g4=Gpio.SetGpioInput("gpio4");
        event_gpio = new GpioEvent() {
            @Override
            public void onGpioSignal(int index,boolean level) {
                long time=System.currentTimeMillis();
                Date date=new Date(time);
                SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                //发广播给MainActivity接收
                final String ymd_hms=format2.format(date);
                Intent intent=new Intent();
                intent.putExtra("index",index);
                intent.putExtra("level",level);
                intent.setAction("com.ruiduoyi.GpioSinal");
                getApplicationContext().sendBroadcast(intent);


                switch (index){
                    case 1:
                        if(level){
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dataBase.insertGpio(mac,jtbh,"A","1",ymd_hms,1,"");
                                    dataBase.insertCollGpio(mac,jtbh,"A","1",ymd_hms,1,"");
                                    //dataBase.insertGpio2(mac,jtbh,"A","1",ymd_hms,1,"");
                                    dataBase.selectGpio();
                                }
                            }).start();
                        }
                        break;
                    case 2:
                        if(level){

                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dataBase.insertGpio(mac,jtbh,"A","2",ymd_hms,1,"");
                                    dataBase.insertCollGpio(mac,jtbh,"A","2",ymd_hms,1,"");
                                    //dataBase.insertGpio2(mac,jtbh,"A","2",ymd_hms,1,"");
                                }
                            }).start();
                        }

                        break;
                    case 3:
                        if(level){

                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dataBase.insertGpio(mac,jtbh,"A","3",ymd_hms,1,"");
                                    dataBase.insertCollGpio(mac,jtbh,"A","3",ymd_hms,1,"");
                                    //dataBase.insertGpio2(mac,jtbh,"A","3",ymd_hms,1,"");
                                }
                            }).start();
                        }
                        break;
                    case 4:
                        if(level){

                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dataBase.insertGpio(mac,jtbh,"A","4",ymd_hms,1,"");
                                    dataBase.insertCollGpio(mac,jtbh,"A","4",ymd_hms,1,"");
                                    //dataBase.insertGpio2(mac,jtbh,"A","4",ymd_hms,1,"");
                                }
                            }).start();
                        }
                        break;
                    default:
                        break;
                }
                if (level){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadGpioData();
                        }
                    }).start();
                }
            }
        };
        event_gpio.MyObserverStart();
    }

    private void updateGpio(){
        timer_gpio=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                uploadGpioData();
            }
        };
        timer_gpio.schedule(timerTask,0,Integer.parseInt(getString(R.string.gpio_update_time)));
    }

    private synchronized void uploadGpioData(){
        Log.w("Synchronized_start","1");
        String sql="";
        List<Map<String,String>> list=dataBase.selectGpio();
        for (int i=0;i<list.size();i++){
            Map<String,String>map=list.get(i);
            String mac=map.get("mac");
            String jtbh=map.get("jtbh");
            String zldm=map.get("zldm");
            String gpio=map.get("gpio");
            String time=map.get("time");
            String num=map.get("num");
            String desc=map.get("desc");
            sql="exec PAD_SrvDataUp '"+mac+"','"+jtbh+"','"+zldm+"','"+gpio+"','"+time+"',"+num+",'"+desc+"'\n";
            try {
                JSONArray list_result= NetHelper.getQuerysqlResultJsonArray(sql);
                if (list_result!=null){
                    if (list_result.length()>0){
                        if (list_result.getJSONObject(0).getString("Column1").equals("OK")){
                            //handler.sendEmptyMessage(0x106);
                            dataBase.insertUploadGpio(mac,jtbh,"A",gpio,time,1,"");
                            dataBase.deleteGpio(time);
                        }else {
                            break;
                        }
                    }else {
                        break;
                    }
                }else {
                    NetHelper.uploadNetworkError("exec PAD_SrvDataUp NetWorkError",jtbh,mac);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.w("Synchronized_start","2");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dataBase.closeDataBase();
        Log.w("gpio_service","onDestroy");
        //timer_gpio.cancel();
    }
}
