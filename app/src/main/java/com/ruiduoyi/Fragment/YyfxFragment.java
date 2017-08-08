package com.ruiduoyi.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.BlYyfxActivity;
import com.ruiduoyi.adapter.YyfxAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.PopupWindowSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YyfxFragment extends Fragment implements View.OnClickListener{
    private String zldm;
    private Button spinner;
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private PopupWindowSpinner spinner_list;
    private YyfxAdapter adapter;
    private String lbdm,jtbh;
    private PopupDialog isReadyDialog;


    public YyfxFragment() {

    }

    public static YyfxFragment newInstance(String zldm) {
        YyfxFragment fragment = new YyfxFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getContext().getSharedPreferences("info",Context.MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        BlYyfxActivity activity= (BlYyfxActivity) getActivity();
        zldm=activity.getZldm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yyfx, container, false);
        initView(view);
        getNetData();
        return view;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x100:
                    final List<List<String>>list= (List<List<String>>) msg.obj;
                    final List<String>data=new ArrayList<>();
                    for (int i=0;i<list.size();i++){
                        data.add(list.get(i).get(0)+"\t\t"+list.get(i).get(1));
                    }
                   if (data.size()>0){
                       getListData(list,0,data);
                   }
                    spinner_list=new PopupWindowSpinner(getContext(),data,R.layout.spinner_list_yyfx,
                            R.id.lab_1,425);
                    spinner_list.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AppUtils.sendCountdownReceiver(getContext());
                            getListData(list,position,data);
                            spinner_list.dismiss();
                        }
                    });
                    break;
                case 0x101:
                    List<List<String>>list1= (List<List<String>>) msg.obj;
                    List<Map<String,String>>data1=new ArrayList<>();
                    for (int i=0;i<list1.size();i++){
                        Map<String,String>map=new HashMap<>();
                        map.put("lab_1",list1.get(i).get(0));
                        map.put("lab_2",list1.get(i).get(1));
                        data1.add(map);
                    }
                    adapter=new YyfxAdapter(getContext(),R.layout.list_item_yyfx,data1);
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };

    private void initView(View view){
        spinner=(Button)view.findViewById(R.id.spinner);
        listView=(ListView)view.findViewById(R.id.list_bl);
        spinner.setOnClickListener(this);
        isReadyDialog=new PopupDialog(getActivity(),400,300);
        isReadyDialog.setTitle("提示");
        isReadyDialog.getOkbtn().setText("确定");
        isReadyDialog.getCancle_btn().setVisibility(View.GONE);
        isReadyDialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReadyDialog.dismiss();
            }
        });
    }

    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'B','"+jtbh+"','"+zldm+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_ZlmYywh 'B'",
                            sharedPreferences.getString("jtbh",""),
                            sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }


    public void upLoadData(final String wkno){
        if (lbdm!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Map<String,String>>selectData=adapter.getSelectData();
                    for (int i=0;i<selectData.size();i++){
                        upLoadOneData(selectData.get(i),wkno);
                    }
                }
            }).start();
        }
    }

    private void upLoadOneData(Map<String,String>selectData,String wkno){
        List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Upd_YclInfo " +
                "'"+jtbh+"','"+zldm+"','"+lbdm+"'," + "'"+selectData.get("lab_1")+"','0','"+wkno+"'");
        if (list!=null){
            if (list.size()>0){
                if (list.get(0).size()>0){
                    if (list.get(0).get(0).equals("OK")){
                        return;
                    }
                }
            }
        }else {

        }
    }


    private void getListData(final List<List<String>>list, final int position, List<String>data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list1=NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'C','"+jtbh+"','"+
                        list.get(position).get(0)+"'");
                if (list1!=null){
                    if (list1.size()>0){
                        if (list1.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x101;
                            msg.obj=list1;
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg=handler.obtainMessage();
                        msg.what=0x101;
                        msg.obj=list1;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
        lbdm=list.get(position).get(0);
        spinner.setText(data.get(position));
    }

    public boolean isReady(){
        if (spinner.getText().toString().equals("")){
            isReadyDialog.setMessage("请先选取原因类别");
            isReadyDialog.show();
            //Toast.makeText(getContext(),"请先选取原因类别",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (adapter==null){
            isReadyDialog.setMessage("原因描述为空");
            isReadyDialog.show();
            //Toast.makeText(getContext(),"原因描述为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(adapter.getSelectData().size()>0)){
            isReadyDialog.setMessage("请先选取原因描述");
            isReadyDialog.show();
            //Toast.makeText(getContext(),"请先选取原因描述",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(getContext());
        switch (v.getId()){
            case R.id.spinner:
                if (spinner_list!=null){
                    spinner_list.showDownOn(spinner);
                }
                break;
        }
    }
}
