package com.ruiduoyi.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.adapter.ShanggangAdapter;
import com.ruiduoyi.model.NetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogSGActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button btn_cancle,btn_save,btn_remake;
    private ListView listView;
    private RadioButton radio_1,radio_2;
    private EditText num_ed;
    private String num="";
    private TextView tip;
    private String jtbh;
    private RadioGroup radioGroup;
    private String type="A";
    String rym_id="";
    String wkno="";
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            num=intent.getStringExtra("num");
            num_ed.setText(num);
            btn_save.setEnabled(false);
            btn_remake.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_ReadResults '"+num+"'");
                    Message msg=handler.obtainMessage();
                    if (list!=null&&list.size()>0){
                        msg.what=0x103;
                        msg.obj=list;
                    }else {
                        msg.what=0x104;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_sg);
        initData();
        initView();
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100://上岗人员初始查询
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    if(list.size()>0){
                        rym_id=list.get(0).get(0);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_LysgList '"+rym_id+"'");
                                Message msg=handler.obtainMessage();
                                if (list!=null){
                                    msg.what=0x102;
                                    msg.obj=list;
                                }else {
                                    msg.what=0x101;
                                }
                                handler.sendMessage(msg);
                            }
                        }).start();
                    }else {
                        Toast.makeText(DialogSGActivity.this,"数据异常",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0x101:
                    Toast.makeText(DialogSGActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x102:
                    List<List<String>>list_2=(List<List<String>>)msg.obj;
                    if (list_2.size()>0){
                        //初始化列表
                        initListView(list_2);
                    }else {
                        Toast.makeText(DialogSGActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0x103:
                    btn_save.setEnabled(true);
                    btn_remake.setEnabled(true);
                    Toast.makeText(DialogSGActivity.this,"验证通过",Toast.LENGTH_SHORT).show();
                    break;
                case 0x104:
                    btn_save.setEnabled(true);
                    btn_remake.setEnabled(true);
                    Toast.makeText(DialogSGActivity.this,"未找到符合记录的物理卡号,请重新刷卡！",Toast.LENGTH_SHORT).show();
                    break;
                case 0x105:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_LysgList '"+rym_id+"'");
                            Message msg=handler.obtainMessage();
                            if (list!=null){
                                msg.what=0x102;
                                msg.obj=list;
                            }else {
                                msg.what=0x101;
                            }
                            handler.sendMessage(msg);
                        }
                    }).start();
                    Intent intent=new Intent();
                    intent.setAction("UpdateInfoFragment");
                    sendBroadcast(intent);
                    Toast.makeText(DialogSGActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0x106:
                    Toast.makeText(DialogSGActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };



    private void initView(){
        btn_cancle=(Button)findViewById(R.id.cancle_btn);
        btn_remake=(Button)findViewById(R.id.remake);
        btn_save=(Button)findViewById(R.id.btn_save);
        radio_1=(RadioButton)findViewById(R.id.radio_1);
        radio_2=(RadioButton)findViewById(R.id.radio_2);
        num_ed=(EditText)findViewById(R.id.num_ed);
        listView=(ListView)findViewById(R.id.sg_list);
        radioGroup=(RadioGroup)findViewById(R.id.radioG);
        tip=(TextView)findViewById(R.id.tip);
        btn_cancle.setOnClickListener(this);
        btn_remake.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radio_1:
                        type="A";
                        break;
                    case R.id.radio_2:
                        type="B";
                        break;
                }
            }
        });
        radio_1.setChecked(true);

    }


    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        IntentFilter receiverfilter=new IntentFilter();
        receiverfilter.addAction("SerialPortNum");
        registerReceiver(receiver,receiverfilter);
        thread_1.start();
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
    }

    private void initListView(List<List<String>>list){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            List<String>item=list.get(i);
            Map map=new HashMap();
            map.put("lab_1",item.get(0));
            map.put("lab_2",item.get(1));
            map.put("lab_3",item.get(2));
            map.put("lab_4",item.get(3));
            data.add(map);
        }
        ShanggangAdapter adapter=new ShanggangAdapter(this,R.layout.list_item_shanggang,data,rym_id,jtbh);
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.remake:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean ruslt= NetHelper.getRunsqlResult("Exec PAD_AddRymInf  '"+jtbh+"' ,'"+wkno+"'");
                        Message msg=handler.obtainMessage();
                        if (ruslt){
                            msg.what=0x105;
                        }else {
                            msg.what=0x106;
                        }
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.btn_save:
                if(num_ed.getText().toString().equals("")){
                    Toast.makeText(DialogSGActivity.this,"请先刷卡验证",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean ruslt= NetHelper.getRunsqlResult("Exec PAD_Cal_RydInf  'ADD','"+jtbh+"' ,'"+type+"' ,'"+num_ed.getText().toString()+"' ,"+rym_id);
                            Message msg=handler.obtainMessage();
                            if (ruslt){
                                msg.what=0x105;
                            }else {
                                msg.what=0x106;
                            }
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    //人员上岗初始查询
    Thread thread_1=new Thread(new Runnable() {
        @Override
        public void run() {
            List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_LysgLoadSelect '"+jtbh+"'");
            Message msg=handler.obtainMessage();
            if (list!=null){
                msg.what=0x100;
                msg.obj=list;
            }else {
                msg.what=0x101;
            }
            handler.sendMessage(msg);
        }
    });



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
