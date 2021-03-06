package com.ruiduoyi.adapter;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.GdglActivity;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/6/8.
 */

public class WorkOrderAdapter extends ArrayAdapter{
    private Context context;
    private int resource;
    private List<Map<String,String>>data;
    private String wkno;
    private Handler handler;
    public WorkOrderAdapter(Context context, int resource,List<Map<String,String>>data,String wkno,Handler handler) {
        super(context, resource,data);
        this.data=data;
        this.context=context;
        this.resource=resource;
        this.wkno=wkno;
        this.handler=handler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String,String>map=data.get(position);
        View view= LayoutInflater.from(getContext()).inflate(resource,null);
        final TextView lab_4=(TextView)view.findViewById(R.id.lab_4);
        TextView lab_5=(TextView)view.findViewById(R.id.lab_5);
        TextView lab_6=(TextView)view.findViewById(R.id.lab_6);
        TextView lab_7=(TextView)view.findViewById(R.id.lab_7);
        TextView lab_8=(TextView)view.findViewById(R.id.lab_8);
        TextView lab_11=(TextView)view.findViewById(R.id.lab_11);
        TextView lab_12=(TextView)view.findViewById(R.id.lab_12);
        TextView lab_13=(TextView)view.findViewById(R.id.lab_13);
        TextView lab_14=(TextView)view.findViewById(R.id.lab_14);
        TextView lab_15=(TextView)view.findViewById(R.id.lab_15);
        LinearLayout backgroup=(LinearLayout)view.findViewById(R.id.bg);
        final Button on_btn=(Button)view.findViewById(R.id.on_btn);
        final Button change_btn=(Button)view.findViewById(R.id.off_btn);
        final int index=position+1;
        on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (on_btn.getText().toString().equals("启动")){
                    //事件开
                    AppUtils.sendCountdownReceiver(getContext());
                    Message msg=handler.obtainMessage();
                    msg.what=0x102;
                    msg.obj=map;
                    handler.sendMessage(msg);
                }else {
                    //事件关
                    AppUtils.sendCountdownReceiver(getContext());
                    Message msg=handler.obtainMessage();
                    msg.what=0x103;
                    msg.obj=map;
                    handler.sendMessage(msg);
                }
            }
        });
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模具变更
                AppUtils.sendCountdownReceiver(getContext());
                Message msg=handler.obtainMessage();
                msg.what=0x106;
                msg.obj=map;
                handler.sendMessage(msg);
            }
        });
        lab_4.setText(map.get("scrq"));
        lab_5.setText(map.get("scxh"));
        lab_6.setText(map.get("zzdh"));
        lab_7.setText(map.get("mjbh"));
        lab_8.setText(map.get("mjmc"));
        /*lab_9.setText(map.get("mjbh"));
        lab_10.setText(map.get("mjmc"));*/
        lab_11.setText(map.get("wldm"));
        lab_12.setText(map.get("pmgg"));
        lab_13.setText(map.get("wgrq"));
        lab_14.setText(map.get("scsl"));
        lab_15.setText(map.get("lpsl"));
        switch (map.get("ztbz")){
            case "1":
                backgroup.setBackgroundColor(getContext().getResources().getColor(R.color.gdgl_1));
                on_btn.setText("暂停");
                break;
            case "2":
                backgroup.setBackgroundColor(getContext().getResources().getColor(R.color.gdgl_2));
                on_btn.setText("启动");
                break;
            case "3":
                backgroup.setBackgroundColor(getContext().getResources().getColor(R.color.gdgl_3));
                on_btn.setText("启动");
                break;
            default:
                backgroup.setBackgroundColor(Color.WHITE);
                break;
        }
        return view;
    }
}
