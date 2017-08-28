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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiduoyi.R;

import org.w3c.dom.Text;

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
        }else {
            view= LayoutInflater.from(getContext()).inflate(resource,null);
        }
        LinearLayout bg=(LinearLayout)view.findViewById(R.id.bg);
        TextView lable_1=(TextView)view.findViewById(R.id.lab_1);
        TextView lable_2=(TextView)view.findViewById(R.id.lab_2);
        TextView lable_3=(TextView)view.findViewById(R.id.lab_3);
        TextView lable_4=(TextView)view.findViewById(R.id.lab_4);
        TextView lable_5=(TextView)view.findViewById(R.id.lab_5);
        Map<String,String>map=data.get(position);
        lable_1.setText(map.get("lab_1"));
        //lable_1.setTextColor(traToColor(map.get("color")));
        lable_2.setText(map.get("lab_2"));
        //lable_2.setTextColor(traToColor(map.get("color")));
        lable_3.setText(map.get("lab_3"));
        //lable_3.setTextColor(traToColor(map.get("color")));
        lable_4.setText(map.get("lab_4"));
        //lable_4.setTextColor(traToColor(map.get("color")));
        lable_5.setText(map.get("lab_5"));
        //lable_5.setTextColor(traToColor(map.get("color")));
        umSetColor(map.get("color"),bg,lable_1,lable_2,lable_3,lable_4,lable_5);
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
    private void umSetColor(String fcColor, LinearLayout bg,TextView lab_1, TextView lab_2, TextView lab_3, TextView lab_4, TextView lab_5){

        if(fcColor.equals("N")){
            bg.setBackgroundColor(getContext().getResources().getColor(R.color.lightgray));
            lab_1.setTextColor(getContext().getResources().getColor(R.color.darkviolet));
            lab_2.setTextColor(getContext().getResources().getColor(R.color.darkviolet));
            lab_3.setTextColor(getContext().getResources().getColor(R.color.darkviolet));
            lab_4.setTextColor(getContext().getResources().getColor(R.color.darkviolet));
            lab_5.setTextColor(getContext().getResources().getColor(R.color.darkviolet));
        }else  if(fcColor.equals("W")){
            bg.setBackgroundColor(getContext().getResources().getColor(R.color.lightgray));
            lab_1.setTextColor(Color.WHITE);
            lab_2.setTextColor(Color.WHITE);
            lab_3.setTextColor(Color.WHITE);
            lab_4.setTextColor(Color.WHITE);
            lab_5.setTextColor(Color.WHITE);
        }else if(fcColor.equals("G")){
            bg.setBackgroundColor(getContext().getResources().getColor(R.color.limegreen));
            lab_1.setTextColor(Color.BLUE);
            lab_2.setTextColor(Color.BLUE);
            lab_3.setTextColor(Color.BLUE);
            lab_4.setTextColor(Color.BLUE);
            lab_5.setTextColor(Color.BLUE);
        }else if(fcColor.equals("Y")){
            bg.setBackgroundColor(Color.YELLOW);
            lab_1.setTextColor(Color.RED);
            lab_2.setTextColor(Color.RED);
            lab_3.setTextColor(Color.RED);
            lab_4.setTextColor(Color.RED);
            lab_5.setTextColor(Color.RED);
        }else if(fcColor.equals("R")){
            bg.setBackgroundColor(Color.RED);
            lab_1.setTextColor(Color.YELLOW);
            lab_2.setTextColor(Color.YELLOW);
            lab_3.setTextColor(Color.YELLOW);
            lab_4.setTextColor(Color.YELLOW);
            lab_5.setTextColor(Color.YELLOW);
        }else if(fcColor.equals("V")){
            bg.setBackgroundColor(getContext().getResources().getColor(R.color.darkviolet));
            lab_1.setTextColor(Color.YELLOW);
            lab_2.setTextColor(Color.YELLOW);
            lab_3.setTextColor(Color.YELLOW);
            lab_4.setTextColor(Color.YELLOW);
            lab_5.setTextColor(Color.YELLOW);
        }else{
            bg.setBackgroundColor(getContext().getResources().getColor(R.color.limegreen));
            lab_1.setTextColor(Color.WHITE);
            lab_2.setTextColor(Color.WHITE);
            lab_3.setTextColor(Color.WHITE);
            lab_4.setTextColor(Color.WHITE);
            lab_5.setTextColor(Color.WHITE);
        }
    }

}
