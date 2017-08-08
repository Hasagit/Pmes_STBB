package com.ruiduoyi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;

import java.util.List;

public class DialogG2Activity extends BaseDialogActivity implements View.OnClickListener{
    private Intent intent_from;
    private Button ok_btn,cancle_btn;
    private TextView title_text;
    private EditText num_edit;
    private String num;
    private String title;
    private String zl;


    //接收刷卡串口广播
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            num=intent.getStringExtra("num");
            num_edit.setText(num);
            Intent intent_to;
            if(num_edit.getText().toString().equals("")){
                Toast.makeText(DialogG2Activity.this,"请刷卡",Toast.LENGTH_SHORT).show();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<List<String>>list= NetHelper.getQuerysqlResult("Declare @wkno varchar(20)\n" +
                                "Exec PAD_DocReadCardID '"+zl+"','"+num_edit.getText().toString()+"',@wkno output  \n" +
                                "Select @wkno");
                        Message msg=handler.obtainMessage();
                        if (list==null){
                            msg.what=0x101;
                        }else {
                            if(list.get(0).get(0).equals("")){
                                msg.what=0x102;
                            }else {
                                msg.what=0x100;
                                msg.obj=list.get(0).get(0);
                            }
                        }
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_g);
        initView();
        initDate();
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100://异常分析刷卡验证通过
                    String wkno=(String)msg.obj;
                    if (zl.equals("D07")){
                        Intent intent_ycfx=new Intent(DialogG2Activity.this,YcfxActivity.class);
                        intent_ycfx.putExtra("wkno",wkno);
                        intent_ycfx.putExtra("cardId",num_edit.getText().toString());
                        startActivity(intent_ycfx);
                    }else if(zl.equals("D08")){
                        Intent intent_blfx=new Intent(DialogG2Activity.this,BlfxActivity.class);
                        intent_blfx.putExtra("wkno",wkno);
                        intent_blfx.putExtra("cardId",num_edit.getText().toString());
                        startActivity(intent_blfx);
                    }else if(zl.equals("D03")){
                        Intent intent_gdgl=new Intent(DialogG2Activity.this,GdglActivity.class);
                        intent_gdgl.putExtra("wkno",wkno);
                        intent_gdgl.putExtra("cardId",num_edit.getText().toString());
                        startActivity(intent_gdgl);
                    }else if (zl.equals(getString(R.string.rysg))){
                    Intent intent=new Intent(DialogG2Activity.this,DialogSGActivity.class);
                    intent.putExtra("cardId",num_edit.getText().toString());
                    intent.putExtra("wkno",wkno);
                    startActivity(intent);
                    finish();
                    return;
                }
                    finish();
                    break;
                case 0x101://服务器异常
                    Toast.makeText(DialogG2Activity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x102://没有权限


                    //测试
                   /* Intent intent=new Intent(DialogG2Activity.this,YcfxActivity.class);
                    intent.putExtra("cardId",num_edit.getText().toString());
                    intent.putExtra("wkno","150001");
                    startActivity(intent);
                    finish();*/
                    //测试



                    Toast.makeText(DialogG2Activity.this,"您没有该指令操作权限",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };



    private void initView(){
        ok_btn=(Button)findViewById(R.id.ok_btn);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        num_edit=(EditText) findViewById(R.id.num_text);
        title_text=(TextView)findViewById(R.id.title_text);
        intent_from=getIntent();
        title=intent_from.getStringExtra("title");
        zl=intent_from.getStringExtra("zl");
        title_text.setText(title);
        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(num_edit.getWindowToken(),0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }


    private void initDate(){
        IntentFilter receiverfilter=new IntentFilter();
        receiverfilter.addAction("SerialPortNum");
        registerReceiver(receiver,receiverfilter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
