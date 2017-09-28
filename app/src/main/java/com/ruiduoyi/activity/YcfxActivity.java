package com.ruiduoyi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.adapter.YichangfenxiAdapter;
import com.ruiduoyi.adapter.YyfxAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.PopupWindowSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YcfxActivity extends BaseActivity implements View.OnClickListener{
    private Button cancle_btn,sub_btn;
    private ListView listView,blmsList;
    private Button spinner_1;
    private TextView text_1,text_2,text_3,text_4,text_5,text_6,text_7,text_8,text_9,text_10,text_11,text_key;
    private String jtbh,lbdm;
    private YyfxAdapter adapter;
    private String wkno="";
    private String keyid;
    private PopupWindowSpinner spinner_list;
    private PopupDialog dialog;
    private PopupDialog dialog_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ycfx);
        initView();
        initData();
    }

    private void initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        sub_btn=(Button)findViewById(R.id.sub_btn);
        text_1=(TextView)findViewById(R.id.text_1);
        text_2=(TextView)findViewById(R.id.text_2);
        text_3=(TextView)findViewById(R.id.text_3);
        text_4=(TextView)findViewById(R.id.text_4);
        text_5=(TextView)findViewById(R.id.text_5);
        text_6=(TextView)findViewById(R.id.text_6);
        text_7=(TextView)findViewById(R.id.text_7);
        text_8=(TextView)findViewById(R.id.text_8);
        text_9=(TextView)findViewById(R.id.text_9);
        text_10=(TextView)findViewById(R.id.text_10);
        text_11=(TextView)findViewById(R.id.text_11);
        text_key=(TextView)findViewById(R.id.text_key);
        spinner_1=(Button) findViewById(R.id.spinner_1);
        blmsList=(ListView)findViewById(R.id.list_bl);
        //spinner_2=(Button) findViewById(R.id.spinner_2);
        spinner_1.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.list_b7);
        cancle_btn.setOnClickListener(this);
        sub_btn.setOnClickListener(this);

        dialog=new PopupDialog(this,400,360);
        dialog.setTitle("提示");
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");


        dialog_tip=new PopupDialog(this,400,360);
        dialog_tip.setTitle("提示");
        dialog_tip.getCancle_btn().setVisibility(View.GONE);
        dialog_tip.getOkbtn().setText("确定");
        dialog_tip.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_tip.dismiss();
            }
        });
    }

    private void initData(){
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        getYcfxListData(1);
    }

    private void initListView(JSONArray list){
        List<Map<String,String>>data=new ArrayList<>();
        try {
            for (int i=0;i<list.length();i++){
                Map<String,String>map=new HashMap<>();
                map.put("lab_1",list.getJSONObject(i).getString("v_rq"));
                map.put("lab_2",list.getJSONObject(i).getString("v_mjbh"));
                map.put("lab_3",list.getJSONObject(i).getString("v_mjmc"));
                map.put("lab_4",list.getJSONObject(i).getString("v_wlmd"));
                map.put("lab_5",list.getJSONObject(i).getString("v_pmgg"));
                map.put("lab_6",list.getJSONObject(i).getString("v_kssj"));
                map.put("lab_7",list.getJSONObject(i).getString("v_jssj"));
                map.put("lab_8",list.getJSONObject(i).getString("v_sjgs"));
                map.put("lab_9",list.getJSONObject(i).getString("v_bzgs"));
                map.put("lab_10",list.getJSONObject(i).getString("v_ksname"));
                map.put("lab_11",list.getJSONObject(i).getString("v_jsname"));
                map.put("lab_12",list.getJSONObject(i).getString("v_zldm"));
                map.put("keyid",list.getJSONObject(i).getString("v_keyid"));
                data.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<TextView>list_text=new ArrayList<>();
        list_text.add(text_1);
        list_text.add(text_2);
        list_text.add(text_3);
        list_text.add(text_4);
        list_text.add(text_5);
        list_text.add(text_6);
        list_text.add(text_7);
        list_text.add(text_8);
        list_text.add(text_9);
        list_text.add(text_10);
        list_text.add(text_11);
        list_text.add(text_key);
        YichangfenxiAdapter adapter=new YichangfenxiAdapter(YcfxActivity.this,R.layout.list_item_b7,data,list_text,handler);
        listView.setAdapter(adapter);
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    JSONArray list_1= (JSONArray) msg.obj;
                    initListView(list_1);
                    break;
                case 0x101:
                    Toast.makeText(YcfxActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x107:
                    Map<String,String>map_select= (Map<String, String>) msg.obj;
                    String zldm= map_select.get("lab_12");
                    keyid=map_select.get("keyid");
                    getYylb(zldm);
                    break;
                case 0x108:
                   initYylbListView((JSONArray) msg.obj);
                    break;
                case 0x109:
                    initYymsListView((JSONArray) msg.obj);
                    break;
                case 0x110:
                    afterUploadsuccess();
                    break;
                case 0x111:
                    sub_btn.setEnabled(true);
                    dialog.setMessageTextColor(Color.RED);
                    dialog.setMessage("提交失败");
                    dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtils.sendCountdownReceiver(YcfxActivity.this);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
                case 0x112:
                    sub_btn.setEnabled(true);
                    dialog_tip.setMessage("请先选择原因描述");
                    dialog_tip.setMessageTextColor(Color.RED);
                    dialog_tip.show();
                    break;
                case 0x113:
                    dialog_tip.setMessage((String) msg.obj);
                    dialog_tip.setMessageTextColor(Color.RED);
                    dialog_tip.show();
                    sub_btn.setEnabled(true);
                    break;
                case 0x114:
                    sub_btn.setEnabled(true);
                    break;




            }
        }
    };


    private void getYylb(final String zldm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'B','"+jtbh+"','"+zldm+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x108;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg=handler.obtainMessage();
                        msg.what=0x108;
                        msg.obj=list;
                        handler.sendMessage(msg);
                    }
                }else {
                    handler.sendEmptyMessage(0x101);
                }*/
                JSONArray list=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_ZlmYywh 'B','"+jtbh+"','"+zldm+"'");
                if (list!=null){
                    Message msg=handler.obtainMessage();
                    msg.what=0x108;
                    msg.obj=list;
                    handler.sendMessage(msg);
                }else {
                    handler.sendEmptyMessage(0x101);
                }
            }
        }).start();
    }

    private void initYylbListView(final JSONArray list){
        final List<String>data=new ArrayList<>();
        try {
            for (int i=0;i<list.length();i++){
                data.add(list.getJSONObject(i).getString("v_lbdm")+"\t\t"+list.getJSONObject(i).getString("v_lbmc"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data.size()>0){
            getListData(list,0,data);
        }
        spinner_list=new PopupWindowSpinner(YcfxActivity.this,data,R.layout.spinner_list_yyfx,
                R.id.lab_1,445);
        spinner_list.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AppUtils.sendCountdownReceiver(YcfxActivity.this);
                getListData(list,position,data);
                spinner_list.dismiss();
            }
        });
    }

    private void initYymsListView(JSONArray list1){
        List<Map<String,String>>data1=new ArrayList<>();
        try {
            for (int i=0;i<list1.length();i++){
                Map<String,String>map=new HashMap<>();
                map.put("lab_1",list1.getJSONObject(i).getString("v_lbdm"));
                map.put("lab_2",list1.getJSONObject(i).getString("v_lbmc"));
                map.put("isCheck","0");
                data1.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter=new YyfxAdapter(YcfxActivity.this,R.layout.list_item_ycfx,data1);
        blmsList.setAdapter(adapter);
    }


    //请求表格数据
    private void getYcfxListData(final int type){
        new Thread(new Runnable() {
        @Override
        public void run() {
            /*List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_YcmInf '"+jtbh+"'");
            Message msg=handler.obtainMessage();
            if(list!=null){
                if(list.size()>0){
                    if (list.get(0).size()>13){
                        msg.what=0x100;
                        msg.obj=list;
                    }
                }else {
                    if (type==1){
                        msg.what=0x100;
                        msg.obj=list;
                    }else {
                        finish();
                    }
                }
            }else {
                handler.sendEmptyMessage(0x101);
            }
            handler.sendMessage(msg);*/
            JSONArray list= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_YcmInf '"+jtbh+"'");
            Message msg=handler.obtainMessage();
            if(list!=null){
                if(list.length()>0){
                    msg.what=0x100;
                    msg.obj=list;
                }else {
                    if (type==1){
                        msg.what=0x100;
                        msg.obj=list;
                    }else {
                        finish();
                    }
                }
            }else {
                handler.sendEmptyMessage(0x101);
            }
            handler.sendMessage(msg);
        }
    }).start();

    }


    //执行上传数据成功之后
    private void afterUploadsuccess(){
        sub_btn.setEnabled(true);
        getYcfxListData(2);
        text_1.setText("");
        text_2.setText("");
        text_3.setText("");
        text_4.setText("");
        text_5.setText("");
        text_6.setText("");
        text_7.setText("");
        text_8.setText("");
        text_9.setText("");
        text_10.setText("");
        text_11.setText("");
        text_key.setText("");
        spinner_1.setText("");
        initYymsListView(new JSONArray());
        initYylbListView(new JSONArray());
    }

    private boolean isReady(){
        if (text_1.getText().toString().equals("")){
            dialog_tip.setMessageTextColor(Color.RED);
            dialog_tip.setMessage("请先选取异常信息");
            dialog_tip.show();
            return false;
        }

        if (spinner_1.getText().toString().equals("")){
            dialog_tip.setMessageTextColor(Color.RED);
            dialog_tip.setMessage("请先选取原因类别");
            dialog_tip.show();
            return false;
        }
       /* final List<Map<String,String>>select_data=adapter.getSelectData();
        if (!(select_data.size()>0)){
            dialog_tip.setMessageTextColor(Color.RED);
            dialog_tip.setMessage("请先选取原因描述");
            dialog_tip.show();
            return false;
        }*/
        return true;
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(YcfxActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.spinner_1:
                if (spinner_list!=null){
                    spinner_list.showDownOn(spinner_1);
                }
                break;
            case R.id.sub_btn:
                if (isReady()){
                   upLoaData();
                }
                break;
            default:
                break;
        }
    }


    private void getListData(final JSONArray list, final int position, List<String>data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray list1=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_ZlmYywh 'C','"+jtbh+"','"+
                            list.getJSONObject(position).getString("v_lbdm")+"'");
                    if (list1!=null){
                        Message msg=handler.obtainMessage();
                        msg.what=0x109;
                        msg.obj=list1;
                        handler.sendMessage(msg);
                    }else {
                        handler.sendEmptyMessage(0x101);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            lbdm=list.getJSONObject(position).getString("v_lbdm");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinner_1.setText(data.get(position));
    }


    private void upLoaData(){
        sub_btn.setEnabled(false);
        final List<Map<String,String>>select_data=adapter.getSelectData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String select_str="";
                List<Map<String,String>>data=adapter.getData();
                for (int i=0;i<data.size();i++){
                    if (data.get(i).get("isCheck").equals("1")){
                        select_str=data.get(i).get("lab_1");
                    }
                    //Map<String,String>uplaod_data=select_data.get(i);
                    //select_str=select_str+uplaod_data.get("lab_1")+";";
                    //upLoadOneData(uplaod_data,wkno);
                }
                if (select_str.equals("")){
                    handler.sendEmptyMessage(0x112);
                    return;
                }
                try {
                    JSONArray list=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Upd_YclInfo " +
                            "'"+jtbh+"','"+text_2.getText().toString()+"','"+lbdm+"'," + "'"+select_str+"',"+keyid+",'"+wkno+"'");
                    if (list!=null){
                        if (list.length()>0){
                            if (list.getJSONObject(0).getString("Column1").equals("OK")){
                                handler.sendEmptyMessage(0x110);
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x113;
                                msg.obj=list.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }else {
                            handler.sendEmptyMessage(0x114);
                        }
                    }else {
                        handler.sendEmptyMessage(0x111);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.sendUpdateInfoFragmentReceiver(this);
        dialog_tip.dismiss();
    }
}
