package com.ruiduoyi.Fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.ruiduoyi.R;
import com.ruiduoyi.activity.ScrzActivity;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.utils.MyAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class InfoFragment extends Fragment  implements View.OnClickListener{

    private BarChart mBarChart;
    private String jtbh;
    private TextView dq_1,dq_2,dq_3,dq_4,dq_5,dq_6,dq_7,dq_8,
                xy_1,xy_2,xy_3,xy_4,xy_5,xy_6,xy_7,xy_8,
                mo_1,mo_2,mo_3,mo_4,mo_5,mo_6,tong_1,tong_2,tong_3,tong_4,tong_5,tong_6,jtbh_text,status,msg_text,
                caozuo_text,jisu_text,cao_name_text,cao_phone,ji_name_text,ji_phone,labRym;
    private SharedPreferences sharedPreferences;
    private ImageView img_1,img_2,img_3,img_4,img_5,img_6,img_7,img_pho1,img_pho2;
    private CardView cardView;
    private ScrollView tip_layout;
    private String filePhath;
    private Timer updateTimer;
    private String mac;



    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getNetDate(0);
        }
    };

    public InfoFragment() {
    }


    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getContext().getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        mac=sharedPreferences.getString("mac","");
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("isBaseInfoFinish","OK");
        editor.commit();
        IntentFilter receiverfilter=new IntentFilter();
        receiverfilter.addAction("UpdateInfoFragment");
        getContext().registerReceiver(receiver,receiverfilter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_info, container, false);
        initView(view);
        return view;
    }


    public void initView(View view){
        mBarChart=(BarChart)view.findViewById(R.id.barChart);
        dq_1=(TextView)view.findViewById(R.id.dq_1);
        dq_2=(TextView)view.findViewById(R.id.dq_2);
        dq_3=(TextView)view.findViewById(R.id.dq_3);
        dq_4=(TextView)view.findViewById(R.id.dq_4);
        dq_5=(TextView)view.findViewById(R.id.dq_5);
        dq_6=(TextView)view.findViewById(R.id.dq_6);
        dq_7=(TextView)view.findViewById(R.id.dq_7);
        dq_8=(TextView)view.findViewById(R.id.dq_8);
        xy_1=(TextView)view.findViewById(R.id.xygd_1);
        xy_2=(TextView)view.findViewById(R.id.xygd_2);
        xy_3=(TextView)view.findViewById(R.id.xygd_3);
        xy_4=(TextView)view.findViewById(R.id.xygd_4);
        xy_5=(TextView)view.findViewById(R.id.xygd_5);
        xy_6=(TextView)view.findViewById(R.id.xygd_6);
        xy_7=(TextView)view.findViewById(R.id.xygd_7);
        xy_8=(TextView)view.findViewById(R.id.xygd_8);

        mo_1=(TextView)view.findViewById(R.id.mo_1);
        mo_2=(TextView)view.findViewById(R.id.mo_2);
        mo_3=(TextView)view.findViewById(R.id.mo_3);
        mo_4=(TextView)view.findViewById(R.id.mo_4);
        mo_5=(TextView)view.findViewById(R.id.mo_5);
        mo_6=(TextView)view.findViewById(R.id.mo_6);

        tong_1=(TextView)view.findViewById(R.id.tong_1);
        tong_2=(TextView)view.findViewById(R.id.tong_2);
        tong_3=(TextView)view.findViewById(R.id.tong_3);
        tong_4=(TextView)view.findViewById(R.id.tong_4);
        tong_5=(TextView)view.findViewById(R.id.tong_5);
        tong_6=(TextView)view.findViewById(R.id.tong_6);
        jtbh_text=(TextView)view.findViewById(R.id.jtbh_text);
        status=(TextView)view.findViewById(R.id.status_text);
        msg_text=(TextView)view.findViewById(R.id.msg_text);
        ji_name_text=(TextView)view.findViewById(R.id.ji_name);
        jisu_text=(TextView)view.findViewById(R.id.jisu);
        ji_phone=(TextView)view.findViewById(R.id.ji_phone);
        cao_name_text=(TextView)view.findViewById(R.id.cao_name);
        caozuo_text=(TextView)view.findViewById(R.id.caozuo);
        cao_phone=(TextView)view.findViewById(R.id.cao_phone);
        labRym=(TextView)view.findViewById(R.id.labRym);
        img_1=(ImageView)view.findViewById(R.id.img_1);
        img_2=(ImageView)view.findViewById(R.id.img_2);
        img_3=(ImageView)view.findViewById(R.id.img_3);
        img_4=(ImageView)view.findViewById(R.id.img_4);
        img_5=(ImageView)view.findViewById(R.id.img_5);
        img_6=(ImageView)view.findViewById(R.id.img_6);
        img_7=(ImageView)view.findViewById(R.id.img_7);
        img_pho1=(ImageView)view.findViewById(R.id.photo_1);
        img_pho2=(ImageView)view.findViewById(R.id.photo_2);
        cardView=(CardView)view.findViewById(R.id.cardView);
        cardView.setOnClickListener(this);
        tip_layout=(ScrollView)view.findViewById(R.id.tip_bg);
        filePhath= Environment.getExternalStorageDirectory().getPath()+"/RdyPmes";
        getNetDate(0);
        updateDataOntime();
        initBarChart(mBarChart);
    }




    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    try {
                        JSONArray list= (JSONArray) msg.obj;
                        if(list.length()>0){
                            dq_1.setText(list.getJSONObject(0).getString("kbm_sxrq"));
                            dq_2.setText(list.getJSONObject(0).getString("kbm_zzdh"));

                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("sjsx",list.getJSONObject(0).getString("kbm_sxrq"));
                            editor.putString("zzdh",list.getJSONObject(0).getString("kbm_zzdh"));//制造单号
                            editor.putString("gddh",list.getJSONObject(0).getString("kbm_sodh"));
                            editor.putString("scph",list.getJSONObject(0).getString("kbm_ph"));
                            editor.putString("mjbh",list.getJSONObject(0).getString("kbm_mjbh"));//模具编号
                            editor.putString("cpbh",list.getJSONObject(0).getString("kbm_wldm"));
                            editor.putString("pmgg",list.getJSONObject(0).getString("kbm_pmgg"));
                            editor.putString("ysdm",list.getJSONObject(0).getString("kbm_ysdm"));
                            editor.putString("mjmc",list.getJSONObject(0).getString("kbm_mjmc"));
                            editor.putString("jhsl",list.getJSONObject(0).getString("kbm_scsl"));
                            editor.putString("lpsl",list.getJSONObject(0).getString("kbm_lpsl"));
                            editor.putString("blsl",list.getJSONObject(0).getString("kbm_blsl"));
                            editor.putString("jzzl",list.getJSONObject(0).getString("kbm_jzzl"));
                            editor.commit();
                            dq_3.setText(list.getJSONObject(0).getString("kbm_sodh"));
                            dq_4.setText(list.getJSONObject(0).getString("kbm_ph"));
                            dq_5.setText(list.getJSONObject(0).getString("kbm_mjbh"));
                            dq_6.setText(list.getJSONObject(0).getString("kbm_wldm"));
                            dq_7.setText(list.getJSONObject(0).getString("kbm_pmgg"));
                            dq_8.setText(list.getJSONObject(0).getString("kbm_ysdm"));
                            xy_1.setText(list.getJSONObject(0).getString("kbm_xxrq"));
                            xy_2.setText(list.getJSONObject(0).getString("kbm_nextzzdh"));
                            xy_3.setText(list.getJSONObject(0).getString("kbm_nextsodh"));
                            xy_4.setText(list.getJSONObject(0).getString("kbm_nextph"));
                            xy_5.setText(list.getJSONObject(0).getString("kbm_nextmjbh"));
                            xy_6.setText(list.getJSONObject(0).getString("kbm_nextwldm"));
                            xy_7.setText(list.getJSONObject(0).getString("kbm_nextpmgg"));
                            xy_8.setText(list.getJSONObject(0).getString("kbm_nextysdm"));
                            mo_1.setText(list.getJSONObject(0).getString("kbm_mjbh"));
                            mo_2.setText(list.getJSONObject(0).getString("kbm_mjmc"));
                            mo_3.setText(list.getJSONObject(0).getString("kbm_xs"));
                            mo_4.setText(list.getJSONObject(0).getString("kbm_cxsj"));
                            mo_5.setText(list.getJSONObject(0).getString("kbm_sjcxsj"));
                            mo_6.setText(list.getJSONObject(0).getString("kbm_mjsb"));
                            tong_1.setText(list.getJSONObject(0).getString("kbm_scsl"));
                            tong_2.setText(list.getJSONObject(0).getString("kbm_chsl"));
                            tong_3.setText(list.getJSONObject(0).getString("kbm_lpsl"));
                            tong_4.setText(list.getJSONObject(0).getString("kbm_blsl"));
                            tong_5.setText(list.getJSONObject(0).getString("kbm_blv"));
                            tong_6.setText(list.getJSONObject(0).getString("kbm_jdcy"));

                            if (!(list.getJSONObject(0).getString("kbm_mjbh").trim().equals("")|list.getJSONObject(0).getString("kbm_nextmjbh").trim().equals(""))){
                                if(!list.getJSONObject(0).getString("kbm_mjbh").trim().equals(list.getJSONObject(0).getString("kbm_nextmjbh").trim())){
                                    xy_5.setBackgroundColor(getResources().getColor(R.color.text_bg));
                                }else {
                                    xy_5.setBackgroundColor(Color.WHITE);
                                }
                            }else {
                                xy_5.setBackgroundColor(Color.WHITE);
                            }
                            if (!(list.getJSONObject(0).getString("kbm_wldm").trim().equals("")|list.getJSONObject(0).getString("kbm_nextwldm").trim().equals(""))){
                                if(!list.getJSONObject(0).getString("kbm_wldm").trim().equals(list.getJSONObject(0).getString("kbm_nextwldm").trim() )){
                                    xy_6.setBackgroundColor(getResources().getColor(R.color.text_bg));
                                }else {
                                    xy_6.setBackgroundColor(Color.WHITE);
                                }
                            }else {
                                xy_6.setBackgroundColor(Color.WHITE);
                            }


                            if(Integer.parseInt(list.getJSONObject(0).getString("kbm_jdcy"))>0){
                                tong_6.setBackgroundColor(getResources().getColor(R.color.large));
                            }else {
                                tong_6.setBackgroundColor(getResources().getColor(R.color.small));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x101:
                    //Toast.makeText(getContext(),"服务器异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x102:
                    List<List<String>>list_tong=(List<List<String>>)msg.obj;
                    if (list_tong.size()<0){
                       // Toast.makeText(getContext(),"数据异常",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    List<String>items_tong=list_tong.get(0);
                    if (items_tong.size()<10){
                        //Toast.makeText(getContext(),"数据异常",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    tong_1.setText(items_tong.get(8));
                    tong_2.setText(items_tong.get(2));
                    tong_3.setText(items_tong.get(3));
                    tong_4.setText(items_tong.get(5));
                    tong_5.setText(items_tong.get(7));
                    tong_6.setText(items_tong.get(9));
                    if(Integer.parseInt(items_tong.get(9))>0){
                        tong_6.setBackgroundColor(getResources().getColor(R.color.large));
                    }else {
                        tong_6.setBackgroundColor(getResources().getColor(R.color.small));
                    }
                    break;
                case 0x103:
                    try {
                        boolean isSame=false;
                        JSONArray array= (JSONArray) msg.obj;
                        List<List<String>>list_jhfh=new ArrayList<>();
                        for (int i=0;i<array.length();i++){
                            List<String>item=new ArrayList<>();
                            item.add(array.getJSONObject(i).getString("v_scrq"));
                            item.add(array.getJSONObject(i).getString("v_moeid"));
                            item.add(array.getJSONObject(i).getString("v_hval"));
                            list_jhfh.add(item);
                        }
                        ArrayList<String> xVals = new ArrayList<>();//X轴数据
                        List<String>yVals=new ArrayList<>();//Y轴数据
                        for (int i=0;i<list_jhfh.size();i++){
                            List<String>items_jhfh=list_jhfh.get(i);
                            String x=items_jhfh.get(0);
                            if(i==0){
                                xVals.add(x);
                            }else {
                                for (int j=0;j<xVals.size();j++){
                                    if (x.equals(xVals.get(j))){
                                        isSame=true;
                                    }
                                }
                                if(!isSame){
                                    xVals.add(x);
                                }
                                isSame=false;
                            }
                        }
                        isSame=false;
                        for (int i=0;i<list_jhfh.size();i++){
                            List<String>items_jhfh=list_jhfh.get(i);
                            String y=items_jhfh.get(1);
                            if(i==0){
                                yVals.add(y);
                            }else {
                                for (int j=0;j<yVals.size();j++){
                                    if (y.equals(yVals.get(j))){
                                        isSame=true;
                                    }
                                }
                                if(!isSame){
                                    yVals.add(y);
                                }
                                isSame=false;
                            }
                        }
                        setData(xVals,yVals,list_jhfh);
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x104:
                    try {
                        JSONArray list_jcxx= (JSONArray) msg.obj;
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("zldm_ss",list_jcxx.getJSONObject(0).getString("kbl_zldm"));
                        //editor.putString("waring",list_jcxx.get(0).get(5));
                        editor.commit();
                        initBasicInfo(list_jcxx);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x105:
                    JSONArray list_phonto= (JSONArray) msg.obj;
                    try {
                        initPhoto(list_phonto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x106:
                    Glide.with(getContext())
                            .load((String) msg.obj)
                            .centerCrop()
                            .placeholder(R.drawable.a_img)
                            .error(R.drawable.a_img)
                            .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                            .skipMemoryCache(true)///跳过内存缓存
                            .into(img_pho1);
                    break;
                case 0x107:
                    Glide.with(getContext())
                            .load((String) msg.obj)
                            .centerCrop()
                            .placeholder(R.drawable.b_img)
                            .error(R.drawable.b_img)
                            .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                            .skipMemoryCache(true)///跳过内存缓存
                            .into(img_pho2);
                    break;
                case 0x108:
                    img_pho1.setImageResource(R.drawable.a_img);
                    break;
                case 0x109:
                    img_pho2.setImageResource(R.drawable.b_img);
                    break;
                case 0x110://网络异常
                    tip_layout.setBackgroundColor(getResources().getColor(R.color.color_7_33));
                    Toast.makeText(getContext(),"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x111:
                    tip_layout.setBackgroundColor(Color.WHITE);
                    break;
            }
        }
    };



    private void initBarChart(BarChart mBarChart){
        mBarChart.setMaxVisibleValueCount(40);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.setHighlightFullBarEnabled(false);
        mBarChart.setTouchEnabled(false); // 设置是否可以触摸
        mBarChart.setDragEnabled(false);// 是否可以拖拽
        mBarChart.setScaleEnabled(false);// 是否可以缩放
        mBarChart.setDrawValueAboveBar(false);
        // 改变y标签的位置
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(24);
        mBarChart.getAxisRight().setEnabled(false);

        Legend l = mBarChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(0f);
        l.setFormToTextSpace(0f);
        l.setXEntrySpace(0f);
        mBarChart.getDescription().setText("");

        //设置表格透明
        //mBarChart.getXAxis().setAxisLineColor(getResources().getColor(R.color.touming));
        mBarChart.getXAxis().setGridColor(getResources().getColor(R.color.touming));
        //mBarChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.touming));
        mBarChart.getAxisLeft().setGridColor(getResources().getColor(R.color.touming));
    }

    private void initBasicInfo(JSONArray list_jcxx) throws JSONException {
        if (list_jcxx.length()<1){
            //Toast.makeText(getContext(),"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            jtbh_text.setText(list_jcxx.getJSONObject(0).getString("kbl_jtbh"));
            status.setText(list_jcxx.getJSONObject(0).getString("kbl_zlmc"));
            umSetColor(list_jcxx.getJSONObject(0).getString("kbl_color"));
            String[] temp=list_jcxx.getJSONObject(0).getString("kbl_msginfo").split("\\\\n");
            String tip="";
            for (int i=0;i<temp.length;i++){
                tip=tip+temp[i]+"\n";
            }
            msg_text.setText(tip);
            if (list_jcxx.getJSONObject(0).getString("kbl_jpflag").equals("1")){
                img_1.setVisibility(View.VISIBLE);
            }else {
                img_1.setVisibility(View.GONE);
            }
            if (list_jcxx.getJSONObject(0).getString("kbl_lpflag").equals("1")){
                img_2.setVisibility(View.VISIBLE);
            }else {
                img_2.setVisibility(View.GONE);
            }
            if (list_jcxx.getJSONObject(0).getString("kbl_waring").equals("1")){
                img_3.setVisibility(View.VISIBLE);
            }else {
                img_3.setVisibility(View.GONE);
            }
            if (list_jcxx.getJSONObject(0).getString("kbl_clflag").equals("1")){
                img_4.setVisibility(View.VISIBLE);
            }else {
                img_4.setVisibility(View.GONE);
            }
            if (list_jcxx.getJSONObject(0).getString("kbl_blflag").equals("1")){
                img_5.setVisibility(View.VISIBLE);
            }else {
                img_5.setVisibility(View.GONE);
            }
            if (list_jcxx.getJSONObject(0).getString("kbl_cxsjflag").equals("1")){
                img_6.setVisibility(View.VISIBLE);
            }else {
                img_6.setVisibility(View.GONE);
            }
            if (list_jcxx.getJSONObject(0).getString("kbl_hdflag").equals("1")){
                img_7.setVisibility(View.VISIBLE);
            }else {
                img_7.setVisibility(View.GONE);
            }
        }
    }


    //初始化条形图
    private void setData(final List<String>xVals, final List<String>yVals, List<List<String>>list_jhfh) {
        //补全工单信息
        for (int i=0;i<yVals.size();i++){
            String temp=yVals.get(i);
            List<String>xtemp=new ArrayList<>();
            for (int j=0;j<list_jhfh.size();j++){
                if (list_jhfh.get(j).get(1).equals(temp)){
                    xtemp.add(list_jhfh.get(j).get(0));
                }
            }
            List<String> needToAddxVal=new ArrayList();
            if (xtemp.size()<xVals.size()){
                for (int j=0;j<xVals.size();j++){
                    String xtemp_str=xtemp.toString();
                    int num=xtemp_str.indexOf(xVals.get(j));
                    if (xtemp.toString().indexOf(xVals.get(j))<1){
                        needToAddxVal.add(xVals.get(j));
                    }
                }
            }
            for (int j=0;j<needToAddxVal.size();j++){
                List<String>add_item=new ArrayList<>();
                add_item.add(needToAddxVal.get(j));
                add_item.add(temp);
                add_item.add("0");
                list_jhfh.add(add_item);
            }
        }








        final ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i=0;i<xVals.size();i++){
            List<String>yItems=new ArrayList<>();
            String time=xVals.get(i);
            for (int j=0;j<yVals.size();j++){
                String dan=yVals.get(j);
                for(int k=0;k<list_jhfh.size();k++){
                    List<String>item=list_jhfh.get(k);
                    if (time.equals(item.get(0))&&dan.equals(item.get(1))){
                        yItems.add(item.get(2));
                    }
                }
            }
            float[] floats=new float[yItems.size()];
            for (int l=0;l<yItems.size();l++){
                floats[l]=Float.parseFloat(String.valueOf(yItems.get(l)));
            }

            yVals1.add(new BarEntry(i, floats));

        }

        //BarChart的api有bug，数据Bar少于三条就出错
        if (xVals.size()<6){
            int yuanlai_size=xVals.size();
            int chaju_size=6-xVals.size();
            for(int i=0;i<chaju_size;i++){
                xVals.add("");
                yVals1.add(new BarEntry(i+yuanlai_size,0));
            }
        }

        XAxis xLabels = mBarChart.getXAxis();
        xLabels.setLabelCount(xVals.size());
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index=(int)value;
                if (index<xVals.size()){
                    return xVals.get(index);
                }else {
                    return "";
                }

            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });




        BarDataSet set1;

        /*if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {*/

        set1 = new BarDataSet(yVals1, "");
        int[]color=new int[]{R.color.color_1,R.color.color_2,R.color.color_3,R.color.color_4,
                R.color.color_5,R.color.color_6,R.color.color_7,R.color.color_8,R.color.color_9,R.color.color_10,};
        List<Integer>colors=new ArrayList<>();
        for (int i=0;i<yVals.size();i++){
            if (yVals.get(i).equals("0")){
                colors.add(getResources().getColor(R.color.touming));
            }else {
                colors.add(getResources().getColor(color[i]));
            }
        }
        set1.setColors(colors);
        String[] yStr=new String[yVals.size()];
        for (int n=0;n<yVals.size();n++){
            yStr[n]=yVals.get(n);
        }
        set1.setStackLabels(yStr);
        set1.setBarBorderColor(getResources().getColor(R.color.touming));
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.5f);
        //data.setValueFormatter(new MyValueFormatter());
        data.setValueTextColor(getResources().getColor(R.color.touming));

        mBarChart.setData(data);
        //}
        mBarChart.setFitBars(true);
        mBarChart.invalidate();
        Log.e("BarChatList",list_jhfh.toString());
    }

    //根据照片信息初始化照片
    private void initPhotoByFileVer(List<List<String>>list){
        if(list.size()>0){
            //下载图片
            for (int i=0;i<list.size();i++){
                final List<String>item=list.get(i);

                String[] str=item.get(6).split("&lt;br&gt;");
                String rym="";
                for (int j=0;j<str.length;j++){
                    rym=rym+str[j]+"\n";
                }
                labRym.setText(rym);

                if(item.get(0).equals("A")){
                    if (!item.get(1).equals("")){
                        //File file=new File(filePhath+"/Photos/"+item.get(1)+".JPG");
                        caozuo_text.setText(item.get(3));
                        cao_name_text.setText(item.get(2));
                        if (item.get(2).length()>5){
                            cao_name_text.setSingleLine();
                            cao_name_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            cao_name_text.setHorizontallyScrolling(true);
                            cao_name_text.setMarqueeRepeatLimit(-1);
                            cao_name_text.setFocusable(true);
                            cao_name_text.setFocusableInTouchMode(true);
                            cao_name_text.requestFocus();
                        }else {
                            cao_name_text.setFocusable(false);
                            cao_name_text.setFocusableInTouchMode(false);
                            cao_name_text.requestFocus();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    NetHelper.downLoadFileByUrlCompareWithVer(item.get(4),filePhath+"/Photos/",item.get(1)+".JPG");
                                    Message msg=handler.obtainMessage();
                                    msg.what=0x106;
                                    msg.obj=filePhath+"/Photos/"+item.get(1)+".JPG";
                                    handler.sendMessage(msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(0x108);
                                }
                            }
                        }).start();
                    }else {
                       handler.sendEmptyMessage(0x108);
                    }
                }else if(item.get(0).equals("B")){
                    if (!item.get(1).equals("")){
                        //File file=new File(filePhath+"/Photos/"+item.get(1)+".JPG");
                        jisu_text.setText(item.get(3));
                        ji_name_text.setText(item.get(2));
                        if (item.get(2).length()>5){
                            ji_name_text.setSingleLine();
                            ji_name_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            ji_name_text.setHorizontallyScrolling(true);
                            ji_name_text.setMarqueeRepeatLimit(-1);
                            ji_name_text.setFocusable(true);
                            ji_name_text.setFocusableInTouchMode(true);
                            ji_name_text.requestFocus();
                        }else {
                            ji_name_text.setFocusable(false);
                            ji_name_text.setFocusableInTouchMode(false);
                            ji_name_text.requestFocus();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    NetHelper.downLoadFileByUrlCompareWithVer(item.get(4),filePhath+"/Photos/",item.get(1)+".JPG");
                                    Message msg=handler.obtainMessage();
                                    msg.what=0x107;
                                    msg.obj=filePhath+"/Photos/"+item.get(1)+".JPG";
                                    handler.sendMessage(msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(0x109);
                                }
                            }
                        }).start();
                    }else {
                        handler.sendEmptyMessage(0x109);
                    }
                }
            }
        }else {
            jisu_text.setText("【技术员】");
            ji_name_text.setText("技术员");
            caozuo_text.setText("【操作员】");
            cao_name_text.setText("操作员");
            labRym.setText("");
        }
    }

    //根据内存是否已经有这张照片初始化照片
    private void initPhotoByExistInStorage(final JSONArray list) throws JSONException {
        if(list.length()>0){
            //下载图片
            for (int i=0;i<list.length();i++){
                final JSONObject item=list.getJSONObject(i);
                if(item.getString("wkm_lb").equals("A")){
                    if (!item.getString("wkm_wkno").equals("")){
                        File file=new File(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG");
                        caozuo_text.setText(item.getString("wkm_zwmc"));
                        cao_name_text.setText(item.getString("wkm_name"));
                        cao_phone.setText(item.getString("wkm_phone"));
                        if (item.getString("wkm_name").length()>5){
                            cao_name_text.setSingleLine();
                            cao_name_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            cao_name_text.setHorizontallyScrolling(true);
                            cao_name_text.setMarqueeRepeatLimit(-1);
                            cao_name_text.setFocusable(true);
                            cao_name_text.setFocusableInTouchMode(true);
                            cao_name_text.requestFocus();
                        }else {
                            cao_name_text.setFocusable(false);
                            cao_name_text.setFocusableInTouchMode(false);
                            cao_name_text.requestFocus();
                        }
                        String[] str=item.getString("wkm_rymname").split("&lt;br&gt;");
                        String rym="";
                        for (int j=0;j<str.length;j++){
                            rym=rym+str[j]+"\n";
                        }
                        labRym.setText(rym);


                        //文件缓存
                        if(file.exists()){
                            Glide.with(getContext()).load(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG")
                                    .asBitmap()
                                    .centerCrop()
                                    .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                                    /*.skipMemoryCache(true)*/.
                                    into(img_pho1);
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File dir=new File(filePhath+"/Photos/");
                                        if (!dir.exists()){
                                            dir.mkdir();
                                        }
                                        URL url=new URL(item.getString("wkm_PicFile"));
                                        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                                        urlConnection.setDoInput(true);
                                        urlConnection.setUseCaches(false);
                                        urlConnection.setRequestMethod("GET");
                                        urlConnection.setConnectTimeout(5000);
                                        urlConnection.connect();
                                        InputStream in=urlConnection.getInputStream();
                                        OutputStream out=new FileOutputStream(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG",false);
                                        byte[] buff=new byte[1024];
                                        int size;
                                        while ((size = in.read(buff)) != -1) {
                                            out.write(buff, 0, size);
                                        }
                                        Message msg=handler.obtainMessage();
                                        msg.what=0x106;
                                        msg.obj=filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG";
                                        handler.sendMessage(msg);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }else {
                        handler.sendEmptyMessage(0x108);
                    }
                }else if(item.getString("wkm_lb").equals("B")){
                    if (!item.getString("wkm_wkno").equals("")){
                        File file=new File(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG");
                        jisu_text.setText(item.getString("wkm_zwmc"));
                        ji_name_text.setText(item.getString("wkm_name"));
                        ji_phone.setText(item.getString("wkm_phone"));
                        if (item.getString("wkm_name").length()>5){
                            ji_name_text.setSingleLine();
                            ji_name_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            ji_name_text.setHorizontallyScrolling(true);
                            ji_name_text.setMarqueeRepeatLimit(-1);
                            ji_name_text.setFocusable(true);
                            ji_name_text.setFocusableInTouchMode(true);
                            ji_name_text.requestFocus();
                        }else {
                            ji_name_text.setFocusable(false);
                            ji_name_text.setFocusableInTouchMode(false);
                            ji_name_text.requestFocus();
                        }

                        //文件缓存
                        if(file.exists()){
                            Glide.with(getContext()).load(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG")
                                    .asBitmap()
                                    .centerCrop()
                                    .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                                    /*.skipMemoryCache(true)*/.
                                    into(img_pho2);
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File dir=new File(filePhath+"/Photos/");
                                        if (!dir.exists()){
                                            dir.mkdir();
                                        }
                                        URL url=new URL(item.getString("wkm_PicFile"));
                                        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                                        urlConnection.setDoInput(true);
                                        urlConnection.setUseCaches(false);
                                        urlConnection.setRequestMethod("GET");
                                        urlConnection.setConnectTimeout(15000);
                                        urlConnection.connect();
                                        InputStream in=urlConnection.getInputStream();
                                        OutputStream out=new FileOutputStream(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG",false);
                                        byte[] buff=new byte[1024];
                                        int size;
                                        while ((size = in.read(buff)) != -1) {
                                            out.write(buff, 0, size);
                                        }
                                        Message msg=handler.obtainMessage();
                                        msg.what=0x107;
                                        msg.obj=filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG";
                                        handler.sendMessage(msg);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }else {
                        handler.sendEmptyMessage(0x109);
                    }
                }
            }
        }else {
            jisu_text.setText("【技术员】");
            ji_name_text.setText("技术员");
            caozuo_text.setText("【操作员】");
            cao_name_text.setText("操作员");
            labRym.setText("");
        }
    }

    private void initPhoto(final JSONArray list) throws JSONException {
        if(list.length()>0){
            //下载图片
            for (int i=0;i<list.length();i++){
                final JSONObject item=list.getJSONObject(i);
                if(item.getString("wkm_lb").equals("A")){
                    if (!item.getString("wkm_wkno").equals("")){
                        File file=new File(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG");
                        caozuo_text.setText(item.getString("wkm_zwmc"));
                        cao_name_text.setText(item.getString("wkm_name"));
                        cao_phone.setText(item.getString("wkm_phone"));
                        if (item.getString("wkm_name").length()>5){
                            cao_name_text.setSingleLine();
                            cao_name_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            cao_name_text.setHorizontallyScrolling(true);
                            cao_name_text.setMarqueeRepeatLimit(-1);
                            cao_name_text.setFocusable(true);
                            cao_name_text.setFocusableInTouchMode(true);
                            cao_name_text.requestFocus();
                        }else {
                            cao_name_text.setFocusable(false);
                            cao_name_text.setFocusableInTouchMode(false);
                            cao_name_text.requestFocus();
                        }
                        String[] str=item.getString("wkm_rymname").split("&lt;br&gt;");
                        String rym="";
                        for (int j=0;j<str.length;j++){
                            rym=rym+str[j]+"\n";
                        }
                        labRym.setText(rym);

                        Message msg=handler.obtainMessage();
                        msg.what=0x106;
                        msg.obj=item.getString("wkm_PicFile");
                        handler.sendMessage(msg);

                        Log.e("show_Img","A");

                    }else {
                        handler.sendEmptyMessage(0x108);
                    }
                }else if(item.getString("wkm_lb").equals("B")){
                    if (!item.getString("wkm_wkno").equals("")){
                        File file=new File(filePhath+"/Photos/"+item.getString("wkm_wkno")+".JPG");
                        jisu_text.setText(item.getString("wkm_zwmc"));
                        ji_name_text.setText(item.getString("wkm_name"));
                        ji_phone.setText(item.getString("wkm_phone"));
                        if (item.getString("wkm_name").length()>5){
                            ji_name_text.setSingleLine();
                            ji_name_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            ji_name_text.setHorizontallyScrolling(true);
                            ji_name_text.setMarqueeRepeatLimit(-1);
                            ji_name_text.setFocusable(true);
                            ji_name_text.setFocusableInTouchMode(true);
                            ji_name_text.requestFocus();
                        }else {
                            ji_name_text.setFocusable(false);
                            ji_name_text.setFocusableInTouchMode(false);
                            ji_name_text.requestFocus();
                        }


                        Message msg=handler.obtainMessage();
                        msg.what=0x107;
                        //item.getString("wkm_PicFile")
                        msg.obj=item.getString("wkm_PicFile");
                        handler.sendMessage(msg);
                        Log.e("show_Img","B");

                    }else {
                        handler.sendEmptyMessage(0x109);
                    }

                }
            }
        }else {
            jisu_text.setText("【技术员】");
            ji_name_text.setText("技术员");
            caozuo_text.setText("【操作员】");
            cao_name_text.setText("操作员");
            labRym.setText("");
        }
    }
    //定时刷新
    private void updateDataOntime(){
        updateTimer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                getNetDate(1);
            }
        };
        updateTimer.schedule(timerTask,Integer.parseInt(getString(R.string.base_info_update_time)),Integer.parseInt(getString(R.string.base_info_update_time)));
    }

    private void getNetDate(final int type){
        //type为1的时候不刷新照片
        //type为0的时候刷新照片
        //工单信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                getInfo(type);
            }
        }).start();
    }

    private synchronized void getInfo(int type){
        JSONArray list2= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_JtmZtInfo '"+jtbh+"'");
        if(list2!=null){
            handler.sendEmptyMessage(0x111);
            if (list2.length()>0){
                Message msg=handler.obtainMessage();
                msg.what=0x104;
                msg.obj=list2;
                handler.sendMessage(msg);
            }
        }else {
            NetHelper.uploadNetworkError("Exec PAD_Get_JtmZtInfo NetWordError",jtbh,mac);
            //handler.sendEmptyMessage(0x101);
            handler.sendEmptyMessage(0x110);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNetDate(type);
            return;
        }



        /*List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_OrderInfo  '"+jtbh+"'");
        if(list!=null){
            handler.sendEmptyMessage(0x111);
            if(list.size()>0){
                if (list.get(0).size()>26){
                    Message msg=handler.obtainMessage();
                    msg.what=0x100;
                    msg.obj=list;
                    handler.sendMessage(msg);
                }
            }
        }else {
            NetHelper.uploadNetworkError("Exec PAD_Get_OrderInfo NetWorkError",jtbh,mac);
            handler.sendEmptyMessage(0x110);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNetDate();
            return;
        }*/
        JSONArray list= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_OrderInfo  '"+jtbh+"'");
        if(list!=null){
            handler.sendEmptyMessage(0x111);
            if(list.length()>0){
                Message msg=handler.obtainMessage();
                msg.what=0x100;
                msg.obj=list;
                handler.sendMessage(msg);
            }
        }else {
            NetHelper.uploadNetworkError("Exec PAD_Get_OrderInfo NetWorkError",jtbh,mac);
            handler.sendEmptyMessage(0x110);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNetDate(type);
            return;
        }



        /*List<List<String>>list3= NetHelper.getQuerysqlResult("Exec PAD_Get_FhChartInfo '"+jtbh+"'");
        if(list3!=null){
            handler.sendEmptyMessage(0x111);
            if (list3.size()>0){
                if (list3.get(0).size()>2){
                    Message msg=handler.obtainMessage();
                    msg.what=0x103;
                    msg.obj=list3;
                    handler.sendMessage(msg);
                }
            }
        }else {
            NetHelper.uploadNetworkError("Exec PAD_Get_FhChartInfo NetWordError",jtbh,mac);
            handler.sendEmptyMessage(0x110);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNetDate();
            return;
        }*/
        JSONArray list3= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_FhChartInfo '"+jtbh+"'");
        if(list3!=null){
            handler.sendEmptyMessage(0x111);
            if (list3.length()>0){
                Message msg=handler.obtainMessage();
                msg.what=0x103;
                msg.obj=list3;
                handler.sendMessage(msg);
            }
        }else {
            NetHelper.uploadNetworkError("Exec PAD_Get_FhChartInfo NetWordError",jtbh,mac);
            handler.sendEmptyMessage(0x110);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNetDate(type);
            return;
        }


        /*List<List<String>>list4= NetHelper.getQuerysqlResult("Exec PAD_Get_PhotoInfo '"+jtbh+"'");
        if(list4!=null){
            handler.sendEmptyMessage(0x111);
            if (list4.size()>0){
                if (list4.get(0).size()>8){
                    Message msg=handler.obtainMessage();
                    msg.what=0x105;
                    msg.obj=list4;
                    handler.sendMessage(msg);
                }
            }
        }else {
            NetHelper.uploadNetworkError("Exec PAD_Get_PhotoInfo NetWordError",jtbh,mac);
            handler.sendEmptyMessage(0x110);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNetDate();
            return;
        }*/
        if (type!=1){
            JSONArray list4= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_PhotoInfo '"+jtbh+"'");
            if(list4!=null){
                handler.sendEmptyMessage(0x111);
                if (list4.length()>0){
                    Message msg=handler.obtainMessage();
                    msg.what=0x105;
                    msg.obj=list4;
                    handler.sendMessage(msg);
                }
            }else {
                NetHelper.uploadNetworkError("Exec PAD_Get_PhotoInfo NetWordError",jtbh,mac);
                handler.sendEmptyMessage(0x110);
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getNetDate(type);
                return;
            }
        }
    }

    private void umSetColor(String fcColor){

        if(fcColor.equals("N")){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.lightgray));
            status.setTextColor(getResources().getColor(R.color.darkviolet));
        }else  if(fcColor.equals("W")){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.lightgray));
            status.setTextColor(Color.WHITE);
        }else if(fcColor.equals("G")){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.limegreen));
            status.setTextColor(Color.BLUE);
        }else if(fcColor.equals("Y")){
            cardView.setCardBackgroundColor(Color.YELLOW);
            status.setTextColor(Color.RED);
        }else if(fcColor.equals("R")){
            cardView.setCardBackgroundColor(Color.RED);
            status.setTextColor(Color.YELLOW);
        }else if(fcColor.equals("V")){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.darkviolet));
            status.setTextColor(Color.YELLOW);
        }else{
            cardView.setCardBackgroundColor(getResources().getColor(R.color.limegreen));
            status.setTextColor(Color.WHITE);
        }
    }

    private int getColorByKey(String color){
         /*
            W	白色
            H	灰色
            Y	黄色
            G	绿色
            V	紫色
            B	蓝色
            P	粉色
            R	红色
            K	黑色
             */
        switch (color)
        {
            case "W":
                return Color.WHITE;
            case "H":
                return getResources().getColor(R.color.lable);
            case "Y":
                return Color.YELLOW;
            case "G":
                return Color.GREEN;
            case "V":
                return getResources().getColor(R.color.color_6);
            case "B":
                return Color.BLUE;
            case "P":
                return getResources().getColor(R.color.color_10);
            case "R":
                return Color.RED;
            case "K":
                return Color.BLACK;
            default:
                return getResources().getColor(R.color.lable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
        updateTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(getContext());
        switch (v.getId()){
            case R.id.cardView:
                getNetDate(0);
                break;
        }
    }


}
