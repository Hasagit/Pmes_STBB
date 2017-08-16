package com.ruiduoyi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ruiduoyi.R;

import java.util.List;
import java.util.Map;

/**
 * Created by DengJf on 2017/8/16.
 */

public class ZbRecyclerViewAdapter extends RecyclerView.Adapter<ZbRecyclerViewAdapter.MyViewHolder>{

    private Context context;
    private List<Map<String,String>>data;

    public ZbRecyclerViewAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_jtbg_jt,null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button jt_btn;
        public MyViewHolder(View view) {
            super(view);
            jt_btn=(Button)view.findViewById(R.id.jt_btn);
        }

    }
}
