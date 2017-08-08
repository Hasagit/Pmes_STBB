package com.ruiduoyi.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ruiduoyi.R;
import com.ruiduoyi.adapter.WorkOrderAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GdglActivity extends BaseActivity implements View.OnClickListener{
    private ListView listView;
    private Button cancle_btn;
    private String jtbh,wkno;
    private PopupDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdgl);
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
                case 0x101:
                    getNetData();
                    String result=(String) msg.obj;
                    if (!result.equals("OK")){
                        dialog.setMessage(result);
                        dialog.show();
                    }
                    break;
            }
        }
    };

    private void initView(){
        dialog=new PopupDialog(this,400,300);
        dialog.setBackgrounpColor(getResources().getColor(R.color.lable));
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listView=(ListView)findViewById(R.id.list_b3);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(this);
    }


    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        getNetData();
    }

    private void  initListView(List<List<String>>lists){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("moeid",item.get(0));
            map.put("scrq",item.get(1));
            map.put("scxh",item.get(2));
            map.put("zzdh",item.get(3));
            map.put("sodh",item.get(4));
            map.put("ph",item.get(5));
            map.put("mjbh",item.get(6));
            map.put("mjmc",item.get(7));
            map.put("wldm",item.get(8));
            map.put("pmgg",item.get(9));
            map.put("wgrq",item.get(10));
            map.put("scsl",item.get(11));
            map.put("lpsl",item.get(12));
            map.put("ztbz",item.get(13));
            data.add(map);
        }
        WorkOrderAdapter adapter=new WorkOrderAdapter(GdglActivity.this,R.layout.list_item_b3,data,wkno,handler);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle_btn:
                AppUtils.sendCountdownReceiver(GdglActivity.this);
                finish();
                break;
            default:
                break;
        }
    }

    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_MoeDet 'A','"+jtbh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>13){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_MoeDet",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog.isShow()){
            dialog.dismiss();
        }
    }
}
