package com.ruiduoyi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ruiduoyi.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by DengJf on 2017/6/7.
 */

public class OeeAdapter extends ArrayAdapter{
    private int resource;
    private List<Map<String,String>>data;

    public OeeAdapter(Context context,int resource,List<Map<String,String>>data) {
        super(context,resource,data);
        this.data=data;
        this.resource=resource;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view;
        if(convertView!=null){
            view= LayoutInflater.from(getContext()).inflate(resource,null);
            TextView lable_1=(TextView)view.findViewById(R.id.lab_1);
            TextView lable_2=(TextView)view.findViewById(R.id.lab_2);
            TextView lable_3=(TextView)view.findViewById(R.id.lab_3);
            TextView lable_4=(TextView)view.findViewById(R.id.lab_4);
            TextView lable_5=(TextView)view.findViewById(R.id.lab_5);
            Map<String,String>map=data.get(position);
            lable_1.setText(map.get("lab_1"));
            lable_1.setTextColor(traToColor(map.get("color")));
            lable_2.setText(map.get("lab_2"));
            lable_2.setTextColor(traToColor(map.get("color")));
            lable_3.setText(map.get("lab_3"));
            lable_3.setTextColor(traToColor(map.get("color")));
            lable_4.setText(map.get("lab_4"));
            lable_4.setTextColor(traToColor(map.get("color")));
            lable_5.setText(map.get("lab_5"));
            lable_5.setTextColor(traToColor(map.get("color")));
        }else {
            view= LayoutInflater.from(getContext()).inflate(resource,null);
            TextView lable_1=(TextView)view.findViewById(R.id.lab_1);
            TextView lable_2=(TextView)view.findViewById(R.id.lab_2);
            TextView lable_3=(TextView)view.findViewById(R.id.lab_3);
            TextView lable_4=(TextView)view.findViewById(R.id.lab_4);
            TextView lable_5=(TextView)view.findViewById(R.id.lab_5);
            Map<String,String>map=data.get(position);
            lable_1.setText(map.get("lab_1"));
            lable_1.setTextColor(traToColor(map.get("color")));
            lable_2.setText(map.get("lab_2"));
            lable_2.setTextColor(traToColor(map.get("color")));
            lable_3.setText(map.get("lab_3"));
            lable_3.setTextColor(traToColor(map.get("color")));
            lable_4.setText(map.get("lab_4"));
            lable_4.setTextColor(traToColor(map.get("color")));
            lable_5.setText(map.get("lab_5"));
            lable_5.setTextColor(traToColor(map.get("color")));
        }
        return view;
    }
    //根据字符串返回相对应的颜色
    private int traToColor(String str){
        int color;
        String temp=str;
        if(temp.trim().equals("W")){
            color= Color.BLACK;
        }else if (temp.trim().equals("G")){
            color=Color.GREEN;
        }
        else if (temp.trim().equals("Y")){
            color=Color.YELLOW;
        }else if (temp.trim().equals("N")){
            color=getContext().getResources().getColor(R.color.bottom_bt_sl);
        }else {
            color= Color.WHITE;
        }
        return color;
    }
}
