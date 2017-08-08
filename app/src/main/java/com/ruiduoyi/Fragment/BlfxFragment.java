package com.ruiduoyi.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.BlYyfxActivity;
import com.ruiduoyi.activity.BlfxActivity;
import com.ruiduoyi.adapter.SigleSelectAdapter2;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.PopupWindowSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlfxFragment extends Fragment implements View.OnClickListener{
    private String zldm,zzdh,jtbh;
    private Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,btn_clear,btn_submit,btn_del,spinner;
    private TextView sub_text,bldm_text,blms_text,blzs_text;
    private Animation anim;
    private String sub_num;
    private ListView listView;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private PopupWindowSpinner spinner_list;
    private SigleSelectAdapter2 adapter2;
    private List<Map<String,String>>data1;
    private int select_position;
    private BlYyfxActivity activity;
    private PopupDialog readyDialog;


    public BlfxFragment() {
    }

    public static BlfxFragment newInstance() {
        BlfxFragment fragment = new BlfxFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anim= AnimationUtils.loadAnimation(getContext(),R.anim.sub_num_anim);
        sharedPreferences=getContext().getSharedPreferences("info",Context.MODE_PRIVATE);
        activity= (BlYyfxActivity) getActivity();
        zzdh=activity.getZzdh();
        zldm=activity.getZldm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blfx, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData(){
        jtbh=sharedPreferences.getString("jtbh","");




        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        final List<List<String>>list= (List<List<String>>) msg.obj;
                        final List<String>data=new ArrayList<>();
                        String pmgg=sharedPreferences.getString("pmgg","");
                        for (int i=0;i<list.size();i++){
                            if (list.get(i).get(2).equals(pmgg)){
                                spinner.setText(list.get(i).get(1)+"\t\t"+list.get(i).get(2));
                                zzdh=list.get(i).get(0);
                            }
                            data.add(list.get(i).get(1)+"\t\t"+list.get(i).get(2));
                        }
                        spinner_list=new PopupWindowSpinner(getContext(),data,R.layout.spinner_list_yyfx,R.id.lab_1,450);
                        spinner_list.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AppUtils.sendCountdownReceiver(getContext());
                                spinner.setText(data.get(position));
                                zzdh=list.get(position).get(0);
                                Log.w("zzdh",zzdh);
                                spinner_list.dismiss();
                            }
                        });
                        break;
                    case 0x101:
                        List<List<String>>list1= (List<List<String>>) msg.obj;
                        data1=new ArrayList<>();
                        for (int i=0;i<list1.size();i++){
                            Map<String,String>map=new HashMap<>();
                            map.put("lab_1",list1.get(i).get(0));
                            map.put("lab_2",list1.get(i).get(1));
                            map.put("lab_3","0");
                            data1.add(map);
                        }
                        adapter2= new SigleSelectAdapter2(getContext(), R.layout.blfx_sigle_select_item, data1) {
                            @Override
                            public void onRadioSelectListener(int position, Map<String, String> map) {
                                select_position=position;
                                bldm_text.setText(map.get("lab_1"));
                                blms_text.setText(map.get("lab_2"));
                            }
                        };
                        listView.setAdapter(adapter2);
                        break;
                }
            }
        };
        getNetData();
    }


    private void initView(View view){
        btn_0=(Button)view.findViewById(R.id.btn_0);
        btn_1=(Button)view.findViewById(R.id.btn_1);
        btn_2=(Button)view.findViewById(R.id.btn_2);
        btn_3=(Button)view.findViewById(R.id.btn_3);
        btn_4=(Button)view.findViewById(R.id.btn_4);
        btn_5=(Button)view.findViewById(R.id.btn_5);
        btn_6=(Button)view.findViewById(R.id.btn_6);
        btn_7=(Button)view.findViewById(R.id.btn_7);
        btn_8=(Button)view.findViewById(R.id.btn_8);
        btn_9=(Button)view.findViewById(R.id.btn_9);
        btn_del=(Button)view.findViewById(R.id.btn_del);
        btn_submit=(Button)view.findViewById(R.id.btn_submit);
        btn_clear=(Button)view.findViewById(R.id.btn_clear);
        sub_text=(TextView)view.findViewById(R.id.sub_text);
        bldm_text=(TextView)view.findViewById(R.id.bldm_text);
        blms_text=(TextView)view.findViewById(R.id.blms_text);
        blzs_text=(TextView)view.findViewById(R.id.blzs);
        listView=(ListView)view.findViewById(R.id.list_blfx);
        spinner=(Button)view.findViewById(R.id.spinner);

        spinner.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        readyDialog=new PopupDialog(getActivity(),400,300);
        readyDialog=new PopupDialog(getActivity(),400,300);
        readyDialog.setTitle("提示");
        readyDialog.getOkbtn().setText("确定");
        readyDialog.getCancle_btn().setVisibility(View.GONE);
        readyDialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyDialog.dismiss();
            }
        });


    }

    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'D','"+jtbh+"','"+zzdh+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>2){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_ZlmYywh 'D'",sharedPreferences.getString("jtnh",""),
                            sharedPreferences.getString("mac",""));
                }


                List<List<String>>list1=NetHelper.getQuerysqlResult("Exec PAD_Get_Blllist");
                if (list1!=null){
                    if (list1.size()>0){
                        if (list1.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.what=0x101;
                            msg.obj=list1;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_Blllist",sharedPreferences.getString("jtbh",""),
                            sharedPreferences.getString("mac",""));
                }


            }
        }).start();
    }

    public void upLoadData(final String wkno){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<data1.size();i++){
                    Map<String,String>map=data1.get(i);
                    if (!map.get("lab_3").equals("0")){
                        upLoadOneData(map,wkno);
                    }
                }
            }
        }).start();
    }

    private void upLoadOneData(Map<String,String>map,String wkno){
        List<List<String>>list=NetHelper.getQuerysqlResult("Exec PAD_Add_BlmInfo " +
                "'A','"+zzdh+"','','','"+jtbh+"','"+zldm+"','"+map.get("lab_1")+"'," +
                "'"+map.get("lab_3")+"','"+wkno+"'");
        if (list!=null){
            if (list.size()>0){
                if (list.get(0).size()>0){
                    if (list.get(0).get(0).equals("OK")){
                        return ;
                    }
                }
            }
        }else {
            upLoadOneData(map,wkno);
        }
    }

    public boolean isReady(){
        if (Integer.parseInt(blzs_text.getText().toString())>0&&spinner.getText().toString().equals("")){
            readyDialog.setMessage("请先选取产品");
            readyDialog.show();
            //Toast.makeText(getContext(),"请先选取产品",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(getContext());
        switch (v.getId()) {
            case R.id.spinner:
                if (spinner_list!=null){
                    spinner_list.showDownOn(spinner);
                }
                break;
            case R.id.btn_0:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if ((!sub_num.equals("0"))&(!sub_num.equals("-"))) {
                    sub_text.setText(sub_num + "0");
                }
                break;
            case R.id.btn_1:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "1");
                } else {
                    sub_text.setText("1");
                }
                break;
            case R.id.btn_2:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "2");
                } else {
                    sub_text.setText("2");
                }
                break;
            case R.id.btn_3:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "3");
                } else {
                    sub_text.setText("3");
                }
                break;
            case R.id.btn_4:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "4");
                } else {
                    sub_text.setText("4");
                }
                break;
            case R.id.btn_5:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "5");
                } else {
                    sub_text.setText("5");
                }
                break;
            case R.id.btn_6:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "6");
                } else {
                    sub_text.setText("6");
                }
                break;
            case R.id.btn_7:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "7");
                } else {
                    sub_text.setText("7");
                }
                break;
            case R.id.btn_8:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "8");
                } else {
                    sub_text.setText("8");
                }
                break;
            case R.id.btn_9:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (!sub_num.equals("0")) {
                    sub_text.setText(sub_num + "9");
                } else {
                    sub_text.setText("9");
                }
                break;
            case R.id.btn_submit:
                if (bldm_text.getText().toString().equals("") | blms_text.getText().toString().equals("")) {
                    readyDialog.setMessage("请先选择不良信息");
                    readyDialog.show();
                    //Toast.makeText(getContext(), "请先选择不良信息", Toast.LENGTH_SHORT).show();
                } else if (spinner.getText().toString().equals("")){
                    readyDialog.setMessage("请先选取产品");
                    readyDialog.show();
                    //Toast.makeText(getContext(),"请先选取产品",Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(activity.getLpsl_str())<Integer.parseInt(blzs_text.getText().toString())+Integer.parseInt(activity.getBlpsl_str())+Integer.parseInt(sub_text.getText().toString())){
                    //int sum=Integer.parseInt(blzs_text.getText().toString())+Integer.parseInt(activity.getBlpsl_str())+Integer.parseInt(sub_text.getText().toString());
                    readyDialog.setMessage("不良品数量不能超过良品数量");
                    readyDialog.show();
                    //Toast.makeText(getContext(),"不良品数量不能超过良品数量",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        data1.get(select_position).put("lab_3",sub_text.getText().toString());
                        adapter2.notifyDataSetChanged();
                        long zongshu=0;
                        for (int i=0;i<data1.size();i++){
                            zongshu=zongshu+Integer.parseInt(data1.get(i).get("lab_3"));
                        }
                        sub_text.setText("0");
                        blzs_text.setText(zongshu+"");
                    }catch (NumberFormatException e){
                        readyDialog.setMessage("数值大小已经超过允许范围");
                        readyDialog.show();
                        //Toast.makeText(getContext(),"数值大小已经超过允许范围",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_del:
                sub_text.startAnimation(anim);
                sub_num = sub_text.getText().toString();
                if (sub_num.equals("0")) {
                    sub_text.setText("-");
                }
                break;
            case R.id.btn_clear:
                sub_text.startAnimation(anim);
                sub_text.setText("0");
                break;
            default:
                break;
        }
    }
}
