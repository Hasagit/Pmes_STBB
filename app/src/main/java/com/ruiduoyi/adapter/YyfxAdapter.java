package com.ruiduoyi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiduoyi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/7/12.
 */

public class YyfxAdapter extends ArrayAdapter{
    private List<Map<String,String>>data;
    private List<Map<String,String>>selectData=new ArrayList<>();
    private int resorce;


    public YyfxAdapter(Context context, int resource, List<Map<String,String>>data) {
        super(context, resource, data);
        this.data=data;
        this.resorce=resource;
    }


    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView!=null){
            view=convertView;
        }else {
            view= LayoutInflater.from(getContext()).inflate(resorce,null);
        }
        TextView lab_1=(TextView) view.findViewById(R.id.lab_1);
        TextView lab_2=(TextView)view.findViewById(R.id.lab_2);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.select_btn);
        final LinearLayout bg=(LinearLayout)view.findViewById(R.id.backgrounp);
        lab_1.setText(data.get(position).get("lab_1"));
        lab_2.setText(data.get(position).get("lab_2"));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    bg.setBackgroundColor(getContext().getResources().getColor(R.color.small));
                    addSelectData(data.get(position));
                }else {
                    bg.setBackgroundColor(Color.WHITE);
                    removeSelectData(data.get(position));
                }
                Log.w("YyfxAdapter",selectData.toString());
            }
        });
        return view;
    }
    private void removeSelectData(Map<String,String>map){
        for (int i=0;i<selectData.size();i++){
            if (map.equals(selectData.get(i))){
                selectData.remove(i);
            }
        }
    }

    private void addSelectData(Map<String,String>map){
        selectData.add(map);
    }

    public List<Map<String,String>>getSelectData(){
        return selectData;
    }

}
