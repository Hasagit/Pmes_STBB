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
    private String jtbh,sub_num,zzdh;
    private Handler handler;
    private TextView mjbh_text,mjmc_text,mjqs_text,cpbh_text,pmgg_text,sjqs_text,jtbh_text;
    private Button cancle_btn;
    private Animation anim;
    private PopupDialog tipDialog;
    private LinearLayout qs_bg,jtbh_bg;
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
        listView_2=(ListView)findViewById(R.id.list_jtjqsbg_2);
        mjbh_text=(TextView)findViewById(R.id.mjbh);
        mjmc_text=(TextView)findViewById(R.id.mjmc);
        mjqs_text=(TextView)findViewById(R.id.mjqs);
        cpbh_text=(TextView)findViewById(R.id.cpbh);
        pmgg_text=(TextView)findViewById(R.id.pmgg);
        sjqs_text=(TextView)findViewById(R.id.sjqs);
        jtbh_text=(TextView)findViewById(R.id.jtbh);
        begin_btn=(Button)findViewById(R.id.begin_btn);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);

        jtbh_bg=(LinearLayout)findViewById(R.id.jtbh_bg);
        qs_bg=(LinearLayout)findViewById(R.id.qs_bg);
        cancle_btn.setOnClickListener(this);
        begin_btn.setOnClickListener(this);
        jtbh_text.setText(jtbh);



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
        Intent intent_from=getIntent();
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
                    case 0x104:
                        Map<String,String>map= (Map<String, String>) msg.obj;
                        mjbh_text.setText(map.get("mjbh"));
                        mjmc_text.setText(map.get("mjmc"));
                        mjqs_text.setText(map.get("mjqs"));
                        cpbh_text.setText(map.get("wldm"));
                        pmgg_text.setText(map.get("pmgg"));
                        sjqs_text.setText(map.get("cpqs"));
                        zzdh=map.get("zzdh");
                        getDutouListData(zzdh);
                        jtbh_text.setText(jtbh);
                        if (mjqs_text.getText().toString().equals(sjqs_text.getText().toString())){
                            sjqs_text.setBackgroundColor(Color.WHITE);
                        }else {
                            sjqs_text.setBackgroundColor(Color.RED);
                        }
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
                    AppUtils.uploadNetworkError("Exec PAD_Get_MoeDet",jtbh,sharedPreferences.getString("mac",""));
                }

            }
        }).start();
    }


    private void getDutouListData(final String zzdh){
        //堵头信息表
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list_dt=NetHelper.getQuerysqlResult("Exec PAD_Get_MoeJtxsInf 'A','"+zzdh+"'");
                if (list_dt!=null){
                    if (list_dt.size()>0){
                        if (list_dt.get(0).size()>5){
                            Message msg=handler.obtainMessage();
                            msg.what=0x101;
                            msg.obj=list_dt;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_MoeJtxsInf 'A'",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }





    private boolean isReady(){
        if (mjbh_text.getText().toString().equals("")){
            tipDialog.setMessage("请先选择工单信息");
            tipDialog.show();
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            getNetData();
            if (data!=null&&adapter_dt!=null){
                data_dt.clear();
                adapter_dt.notifyDataSetChanged();
            }
            mjbh_text.setText("");
            mjmc_text.setText("");
            mjqs_text.setText("");
            cpbh_text.setText("");
            pmgg_text.setText("");
            sjqs_text.setText("");
            jtbh_text.setText("");
            sjqs_text.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(JtjqsbgActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.begin_btn:
                begin_btn.startAnimation(anim2);
                if (isReady()){
                    Intent intent=new Intent(JtjqsbgActivity.this,Jtjqsbg2Activity.class);
                    intent.putExtra("wkno",wkno);
                    intent.putExtra("zzdh",zzdh);
                    intent.putExtra("mjbh",mjbh_text.getText().toString());
                    intent.putExtra("mjmc",mjmc_text.getText().toString());
                    intent.putExtra("mjqs",mjqs_text.getText().toString());
                    intent.putExtra("cpqs",sjqs_text.getText().toString());
                    startActivityForResult(intent,1);
                }
                break;
            default:
                break;
        }
    }
}
