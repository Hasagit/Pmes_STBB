package com.ruiduoyi.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ruiduoyi.R;
import com.ruiduoyi.utils.AppUtils;

import java.util.ArrayList;

public class BaseDialogActivity extends Activity {
    protected static String jtbh;
    protected static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppUtils.activityList==null){
            AppUtils.activityList=new ArrayList<>();
        }
        AppUtils.addActivity(this);
        hideVirtualKey();
        setFinishOnTouchOutside(false);
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
    }
    private void hideVirtualKey(){
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.GONE;
        window.setAttributes(params);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removeActivity(this);
    }
}
