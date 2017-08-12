package com.ruiduoyi.adapter;

import android.content.Context;
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

import com.ruiduoyi.R;
import com.ruiduoyi.utils.AppUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/6/13.
 */

public class YichangfenxiAdapter extends ArrayAdapter{
    private List<Map<String,String>>data;
    private int resource;
    private List<TextView>list_text;
    private Handler handler;
    public YichangfenxiAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Map<String,String>>data,List<TextView>list_text,Handler handler) {
        super(context, resource, data);
        this.data=data;
        this.resource=resource;
        this.list_text=list_text;
        this.handler=handler;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resource,null);
        final Map<String,String>map=data.get(position);
        Button btn=(Button)view.findViewById(R.id.btn_1);
        TextView lab_1=(TextView)view.findViewById(R.id.lab_1);
        TextView lab_2=(TextView)view.findViewById(R.id.lab_2);
        TextView lab_3=(TextView)view.findViewById(R.id.lab_3);
        TextView lab_4=(TextView)view.findViewById(R.id.lab_4);
        TextView lab_5=(TextView)view.findViewById(R.id.lab_5);
        TextView lab_6=(TextView)view.findViewById(R.id.lab_6);
        TextView lab_7=(TextView)view.findViewById(R.id.lab_7);
        TextView lab_8=(TextView)view.findViewById(R.id.lab_8);
        TextView lab_9=(TextView)view.findViewById(R.id.lab_9);
        TextView lab_10=(TextView)view.findViewById(R.id.lab_10);
        TextView lab_11=(TextView)view.findViewById(R.id.lab_11);
        lab_1.setText(map.get("lab_1"));
        lab_2.setText(map.get("lab_2"));
        lab_3.setText(map.get("lab_3"));
        lab_4.setText(map.get("lab_4"));
        lab_5.setText(map.get("lab_5"));
        lab_6.setText(map.get("lab_6"));
        lab_7.setText(map.get("lab_7"));
        lab_8.setText(map.get("lab_8"));
        lab_9.setText(map.get("lab_9"));
        lab_10.setText(map.get("lab_10"));
        lab_11.setText(map.get("lab_11"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(getContext());
                //选取按钮点击事件
                list_text.get(0).setText(map.get("lab_1"));
                list_text.get(1).setText(map.get("lab_12"));
                list_text.get(2).setText(map.get("lab_2"));
                list_text.get(3).setText(map.get("lab_4"));
                list_text.get(4).setText(map.get("lab_5"));
                list_text.get(5).setText(map.get("lab_6"));
                list_text.get(6).setText(map.get("lab_7"));
                list_text.get(7).setText(map.get("lab_8"));
                list_text.get(8).setText(map.get("lab_9"));
                list_text.get(9).setText(map.get("lab_10"));
                list_text.get(10).setText(map.get("lab_11"));
                list_text.get(11).setText(map.get("lab_3"));
                Message msg=handler.obtainMessage();
                msg.what=0x107;
                //msg.obj=map.get("lab_12");
                msg.obj=map;
                handler.sendMessage(msg);
            }
        });
        return view;
    }

}
