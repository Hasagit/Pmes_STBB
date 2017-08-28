package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

public class DialogMxbgActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button cancle_btn,save_btn;
    private TextView gddh_text,pmgg_text,mjbh_text,mjmc_text,bzxs_text;
    private EditText sjxs_ed;
    private Handler handler;
    private String gddh_str,wkno,pmgg_str,mjmc_str,mjbh_str,bzxs_str,zzdh_str;
    private PopupDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_mxbg);
        initData();
        initView();
    }


    private void initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        save_btn=(Button)findViewById(R.id.save_btn);
        gddh_text=(TextView)findViewById(R.id.gddh);
        pmgg_text=(TextView)findViewById(R.id.pmgg);
        mjmc_text=(TextView)findViewById(R.id.mjmc);
        mjbh_text=(TextView)findViewById(R.id.mjbh);
        bzxs_text=(TextView)findViewById(R.id.bzxs);
        sjxs_ed=(EditText)findViewById(R.id.sjxs);
        gddh_text.setText(gddh_str);
        pmgg_text.setText(pmgg_str);
        mjmc_text.setText(mjmc_str);
        mjbh_text.setText(mjbh_str);
        bzxs_text.setText(bzxs_str);


        cancle_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        dialog=new PopupDialog(this,450,350);
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(DialogMxbgActivity.this);
                dialog.dismiss();
            }
        });
        dialog.setTitle("提示");
    }

    private void initData(){
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        gddh_str=intent_from.getStringExtra("sodh");
        pmgg_str=intent_from.getStringExtra("pmgg");
        mjmc_str=intent_from.getStringExtra("mjmc");
        mjbh_str=intent_from.getStringExtra("mjbh");
        bzxs_str=intent_from.getStringExtra("bzxs");
        zzdh_str=intent_from.getStringExtra("zzdh");
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        dialog.setMessageTextColor(Color.RED);
                        dialog.setMessage((String) msg.obj);
                        dialog.show();
                        break;
                    case 0x101:
                        setResult(1);
                        finish();
                        break;
                }
            }
        };
    }


    private void updateMx(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql=String.format("Exec PAD_Upd_MoeJtXs 'D',%s,'','',%s,null,null,%s,0",
                            zzdh_str,sjxs_ed.getText().toString(),wkno);
                    JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                    if (array!=null){
                        if (array.length()>0){
                            if (array.getJSONObject(0).getString("Column1").equals("OK")){
                                handler.sendEmptyMessage(0x101);
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x100;
                                msg.obj=array.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }
                    }else {
                        NetHelper.uploadNetworkError(sql,sharedPreferences.getString("jtbh",""),sharedPreferences.getString("mac",""));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isReady(){
        if (sjxs_ed.getText().toString().equals("")){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("请先输入实际穴数");
            dialog.show();
            return false;
        }
        if (Integer.parseInt(sjxs_ed.getText().toString())>Integer.parseInt(bzxs_str)){
            dialog.setMessageTextColor(Color.RED);
            dialog.setMessage("实际穴数不能大于标准穴数");
            dialog.show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(DialogMxbgActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                setResult(3);
                finish();
                break;
            case R.id.save_btn:
                if (isReady()){
                    updateMx();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}
