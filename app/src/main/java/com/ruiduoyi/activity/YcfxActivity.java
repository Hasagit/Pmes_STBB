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
    String wkno="";
    private PopupWindowSpinner spinner_list;
    private PopupDialog dialog;
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

        dialog=new PopupDialog(this,400,300);
        dialog.setTitle("提示");
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
    }

    private void initData(){
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        thread_1.start();
    }

    private void initListView(List<List<String>>list){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            List<String>item=list.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("lab_1",item.get(2));
            map.put("lab_2",item.get(3));
            map.put("lab_3",item.get(4));
            map.put("lab_4",item.get(5));
            map.put("lab_5",item.get(6));
            map.put("lab_6",item.get(8));
            map.put("lab_7",item.get(9));
            map.put("lab_8",item.get(10));
            map.put("lab_9",item.get(11));
            map.put("lab_10",item.get(12));
            map.put("lab_11",item.get(13));
            map.put("lab_12",item.get(7));
            data.add(map);
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
                    List<List<String>>list_1=(List<List<String>>)msg.obj;
                    initListView(list_1);
                    break;
                case 0x107:
                    String zldm= (String) msg.obj;
                    getYylb(zldm);
                    break;
                case 0x108:
                    final List<List<String>>list= (List<List<String>>) msg.obj;
                    final List<String>data=new ArrayList<>();
                    for (int i=0;i<list.size();i++){
                        data.add(list.get(i).get(0)+"\t\t"+list.get(i).get(1));
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
                    break;
                case 0x109:
                    List<List<String>>list1= (List<List<String>>) msg.obj;
                    List<Map<String,String>>data1=new ArrayList<>();
                    for (int i=0;i<list1.size();i++){
                        Map<String,String>map=new HashMap<>();
                        map.put("lab_1",list1.get(i).get(0));
                        map.put("lab_2",list1.get(i).get(1));
                        data1.add(map);
                    }
                    adapter=new YyfxAdapter(YcfxActivity.this,R.layout.list_item_ycfx,data1);
                    blmsList.setAdapter(adapter);
                    break;
                case 0x110:
                    dialog.setMessage("提交成功");
                    dialog.setMessageTextColor(Color.BLACK);
                    dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtils.sendCountdownReceiver(YcfxActivity.this);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
                case 0x111:
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


            }
        }
    };


    private void getYylb(final String zldm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'B','"+jtbh+"','"+zldm+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x108;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        }).start();
    }





    //请求表格数据
    Thread thread_1=new Thread(new Runnable() {
        @Override
        public void run() {
            List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_YcmInf '"+jtbh+"'");
            Message msg=handler.obtainMessage();
            if(list!=null){
                msg.what=0x100;
                msg.obj=list;
            }else {
                msg.what=0x101;
            }
            handler.sendMessage(msg);
        }
    });

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
                final List<Map<String,String>>select_data=adapter.getSelectData();
                if (!(select_data.size()>0)){
                    Toast.makeText(YcfxActivity.this,"请先选取原因描述",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i=0;i<select_data.size();i++){
                                Map<String,String>uplaod_data=select_data.get(i);
                                upLoadOneData(uplaod_data,wkno);
                            }
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }


    private void getListData(final List<List<String>>list, final int position, List<String>data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list1=NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'C','"+jtbh+"','"+list.get(position).get(0)+"'");
                if (list1!=null){
                    if (list1.size()>0){
                        if (list1.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x109;
                            msg.obj=list1;
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg=handler.obtainMessage();
                        msg.what=0x109;
                        msg.obj=list1;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
        lbdm=list.get(position).get(0);
        spinner_1.setText(data.get(position));
    }


    private void upLoadOneData(Map<String,String>selectData,String wkno){
        List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Upd_YclInfo " +
                "'"+jtbh+"','"+text_2.getText().toString()+"','"+lbdm+"'," + "'"+selectData.get("lab_1")+"','0','"+wkno+"'");
        if (list!=null){
            if (list.size()>0){
                if (list.get(0).size()>0){
                    if (list.get(0).get(0).equals("OK")){
                        handler.sendEmptyMessage(0x110);
                    }
                }
            }
        }else {
            handler.sendEmptyMessage(0x111);
        }
    }

}
