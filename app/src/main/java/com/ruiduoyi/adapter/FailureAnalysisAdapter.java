package com.ruiduoyi.adapter;

import android.content.Context;
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
import com.ruiduoyi.activity.MjxxActivity;
import com.ruiduoyi.utils.AppUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/6/8.
 */

public class FailureAnalysisAdapter extends ArrayAdapter{
    List<Map<String,String>>data;
    Context context;
    int resource;
    TextView bldm_text,blms_text;
    public FailureAnalysisAdapter(@NonNull Context context, @LayoutRes int resource,List<Map<String,String>>data,TextView bldm_text,TextView blms_text) {
        super(context, resource,data);
        this.data=data;
        this.resource=resource;
        this.context=context;
        this.bldm_text=bldm_text;
        this.blms_text=blms_text;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Map<String,String>map=data.get(position);
        View view= LayoutInflater.from(getContext()).inflate(resource,null);
        TextView lab_1=(TextView)view.findViewById(R.id.lab_1);
        TextView lab_2=(TextView)view.findViewById(R.id.lab_2);
        Button select_btn=(Button)view.findViewById(R.id.select_btn);
        lab_1.setText(map.get("lab_1"));
        lab_2.setText(map.get("lab_2"));
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(getContext());
                bldm_text.setText(map.get("lab_1"));
                blms_text.setText(map.get("lab_2"));
            }
        });
        return view;
    }
}
