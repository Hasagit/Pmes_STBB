package com.ruiduoyi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/7/15.
 */

public class Jtqsbg1Adapter extends ArrayAdapter{
    private Context context;
    private int resource;
    private List<Map<String,String>> data;
    private Handler handler;
    public Jtqsbg1Adapter(Context context, int resource, List<Map<String,String>>data, Handler handler) {
        super(context, resource,data);
        this.data=data;
        this.context=context;
        this.resource=resource;
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
        TextView lab_9=(TextView)view.findViewById(R.id.lab_9);
        TextView lab_10=(TextView)view.findViewById(R.id.lab_10);
        TextView lab_11=(TextView)view.findViewById(R.id.lab_11);
        TextView lab_12=(TextView)view.findViewById(R.id.lab_12);
        TextView lab_13=(TextView)view.findViewById(R.id.lab_13);
        TextView lab_14=(TextView)view.findViewById(R.id.lab_14);
        TextView lab_15=(TextView)view.findViewById(R.id.lab_15);
        TextView lab_16=(TextView)view.findViewById(R.id.lab_16);
        TextView lab_17=(TextView)view.findViewById(R.id.lab_17);
        LinearLayout backgroup=(LinearLayout)view.findViewById(R.id.bg);
        final Button on_btn=(Button)view.findViewById(R.id.on_btn);
        final Button off_btn=(Button)view.findViewById(R.id.off_btn);
        final int index=position+1;
        on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(getContext());
                Message msg=handler.obtainMessage();
                msg.what=0x104;
                msg.obj=map;
                handler.sendMessage(msg);
            }
        });
        /*off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //事件关
                AppUtils.sendCountdownReceiver(getContext());
                off_btn.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<List<String>>list= NetHelper.getQuerysqlResult("Exec  PAD_Start_MoeInfo 'Stop','"
                                +map.get("moeid")+"','"+wkno+"'");
                        if (list!=null){
                            if (list.size()>0){
                                if (list.get(0).size()>0){
                                    Message msg=handler.obtainMessage();
                                    msg.what=0x101;
                                    msg.obj=list.get(0).get(0);
                                    handler.sendMessage(msg);
                                }
                            }
                        }
                    }
                }).start();
            }
        });*/
        lab_4.setText(map.get("scrq"));
        lab_5.setText(map.get("scxh"));
        lab_6.setText(map.get("zzdh"));
        lab_7.setText(map.get("sodh"));
        lab_8.setText(map.get("ph"));
        lab_9.setText(map.get("mjbh"));
        lab_10.setText(map.get("mjmc"));
        lab_11.setText(map.get("wldm"));
        lab_12.setText(map.get("pmgg"));
        lab_13.setText(map.get("wgrq"));
        lab_14.setText(map.get("scsl"));
        lab_15.setText(map.get("lpsl"));
        lab_16.setText(map.get("mjqs"));
        lab_17.setText(map.get("cpqs"));
        /*switch (map.get("ztbz")){
            case "1":
                backgroup.setBackgroundColor(getContext().getResources().getColor(R.color.gdgl_1));
                break;
            case "2":
                backgroup.setBackgroundColor(getContext().getResources().getColor(R.color.gdgl_2));
                break;
            case "3":
                backgroup.setBackgroundColor(getContext().getResources().getColor(R.color.gdgl_3));
                break;
            default:
                backgroup.setBackgroundColor(Color.WHITE);
                break;
        }*/
        return view;
    }
}
