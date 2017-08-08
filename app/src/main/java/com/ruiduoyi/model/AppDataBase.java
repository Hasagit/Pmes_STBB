package com.ruiduoyi.model;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/6/12.
 */

public class AppDataBase {
    Context context;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    public AppDataBase(Context context) {
        this.context=context;
        openHelper=new SQLiteOpenHelper(context,"Rdyapp.db",null,1) {
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
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        database=openHelper.getReadableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }


    public void insertGpio(String mac,String jtbh,String zldm,String gpio,String time,int num,String desc){
        database.execSQL("insert into gpio_info (PadID,PadJtbh,PadZlCode,PadSignalNO,PadTime,PadVal,PadDesc)" +
                " values ('"+mac+"','"+jtbh+"','"+zldm+"',+'"+gpio+"','"+time+"',"+num+",'"+desc+"')");
        Log.w("insertgpio","insert into gpio_info (PadID,PadJtbh,PadZlCode,PadSignalNO,PadTime,PadVal,PadDesc)" +
                " values ('"+mac+"','"+jtbh+"','"+zldm+"',+'"+gpio+"','"+time+"',"+num+",'"+desc+"')");
    }


    public void deleteGpio(String time){
        database.execSQL("delete from gpio_info where PadTime ='"+time+"'");
        Log.e("delete",time);
    }

    public List<Map<String,String>> selectGpio(){
        List<Map<String,String>> list=new ArrayList<>();
        Cursor cursor=database.rawQuery("select * from gpio_info order by PadTime",null);
        int num=0;
        while (cursor.moveToNext()) {
            Map<String,String>item=new HashMap<>();
            item.put("mac",cursor.getString(1));
            item.put("jtbh",cursor.getString(2));
            item.put("zldm",cursor.getString(3));
            item.put("gpio",cursor.getString(4));
            item.put("time",cursor.getString(5));
            item.put("num",cursor.getInt(6)+"");
            item.put("desc",cursor.getString(7));
            num=num+1;
            if (num>10){
                break;
            }
            list.add(item);
            Log.e("sql",item.toString());
        }
        return list;
    }

}
