package com.ruiduoyi.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.Dialog.DialogMjbgActivity;
import com.ruiduoyi.adapter.WorkOrderAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;

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
                    JSONArray list= (JSONArray) msg.obj;
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
                case 0x102://启动
                    beginEvent((Map<String, String>) msg.obj);
                    break;
                case 0x103://暂停
                    stopEvent((Map<String, String>) msg.obj);
                    break;
                case 0x104:
                    Toast.makeText(GdglActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x105:
                    dialog.setMessage((String) msg.obj);
                    dialog.show();
                    break;
                case 0x106://模具变更
                    Map<String,String>map= (Map<String, String>) msg.obj;
                    Intent intent=new Intent(GdglActivity.this, DialogMjbgActivity.class);
                    intent.putExtra("mjbh",map.get("mjbh"));
                    intent.putExtra("wldm",map.get("wldm"));
                    intent.putExtra("zzdh",map.get("zzdh"));
                    startActivityForResult(intent,1);
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

    private void  initListView(JSONArray lists){
        List<Map<String,String>>data=new ArrayList<>();
        try {
            for (int i=0;i<lists.length();i++){
                Map<String,String>map=new HashMap<>();
                map.put("moeid",lists.getJSONObject(i).getString("v_moeid"));
                map.put("scrq",lists.getJSONObject(i).getString("v_scrq"));
                map.put("scxh",lists.getJSONObject(i).getString("v_scxh"));
                map.put("zzdh",lists.getJSONObject(i).getString("v_zzdh"));
                map.put("sodh",lists.getJSONObject(i).getString("v_sodh"));
                map.put("ph",lists.getJSONObject(i).getString("v_ph"));
                map.put("mjbh",lists.getJSONObject(i).getString("v_mjbh"));
                map.put("mjmc",lists.getJSONObject(i).getString("v_mjmc"));
                map.put("wldm",lists.getJSONObject(i).getString("v_wldm"));
                map.put("pmgg",lists.getJSONObject(i).getString("v_pmgg"));
                map.put("wgrq",lists.getJSONObject(i).getString("v_wgrq"));
                map.put("scsl",lists.getJSONObject(i).getString("v_scsl"));
                map.put("lpsl",lists.getJSONObject(i).getString("v_lpsl"));
                map.put("ztbz",lists.getJSONObject(i).getString("v_ztbz"));
                data.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                JSONArray list=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_MoeDet 'A','"+jtbh+"'");
                if (list!=null){
                    if (list.length()>0){
                        Message msg=handler.obtainMessage();
                        msg.what=0x100;
                        msg.obj=list;
                        handler.sendMessage(msg);
                    }
                }else {
                    handler.sendEmptyMessage(0x102);
                    NetHelper.uploadNetworkError("Exec PAD_Get_MoeDet",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }


    private void stopEvent(final Map<String,String>map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray list= NetHelper.getQuerysqlResultJsonArray("Exec  PAD_Start_MoeInfo 'Stop','"
                            +map.get("moeid")+"','"+wkno+"'");
                    if (list!=null){
                        if (list.length()>0){
                            if (list.getJSONObject(0).getString("Column1").equals("OK")){
                                getNetData();
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x105;
                                msg.obj=list.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }
                    }else {
                        handler.sendEmptyMessage(0x104);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void beginEvent(final Map<String,String>map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray list= NetHelper.getQuerysqlResultJsonArray("Exec  PAD_Start_MoeInfo 'Start','"
                            +map.get("moeid")+"','"+wkno+"'");
                    if (list!=null){
                        if (list.length()>0){
                            if (list.getJSONObject(0).getString("Column1").equals("OK")){
                                getNetData();
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x105;
                                msg.obj=list.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }
                    }else {
                        handler.sendEmptyMessage(0x104);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 0:
                getNetData();
                break;
            case 1:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog.isShow()){
            dialog.dismiss();
        }
        AppUtils.sendUpdateInfoFragmentReceiver(GdglActivity.this);
    }
}
