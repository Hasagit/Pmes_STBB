package com.ruiduoyi.activity.Dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SbxxActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button ok_btn,cancle_btn;
    private ListView listView;
    private SimpleAdapter adapter;
    private TextView jtbh_text,jtmc_text,jtdw_text,wgip_text,bbdm_text,minkd_text,
            maxkd_text,minhd_text,maxhd_text;
    private String jtbh,mac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbxx);
        initData();
        initView();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    try {
                        JSONArray list1= (JSONArray) msg.obj;
                        jtbh_text.setText(list1.getJSONObject(0).getString("v_jtbh"));
                        jtmc_text.setText(list1.getJSONObject(0).getString("v_jtmc"));
                        bbdm_text.setText(list1.getJSONObject(0).getString("v_bbdm"));
                        jtdw_text.setText(list1.getJSONObject(0).getString("v_jtdw"));
                        wgip_text.setText(list1.getJSONObject(0).getString("v_wgip"));
                        minkd_text.setText(list1.getJSONObject(0).getString("v_minkd"));
                        maxkd_text.setText(list1.getJSONObject(0).getString("v_maxkd"));
                        minhd_text.setText(list1.getJSONObject(0).getString("v_minhd"));
                        maxhd_text.setText(list1.getJSONObject(0).getString("v_maxhd"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x101:
                    JSONArray list= (JSONArray) msg.obj;
                    initList(list);
                    break;
                case 0x102:
                    Toast.makeText(SbxxActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    private void initView(){
        ok_btn=(Button)findViewById(R.id.ok_btn);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        listView=(ListView)findViewById(R.id.b1_list);
        bbdm_text=(TextView)findViewById(R.id.bbdm);
        jtbh_text=(TextView)findViewById(R.id.jtbh);
        jtdw_text=(TextView)findViewById(R.id.jtdw);
        jtmc_text=(TextView)findViewById(R.id.jtmc);
        wgip_text=(TextView)findViewById(R.id.wgid);
        minhd_text=(TextView)findViewById(R.id.minhd) ;
        maxhd_text=(TextView)findViewById(R.id.maxhd);
        minkd_text=(TextView)findViewById(R.id.minkd);
        maxkd_text=(TextView)findViewById(R.id.maxkd);
        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);

    }

    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        mac=sharedPreferences.getString("mac","");
        getNetData();
    }


    private void initList(JSONArray list){
        List<Map<String,String>>data=new ArrayList<>();
        try {
            for (int i=0;i<list.length();i++){
                Map<String,String>map=new HashMap<>();
                map.put("mjbh",list.getJSONObject(i).getString("v_mjbh"));
                map.put("mjmc",list.getJSONObject(i).getString("v_mjmc"));
                map.put("hmcs",list.getJSONObject(i).getString("v_hmcs"));
                map.put("rate",list.getJSONObject(i).getString("v_rate"));
                map.put("newsj",list.getJSONObject(i).getString("v_newsj"));
                data.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter=new SimpleAdapter(this,data,R.layout.list_item_b1,new String[]{"mjbh","mjmc","hmcs","rate","newsj"},
                new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3,R.id.lab_4,R.id.lab_5});
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(SbxxActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.ok_btn:
                finish();
                break;
            default:
                break;
        }
    }

    private void getNetData(){
        //请求设备信息线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray list=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_JtmMstr '"+jtbh+"'");
                if (list!=null){
                    if (list.length()>0){
                        Message msg=handler.obtainMessage();
                        msg.what=0x100;
                        msg.obj=list;
                        handler.sendMessage(msg);
                    }
                }else {
                    handler.sendEmptyMessage(0x102);
                    NetHelper.uploadNetworkError("Exec PAD_Get_JtmMstr NetWorkError",jtbh,mac);
                }
            }
        }).start();

        //请求列表信息线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray list=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_SbmHgl '"+jtbh+"'");
                if (list!=null){
                    if (list.length()>0){
                        Message msg=handler.obtainMessage();
                        msg.what=0x101;
                        msg.obj=list;
                        handler.sendMessage(msg);
                    }
                }else {
                    handler.sendEmptyMessage(0x102);
                    NetHelper.uploadNetworkError("Exec PAD_Get_SbmHgl NetWorkError",jtbh,mac);
                }
            }
        }).start();
    }
}
