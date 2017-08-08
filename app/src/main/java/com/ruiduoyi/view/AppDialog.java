package com.ruiduoyi.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Spinner;

import com.ruiduoyi.R;

/**
 * Created by DengJf on 2017/7/17.
 */

public class AppDialog {
    private Dialog mDialog;
    private Context context;
    private AlertDialog.Builder builder;
    private Spinner spinner;

    public AppDialog(Context context) {
        this.context = context;
        builder=new AlertDialog.Builder(context);
    }

    public void setTitle(String title){
        builder.setTitle(title);
    }


    public void setMessage(String msg){
        builder.setMessage(msg);
    }


    public void setCancelable(boolean isCancelable){
        builder.setCancelable(isCancelable);
    }

    public void setOkbtn(String ok, DialogInterface.OnClickListener onClickListener){
        builder.setNeutralButton(ok,onClickListener);
    }

    public void setCanclebtn(String cancle, DialogInterface.OnClickListener onClickListener){
        builder.setNegativeButton(cancle,onClickListener);
    }

    public void dismiss(){
        mDialog.dismiss();
    }



    public void show(){
        mDialog=builder.create();
        mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                mDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        mDialog.show();
    }

}
