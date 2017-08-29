package com.ruiduoyi.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.Dialog.DialogJtbgActivity;
import com.ruiduoyi.activity.Dialog.DialogMxbgActivity;
import com.ruiduoyi.adapter.Jtqsbg1Adapter;
import com.ruiduoyi.adapter.SigleSelectJtjqsbg;
import com.ruiduoyi.adapter.WorkOrderAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JtjqsbgActivity extends BaseActivity implements View.OnClickListener{
    private ListView listView_1,listView_2;
    private String jtbh,zzdh;
    private Handler handler;
    private Button cancle_btn;
    private Animation anim;
    private PopupDialog tipDialog;
    private Animation anim2;
    private Button begin_btn;
    private String wkno;
    private List<Map<String,String>>data_dt;
    private SimpleAdapter adapter_dt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jtjqsbg);
        initData();
        initView();
    }


    private void initView(){
        listView_1=(ListView)findViewById(R.id.list_jtjqsbg_1);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(this);



        tipDialog=new PopupDialog(this,400,300);
        tipDialog.getCancle_btn().setVisibility(View.GONE);
        tipDialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.dismiss();
            }
        });
        tipDialog.setTitle("提示");
        //tipDialog.setMessageTextColor(Color.BLACK);
        tipDialog.getOkbtn().setText("确定");

    }

    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        anim= AnimationUtils.loadAnimation(this,R.anim.sub_num_anim);
        anim2=AnimationUtils.loadAnimation(this,R.anim.scale_anim);
        final Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        List<List<String>>list=(List<List<String>>)msg.obj;
                        initListView(list);
                        break;
                    case 0x101:
                        List<List<String>>list1= (List<List<String>>) msg.obj;
                        data_dt=new ArrayList<>();
                        for (int i=0;i<list1.size();i++){
                            Map<String,String>map=new HashMap<>();
                            map.put("lab_1",list1.get(i).get(0));
                            map.put("lab_2",list1.get(i).get(1));
                            map.put("lab_3",list1.get(i).get(2));
                            map.put("lab_4",list1.get(i).get(3));
                            map.put("lab_5",list1.get(i).get(4));
                            map.put("lab_6",list1.get(i).get(5));
                            data_dt.add(map);
                        }
                        adapter_dt=new SimpleAdapter(JtjqsbgActivity.this,data_dt,R.layout.list_item_jtjqsbg_3,
                                new String[]{"lab_1","lab_2","lab_3","lab_4","lab_5","lab_6"},
                                new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3,R.id.lab_4,R.id.lab_5,R.id.lab_6});
                        listView_2.setAdapter(adapter_dt);
                        break;
                    case 0x104://模穴变更
                        Map<String,String>mo_map= (Map<String, String>) msg.obj;
                        Intent intent_mxbg=new Intent(JtjqsbgActivity.this, DialogMxbgActivity.class);
                        intent_mxbg.putExtra("sodh",mo_map.get("sodh"));
                        intent_mxbg.putExtra("wkno",wkno);
                        intent_mxbg.putExtra("mjmc",mo_map.get("mjmc"));
                        intent_mxbg.putExtra("mjbh",mo_map.get("mjbh"));
                        intent_mxbg.putExtra("pmgg",mo_map.get("pmgg"));
                        intent_mxbg.putExtra("bzxs",mo_map.get("mjqs"));
                        intent_mxbg.putExtra("zzdh",mo_map.get("zzdh"));
                        startActivityForResult(intent_mxbg,1);
                        //zzdh=mo_map.get("zzdh");
                        break;
                    case 0x105://机台变更
                        Map<String,String>jt_map= (Map<String, String>) msg.obj;
                        Intent intent_jt=new Intent(JtjqsbgActivity.this, DialogJtbgActivity.class);
                        intent_jt.putExtra("zzdh",jt_map.get("zzdh"));
                        intent_jt.putExtra("wkno",wkno);
                        startActivityForResult(intent_jt,1);
                        break;
                    case 0x106:
                        Toast.makeText(JtjqsbgActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };


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
            map.put("mjqs",item.get(14));
            map.put("cpqs",item.get(15));
            data.add(map);
        }
        Jtqsbg1Adapter adapter_1;
        adapter_1=new Jtqsbg1Adapter(JtjqsbgActivity.this,R.layout.list_item_jtjqsbg1,data,handler);
        listView_1.setAdapter(adapter_1);
    }


    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //工单信息表
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_MoeDet 'A','"+jtbh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>15){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    handler.sendEmptyMessage(0x106);
                    NetHelper.uploadNetworkError("Exec PAD_Get_MoeDet",jtbh,sharedPreferences.getString("mac",""));
                }

            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1://机台变更
                getNetData();
                break;
            case 2://穴数变更
                break;
        }
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(JtjqsbgActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            default:
                break;
        }
    }
}
