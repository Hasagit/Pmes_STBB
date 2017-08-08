package com.ruiduoyi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.utils.AppUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/7/13.
 */

public abstract class SigleSelectAdapter2 extends ArrayAdapter{
    private Context context;
    private List<Map<String,String>> userList;
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个


    public SigleSelectAdapter2( Context context, int resource, List<Map<String,String>> objects) {
        super(context, resource, objects);
        this.context=context;
        userList=objects;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SigleSelectAdapter2.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.blfx_sigle_select_item, null);
            holder = new SigleSelectAdapter2.ViewHolder();
            holder.background = (LinearLayout) convertView.findViewById(R.id.backgrounp);
            holder.lab_1 = (TextView) convertView.findViewById(R.id.lab_1);
            holder.lab_2=(TextView)convertView.findViewById(R.id.lab_2);
            holder.lab_3=(TextView)convertView.findViewById(R.id.lab_3);
            convertView.setTag(holder);
        }else{
            holder=(SigleSelectAdapter2.ViewHolder) convertView.getTag();
        }
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.select_btn);
        holder.rdBtn = radio;
        holder.lab_1.setText(userList.get(position).get("lab_1"));
        holder.lab_2.setText(userList.get(position).get("lab_2"));
        holder.lab_3.setText(userList.get(position).get("lab_3"));
//当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中
        holder.background.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(context);
                holder.rdBtn.setChecked(true);
                //重置，确保最多只有一项被选中
                onRadioSelectListener(position,userList.get(position));
                for(String key:states.keySet()){
                    states.put(key, false);

                }
                states.put(String.valueOf(position), radio.isChecked());
                SigleSelectAdapter2.this.notifyDataSetChanged();
            }
        });
        holder.rdBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.background.setBackgroundColor(getContext().getResources().getColor(R.color.small));
                }else {
                    holder.background.setBackgroundColor(Color.WHITE);
                }
            }
        });

        holder.rdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置，确保最多只有一项被选中
                onRadioSelectListener(position,userList.get(position));
                for(String key:states.keySet()){
                    states.put(key, false);

                }
                states.put(String.valueOf(position), radio.isChecked());
                SigleSelectAdapter2.this.notifyDataSetChanged();
            }
        });


        boolean res=false;
        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){
            res=false;
            states.put(String.valueOf(position), false);
        }
        else
            res = true;

        holder.rdBtn.setChecked(res);

        return convertView;
    }

    static class ViewHolder {
        LinearLayout background;
        TextView lab_1;
        TextView lab_2;
        TextView lab_3;
        RadioButton rdBtn;
    }

    public abstract void onRadioSelectListener(int position,Map<String,String>map);

}
