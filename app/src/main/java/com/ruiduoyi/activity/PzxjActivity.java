package com.ruiduoyi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.Dialog.DialogGdSelectActivity;
import com.ruiduoyi.adapter.EasyArrayAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PzxjActivity extends BaseActivity implements View.OnClickListener{
    private String wkno,gdid;
    private TextView jtbh_text,cpbh_text,pm_text,scdh_text,mjbh_text,mqzs_text,cl_text,ys_text,
    jcsj_text;
    private EditText scqs_edit,jcsl_edit,bz_edit;
    private ListView jc_list,gs_list;
    private RadioGroup radioGroup;
    private RadioButton hg_radio,bhg_radio;
    private Button ok_btn,cancle_btn;
    private Handler handler;
    private List<Map<String,String>>jc_data,gs_data,gs_select_data,jc_select_data;
    private PopupDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pzxj);
        initData();
        initView();
    }


    private void initView(){
        jtbh_text=(TextView)findViewById(R.id.jtbh);
        cpbh_text=(TextView)findViewById(R.id.cpbh);
        pm_text=(TextView)findViewById(R.id.pm);
        scdh_text=(TextView)findViewById(R.id.scdh);
        mjbh_text=(TextView)findViewById(R.id.mjbh);
        mqzs_text=(TextView)findViewById(R.id.mqzs);
        scqs_edit=(EditText) findViewById(R.id.scqs);
        cl_text=(TextView)findViewById(R.id.cl);
        ys_text=(TextView)findViewById(R.id.ys);
        jcsj_text=(TextView)findViewById(R.id.jcsj);
        jcsl_edit=(EditText)findViewById(R.id.jcsl);
        ok_btn=(Button)findViewById(R.id.save_btn);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        jc_list=(ListView)findViewById(R.id.list_1);
        gs_list=(ListView)findViewById(R.id.list_2);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        hg_radio=(RadioButton)findViewById(R.id.hg_radio);
        bhg_radio=(RadioButton)findViewById(R.id.bhg_radio);
        bz_edit=(EditText)findViewById(R.id.bz);

        dialog=new PopupDialog(this,450,350);
        dialog.getOkbtn().setText("确定");
        dialog.setTitle("提示");
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(PzxjActivity.this);
                dialog.dismiss();
            }
        });
        cancle_btn.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
    }

    private void initData(){
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        try {
                            JSONObject json= (JSONObject) msg.obj;
                            jtbh_text.setText(json.getString("moe_jtbh"));
                            cpbh_text.setText(json.getString("moe_wldm"));
                            pm_text.setText(json.getString("itm_pmgg"));
                            scdh_text.setText(json.getString("moe_zzdh"));
                            mjbh_text.setText(json.getString("moe_mjbh"));
                            mqzs_text.setText(json.getString("mjm_xs"));
                            scqs_edit.setText(json.getString("moe_xs"));
                            cl_text.setText(json.getString("itm_czdm"));
                            ys_text.setText(json.getString("ysm_ysmc"));
                            jcsj_text.setText(json.getString("moe_chktime"));
                            gdid=json.getString("moe_id");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case 0x101:
                        initJcList((List<Map<String, String>>) msg.obj);
                        break;
                    case 0x102:
                        initGsList((List<Map<String, String>>) msg.obj);
                        break;
                    case 0x103:
                        finish();
                        break;
                    case 0x104:
                        dialog.setMessage("数据上传失败，请重试");
                        dialog.setMessageTextColor(Color.RED);
                        dialog.show();
                        break;
                }
            }
        };


        getGdData();
        getJcListData();
        getGsData();
    }


    private void getGdData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray array= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_OrderInfo2 '"+jtbh+"' ");
                if (array!=null){
                    try {
                        Message msg=handler.obtainMessage();
                        if (array.length()==1){
                            msg.what=0x100;
                            msg.obj=array.get(0);
                            handler.sendMessage(msg);
                        }else if (array.length()>1){
                            Intent intent=new Intent(PzxjActivity.this,DialogGdSelectActivity.class);
                            intent.putExtra("gd_array",array.toString());
                            startActivityForResult(intent,1);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_OrderInfo2",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }

    private void getJcListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray array=NetHelper.getQuerysqlResultJsonArray(" PAD_GetQcItem");
                    if (array!=null){
                        List<Map<String,String>>data=new ArrayList<Map<String, String>>();
                        for (int i=0;i<array.length();i++){
                            Map<String,String>map=new HashMap<String, String>();
                            JSONObject json=array.getJSONObject(i);
                            map.put("lab_1",json.getString("lbm_lbdm"));
                            map.put("lab_2",json.getString("lbm_lbmc"));
                            map.put("lab_3","");
                            map.put("lab_4","");
                            map.put("lab_5","");
                            map.put("lab_6","");
                            data.add(map);
                        }
                        Message msg=handler.obtainMessage();
                        msg.what=0x101;
                        msg.obj=data;
                        handler.sendMessage(msg);
                    }else {
                        AppUtils.uploadNetworkError("PAD_GetQcItem",jtbh,sharedPreferences.getString("mac",""));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initJcList(final List<Map<String,String>>data){
        jc_data=data;
        EasyArrayAdapter adapter=new EasyArrayAdapter(this,R.layout.list_item_pzxj_jc,jc_data) {
            @Override
            public View getEasyView(final int position, View convertView, ViewGroup parent) {
                View view= LayoutInflater.from(getContext()).inflate(R.layout.list_item_pzxj_jc,null);
                /*if (convertView!=null){
                    view=convertView;
                }else {
                    view= LayoutInflater.from(getContext()).inflate(R.layout.list_item_pzxj_jc,null);
                }*/
                TextView lab_1=(TextView) view.findViewById(R.id.lab_1);
                TextView lab_2=(TextView) view.findViewById(R.id.lab_2);
                EditText lab_3=(EditText)view.findViewById(R.id.lab_3);
                EditText lab_4=(EditText)view.findViewById(R.id.lab_4);
                EditText lab_5=(EditText)view.findViewById(R.id.lab_5);
                EditText lab_6=(EditText)view.findViewById(R.id.lab_6);
                lab_3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_3",s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_3",s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                lab_4.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_4",s.toString());

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_4",s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                lab_5.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_5",s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_5",s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                lab_6.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_6",s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        jc_data.get(position).put("lab_6",s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lab_1.setText(jc_data.get(position).get("lab_1"));
                lab_2.setText(jc_data.get(position).get("lab_2"));
                lab_3.setText(jc_data.get(position).get("lab_3"));
                lab_4.setText(jc_data.get(position).get("lab_4"));
                lab_5.setText(jc_data.get(position).get("lab_5"));
                lab_6.setText(jc_data.get(position).get("lab_6"));
                return view;
            }
        };
        jc_list.setAdapter(adapter);
    }


    private void getGsData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray array=NetHelper.getQuerysqlResultJsonArray("Exec PAD_GetQcImproveList");
                    if (array!=null){
                        List<Map<String,String>>data=new ArrayList<Map<String, String>>();
                        for (int i=0;i<array.length();i++){
                            Map<String,String>map=new HashMap<String, String>();
                            map.put("lab_1",array.getJSONObject(i).getString("lbm_lbdm"));
                            map.put("lab_2",array.getJSONObject(i).getString("lbm_lbmc"));
                            map.put("isSelect","0");
                            data.add(map);
                        }
                        Message msg=handler.obtainMessage();
                        msg.what=0x102;
                        msg.obj=data;
                        handler.sendMessage(msg);
                    }else {
                        AppUtils.uploadNetworkError("Exec PAD_GetQcImproveList",jtbh,"");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initGsList(final List<Map<String,String>>data){
        gs_data=data;
        EasyArrayAdapter adapter=new EasyArrayAdapter(this,R.layout.list_item_pzxj_gs,gs_data) {
            @Override
            public View getEasyView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView!=null){
                    view=convertView;
                }else {
                    view=LayoutInflater.from(getContext()).inflate(R.layout.list_item_pzxj_gs,null);
                }
                TextView lab_1=(TextView)view.findViewById(R.id.lab_1);
                TextView lab_2=(TextView)view.findViewById(R.id.lab_2);
                final CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkbox);
                LinearLayout bg=(LinearLayout)view.findViewById(R.id.bg);
                final Map<String,String>map=gs_data.get(position);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        if (isChecked){
                            map.put("isSelect","1");
                        }else {
                            map.put("isSelect","0");
                        }
                    }
                });
                bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.sendCountdownReceiver(PzxjActivity.this);
                        if (map.get("isSelect").equals("0")){
                            checkBox.setChecked(true);
                            map.put("isSelect","1");
                        }else {
                            checkBox.setChecked(false);
                            map.put("isSelect","0");
                        }
                    }
                });
                lab_1.setText(map.get("lab_1"));
                lab_2.setText(map.get("lab_2"));
                if (map.get("isSelect").equals("1")){
                    checkBox.setChecked(true);
                }else if (map.get("isSelect").equals("0")){
                    checkBox.setChecked(false);
                }
                return view;
            }
        };
        gs_list.setAdapter(adapter);
    }

    private boolean isReady(){
        if (jcsl_edit.getText().toString().equals("")){
            dialog.setMessage("请先输入检查数量");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        jc_select_data=new ArrayList<>();
        for (int i=0;i<jc_data.size();i++){
            if ((!jc_data.get(i).get("lab_3").equals(""))|(!jc_data.get(i).get("lab_4").equals(""))|
                    (!jc_data.get(i).get("lab_5").equals(""))|(!jc_data.get(i).get("lab_6").equals(""))){
                jc_select_data.add(jc_data.get(i));
            }
        }
        if ((jc_select_data.size()==0)&&bhg_radio.isChecked()){
            dialog.setMessage("至少选择一个检查项目且缺陷数量至少有一种需要填写");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        gs_select_data=new ArrayList<>();
        for (int i=0;i<gs_data.size();i++){
            if (gs_data.get(i).get("isSelect").equals("1")){
                gs_select_data.add(gs_data.get(i));
            }
        }
        if ((gs_select_data.size()==0)&&bhg_radio.isChecked()){
            dialog.setMessage("改善措施至少选择一种");
            dialog.setMessageTextColor(Color.RED);
            dialog.show();
            return false;
        }
        return true;
    }


    private void upLoadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String pd_result="";
                    String gs_dm="";
                    String gs_mc="";
                    if (hg_radio.isChecked()){
                        pd_result="OK";
                    }else {
                        pd_result="NG";
                    }
                    for (int i=0;i<gs_select_data.size();i++){
                        gs_dm=gs_dm+gs_select_data.get(i).get("lab_1")+";";
                        gs_mc=gs_mc+gs_select_data.get(i).get("lab_2")+";";
                    }
                    if (gs_dm.length()>0){
                        gs_dm=gs_dm.substring(0,gs_dm.length()-1);
                    }
                    if (gs_mc.length()>0){
                        gs_mc=gs_mc.substring(0,gs_mc.length()-1);
                    }
                    JSONArray array=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Qcxj_Create "+gdid+","+scqs_edit.getText().toString()+"," +
                            jcsl_edit.getText().toString()+", '"+pd_result+"', '"+wkno+"','"+bz_edit.getText().toString()+"','"+gs_dm+"','"+gs_mc+"'");
                    if (array!=null){
                        if (array.length()>0){
                            String result=array.getJSONObject(0).getString("Column1");
                            String exec_result=result.substring(0,2);
                            if (exec_result.equals("OK")){
                                String xjdh=result.substring(2,result.length());
                                List<String>upload_mx_result=new ArrayList<String>();
                                for (int i=0;i<jc_select_data.size();i++){
                                    String saf=jc_select_data.get(i).get("lab_3");
                                    String cr=jc_select_data.get(i).get("lab_4");
                                    String maj=jc_select_data.get(i).get("lab_5");
                                    String min=jc_select_data.get(i).get("lab_6");
                                    if (saf.equals("")){
                                        saf="0";
                                    }
                                    if (cr.equals("")){
                                        cr="0";
                                    }
                                    if (maj.equals("")){
                                        maj="0";
                                    }
                                    if (min.equals("")){
                                        min="0";
                                    }

                                    JSONArray array2=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Qcxj_Det_Insert" +
                                            " '"+xjdh+"','"+jc_select_data.get(i).get("lab_1")+"',"+saf+"," +
                                            ""+cr+","+maj+
                                            ","+min);
                                    upload_mx_result.add(array2.getJSONObject(0).getString("Column1"));
                                }
                                int sussesTime=0;
                                for (int i=0;i<upload_mx_result.size();i++){
                                    if (upload_mx_result.get(i).equals("OK")){
                                        sussesTime=sussesTime+1;
                                    }else {
                                        handler.sendEmptyMessage(0x104);
                                    }
                                }
                                if (sussesTime==jc_select_data.size()){
                                    finish();
                                }
                            }else {
                                //执行失败
                            }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            try {
                JSONObject json=new JSONObject(data.getStringExtra("gd_json"));
                Message msg=handler.obtainMessage();
                msg.what=0x100;
                msg.obj=json;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (resultCode==2){
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(PzxjActivity.this);
        switch (v.getId()){
            case R.id.save_btn:
                if (isReady()){
                    upLoadData();
                }
                break;
            case R.id.cancle_btn:
                finish();
                break;
        }
    }
}
