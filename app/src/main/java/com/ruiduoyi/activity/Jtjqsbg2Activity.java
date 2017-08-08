package com.ruiduoyi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.adapter.EasyArrayAdapter;
import com.ruiduoyi.adapter.SigleSelectJtjqsbg;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.PopupWindowSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jtjqsbg2Activity extends BaseActivity implements View.OnClickListener{
    private Handler handler;
    private Animation anim;
    private String jtbh,zzdh,wkno,sub_num,mjbh,mjqs,mjmc,cpqs,lbdm;
    private SharedPreferences sharedPreferences;
    private TextView jtbh_text,new_jtbh_text,mjbh_text,mjmc_text,mjxs_text,cpxs_text,
            row_text,col_text,sub_text;
    private Button spinner,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,
            btn_clear,btn_submit,cancle_btn;
    private ListView listView_1,listView_2;
    private PopupWindowSpinner spinner_list;
    private PopupDialog dialog;
    private EasyArrayAdapter easyAdapter;
    private List<Map<String, String>>data_dt;
    private Button jtbhSub_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jtjqsbg2);
        initData();
        initView();
    }


    private void initView(){
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
        btn_submit=(Button)findViewById(R.id.btn_submit);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        jtbhSub_btn=(Button)findViewById(R.id.jtbh_sub);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        jtbh_text=(TextView)findViewById(R.id.jtbh_text);
        mjbh_text=(TextView)findViewById(R.id.mjbh_text);
        mjmc_text=(TextView)findViewById(R.id.mjmc_text);
        mjxs_text=(TextView)findViewById(R.id.mjxs_text);
        cpxs_text=(TextView)findViewById(R.id.cpxs_text);
        row_text=(TextView)findViewById(R.id.row);
        col_text=(TextView)findViewById(R.id.col);
        new_jtbh_text=(TextView)findViewById(R.id.new_jtbh_text);
        spinner=(Button)findViewById(R.id.spinner);
        listView_1=(ListView)findViewById(R.id.list_1);
        listView_2=(ListView)findViewById(R.id.list_2);

        jtbhSub_btn.setOnClickListener(this);
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
        btn_submit.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        row_text.setOnClickListener(this);
        col_text.setOnClickListener(this);
        spinner.setOnClickListener(this);

        jtbh_text.setText(jtbh);
        mjbh_text.setText(mjbh);
        mjmc_text.setText(mjmc);
        mjxs_text.setText(mjqs);
        cpxs_text.setText(cpqs);
        row_text.setBackgroundColor(getResources().getColor(R.color.small));
        sub_text=row_text;


        if (!mjxs_text.getText().equals(cpxs_text.getText().toString())){
            cpxs_text.setBackgroundColor(Color.RED);
        }else {
            cpxs_text.setBackgroundColor(Color.WHITE);
        }

        dialog=new PopupDialog(this,400,300);
        dialog.setTitle("提示");
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppUtils.sendCountdownReceiver(Jtjqsbg2Activity.this);
            }
        });
    }

    private void initData(){
        anim= AnimationUtils.loadAnimation(this,R.anim.sub_num_anim);



        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        Intent intent_from=getIntent();
        zzdh=intent_from.getStringExtra("zzdh");
        wkno=intent_from.getStringExtra("wkno");
        mjbh=intent_from.getStringExtra("mjbh");
        mjmc=intent_from.getStringExtra("mjmc");
        mjqs=intent_from.getStringExtra("mjqs");
        cpqs=intent_from.getStringExtra("cpqs");



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        final List<List<String>>list= (List<List<String>>) msg.obj;
                        final List<String>data_spinner=new ArrayList<>();
                        for (int i=0;i<list.size();i++){
                            data_spinner.add("\t"+list.get(i).get(1));
                        }
                        spinner_list=new PopupWindowSpinner(Jtjqsbg2Activity.this,data_spinner,R.layout.spinner_list_yyfx,R.id.lab_1,305);
                        spinner_list.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                spinner.setText(data_spinner.get(position));
                                lbdm=list.get(position).get(0);
                                Log.w("lbdm",lbdm);
                                spinner_list.dismiss();
                            }
                        });
                        break;
                    case 0x101:
                        List<List<String>>list1= (List<List<String>>) msg.obj;
                        List<Map<String,String>>data=new ArrayList<>();
                        for (int i=0;i<list1.size();i++){
                            Map<String,String>map=new HashMap<>();
                            map.put("lab_1",list1.get(i).get(0));
                            map.put("lab_2",list1.get(i).get(1));
                            map.put("lab_3",list1.get(i).get(2));
                            map.put("lab_4",list1.get(i).get(3));
                            data.add(map);
                        }
                        SigleSelectJtjqsbg adapter=new SigleSelectJtjqsbg(Jtjqsbg2Activity.this,R.layout.list_item_jtjqsbg_2,data) {
                            @Override
                            public void onRadioSelectListener(int position, Map<String, String> map) {
                                new_jtbh_text.setText(map.get("lab_1"));
                            }
                        };
                        listView_1.setAdapter(adapter);
                        break;
                    case 0x102:
                        getGongdanData();
                        /*dialog.setMessageTextColor(Color.BLACK);
                        dialog.setMessage("提交成功");
                        dialog.show();*/
                        col_text.setText("");
                        row_text.setText("");
                        getDutouListData(zzdh);
                        break;
                    case 0x103:
                        dialog.setMessageTextColor(Color.RED);
                        dialog.setMessage((String) msg.obj);
                        dialog.show();
                        break;
                    case 0x104:
                        List<List<String>>list_dt= (List<List<String>>) msg.obj;
                        data_dt=new ArrayList<>();
                        for (int i=0;i<list_dt.size();i++){
                            Map<String,String>map=new HashMap<>();
                            map.put("lab_1",list_dt.get(i).get(0));
                            map.put("lab_2",list_dt.get(i).get(1));
                            map.put("lab_3",list_dt.get(i).get(2));
                            map.put("lab_4",list_dt.get(i).get(3));
                            map.put("lab_5",list_dt.get(i).get(4));
                            map.put("lab_6",list_dt.get(i).get(5));
                            data_dt.add(map);
                        }
                        initDutouList(data_dt);
                        break;
                    case 0x105:
                        getGongdanData();
                        getDutouListData(zzdh);
                        Button button= (Button) msg.obj;
                        button.setText("已修复");
                        /*dialog.setMessageTextColor(Color.BLACK);
                        dialog.setMessage("提交成功");
                        dialog.show();*/
                        break;
                    case 0x106:
                        Button button2= (Button) msg.obj;
                        button2.setText("已修复");
                        button2.setEnabled(true);
                        dialog.setMessageTextColor(Color.RED);
                        dialog.setMessage("提交失败，请重试");
                        dialog.show();
                        break;
                    case 0x107:
                        jtbhSub_btn.setEnabled(true);
                        jtbhSub_btn.setText("提交");
                        jtbh_text.setText(new_jtbh_text.getText().toString());
                        new_jtbh_text.setText("");
                        /*dialog.setMessageTextColor(Color.BLACK);
                        dialog.setMessage("提交成功");
                        dialog.show();*/
                        break;
                    case 0x108:
                        jtbhSub_btn.setEnabled(true);
                        jtbhSub_btn.setText("提交");
                        dialog.setMessageTextColor(Color.RED);
                        dialog.setMessage((String) msg.obj);
                        dialog.show();
                        break;
                    case 0x109:
                        List<List<String>>list_gongdan= (List<List<String>>) msg.obj;
                        for (int i=0;i<list_gongdan.size();i++){
                            if (list_gongdan.get(i).get(3).equals(zzdh)){
                                cpxs_text.setText(list_gongdan.get(i).get(15));
                            }
                        }
                        if (!mjxs_text.getText().equals(cpxs_text.getText().toString())){
                            cpxs_text.setBackgroundColor(Color.RED);
                        }else {
                            cpxs_text.setBackgroundColor(Color.WHITE);
                        }
                    default:
                        break;
                }
            }
        };


        getNetData();

    }


    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_MoeJtxsInf 'C','"+zzdh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_MoeJtxsInf 'C'",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();


        getList2Data(zzdh);

        getDutouListData(zzdh);
    }


    private void getGongdanData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //工单信息表
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_MoeDet 'A','"+jtbh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>15){
                            Message msg=handler.obtainMessage();
                            msg.what=0x109;
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

    //机台信息表
    private void getList2Data(final String zzdh){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_MoeJtxsInf 'B','"+zzdh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>3){
                            Message msg=handler.obtainMessage();
                            msg.what=0x101;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_MoeJtXs  'B'",jtbh,sharedPreferences.getString("mac",""));
                }

            }
        }).start();
    }


    //堵头信息表
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
                           msg.what=0x104;
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

    //初始化堵头信息表
    private void initDutouList(final List<Map<String,String>>data){
        easyAdapter=new EasyArrayAdapter(this,R.layout.list_item_dutou_2,data) {
            @Override
            public View getEasyView(final int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView!=null){
                    view=convertView;
                }else {
                    view= LayoutInflater.from(getContext()).inflate(R.layout.list_item_dutou_2,null);
                }
                final Map<String,String>map=data.get(position);
                TextView lab_1=(TextView) view.findViewById(R.id.lab_1);
                TextView lab_2=(TextView) view.findViewById(R.id.lab_2);
                TextView lab_3=(TextView) view.findViewById(R.id.lab_3);
                TextView lab_4=(TextView) view.findViewById(R.id.lab_4);
                TextView lab_5=(TextView) view.findViewById(R.id.lab_5);
                TextView lab_6=(TextView) view.findViewById(R.id.lab_6);
                final Button upload=(Button)view.findViewById(R.id.xiufu);
                lab_1.setText(map.get("lab_1"));
                lab_2.setText(map.get("lab_2"));
                lab_3.setText(map.get("lab_3"));
                lab_4.setText(map.get("lab_4"));
                lab_5.setText(map.get("lab_5"));
                lab_6.setText(map.get("lab_6"));
                if (map.get("lab_5").equals("")|map.get("lab_6").equals("")){
                    upload.setText("修复");
                }
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.sendCountdownReceiver(Jtjqsbg2Activity.this);
                        upload.setText("提交中");
                        upload.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Upd_MoeJtXs  'C','"+zzdh+"',''," +
                                        "'"+map.get("lab_1")+"',0,0,'','"+wkno+"'");
                                if (list!=null&&list.size()>0&&list.get(0).size()>0&&list.get(0).get(0).equals("OK")){
                                    Message msg=handler.obtainMessage();
                                    msg.what=0x105;
                                    msg.arg1=position;
                                    msg.obj=upload;
                                    handler.sendMessage(msg);
                                }else {
                                    Message msg=handler.obtainMessage();
                                    msg.what=0x106;
                                    msg.arg1=position;
                                    msg.obj=upload;
                                    handler.sendMessage(msg);
                                    AppUtils.uploadNetworkError("Exec PAD_Upd_MoeJtXs  'C'",jtbh,sharedPreferences.getString("mac",""));
                                }
                            }
                        }).start();
                    }
                });
                return view;
            }
        };


        listView_2.setAdapter(easyAdapter);
    }

    private void addDutou(final String zzdh, final String row, final String col, final String yydm, final String wkno){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Upd_MoeJtXs  'B','"+zzdh+"'," +
                        "'','','"+row+"','"+col+"','"+yydm+"','"+wkno+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>0){
                            if (list.get(0).get(0).equals("OK")){
                                handler.sendEmptyMessage(0x102);
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x103;
                                msg.obj=list.get(0).get(0);
                                handler.sendMessage(msg);
                            }
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Upd_MoeJtXs  'B'",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }


    private boolean isReady(){
        if (row_text.getText().toString().equals("")){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("请先输入堵头位置排数");
            dialog.show();
            return false;
        }
        if (Integer.parseInt(row_text.getText().toString())>99|Integer.parseInt(row_text.getText().toString())==0){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("堵头位置排数输入有误");
            dialog.show();
            return false;
        }
        if (col_text.getText().toString().equals("")){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("请先输入堵头位置列数");
            dialog.show();
            return false;
        }
        if (Integer.parseInt(col_text.getText().toString())>99|Integer.parseInt(col_text.getText().toString())==0){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("堵头位置列数输入有误");
            dialog.show();
            return false;
        }
        if (spinner.getText().toString().equals("")){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("请先选择堵头原因");
            dialog.show();
            return false;
        }
        return true;
    }

    private void uploadJtbh(final String new_jtbh, final String wkno, final String zzdh){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Upd_MoeJtXs  'A','"+zzdh+"','"+new_jtbh+"','',0,0,'','"+wkno+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>0){
                            if (list.get(0).get(0).equals("OK")){
                                handler.sendEmptyMessage(0x107);
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x108;
                                msg.obj=list.get(0).get(0);
                                handler.sendMessage(msg);
                            }
                        }
                    }
                }else {
                }
            }
        }).start();
    }

    private boolean jtbhIsReady(){
        if (new_jtbh_text.getText().toString().equals("")){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("请先选择机台编号");
            dialog.show();
            return false;
        }
        if (new_jtbh_text.getText().toString().equals(jtbh_text.getText().toString())){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("机台编号没有发生变更");
            dialog.show();
            return false;
        }
        return true;
    }



    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(Jtjqsbg2Activity.this);
        switch (v.getId()){
            case R.id.jtbh_sub:
                if (jtbhIsReady()){
                    jtbhSub_btn.setEnabled(false);
                    jtbhSub_btn.setText("提交中");
                    uploadJtbh(new_jtbh_text.getText().toString(),wkno,zzdh);
                }
                break;
            case R.id.spinner:
                if (spinner_list!=null){
                    spinner_list.showDownOn(spinner);
                }
                break;
            case R.id.row:
                row_text.setBackgroundColor(getResources().getColor(R.color.small));
                col_text.setBackgroundColor(Color.WHITE);
                sub_text=row_text;
                break;
            case R.id.col:
                row_text.setBackgroundColor(Color.WHITE);
                col_text.setBackgroundColor(getResources().getColor(R.color.small));
                sub_text=col_text;
                break;
            case R.id.cancle_btn:
                setResult(1,new Intent());
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
                    addDutou(zzdh,row_text.getText().toString(),col_text.getText().toString(),lbdm,wkno);
                }
                break;
            case R.id.btn_clear:
                sub_text.startAnimation(anim);
                sub_text.setText("0");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spinner_list!=null){
            spinner_list.dismiss();
        }
        if (dialog.isShow()){
            dialog.dismiss();
        }
    }
}
