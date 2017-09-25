package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class DialogXjActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button cancle_btn,save_btn;
    private EditText editText;
    private String wkno,v_id;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_xj);
        initData();
        initView();

    }


    private void initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        save_btn=(Button)findViewById(R.id.save_btn);
        editText=(EditText)findViewById(R.id.ed);
        cancle_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
    }

    private void initData(){
        Intent intent=getIntent();
        wkno=intent.getStringExtra("wkno");
        v_id=intent.getStringExtra("v_id");
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        save_btn.setEnabled(true);
                        break;
                    case 0x101:
                        save_btn.setEnabled(true);
                        Toast.makeText(DialogXjActivity.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void uploadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql=String.format("exec PAD_Update_PzmInf '%S', '%S', %S",wkno,editText.getText().toString(),v_id);
                    JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                    if (array!=null){
                        if (array.length()>0){
                            if (array.getJSONObject(0).getString("Column1").equals("OK")){
                                AppUtils.sendUpdateXunjianReceiver(DialogXjActivity.this);
                                finish();
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=0x101;
                                msg.obj=array.getJSONObject(0).getString("Column1");
                                handler.sendMessage(msg);
                            }
                        }else {
                            handler.sendEmptyMessage(0x100);
                        }
                    }else {
                        Message msg=handler.obtainMessage();
                        msg.what=0x101;
                        msg.obj="网络异常";
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private boolean isReady(){
        if (editText.getText().toString().equals("")){
            Toast.makeText(this,"请先输入处理结果",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.save_btn:
                if (isReady()){
                    save_btn.setEnabled(false);
                    uploadData();
                }
                break;
        }
    }
}
