package com.ruiduoyi.activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrzActivity extends BaseActivity implements View.OnClickListener{
    private ListView listView;
    private Button ok_btn,cancle_btn;
    private SimpleAdapter adapter;
    private List<Map<String,String>>data;
    private String jtbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrz);
        initView();
        initData();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    initListView(list);
                    break;
                default:
                    break;
            }
        }
    };


    private void initView(){
        listView=(ListView)findViewById(R.id.b6_list);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        ok_btn=(Button)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
    }
    private void initListView(List<List<String>>lists){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("lab_1",item.get(0));//v_jtbh
            map.put("lab_2",item.get(1));//v_scrq
            map.put("lab_3",item.get(2));//v_scxh
            map.put("lab_4",item.get(3));//v_zlmc
            map.put("lab_5",item.get(4));//ksrq
            map.put("lab_6",item.get(5));//jsrq
            map.put("lab_7",item.get(6));//v_min
            map.put("lab_8",item.get(7));//v_lpsl
            map.put("lab_9",item.get(8));//v_blsl
            map.put("lab_10",item.get(9));// v_ccsl
            map.put("lab_11",item.get(10));//v_cxsj
            map.put("lab_12",item.get(11));//v_ksname
            map.put("lab_13",item.get(12));//v_jsname
            map.put("lab_14",item.get(13));//v_zzdh
            map.put("lab_15",item.get(14));//v_sodh
            map.put("lab_16",item.get(15));//v_ph
            map.put("lab_17",item.get(16));//v_wldm
            map.put("lab_18",item.get(17));//v_pmgg
            map.put("lab_19",item.get(18));//v_mjbh
            map.put("lab_20",item.get(19));//v_mjmc
            data.add(map);
        }
        adapter=new SimpleAdapter(this,data,R.layout.list_item_b6,new String[]{"lab_1","lab_2",
                "lab_3","lab_4","lab_5","lab_6","lab_7","lab_8","lab_9","lab_10","lab_11","lab_12",
                "lab_13","lab_14","lab_15","lab_16","lab_17","lab_18","lab_19","lab_20",},new int[]{R.id.lable_1,R.id.lable_2,R.id.lable_3,
                R.id.lable_4,R.id.lable_5,R.id.lable_6,R.id.lable_7,R.id.lable_8,R.id.lable_9,
                R.id.lable_10,R.id.lable_11,R.id.lable_12,R.id.lable_13,R.id.lable_14,R.id.lable_15,R.id.lable_16,R.id.lable_17,R.id.lable_18,R.id.lable_19,R.id.lable_20});
        listView.setAdapter(adapter);
    }

    private void initData(){
        jtbh=sharedPreferences.getString("jtbh","");
        getNetData();
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(ScrzActivity.this);
        switch (v.getId()){
            case R.id.ok_btn:
                finish();
                break;
            case R.id.cancle_btn:
                finish();
                break;
            default:
                break;
        }
    }

   private void getNetData(){
       //生产日志
       new Thread(new Runnable() {
           @Override
           public void run() {
               List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_SdmMstr '"+jtbh+"'");
               if (list!=null){
                   if (list.size()>0){
                       if (list.get(0).size()>21){
                            Message msg=handler.obtainMessage();
                           msg.what=0x100;
                           msg.obj=list;
                           handler.sendMessage(msg);
                       }
                   }
               }else {
                   AppUtils.uploadNetworkError("Exec PAD_Get_SdmMstr",jtbh,sharedPreferences.getString("mac",""));
               }
           }
       }).start();
   }

}
