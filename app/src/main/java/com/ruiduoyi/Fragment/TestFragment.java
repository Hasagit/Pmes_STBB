package com.ruiduoyi.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.ruiduoyi.R;
import com.ruiduoyi.activity.DialogB5Activty;
import com.ruiduoyi.adapter.EasyArrayAdapter;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestFragment extends Fragment implements View.OnClickListener{
    private DrawerLayout drawer;
    private Button open_btn,close_btn;
    private Animation anim;
    private ListView pdf_list;
    private PDFView pdfView;
    private SharedPreferences sharedPreferences;
    private String jtbh;
    private Handler handler;
    private String pdf_dir_str;
    private LinearLayout waitingView;
    private PopupDialog dialog;
    private BroadcastReceiver receiver;




    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anim= AnimationUtils.loadAnimation(getContext(),R.anim.scale_anim);
        pdf_dir_str= Environment.getExternalStorageDirectory().getPath()+"/RdyPmes/Pdf";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initView(View view){
        drawer=(DrawerLayout)view.findViewById(R.id.zy_drawer);
        open_btn=(Button)view.findViewById(R.id.open_btn);
        close_btn=(Button)view.findViewById(R.id.close_btn);
        pdf_list=(ListView)view.findViewById(R.id.pdf_list);
        pdfView=(PDFView)view.findViewById(R.id.pdfview);
        waitingView=(LinearLayout)view.findViewById(R.id.waitingView);
        close_btn.setOnClickListener(this);
        open_btn.setOnClickListener(this);
        pdfView.fromAsset("default.pdf").defaultPage(0).load();
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        dialog=new PopupDialog(getActivity(),450,350);
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.sendCountdownReceiver(getContext());
                dialog.dismiss();
            }
        });
        dialog.getOkbtn().setText("确定");
        pdfView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                AppUtils.sendCountdownReceiver(getContext());
                return false;
            }
        });
    }

    private void initData(){
        sharedPreferences=getContext().getSharedPreferences("info",Context.MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getPdfListData();
                pdfView.fromAsset("default.pdf").defaultPage(0).load();
                drawer.openDrawer(Gravity.RIGHT);
            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.Ruiduoyi.UpdataPdfList");
        getContext().registerReceiver(receiver,intentFilter);



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        initPdfList((List<Map<String, String>>) msg.obj);
                        break;
                    case 0x101:
                        waitingView.setVisibility(View.GONE);
                        pdfView.fromFile(new File((String) msg.obj)).defaultPage(0).load();
                        break;
                    case 0x102:
                        waitingView.setVisibility(View.GONE);
                        dialog.setMessageTextColor(Color.RED);
                        dialog.setMessage("文件下载失败，请重试");
                        dialog.show();
                        break;
                    case 0x103:
                        Toast.makeText(getContext(),"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        getPdfListData();
    }


    private void getPdfListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql=String.format("Exec PAD_Get_DocList '%S'",jtbh);
                    JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                    if (array!=null){
                        List<Map<String,String>>data=new ArrayList<Map<String, String>>();
                        if (array.length()>0){
                            for (int i=0;i<array.length();i++){
                                JSONObject json=array.getJSONObject(i);
                                Map<String,String>map=new HashMap<String, String>();
                                map.put("lab_1",json.getString("doc_name"));
                                map.put("file_url",json.getString("doc_path"));
                                data.add(map);
                            }
                        }
                        Message msg=handler.obtainMessage();
                        msg.what=0x100;
                        msg.obj=data;
                        handler.sendMessage(msg);
                    }else {
                        handler.sendEmptyMessage(0x103);
                        NetHelper.uploadNetworkError("Exec PAD_Get_DocList",jtbh,sharedPreferences.getString("mac",""));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void initPdfList(final List<Map<String,String>>data){
        EasyArrayAdapter adapter=new EasyArrayAdapter(getContext(),R.layout.list_item_pdf ,data){
            @Override
            public View getEasyView(final int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView!=null){
                    view=convertView;
                }else {
                    view=LayoutInflater.from(getContext()).inflate(R.layout.list_item_pdf,null);
                }
                TextView lab_1=(TextView)view.findViewById(R.id.lab_1);
                final CardView cardView=(CardView)view.findViewById(R.id.card);
                lab_1.setText(data.get(position).get("lab_1"));
                if (data.get(position).get("file_url").equals("")){
                    cardView.setEnabled(false);
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.lable));
                    lab_1.setTextColor(getResources().getColor(R.color.bottom_bt_sl));
                }
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.sendCountdownReceiver(getContext());
                        cardView.startAnimation(anim);
                        waitingView.setVisibility(View.VISIBLE);
                        pdfView.fromAsset("empty.pdf").defaultPage(0).load();
                        drawer.closeDrawer(Gravity.RIGHT);
                        showPDF(data,position);
                    }
                });
                return view;
            }
        };
        pdf_list.setAdapter(adapter);
    }


    private void showPDF(final List<Map<String,String>>data, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                   NetHelper.downLoadFileByUrlCompareWithVer(data.get(position).get("file_url"),
                           pdf_dir_str, data.get(position).get("lab_1")+".pdf");
                   Message msg=handler.obtainMessage();
                   msg.what=0x101;
                   msg.obj=pdf_dir_str+"/"+data.get(position).get("lab_1")+".pdf";
                   handler.sendMessage(msg);
                   /*File pdf_dir=new File(pdf_dir_str);
                   if (pdf_dir.exists()){
                       pdf_dir.mkdir();
                   }
                   File pdf_file=new File(pdf_dir_str+"/"+data.get(position).get("lab_1")+".pdf");
                   if (pdf_file.exists()){
                       Message msg=handler.obtainMessage();
                       msg.what=0x101;
                       msg.obj=pdf_dir_str+"/"+data.get(position).get("lab_1")+".pdf";
                       handler.sendMessage(msg);
                   }else {
                       NetHelper.downLoadFileByUrl(data.get(position).get("file_url"),
                               pdf_dir_str, data.get(position).get("lab_1")+".pdf");
                       Message msg=handler.obtainMessage();
                       msg.what=0x101;
                       msg.obj=pdf_dir_str+"/"+data.get(position).get("lab_1")+".pdf";
                       handler.sendMessage(msg);
                   }*/
               } catch (IOException e) {
                   handler.sendEmptyMessage(0x102);
                   e.printStackTrace();
               }
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(getContext());
        switch (v.getId()){
            case R.id.open_btn:
                open_btn.startAnimation(anim);
                drawer.openDrawer(Gravity.RIGHT);
                break;
            case R.id.close_btn:
                close_btn.startAnimation(anim);
                drawer.closeDrawer(Gravity.RIGHT);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
        dialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
