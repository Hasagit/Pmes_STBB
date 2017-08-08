package com.ruiduoyi.adapter;

/**
 * Created by DengJf on 2017/6/25.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.utils.AppUtils;

public abstract class SigleSelectAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,String>> userList;
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个
    public SigleSelectAdapter(Context context, List<Map<String,String>> userList)
    {
        this.context = context;
        this.userList= userList;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_b8, null);
            holder = new ViewHolder();
            holder.background = (LinearLayout) convertView.findViewById(R.id.backgrounp);
            holder.lab_1 = (TextView) convertView.findViewById(R.id.lab_1);
            holder.lab_2=(TextView)convertView.findViewById(R.id.lab_2);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.select_btn);
        holder.rdBtn = radio;
        holder.lab_1.setText(userList.get(position).get("lab_1"));
        holder.lab_2.setText(userList.get(position).get("lab_2"));
//当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中
        holder.background.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                holder.rdBtn.setChecked(true);
                //重置，确保最多只有一项被选中
                onRadioSelectListener(position,userList.get(position));
                for(String key:states.keySet()){
                    states.put(key, false);

                }
                states.put(String.valueOf(position), radio.isChecked());
                SigleSelectAdapter.this.notifyDataSetChanged();
            }
        });
        holder.rdBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppUtils.sendCountdownReceiver(context);
                if (isChecked){
                    holder.background.setBackgroundColor(context.getResources().getColor(R.color.small));
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
                SigleSelectAdapter.this.notifyDataSetChanged();
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
        RadioButton rdBtn;
    }

    public abstract void onRadioSelectListener(int position,Map<String,String>map);

}