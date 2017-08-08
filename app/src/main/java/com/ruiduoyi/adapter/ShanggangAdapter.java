package com.ruiduoyi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/6/15.
 */

public class ShanggangAdapter extends ArrayAdapter{
    List<Map<String,String>>data;
    int resourse;
    String rym_id,jtbh;
    public ShanggangAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull   List<Map<String,String>>data,String rym_id,String jtbh) {
        super(context, resource, data);
        this.data=data;
        this.resourse=resource;
        this.rym_id=rym_id;
        this.jtbh=jtbh;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    data.remove(msg.arg1);
                    notifyDataSetChanged();
                    Intent intent=new Intent();
                    intent.setAction("UpdateInfoFragment");
                    getContext().sendBroadcast(intent);
                    break;
                case 0x101:
                    Toast.makeText(getContext(),"操作失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Map<String,String>map=data.get(position);
        View view= LayoutInflater.from(getContext()).inflate(resourse,null);
        TextView lab_1=(TextView)view.findViewById(R.id.lab_1);
        TextView lab_2=(TextView)view.findViewById(R.id.lab_2);
        TextView lab_3=(TextView)view.findViewById(R.id.lab_3);
        TextView lab_4=(TextView)view.findViewById(R.id.lab_4);
        Button del_btn=(Button)view.findViewById(R.id.btn_del);
        lab_1.setText(map.get("lab_1"));
        lab_2.setText(map.get("lab_2"));
        lab_3.setText(map.get("lab_3"));
        lab_4.setText(map.get("lab_4"));
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(getContext());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean result= NetHelper.getRunsqlResult("Exec PAD_Cal_RydInf  'DEL','' ,'"+jtbh+"','"+map.get("lab_2")+"',"+rym_id);
                        Message msg=handler.obtainMessage();
                        if (result){
                            msg.what=0x100;
                            msg.arg1=position;
                        }else {
                            msg.what=0x101;
                        }
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
        return view;
    }
}
