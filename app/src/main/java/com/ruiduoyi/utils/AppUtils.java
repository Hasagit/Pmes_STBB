package com.ruiduoyi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ruiduoyi.model.NetHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengJf on 2017/7/1.
 */

public class AppUtils {
    public static List<Activity>activityList;

    public static void addActivity(Activity activity){
       activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        for (int i=0;i<activityList.size();i++){
            if (activityList.get(i).getLocalClassName().equals(activity.getLocalClassName())){
                activityList.remove(i);
            }
        }
    }

    public static Activity getActivity(String activityClassName){
        for (int i=0;i<activityList.size();i++){
            String name=activityList.get(i).getLocalClassName();
            if (activityList.get(i).getLocalClassName().equals(activityClassName)){
                return activityList.get(i);
            }
        }
        return null;
    }

    public static void removeActivityWithoutThis(Activity activity){
       for (int i=0;i<activityList.size();i++){
            if (!activityList.get(i).getLocalClassName().equals(activity.getLocalClassName())){
                activityList.get(i).finish();
                activityList.remove(i);
            }
        }
    }

    public static void removAllActivity(){
        for (int i=0;i<activityList.size();i++){
            activityList.get(i).finish();
        }
    }





    public static String getAppVersionName(Context context) {
        String versionName = "";
        int versioncode=0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    public static void DownLoadFileByUrl(String url_str,String filePath,String fileName){
        URL url= null;
        try {
            File file=new File(filePath);
           if (!file.exists()){
               file.mkdir();
           }
            url = new URL(url_str);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            InputStream in=urlConnection.getInputStream();
            OutputStream out=new FileOutputStream(filePath+"/"+fileName,false);
            byte[] buff=new byte[1024];
            int size;
            while ((size = in.read(buff)) != -1) {
                out.write(buff, 0, size);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void setSystemTime(final Context cxt, String datetimes) {
        // yyyyMMdd.HHmmss】
        /**
         * 可用busybox 修改时间
         */
        /*
         * String
         * cmd="busybox date  \""+bt_date1.getText().toString()+" "+bt_time1
         * .getText().toString()+"\""; String cmd2="busybox hwclock  -w";
         */
        try {
            Process process = Runtime.getRuntime().exec("su");
//          String datetime = "20131023.112800"; // 测试的设置的时间【时间格式
            String datetime = ""; // 测试的设置的时间【时间格式
            datetime = datetimes.toString(); // yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            //Toast.makeText(cxt, "请获取Root权限", Toast.LENGTH_SHORT).show();
        }
    }


    public static void uploadNetworkError(String errms,String jtbh,String mac){
        NetHelper.getRunsqlResult("Exec PAD_Add_PadLogInfo '7','"+errms+"','"+jtbh+"','"+mac+"'");
    }

    public static boolean uploadErrorMsg(String errms,String jtbh,String mac,String code){
        String sql="Exec PAD_Add_PadLogInfo '"+code+"','"+errms+"','"+jtbh+"','"+mac+"'";
        boolean result=NetHelper.getRunsqlResult(sql);
        Log.e("","");
        return result;
    }

    public static void sendCountdownReceiver(Context context){
        Intent intent=new Intent();
        intent.setAction("com.Ruiduoyi.CountdownToInfo");
        context.sendBroadcast(intent);
    }



}
