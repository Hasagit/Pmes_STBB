package com.ruiduoyi.activity.Dialog;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

public class ReSetServerIpActivity extends BaseDialogActivity implements View.OnClickListener{
    private Button ok_btn,exit_btn;
    private EditText server_ip_ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_server_ip);
        initView();
    }

    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
    }

    private void initView(){
        ok_btn=(Button)findViewById(R.id.ok_btn);
        exit_btn=(Button)findViewById(R.id.exit_btn);
        server_ip_ed=(EditText)findViewById(R.id.service_id);
        ok_btn.setOnClickListener(this);
        exit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(ReSetServerIpActivity.this);
        switch (v.getId()){
            case R.id.ok_btn:
                NetHelper.URL=server_ip_ed.getText().toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("service_ip",server_ip_ed.getText().toString());
                editor.commit();
                setResult(1);
                finish();
                break;
            case R.id.exit_btn:
                setResult(2);
                finish();
                break;
        }
    }
}
