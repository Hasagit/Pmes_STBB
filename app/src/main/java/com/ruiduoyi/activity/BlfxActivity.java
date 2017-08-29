package com.ruiduoyi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.Dialog.BaseDialogActivity;
import com.ruiduoyi.adapter.FailureAnalysisAdapter;
import com.ruiduoyi.adapter.SigleSelectAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.PopupWindowSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlfxActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button cancle_btn;
    private Button spinner;//不良产品下拉框
    private ListView listView,listView_register;
    private String jtbh;
    private TextView sjsx_text,zzdh_text,gddh_text,scph_text,mjbh_text,cpbh_text,pmgg_text,mjmc_text,jhsl_text,lpsl_text,blpsl_text,bldm_text,blms_text;
    private Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,btn_clear,btn_submit,btn_del;
    private TextView sub_text;
    private Animation anim;
    private String sub_num;
    private String wkno,zzdh_str;
    private SigleSelectAdapter adapter2;
    private List<Map<String,String>>data1;
    private int select_position;
    private PopupWindowSpinner popupWindowSpinner;
    private List<String>zzdh_list=new ArrayList<>();
    private int zzdh_position;
    private PopupDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blfx);
        initData();
        initView();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100://初始化
                    List<List<String>>list_spinner=(List<List<String>>)msg.obj;
                    initSpinner(list_spinner);
                    break;
                case 0x101:
                    List<List<String>>list1= (List<List<String>>) msg.obj;
                    data1=new ArrayList<>();
                    for (int i=0;i<list1.size();i++){
                        Map<String,String>map=new HashMap<>();
                        map.put("lab_1",list1.get(i).get(0));
                        map.put("lab_2",list1.get(i).get(1));
                        map.put("lab_3","0");
                        data1.add(map);
                    }
                    adapter2= new SigleSelectAdapter(BlfxActivity.this, data1) {
                        @Override
                        public void onRadioSelectListener(int position,Map<String, String> map) {
                            select_position=position;
                            bldm_text.setText(map.get("lab_1"));
                            blms_text.setText(map.get("lab_2"));
                        }

                    };
                    listView.setAdapter(adapter2);
                    break;
                case 0x102:
                    //blpsl_text.setText(Integer.parseInt(blpsl_text.getText().toString())+Integer.parseInt(sub_text.getText().toString())+"");
                    List<List<String>>list2= (List<List<String>>) msg.obj;
                    List<Map<String,String>>data=new ArrayList<>();
                    for (int i=0;i<list2.size();i++){
                        Map<String,String>map=new HashMap<>();
                        List<String>item=list2.get(i);
                        map.put("lab_1",item.get(0));
                        map.put("lab_2",item.get(1));
                        map.put("lab_3",item.get(2));
                        map.put("lab_4",item.get(3));
                        data.add(map);
                    }
                    SimpleAdapter adapter=new SimpleAdapter(BlfxActivity.this,data,R.layout.list_item_b8_2,
                            new String[]{"lab_1","lab_2","lab_3","lab_4"},new int[]{R.id.lab_1,R.id.lab_2,
                            R.id.lab_3,R.id.lab_4});
                    listView_register.setAdapter(adapter);
                    listView_register.startAnimation(anim);
                    break;
                case 0x103:
                    List<List<String>>list= (List<List<String>>) msg.obj;
                    sjsx_text.setText(list.get(0).get(0));
                    zzdh_text.setText(list.get(0).get(1));
                    gddh_text.setText(list.get(0).get(2));
                    scph_text.setText(list.get(0).get(3));
                    mjbh_text.setText(list.get(0).get(4));
                    cpbh_text.setText(list.get(0).get(5));
                    pmgg_text.setText(list.get(0).get(6));
                    mjmc_text.setText(list.get(0).get(16));
                    jhsl_text.setText(list.get(0).get(21));
                    lpsl_text.setText(list.get(0).get(23));
                    blpsl_text.setText(list.get(0).get(24));
                    break;
                case 0x104:
                    afterConmmit();
                    break;
                case 0x105:
                    Toast.makeText(BlfxActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        zzdh_str=sharedPreferences.getString("zzdh","");
        anim= AnimationUtils.loadAnimation(this,R.anim.sub_num_anim);
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        getNetData();
        getGdInfo();
    }

    private void initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        spinner=(Button) findViewById(R.id.blcp_spinner);
        listView=(ListView)findViewById(R.id.list_b8);
        sub_text=(TextView)findViewById(R.id.sub_text);
        sjsx_text=(TextView)findViewById(R.id.dq_1);
        zzdh_text=(TextView)findViewById(R.id.dq_2);
        gddh_text=(TextView)findViewById(R.id.dq_3);
        scph_text=(TextView)findViewById(R.id.dq_4);
        mjbh_text=(TextView)findViewById(R.id.dq_5);
        mjmc_text=(TextView)findViewById(R.id.dq_6);
        cpbh_text=(TextView)findViewById(R.id.dq_7);
        pmgg_text=(TextView)findViewById(R.id.dq_8);
        jhsl_text=(TextView)findViewById(R.id.dq_9);
        lpsl_text=(TextView)findViewById(R.id.dq_10);
        blpsl_text=(TextView)findViewById(R.id.dq_11);
        bldm_text=(TextView)findViewById(R.id.bldm_text);
        blms_text=(TextView)findViewById(R.id.blms_text);




        btn_0=(Button)findViewById(R.id.btn_0);
        btn_1=(Button)findViewById(R.id.btn_1);
        btn_2=(Button)findViewById(R.id.btn_2);
        btn_3=(Button)findViewById(R.id.btn_3);
        btn_4=(Button)findViewById(R.id.btn_4);
        btn_5=(Button)findViewById(R.id.btn_5);
        btn_6=(Button)findViewById(R.id.btn_6);
        btn_7=(Button)findViewById(R.id.btn_7);
        btn_8=(Button)findViewById(R.id.btn_8);
        btn_9=(Button)findViewById(R.id.btn_9);
        btn_del=(Button)findViewById(R.id.btn_del);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        listView_register=(ListView)findViewById(R.id.list_b8_2);
        cancle_btn.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        dialog=new PopupDialog(this,450,350);
        dialog.setTitle("提示");
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void initListView(List<List<String>>lists){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            Map<String,String>map=new HashMap<>();
            List<String>item=lists.get(i);
            map.put("lab_1",item.get(0));
            map.put("lab_2",item.get(1));
            map.put("isFocus",1+"");
            data.add(map);
        }
        SigleSelectAdapter adapter1=new SigleSelectAdapter(BlfxActivity.this,data) {
            @Override
            public void onRadioSelectListener(int position,Map<String, String> map) {
                bldm_text.setText(map.get("lab_1"));
                blms_text.setText(map.get("lab_2"));
            }
        };
        FailureAnalysisAdapter adapter=new FailureAnalysisAdapter(BlfxActivity.this,R.layout.list_item_b8,data,bldm_text,blms_text);
        listView.setAdapter(adapter1);
    }


    private void initSpinner(List<List<String>>lists){
        final List<String>data=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            data.add(item.get(1)+"\t\t"+item.get(2));
            zzdh_list.add(item.get(0));
        }
        popupWindowSpinner=new PopupWindowSpinner(BlfxActivity.this,data,R.layout.spinner_list_b7,R.id.lab_1,305);
        popupWindowSpinner.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinner.setText(data.get(position));
                zzdh_position=position;
                popupWindowSpinner.dismiss();
            }
        });
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (popupWindowSpinner!=null){
                   popupWindowSpinner.showDownOn(spinner);
               }
            }
        });
        /*ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);*/
    }


    private void afterConmmit(){
        sub_text.setText("0");
        sub_text.startAnimation(anim);
        bldm_text.setText("");
        blms_text.setText("");
        btn_submit.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.btn_0:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if((!sub_num.equals("0"))&&(!sub_num.equals("-"))){
                   sub_text.setText(sub_num+"0");
                }
                break;
            case R.id.btn_1:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"1");
                }else {
                    sub_text.setText("1");
                }
                break;
            case R.id.btn_2:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"2");
                }else {
                    sub_text.setText("2");
                }
                break;
            case R.id.btn_3:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"3");
                }else {
                    sub_text.setText("3");
                }
                break;
            case R.id.btn_4:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"4");
                }else {
                    sub_text.setText("4");
                }
                break;
            case R.id.btn_5:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"5");
                }else {
                    sub_text.setText("5");
                }
                break;
            case R.id.btn_6:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"6");
                }else {
                    sub_text.setText("6");
                }
                break;
            case R.id.btn_7:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"7");
                }else {
                    sub_text.setText("7");
                }
                break;
            case R.id.btn_8:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"8");
                }else {
                    sub_text.setText("8");
                }
                break;
            case R.id.btn_9:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(!sub_num.equals("0")){
                    sub_text.setText(sub_num+"9");
                }else {
                    sub_text.setText("9");
                }
                break;
            case R.id.btn_submit:
                if (isReady()){
                    btn_submit.setEnabled(false);
                    upLoadData(wkno);
                }
                break;
            case R.id.btn_del:
                sub_text.startAnimation(anim);
                sub_num=sub_text.getText().toString();
                if(sub_num.equals("0")){
                    sub_text.setText("-");
                }
                break;
            case R.id.btn_clear:
                sub_text.startAnimation(anim);
                sub_text.setText("0");
                break;
            default:
                break;
        }
    }

    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //产品选取
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'D','"+jtbh+"','"+zzdh_str+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>2){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    handler.sendEmptyMessage(0x105);
                    NetHelper.uploadNetworkError("Exec PAD_Get_ZlmYywh 'D'"+jtbh+"','",sharedPreferences.getString("jtnh",""),
                            sharedPreferences.getString("mac",""));
                }


                //不良表格
                List<List<String>>list1=NetHelper.getQuerysqlResult("Exec PAD_Get_Blllist");
                if (list1!=null){
                    if (list1.size()>0){
                        if (list1.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x101;
                            msg.obj=list1;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    handler.sendEmptyMessage(0x105);
                    NetHelper.uploadNetworkError("Exec PAD_Get_Blllist",sharedPreferences.getString("jtbh",""),
                            sharedPreferences.getString("mac",""));
                }


                List<List<String>>list2=NetHelper.getQuerysqlResult("Exec PAD_Get_BlmInfo '"+jtbh+"'");
                if (list2!=null){
                    if (list2.size()>0){
                        if (list2.get(0).size()>3){
                            Message msg=handler.obtainMessage();
                            msg.what=0x102;
                            msg.obj=list2;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    handler.sendEmptyMessage(0x105);
                }


            }
        }).start();
    }



    public void upLoadData(final String wkno){
        new Thread(new Runnable() {
            @Override
            public void run() {
                upLoadOneData(wkno);
            }
        }).start();
    }


    private boolean isReady(){
        if (sub_text.getText().toString().length()>9){
            dialog.setMessage("数值已经超过允许范围");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        if (spinner.getText().toString().equals("")){
            dialog.setMessage("请先选取产品");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;

        }
        if (bldm_text.getText().toString().equals("")){
            dialog.setMessage("请先选取不良描述");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        if (sub_text.getText().toString().equals("0")){
            dialog.setMessage("请先输入不良数");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        if (Integer.parseInt(lpsl_text.getText().toString())<Integer.parseInt(sub_text.getText().toString().trim())){
            dialog.setMessage("输入的数量不能大于良品数量");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        return true;
    }

    private void getGdInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_OrderInfo  '"+jtbh+"'");
                if(list!=null){
                    handler.sendEmptyMessage(0x111);
                    if(list.size()>0){
                        if (list.get(0).size()>26){
                            Message msg=handler.obtainMessage();
                            msg.what=0x103;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    handler.sendEmptyMessage(0x105);
                    NetHelper.uploadNetworkError("Exec PAD_Get_OrderInfo NetWorkError",jtbh,sharedPreferences.getString("mac",""));
                    //handler.sendEmptyMessage(0x110);
                }
            }
        }).start();
    }

    private void upLoadOneData(String wkno){
        List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Add_BlmInfo " +
                "'A','"+zzdh_list.get(zzdh_position)+"','','','"+jtbh+"','','"+bldm_text.getText().toString()+"'," +
                "'"+sub_text.getText().toString()+"','"+wkno+"'");
        handler.sendEmptyMessage(0x104);
        if (list!=null){
            if (list.size()>0){
                if (list.get(0).size()>0){
                    if (list.get(0).get(0).equals("OK")){
                        getGdInfo();
                        List<List<String>>list1=NetHelper.getQuerysqlResult("Exec PAD_Get_BlmInfo '"+jtbh+"'");
                        if (list1!=null){
                            if (list1.size()>0){
                                if (list1.get(0).size()>3){
                                    Message msg=handler.obtainMessage();
                                    msg.what=0x102;
                                    msg.obj=list1;
                                    handler.sendMessage(msg);
                                }
                            }
                        }else {
                            handler.sendEmptyMessage(0x105);
                        }
                        return ;
                    }
                }
            }
        }else {
            handler.sendEmptyMessage(0x105);
            upLoadOneData(wkno);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.sendUpdateInfoFragmentReceiver(this);
        dialog.dismiss();
    }



}
