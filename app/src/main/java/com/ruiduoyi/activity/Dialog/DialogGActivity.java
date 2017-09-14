package com.ruiduoyi.activity.Dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.BlfxActivity;
import com.ruiduoyi.activity.GdglActivity;
import com.ruiduoyi.activity.JtjqsbgActivity;
import com.ruiduoyi.activity.PzxjActivity;
import com.ruiduoyi.activity.YcfxActivity;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DialogGActivity extends BaseDialogActivity implements View.OnClickListener{
    private Intent intent_from;
    private Button ok_btn,cancle_btn;
    private TextView title_text,tip_text;
    private EditText num_edit;
    private String num;
    private String title,type,zldm,jtbh;
    private FrameLayout yy_bg;
    private boolean isFromBlyyfx;
    //接收刷卡串口广播
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            num=intent.getStringExtra("num");
            num_edit.setText(num);
            if (sharedPreferences.getString("dialog_g_finish","OK").equals("OK")){
                setFinishNO();
                if (title.equals("品质巡机")){
                    readEles();
                }else if (title.equals("呼叫")){
                    Intent intent_hj=new Intent(DialogGActivity.this,DialogHjActivity.class);
                    intent_hj.putExtra("wkno",num);
                    startActivity(intent_hj);
                    finish();
                }else if (title.equals("结束呼叫")){
                    Intent intent_jshj=new Intent(DialogGActivity.this,DialogJshjActivity.class);
                    intent_jshj.putExtra("wkno",num);
                    startActivity(intent_jshj);
                    finish();
                }else {
                    getNetData(0x100);
                }
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
                case 0x100:
                    String readCardResult= (String) msg.obj;
                    execOPRorDOC(readCardResult);
                    break;
                case 0x101:
                    String result= (String) msg.obj;
                    String wkno=result.substring(2,result.length());
                    Intent intent=new Intent();
                    intent.putExtra("wkno",wkno);
                    setFinishOK();
                    setResult(0,intent);
                    finish();
                    break;
                case 0x102://操作失败
                    String tip_str=(String)msg.obj;
                    tip_text.setText(tip_str);
                    tip_text.setTextColor(Color.RED);
                    break;
                case 0x103:
                    Toast.makeText(DialogGActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
        tip_text=(TextView)findViewById(R.id.tip);
        intent_from=getIntent();
        /*yy_bg=(FrameLayout)findViewById(R.id.yy_bg);
        yylb_spinner=(Button)findViewById(R.id.yylb);
        yymc_spinner=(Button)findViewById(R.id.yymc);*/
        title=intent_from.getStringExtra("title");
        zldm=intent_from.getStringExtra("zldm");
        type=intent_from.getStringExtra("type");
        isFromBlyyfx=intent_from.getBooleanExtra("isFromBlyyfx",false);
        title_text.setText(title);
        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(num_edit.getWindowToken(),0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


    }

    private void initDate(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        IntentFilter receiverfilter=new IntentFilter();
        receiverfilter.addAction("SerialPortNum");
        registerReceiver(receiver,receiverfilter);
        setFinishOK();
    }

    private void getNetData(final int what){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("PAD_Read_CardID '"
                        +type+"','"+zldm+"','"+num+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>0){
                            Message msg=handler.obtainMessage();
                            msg.what=what;
                            msg.obj=list.get(0).get(0);
                            handler.sendMessage(msg);
                        }else {
                            setFinishOK();
                        }
                    }else {
                        setFinishOK();
                    }
                }else {
                    setFinishOK();
                    handler.sendEmptyMessage(0x103);
                    NetHelper.uploadNetworkError("PAD_Read_CardID",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }

    private void execOPRorDOC(String readCardResult){
        final String two=readCardResult.substring(0,2);
        if (readCardResult.substring(0,2).equals("OK")){
            if (type.equals("OPR")){//执行指令操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_SrvCon '"+jtbh+"','"+zldm+"','"+num+"',''");
                        AppUtils.sendUpdateInfoFragmentReceiver(DialogGActivity.this);
                        if (list!=null){
                            if (list.size()>0){
                                if (list.get(0).size()>0){
                                    if (list.get(0).get(0).trim().equals("OK")){
                                        if (isFromBlyyfx){//从BlyyfxActivity启动来的
                                            type="DOC";
                                            getNetData(0x101);
                                        }else {//从statusFragment启动来的
                                            setFinishOK();
                                            AppUtils.sendReturnToInfoReceiver(DialogGActivity.this);
                                            finish();
                                        }
                                    }else {
                                        Message msg=handler.obtainMessage();
                                        msg.what=0x102;
                                        msg.obj=list.get(0).get(0);
                                        handler.sendMessage(msg);
                                    }
                                }else {
                                    setFinishOK();
                                }
                            }else {
                                setFinishOK();
                            }
                        }else {
                            setFinishOK();
                            handler.sendEmptyMessage(0x103);
                        }
                    }
                }).start();
            }else {//执行文档操作
                setFinishOK();
                String wkno=readCardResult.substring(2,readCardResult.length());
                AppUtils.sendUpdateInfoFragmentReceiver(DialogGActivity.this);
                Intent intent;
                if (title.equals("工单管理")){
                    intent=new Intent(DialogGActivity.this,GdglActivity.class);
                    intent.putExtra("wkno",wkno);
                    startActivity(intent);
                }else if(title.equals("异常分析")){
                    intent=new Intent(DialogGActivity.this,YcfxActivity.class);
                    intent.putExtra("wkno",wkno);
                    startActivity(intent);
                }else if(title.equals("不良分析")){
                    intent=new Intent(DialogGActivity.this,BlfxActivity.class);
                    intent.putExtra("wkno",wkno);
                    startActivity(intent);
                }else if(title.equals("穴数变更")){
                    intent=new Intent(DialogGActivity.this,JtjqsbgActivity.class);
                    intent.putExtra("wkno",wkno);
                    startActivity(intent);
                }
                finish();

            }
        }else {
            setFinishOK();
            tip_text.setText(readCardResult);
            tip_text.setTextColor(Color.RED);
        }
    }

    private void readEles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                   JSONArray array=NetHelper.getQuerysqlResultJsonArray(" Exec PAD_Check_Usr_Prg '"
                           +num_edit.getText().toString()+"','MOM104D6'");
                   if (array!=null){
                       if (array.length()>0){
                           JSONObject json=array.getJSONObject(0);
                           String readCardResult=json.getString("Column1");
                           if (readCardResult.substring(0,2).equals("OK")){
                               Intent intent=new Intent(DialogGActivity.this,PzxjActivity.class);;
                               intent.putExtra("wkno",readCardResult.substring(2,readCardResult.length()));
                               startActivity(intent);
                               setFinishOK();
                               finish();
                           }else {
                               Message msg=handler.obtainMessage();
                               msg.what=0x102;
                               msg.obj=readCardResult;
                               handler.sendMessage(msg);
                               setFinishOK();
                           }
                       }else {
                           setFinishOK();
                       }
                   }else {
                       setFinishOK();
                       handler.sendEmptyMessage(0x103);
                       NetHelper.uploadNetworkError("Exec PAD_Check_Usr_Prg",jtbh,sharedPreferences.getString("mac",""));
                   }
               }catch (JSONException e){
                   e.printStackTrace();
               }
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_btn:
                Intent intent=new Intent();
                setResult(1,intent);
                AppUtils.sendCountdownReceiver(DialogGActivity.this);
                finish();
                break;
            case R.id.cancle_btn:
                Intent intent2=new Intent();
                setResult(1,intent2);
                AppUtils.sendCountdownReceiver(DialogGActivity.this);
                finish();
                break;
        }
    }

    private void setFinishOK(){
        Log.w("dialogG","OK");
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("dialog_g_finish","OK");
        editor.commit();
    }
    private void setFinishNO(){
        Log.w("dialogG","NO");
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("dialog_g_finish","NO");
        editor.commit();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
