package com.ruiduoyi.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruiduoyi.R;

/**
 * Created by DengJf on 2017/6/29.
 */

public class PopupDialog {
    private Activity context;
    private Button try_again;
    private Button cancle_btn;
    private TextView title;
    private TextView message;
    private PopupWindow dialog;
    private LinearLayout backgrounp;

    public PopupDialog(Activity context,int width,int height) {
        this.context = context;

        View contenView= LayoutInflater.from(context).inflate(R.layout.popup_dialog1,null);
        dialog=new PopupWindow(contenView, width, height);
        title=(TextView)contenView.findViewById(R.id.title);
        cancle_btn=(Button)contenView.findViewById(R.id.cancle_btn);
        try_again=(Button)contenView.findViewById(R.id.again_btn);
        message=(TextView)contenView.findViewById(R.id.msg_text);
        backgrounp=(LinearLayout)contenView.findViewById(R.id.backgrounp);
        dialog.setBackgroundDrawable(new ColorDrawable(0xffffff));
    }

    public void setTitle(String title_str){
        title.setText(title_str);
    }

    public void setMessage(String msg){
        message.setText(msg);
    }

    public void setCancelable(boolean cancelable){
        dialog.setOutsideTouchable(!cancelable);
        dialog.setTouchable(cancelable);
        dialog.setClippingEnabled(cancelable);
    }

    public void show(){
        dialog.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public Button getOkbtn() {
        return try_again;
    }

    public Button getCancle_btn() {
        return cancle_btn;
    }

    public void setBackgrounpColor(int color){
        backgrounp.setBackgroundColor(color);
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public boolean isShow(){
        return dialog.isShowing();
    }


    public void setMessageTextColor(int color){
        message.setTextColor(color);
    }
}
