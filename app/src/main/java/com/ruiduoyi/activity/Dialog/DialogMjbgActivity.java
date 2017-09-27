package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.view.PopupWindowSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogMjbgActivity extends BaseDialogActivity implements View.OnClickListener{
    private String mjbh,wldm,moeid;
    private Handler handler;
    private Button save_btn,cancle_btn,mjbh_sp;
    private PopupWindowSpinner spinnerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_mjbg);
        initData();
        initView();
    }


    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        Intent intent_from=getIntent();
        mjbh=intent_from.getStringExtra("mjbh");
        wldm=intent_from.getStringExtra("wldm");
        moeid=intent_from.getStringExtra("zzdh");


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        initSpList((JSONArray) msg.obj);
                        break;
                    case 0x101:
                        Toast.makeText(DialogMjbgActivity.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        getSpData();

    }

    private void initView(){
        save_btn=(Button)findViewById(R.id.save_btn);
        cancle_btn=(Button) findViewById(R.id.cancle_btn);
        mjbh_sp=(Button)findViewById(R.id.mjbh_sp);

        save_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
        mjbh_sp.setOnClickListener(this);

    }

    private void getSpData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql=String.format("Select itd_wldm, itd_mjbh, itd_xs, itd_cxsj From itd_det" +
                                " Where itd_wldm = '%S'", wldm);
                JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                if (array!=null){
                    Message msg=handler.obtainMessage();
                    msg.obj=array;
                    msg.what=0x100;
                    handler.sendMessage(msg);
                }else {
                    NetHelper.uploadNetworkError(sql,jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }


    private void initSpList(JSONArray array){
        final List<String>data=new ArrayList<>();
        try {
            for (int i=0;i<array.length();i++){
                data.add(array.getJSONObject(i).getString("itd_mjbh"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinnerList=new PopupWindowSpinner(this,data,R.layout.spinner_list_b7,R.id.lab_1,350);
        spinnerList.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mjbh_sp.setText(data.get(position));
                spinnerList.dismiss();
            }
        });
    }

    private boolean isReady(){
        if (mjbh_sp.getText().toString().equals("")){
            Toast.makeText(this,"请先选择模具编号",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void upLoadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql=String.format("Exec PAD_Update_Moe_Mjbh '%S', '%S'",
                            moeid,mjbh_sp.getText().toString());
                    JSONArray array=NetHelper.getQuerysqlResultJsonArray(sql);
                    if (array!=null){
                        if (array.length()>0){
                            if (array.getJSONObject(0).getString("Column1").equals("OK")){
                                setResult(0);
                                finish();
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x101;
                                msg.obj=array.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }
                    }else {
                        Message msg=handler.obtainMessage();
                        msg.what=0x101;
                        msg.obj="网络异常";
                        handler.sendMessage(msg);
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
        switch (v.getId()){
            case R.id.save_btn:
                if (isReady()){
                    upLoadData();
                }
                break;
            case R.id.cancle_btn:
                setResult(1);
                finish();
                break;
            case R.id.mjbh_sp:
                if (spinnerList!=null){
                    spinnerList.showDownOn(mjbh_sp);
                }
                break;
        }
    }
}
