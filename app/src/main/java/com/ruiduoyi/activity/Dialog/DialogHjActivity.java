package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.adapter.EasyArrayAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogHjActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button call_btn,cancle_btn;
    private Handler handler;
    private ListView listView;
    private List<Map<String,String>> data;
    private CheckBox box_jtyc,box_cpyc,box_ql,box_gscc;
    private String wkno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_hj);
        initData();
        initView();
    }


    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        initList((JSONArray) msg.obj);
                        break;
                    case 0x101:
                        Toast.makeText(DialogHjActivity.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
                        call_btn.setEnabled(true);
                        break;
                    case 0x102:
                        Toast.makeText(DialogHjActivity.this, "网络异常",Toast.LENGTH_SHORT).show();
                        call_btn.setEnabled(true);
                        break;
                }
            }
        };
        getListData();
    }

    private void initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        call_btn=(Button)findViewById(R.id.call_btn);
        listView=(ListView)findViewById(R.id.list_1);
        box_cpyc=(CheckBox)findViewById(R.id.cpyc);
        box_gscc=(CheckBox) findViewById(R.id.gscc);
        box_jtyc=(CheckBox)findViewById(R.id.jtyc);
        box_ql=(CheckBox)findViewById(R.id.ql);
        cancle_btn.setOnClickListener(this);
        call_btn.setOnClickListener(this);

    }


    private void initList(JSONArray array)  {
        data=new ArrayList<>();
        try {
            for (int i=0;i<array.length();i++){
                if (i%2==0){
                    Map<String,String>map=new HashMap<>();
                    map.put("wkno_1",array.getJSONObject(i).getString("wkm_wkno"));
                    map.put("name_1",array.getJSONObject(i).getString("wkm_name"));
                    map.put("gzdm_1",array.getJSONObject(i).getString("wkm_gzdm"));
                    map.put("isSelect_1","0");
                    map.put("wkno_2","");
                    map.put("name_2","");
                    map.put("gzdm_2","");
                    map.put("isSelect_2","0");
                    data.add(map);
                }else {
                    int position=i/2;
                    data.get(position).put("wkno_2",array.getJSONObject(i).getString("wkm_wkno"));
                    data.get(position).put("name_2",array.getJSONObject(i).getString("wkm_name"));
                    data.get(position).put("gzdm_2",array.getJSONObject(i).getString("wkm_gzdm"));
                    data.get(position).put("isSelect_2,","0");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EasyArrayAdapter adapter=new EasyArrayAdapter(this,R.layout.list_item_hj,data) {
            @Override
            public View getEasyView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView!=null){
                    view=convertView;
                }else {
                    view= LayoutInflater.from(getContext()).inflate(R.layout.list_item_hj,null);
                }
                TextView wkno_1=(TextView)view.findViewById(R.id.wkno_1);
                TextView name_1=(TextView)view.findViewById(R.id.name_1);
                TextView gzdm_1=(TextView)view.findViewById(R.id.gzdm_1);
                final CheckBox checkBox_1=(CheckBox)view.findViewById(R.id.checkbox_1);
                final LinearLayout bg_1=(LinearLayout)view.findViewById(R.id.bg_1);
                TextView wkno_2=(TextView)view.findViewById(R.id.wkno_2);
                TextView name_2=(TextView)view.findViewById(R.id.name_2);
                TextView gzdm_2=(TextView)view.findViewById(R.id.gzdm_2);
                final CheckBox checkBox_2=(CheckBox)view.findViewById(R.id.checkbox_2);
                final LinearLayout bg_2=(LinearLayout)view.findViewById(R.id.bg_2);
                final Map<String,String>map=data.get(position);
                wkno_1.setText(map.get("wkno_1"));
                name_1.setText(map.get("name_1"));
                gzdm_1.setText(map.get("gzdm_1"));
                wkno_2.setText(map.get("wkno_2"));
                name_2.setText(map.get("name_2"));
                gzdm_2.setText(map.get("gzdm_2"));
                if (map.get("isSelect_1").equals("0")){
                    checkBox_1.setChecked(false);
                    bg_1.setBackgroundColor(Color.WHITE);
                }else {
                    checkBox_1.setChecked(true);
                    bg_1.setBackgroundColor(getResources().getColor(R.color.small));
                }

                if (map.get("isSelect_2").equals("0")){
                    checkBox_2.setChecked(false);
                    bg_2.setBackgroundColor(Color.WHITE);
                }else {
                    checkBox_2.setChecked(true);
                    bg_2.setBackgroundColor(getResources().getColor(R.color.small));
                }



                checkBox_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        AppUtils.sendCountdownReceiver(DialogHjActivity.this);
                        if (isChecked){
                            map.put("isSelect_1","1");
                            bg_1.setBackgroundColor(getResources().getColor(R.color.small));
                        }else {
                            map.put("isSelect_1","0");
                            bg_1.setBackgroundColor(Color.WHITE);
                        }
                    }
                });
                bg_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.sendCountdownReceiver(DialogHjActivity.this);
                        if (map.get("isSelect_1").equals("0")){
                            map.put("isSelect_1","1");
                            bg_1.setBackgroundColor(getResources().getColor(R.color.small));
                            checkBox_1.setChecked(true);
                        }else {
                            map.put("isSelect_1","0");
                            bg_1.setBackgroundColor(Color.WHITE);
                            checkBox_1.setChecked(false);
                        }
                    }
                });


                checkBox_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        AppUtils.sendCountdownReceiver(DialogHjActivity.this);
                        if (isChecked){
                            map.put("isSelect_2","1");
                            bg_2.setBackgroundColor(getResources().getColor(R.color.small));
                        }else {
                            map.put("isSelect_2","0");
                            bg_2.setBackgroundColor(Color.WHITE);
                        }
                    }
                });
                bg_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.sendCountdownReceiver(DialogHjActivity.this);
                        if (map.get("isSelect_2").equals("0")){
                            map.put("isSelect_2","1");
                            bg_2.setBackgroundColor(getResources().getColor(R.color.small));
                            checkBox_2.setChecked(true);
                        }else {
                            map.put("isSelect_2","0");
                            bg_2.setBackgroundColor(Color.WHITE);
                            checkBox_2.setChecked(false);
                        }
                    }
                });

                return view;
            }
        };

        listView.setAdapter(adapter);

    }



    private void getListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray array= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_Call_Wkm_List");
                if (array!=null){
                    Message msg=handler.obtainMessage();
                    msg.what=0x100;
                    msg.obj=array;
                    handler.sendMessage(msg);
                }else {
                    NetHelper.uploadNetworkError("Exec PAD_Get_Call_Wkm_List",sharedPreferences.getString("jtbh",""),
                            sharedPreferences.getString("mac",""));
                }

            }
        }).start();
    }


    private boolean isReady(){
        Log.w("g_q_j_c",box_gscc.isChecked()+""+box_ql.isChecked()+box_jtyc.isChecked()+box_cpyc.isChecked());
        if (!(box_gscc.isChecked()|box_ql.isChecked()|box_jtyc.isChecked()|box_cpyc.isChecked())){
            Toast.makeText(DialogHjActivity.this, "至少选择一个呼叫原因",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (data==null){
            Toast.makeText(DialogHjActivity.this, "至少选择一个被呼叫人员",Toast.LENGTH_SHORT).show();
            return false;
        }
        List<Map<String,String>>select_data=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            if (data.get(i).get("isSelect_1").equals("1")){
                Map<String,String>map=new HashMap<>();
                map.put("wkno",data.get(i).get("wkno_1"));
                map.put("name",data.get(i).get("name_1"));
                select_data.add(map);
            }
            if (data.get(i).get("isSelect_2").equals("1")){
                Map<String,String>map=new HashMap<>();
                map.put("wkno",data.get(i).get("wkno_2"));
                map.put("name",data.get(i).get("name_2"));
                select_data.add(map);
            }
        }
        if (select_data.size()==0){
            Toast.makeText(DialogHjActivity.this, "至少选择一个被呼叫人员",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void upLoadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String hjyy="";
                String hjry="";
                for (int i=0;i<data.size();i++){
                    if (data.get(i).get("isSelect_1").equals("1")){
                        hjry=hjry+data.get(i).get("wkno_1")+",";
                    }
                    if (data.get(i).get("isSelect_2").equals("1")){
                        hjry=hjry+data.get(i).get("wkno_2")+",";
                    }
                }
                hjry=hjry.substring(0,hjry.length()-1);
                if (box_jtyc.isChecked()){
                    hjyy=hjyy+"机台异常,";
                }
                if (box_cpyc.isChecked()){
                    hjyy=hjyy+"产品异常,";
                }
                if (box_ql.isChecked()){
                    hjyy=hjyy+"缺料,";
                }
                if (box_gscc.isChecked()){
                    hjyy=hjyy+"够数/超产,";
                }
                hjyy=hjyy.substring(0,hjyy.length()-1);
                Log.w("hjry",hjry);
                Log.w("hjyy",hjyy);
                String sql=String.format("Exec PAD_Call_Start '%S', '%S', '%S', '%S'",jtbh,wkno,hjyy,hjry);
                JSONArray array=NetHelper.getQuerysqlResultJsonArray(sql);
                try {
                    if (array!=null){
                        if (array.length()>0){
                            if (array.getJSONObject(0).getString("Column1").equals("OK")){
                                Message msg=handler.obtainMessage();
                                msg.what=0x101;
                                msg.obj="呼叫成功";
                                handler.sendMessage(msg);
                                finish();
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x101;
                                msg.obj=array.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }else {
                            handler.sendEmptyMessage(0x102);
                        }
                    }else {
                        handler.sendEmptyMessage(0x102);
                        NetHelper.uploadNetworkError(sql,jtbh,sharedPreferences.getString("mac",""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(DialogHjActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.call_btn:
                if (isReady()){
                    call_btn.setEnabled(false);
                    upLoadData();
                }
                break;
        }
    }
}
