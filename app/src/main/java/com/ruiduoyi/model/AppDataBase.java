package com.ruiduoyi.model;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/6/12.
 */

public  class AppDataBase {
    Context context;
    private SQLiteOpenHelper openHelper;
    private  SQLiteDatabase database;

    public AppDataBase(Context context) {
        this.context=context;
        openHelper=new SQLiteOpenHelper(context,"Rdyapp.db",null,3) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists gpio_info("
                        +"id integer primary key autoincrement,"
                        +"PadID varchar not null,"
                        +"PadJtbh varchar not null,"
                        +"PadZlCode varchar not null,"
                        +"PadSignalNO varchar not null,"
                        +"PadTime datetime not null,"
                        +"PadVal int not null,"
                        +"PadDesc varchar not null)");

                db.execSQL("create table if not exists file_info("
                        +"file_name varchar primary key,"
                        +"file_ver varchar not null)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("create table if not exists gpio_info("
                        +"id integer primary key autoincrement,"
                        +"PadID varchar not null,"
                        +"PadJtbh varchar not null,"
                        +"PadZlCode varchar not null,"
                        +"PadSignalNO varchar not null,"
                        +"PadTime datetime not null,"
                        +"PadVal int not null,"
                        +"PadDesc varchar not null)");
                db.execSQL("create table if not exists file_info("
                        +"file_name varchar primary key,"
                        +"file_ver varchar not null)");

                //上传成功的gpio信号
                db.execSQL("create table if not exists upload_gpio("
                        +"id integer primary key autoincrement,"
                        +"PadID varchar not null,"
                        +"PadJtbh varchar not null,"
                        +"PadZlCode varchar not null,"
                        +"PadSignalNO varchar not null,"
                        +"PadTime datetime not null,"
                        +"PadVal int not null,"
                        +"PadDesc varchar not null)");

                //采集到的gpio信号
                db.execSQL("create table if not exists coll_gpio("
                        +"id integer primary key autoincrement,"
                        +"PadID varchar not null,"
                        +"PadJtbh varchar not null,"
                        +"PadZlCode varchar not null,"
                        +"PadSignalNO varchar not null,"
                        +"PadTime datetime not null,"
                        +"PadVal int not null,"
                        +"PadDesc varchar not null)");
                Log.e("sql_update","succes");
            }
        };
        database=openHelper.getReadableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public  void insertGpio(String mac,String jtbh,String zldm,String gpio,String time,int num,String desc){
        synchronized(this) {
            database.execSQL("insert into gpio_info (PadID,PadJtbh,PadZlCode,PadSignalNO,PadTime,PadVal,PadDesc)" +
                    " values ('" + mac + "','" + jtbh + "','" + zldm + "','" + gpio + "','" + time + "'," + num + ",'" + desc + "')");
        }
        Log.w("insertgpio","insert into gpio_info (PadID,PadJtbh,PadZlCode,PadSignalNO,PadTime,PadVal,PadDesc)" +
                " values ('"+mac+"','"+jtbh+"','"+zldm+"','"+gpio+"','"+time+"',"+num+",'"+desc+"')");
    }

    public void insertCollGpio(String mac,String jtbh,String zldm,String gpio,String time,int num,String desc){
        synchronized(this) {
            database.execSQL("insert into coll_gpio (PadID,PadJtbh,PadZlCode,PadSignalNO,PadTime,PadVal,PadDesc)" +
                    " values ('" + mac + "','" + jtbh + "','" + zldm + "','" + gpio + "','" + time + "'," + num + ",'" + desc + "')");
        }
    }


    public void insertUploadGpio(String mac,String jtbh,String zldm,String gpio,String time,int num,String desc){
        synchronized(this) {
            database.execSQL("insert into upload_gpio (PadID,PadJtbh,PadZlCode,PadSignalNO,PadTime,PadVal,PadDesc)" +
                    " values ('" + mac + "','" + jtbh + "','" + zldm + "','" + gpio + "','" + time + "'," + num + ",'" + desc + "')");
        }
    }


    public  void deleteGpio(String time){
        synchronized(this) {
            database.execSQL("delete from gpio_info where PadTime ='"+time+"'");
        }
        Log.e("delete",time);
    }

    public  void deleteThreeDayCollGpio(){
        synchronized(this) {
            Date date=new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateStr=simpleDateFormat.format(date);

            database.execSQL("delete from coll_gpio where PadTime <date('"+dateStr+"','start of day','-2 days')");
            Log.e("del_sql","delete from coll_gpio where PadTime <date('"+dateStr+"','start of day','-3 days')");
        }
    }

    public  void deleteThreeDayUploadGpio(){
        synchronized(this) {
            Date date=new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateStr=simpleDateFormat.format(date);
            database.execSQL("delete from upload_gpio where PadTime <date('"+dateStr+"','start of day','-2 days')");
            Log.e("del_sql","delete from upload_gpio where PadTime <date('"+dateStr+"','start of day','-3 days')");
        }
    }


    public  List<Map<String,String>> selectGpio(){
        synchronized(this) {
            List<Map<String,String>> list=new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from gpio_info order by PadTime limit 0,10", null);
            int num = 0;
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("mac", cursor.getString(1));
                item.put("jtbh", cursor.getString(2));
                item.put("zldm", cursor.getString(3));
                item.put("gpio", cursor.getString(4));
                item.put("time", cursor.getString(5));
                item.put("num", cursor.getInt(6) + "");
                item.put("desc", cursor.getString(7));
                num = num + 1;
                if (num > 10) {
                    break;
                }
                list.add(item);
                Log.e("sql", item.toString());
            }
            cursor.close();
            return list;
        }
    }

    public boolean comparedFileVer(String file_name,String new_ver){
        synchronized(this) {
            String old_ver="";
            Cursor cursor = database.rawQuery("select file_ver from file_info where file_name='"+file_name+"'", null);
            while (cursor.moveToNext()) {
               old_ver=cursor.getString(0);
            }
            //Log.e("old&new",old_ver+"   "+new_ver);
            if (old_ver.equals("")){
                return false;
            }else if (!old_ver.equals(new_ver)){
                return false;
            }else {
                return true;
            }
        }
    }

    public void insertFile_info(String file_name,String file_ver){
        synchronized(this) {
            Cursor cursor=database.rawQuery("select file_name from file_info where file_name='"+file_name+"'",null);
           if (cursor.moveToNext()){
               database.execSQL("update file_info set file_ver='"+file_ver+"' where file_name='"+file_name+"'");
               return;
           }
            database.execSQL("insert into file_info (file_name,file_ver)" +
                    " values ('"+file_name+"','"+file_ver+"')");
        }
    }

    public void closeDataBase(){
        database.close();
    }

}
