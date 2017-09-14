package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;

public class DialogJshjActivity extends BaseDialogActivity implements View.OnClickListener{
    private CardView cardView_jshj,cardView_fqhj;
    private Button cancle_btn;
    private TextView text_hjnr,text_hjsj,text_ys,text_hjr;
    private Animation anim;
    private Handler handler;
    private String wkno,yhjr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_jshj);
        initData();
        initView();
    }



    private void initData(){
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        anim= AnimationUtils.loadAnimation(this,R.anim.scale_anim);
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        try {
                            JSONArray array= (JSONArray) msg.obj;
                            text_hjnr.setText(array.getJSONObject(0).getString("cal_ycms"));
                            text_hjsj.setText(array.getJSONObject(0).getString("cal_kssj"));
                            text_ys.setText(array.getJSONObject(0).getString("cal_ys"));
                            text_hjr.setText(array.getJSONObject(0).getString("cal_hjchry_name"));
                            yhjr=array.getJSONObject(0).getString("cal_hjry");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0x101:
                        cardView_jshj.setEnabled(true);
                        cardView_fqhj.setEnabled(true);
                        Toast.makeText(DialogJshjActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        cardView_jshj.setEnabled(true);
                        cardView_fqhj.setEnabled(true);
                        Toast.makeText(DialogJshjActivity.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        getNetData();
    }


    private void initView(){
        text_hjnr=(TextView)findViewById(R.id.hjnr);
        text_hjsj=(TextView)findViewById(R.id.hjsj);
        text_ys=(TextView)findViewById(R.id.ys);
        text_hjr=(TextView)findViewById(R.id.hjr);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        cardView_fqhj=(CardView)findViewById(R.id.fqhj);
        cardView_jshj=(CardView)findViewById(R.id.jshj);

        cancle_btn.setOnClickListener(this);
        cardView_fqhj.setOnClickListener(this);
        cardView_jshj.setOnClickListener(this);

    }

    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql=String.format("Exec PAD_Get_Call_Info '%S'",jtbh);
                JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                if (array!=null){
                    if (array.length()>0){
                        Message msg=handler.obtainMessage();
                        msg.what=0x100;
                        msg.obj=array;
                        handler.sendMessage(msg);
                    }else {
                        Message msg=handler.obtainMessage();
                        msg.what=0x102;
                        msg.obj="该机台没有呼叫记录";
                        handler.sendMessage(msg);
                    }
                }else {
                    NetHelper.uploadNetworkError(sql,jtbh,sharedPreferences.getString("mac",""));
                    handler.sendEmptyMessage(0x101);
                }
            }
        }).start();
    }


    private boolean isRead(String execType){
        if (yhjr==null){
            Toast.makeText(DialogJshjActivity.this,"该机台没有呼叫记录",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (execType.equals("CANCEL")&(!yhjr.equals(wkno))){
            Toast.makeText(DialogJshjActivity.this,"放弃呼叫仅可由原呼叫人员操作",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void uploadCallExec(final String execType){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql=String.format("Exec PAD_Call_Finish '%S', '%S', '%S'",jtbh,wkno,execType);
                    JSONArray array=NetHelper.getQuerysqlResultJsonArray(sql);
                    if (array!=null){
                        if (array.length()>0){
                            if (array.getJSONObject(0).getString("Column1").equals("OK")){
                                Message msg=handler.obtainMessage();
                                if (execType.equals("CANCLE")){
                                    msg.obj="放弃呼叫成功";
                                }else {
                                    msg.obj="结束呼叫成功";
                                }
                                msg.what=0x102;
                                handler.sendMessage(msg);
                                finish();
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x102;
                                msg.obj=array.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }
                    }else {
                        handler.sendEmptyMessage(0x101);
                        NetHelper.uploadNetworkError(sql,jtbh,sharedPreferences.getString("mac",""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(DialogJshjActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.jshj:
                cardView_jshj.startAnimation(anim);
                if (isRead("FINISH")){
                    cardView_jshj.setEnabled(false);
                    cardView_fqhj.setEnabled(false);
                    uploadCallExec("FINISH");
                }
                break;
            case R.id.fqhj:
                cardView_fqhj.startAnimation(anim);
                if (isRead("CANCEL")){
                    cardView_jshj.setEnabled(false);
                    cardView_fqhj.setEnabled(false);
                    uploadCallExec("CANCEL");
                }
                break;
        }
    }
}
