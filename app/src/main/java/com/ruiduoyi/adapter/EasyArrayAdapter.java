package com.ruiduoyi.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/7/19.
 */

public abstract class EasyArrayAdapter extends ArrayAdapter{


    public EasyArrayAdapter( Context context,  int resource,  List<Map<String,String>> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getEasyView(position, convertView, parent);
    }

    public abstract View getEasyView(int position,View convertView,ViewGroup parent);
}
