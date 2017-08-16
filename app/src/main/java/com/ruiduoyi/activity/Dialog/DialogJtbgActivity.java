package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.adapter.EasyArrayAdapter;
import com.ruiduoyi.adapter.ZbRecyclerViewAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.ZubieRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogJtbgActivity extends BaseDialogActivity{
    private String zzdh,wkno;
    private Handler handler;
    private ListView listView;
    private CheckBox checkBox;
    private TextView jtbh_text;
    private Button cancle_btn;
    private PopupDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_jtbg);
        initData();
        initView();
    }



    private void initData(){
        Intent intent_from=getIntent();
        wkno=intent_from.getStringExtra("wkno");
        zzdh=intent_from.getStringExtra("zzdh");
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        initList((JSONArray) msg.obj);
                        break;
                    case 0x101:
                        dialog.setMessageTextColor(Color.RED);
                        dialog.setMessage((String) msg.obj);
                        dialog.show();
                        break;
                }
            }
        };

        getListData();
    }


    private void initView(){
        jtbh_text=(TextView)findViewById(R.id.jtbh_text);
        listView=(ListView)findViewById(R.id.list);
        checkBox=(CheckBox)findViewById(R.id.checkbox);
        jtbh_text.setText(jtbh);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(DialogJtbgActivity.this);
                setResult(3);
                finish();
            }
        });

        dialog=new PopupDialog(this,450,350);
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
        dialog.setTitle("提示");
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(DialogJtbgActivity.this);
                dialog.dismiss();
            }
        });
    }


    private void getListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql=String.format("select jtm_jtbh, jtm_bbdm from jtm_mstr where jtm_flag = 1 and jtm_jtbh <> %s order by jtm_jtbh",jtbh);
                JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                if (array!=null){
                    Message msg=handler.obtainMessage();
                    msg.what=0x100;
                    msg.obj=array;
                    handler.sendMessage(msg);
                }else {
                    AppUtils.uploadNetworkError("sql",jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }


    private void initList(JSONArray array){
        try {
            final List<String>bbdm_list=new ArrayList<String>();
            final List<List<Map<String,String>>>data= new ArrayList<>();
            for (int i=0;i<array.length();i++){
                if (AppUtils.calculate(bbdm_list.toString(),array.getJSONObject(i).getString("jtm_bbdm"))<1){
                    bbdm_list.add(array.getJSONObject(i).getString("jtm_bbdm"));
                }
            }
            for (int i=0;i<bbdm_list.size();i++){
                String jtm_bbdm=bbdm_list.get(i);
                List<Map<String,String>>item=new ArrayList<Map<String, String>>();
                for (int j=0;j<array.length();j++){
                    if (array.getJSONObject(j).getString("jtm_bbdm").equals(jtm_bbdm )){
                        Map<String,String>map=new HashMap<String, String>();
                        map.put("jtm_bbdm",array.getJSONObject(j).getString("jtm_bbdm"));
                        map.put("jtm_jtbh",array.getJSONObject(j).getString("jtm_jtbh"));
                        item.add(map);
                    }
                }
                data.add(item);
            }
            ArrayAdapter adapter=new ArrayAdapter(this,R.layout.list_item_jtbg_zb,bbdm_list){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view;
                    if (convertView!=null){
                        view=convertView;
                    }else {
                        view= LayoutInflater.from(getContext()).inflate(R.layout.list_item_jtbg_zb,null);
                    }
                    ZubieRecyclerView recyclerView=(ZubieRecyclerView)view.findViewById(R.id.list_1);
                    TextView zb_text=(TextView)view.findViewById(R.id.zb_text);
                    zb_text.setText("组别 "+bbdm_list.get(position));
                    final List<Map<String,String>>zb_data=data.get(position);

                    ZbRecyclerViewAdapter zbRecyclerViewAdapter=new ZbRecyclerViewAdapter(DialogJtbgActivity.this,zb_data){
                        @Override
                        public void onBindViewHolder(MyViewHolder holder, final int position) {
                            super.onBindViewHolder(holder, position);
                            holder.jt_btn.setText(zb_data.get(position).get("jtm_jtbh"));
                            holder.jt_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Toast.makeText(getContext(),zb_data.get(position).get("jtm_jtbh"),Toast.LENGTH_SHORT).show();
                                    AppUtils.sendCountdownReceiver(DialogJtbgActivity.this);
                                    changJt(zb_data.get(position).get("jtm_jtbh"));
                                }
                            });
                        }


                    };
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(),8);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(zbRecyclerViewAdapter);
                    return view;
                }
            };
            listView.setAdapter(adapter);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    private void changJt(final String jtbh){
        new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                   String isCheck="1";
                   if (checkBox.isChecked()){
                       isCheck="1";
                   }else {
                       isCheck="0";
                   }
                   String sql=String.format("Exec PAD_Upd_MoeJtXs 'A', '%s', '%s', '', Null, Null, '', '%s', %s",
                           zzdh,jtbh,wkno,isCheck);
                   JSONArray array=NetHelper.getQuerysqlResultJsonArray(sql);
                   if (array!=null){
                       if (array.length()>0){
                           if (array.getJSONObject(0).getString("Column1").equals("OK")){
                               setResult(1);
                               finish();
                           }else {
                               Message msg=handler.obtainMessage();
                               msg.what=0x101;
                               msg.obj=array.getJSONObject(0).getString("Column1");
                               handler.sendMessage(msg);
                           }
                       }
                   }else {
                       AppUtils.uploadNetworkError(sql,jtbh,sharedPreferences.getString("mac",""));
                   }
               }catch (JSONException e){
                   e.printStackTrace();
               }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}
