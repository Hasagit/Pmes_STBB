package com.ruiduoyi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

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
                    List<List<String>>list1=(List<List<String>>)msg.obj;
                    List<String>item1=list1.get(0);
                    jtbh_text.setText(item1.get(0));
                    jtmc_text.setText(item1.get(1));
                    bbdm_text.setText(item1.get(2));
                    jtdw_text.setText(item1.get(3));
                    wgip_text.setText(item1.get(4));
                    minkd_text.setText(item1.get(5));
                    maxkd_text.setText(item1.get(6));
                    minhd_text.setText(item1.get(7));
                    maxhd_text.setText(item1.get(8));
                    break;
                case 0x101:
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    initList(list);
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


    private void initList(List<List<String>>list){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            List<String>item=list.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("mjbh",item.get(0));
            map.put("mjmc",item.get(1));
            map.put("hmcs",item.get(2));
            map.put("rate",item.get(3));
            map.put("newsj",item.get(4));
            data.add(map);
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
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_JtmMstr '"+jtbh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>8){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_JtmMstr NetWorkError",jtbh,mac);
                }
            }
        }).start();

        //请求列表信息线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_SbmHgl '"+jtbh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>4){
                            Message msg=handler.obtainMessage();
                            msg.what=0x101;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_SbmHgl NetWorkError",jtbh,mac);
                }
            }
        }).start();
    }
}
