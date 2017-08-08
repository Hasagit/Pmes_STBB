package com.ruiduoyi.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruiduoyi.R;

import java.util.List;

/**
 * Created by DengJf on 2017/6/20.
 */

public class PopupWindowSpinner {
    private Context context;
    private List<String>data;
    private int resouse;
    private PopupWindow popupWindow;
    private ListView listView;
    private ArrayAdapter adapter;
    private View contentView;
    private int textViewId;
    public PopupWindowSpinner(Context context,List<String>data,int resouse,int id,int width) {
        this.context=context;
        this.data=data;
        this.resouse=resouse;
        textViewId=id;
        contentView= LayoutInflater.from(context).inflate(R.layout.popupwindow_spinner,null);
        listView=(ListView)contentView.findViewById(R.id.pwspinner);
        adapter=new ArrayAdapter(context,resouse,id,data);
        listView.setAdapter(adapter);
        popupWindow=new PopupWindow(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
    }

    public ListView getListView() {
        return listView;
    }

    public void showUpOn(View view){
        int[] location = new int[2];
        int[] location2=new int[2];
        view.getLocationOnScreen(location);
        int w=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        contentView.measure(w,h);
        int mh=contentView.getMeasuredHeight();
        popupWindow.showAtLocation(contentView, Gravity.TOP|Gravity.START, location[0], location[1]-mh);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void showDownOn(View view){
        popupWindow.showAsDropDown(view);
    }

}
