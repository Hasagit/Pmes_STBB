package com.ruiduoyi.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class InfoFragment extends Fragment  implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BarChart mBarChart;
    private String jtbh;
    private TextView dq_1,dq_2,dq_3,dq_4,dq_5,dq_6,dq_7,dq_8,
                xy_1,xy_2,xy_3,xy_4,xy_5,xy_6,xy_7,xy_8,
                mo_1,mo_2,mo_3,mo_4,mo_5,mo_6,tong_1,tong_2,tong_3,tong_4,tong_5,tong_6,jtbh_text,status,msg_text,
                caozuo_text,jisu_text,cao_name_text,ji_name_text,labRym;
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
            getNetDate();
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
        cao_name_text=(TextView)view.findViewById(R.id.cao_name);
        caozuo_text=(TextView)view.findViewById(R.id.caozuo);
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
        filePhath=getContext().getCacheDir().getPath();
        getNetDate();
        updateDataOntime();
        initBarChart(mBarChart);
    }




    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    if(list.size()>0){
                        List<String>item=list.get(0);
                        dq_1.setText(item.get(0));
                        dq_2.setText(item.get(1));

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("sjsx",item.get(0));
                        editor.putString("zzdh",item.get(1));//制造单号
                        editor.putString("gddh",item.get(2));
                        editor.putString("scph",item.get(3));
                        editor.putString("mjbh",item.get(4));//模具编号
                        editor.putString("cpbh",item.get(5));
                        editor.putString("pmgg",item.get(6));
                        editor.putString("ysdm",item.get(7));
                        editor.putString("mjmc",item.get(16));
                        editor.putString("jhsl",item.get(21));
                        editor.putString("lpsl",item.get(23));
                        editor.putString("blsl",item.get(24));
                        editor.commit();
                        dq_3.setText(item.get(2));
                        dq_4.setText(item.get(3));
                        dq_5.setText(item.get(4));
                        dq_6.setText(item.get(5));
                        dq_7.setText(item.get(6));
                        dq_8.setText(item.get(7));
                        xy_1.setText(item.get(8));
                        xy_2.setText(item.get(9));
                        xy_3.setText(item.get(10));
                        xy_4.setText(item.get(11));
                        xy_5.setText(item.get(12));
                        xy_6.setText(item.get(13));
                        xy_7.setText(item.get(14));
                        xy_8.setText(item.get(15));
                        mo_1.setText(item.get(4));
                        mo_2.setText(item.get(16));
                        mo_3.setText(item.get(17));
                        mo_4.setText(item.get(18));
                        mo_5.setText(item.get(19));
                        mo_6.setText(item.get(20));
                        tong_1.setText(item.get(21));
                        tong_2.setText(item.get(22));
                        tong_3.setText(item.get(23));
                        tong_4.setText(item.get(24));
                        tong_5.setText(item.get(25));
                        tong_6.setText(item.get(26));

                        if (!(item.get(4).trim().equals("")|item.get(12).trim().equals(""))){
                            if(!item.get(4).trim().equals(item.get(12).trim())){
                                xy_5.setBackgroundColor(getResources().getColor(R.color.text_bg));
                            }else {
                                xy_5.setBackgroundColor(Color.WHITE);
                            }
                        }else {
                            xy_5.setBackgroundColor(Color.WHITE);
                        }
                        if (!(item.get(5).trim().equals("")|item.get(13).trim().equals(""))){
                            if(!item.get(5).trim().equals(item.get(13).trim() )){
                                xy_6.setBackgroundColor(getResources().getColor(R.color.text_bg));
                            }else {
                                xy_6.setBackgroundColor(Color.WHITE);
                            }
                        }else {
                            xy_6.setBackgroundColor(Color.WHITE);
                        }


                        if(Integer.parseInt(item.get(26))>0){
                            tong_6.setBackgroundColor(getResources().getColor(R.color.large));
                        }else {
                            tong_6.setBackgroundColor(getResources().getColor(R.color.small));
                        }
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
                        ArrayList<String> xVals = new ArrayList<>();//X轴数据
                        List<String>yVals=new ArrayList<>();//Y轴数据
                        List<List<String>>list_jhfh=(List<List<String>>)msg.obj;
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
                    }
                break;
                case 0x104:
                    List<List<String>>list_jcxx=(List<List<String>>)msg.obj;
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("zldm_ss",list_jcxx.get(0).get(1));
                    //editor.putString("waring",list_jcxx.get(0).get(5));
                    editor.commit();
                    initBasicInfo(list_jcxx);
                    break;
                case 0x105:
                    List<List<String>>list_phonto=(List<List<String>>)msg.obj;
                    initPhoto(list_phonto);
                    break;
                case 0x106:
                    Glide.with(getContext()).load((String)msg.obj).into(img_pho1);
                    break;
                case 0x107:
                    Glide.with(getContext()).load((String)msg.obj).into(img_pho2);
                    break;
                case 0x108:
                    img_pho1.setImageResource(R.drawable.a_img);
                    break;
                case 0x109:
                    img_pho2.setImageResource(R.drawable.b_img);
                    break;
                case 0x110://网络异常
                    tip_layout.setBackgroundColor(getResources().getColor(R.color.color_7_33));
                    break;
                case 0x111:
                    tip_layout.setBackgroundColor(Color.WHITE);
                    break;
            }
        }
    };
    private void initBarChart(BarChart mBarChart){
        //mBarChart.setOnChartValueSelectedListener(this);
        //mBarChart.getDescription().setEnabled(false);
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
    }

    private void initBasicInfo(List<List<String>>list_jcxx){
        if (list_jcxx.size()<1){
            //Toast.makeText(getContext(),"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            List<String>item=list_jcxx.get(0);
            jtbh_text.setText(item.get(0));
            status.setText(item.get(10));
            umSetColor(item.get(2));
            String[] temp=item.get(11).split("\\\\n");
            String tip="";
            for (int i=0;i<temp.length;i++){
                tip=tip+temp[i]+"\n";
            }
            msg_text.setText(tip);
            if (item.get(3).equals("1")){
                img_1.setVisibility(View.VISIBLE);
            }else {
                img_1.setVisibility(View.GONE);
            }
            if (item.get(4).equals("1")){
                img_2.setVisibility(View.VISIBLE);
            }else {
                img_2.setVisibility(View.GONE);
            }
            if (item.get(5).equals("1")){
                img_3.setVisibility(View.VISIBLE);
            }else {
                img_3.setVisibility(View.GONE);
            }
            if (item.get(6).equals("1")){
                img_4.setVisibility(View.VISIBLE);
            }else {
                img_4.setVisibility(View.GONE);
            }
            if (item.get(7).equals("1")){
                img_5.setVisibility(View.VISIBLE);
            }else {
                img_5.setVisibility(View.GONE);
            }
            if (item.get(8).equals("1")){
                img_6.setVisibility(View.VISIBLE);
            }else {
                img_6.setVisibility(View.GONE);
            }
            if (item.get(9).equals("1")){
                img_7.setVisibility(View.VISIBLE);
            }else {
                img_7.setVisibility(View.GONE);
            }
        }
    }


    //初始化
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
        /*switch (yVals.size()){
            case 0:
                set1.setColor(getResources().getColor(R.color.tongming));
                break;
            case 1:
                set1.setColors(getResources().getColor(color[0]));
                break;
            case 2:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]));
                break;
            case 3:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),getResources().getColor(color[2]));
                break;
            case 4:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]));
                break;
            case 5:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]),getResources().getColor(color[4]));
                break;
            case 6:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]),getResources().getColor(color[4]),
                        getResources().getColor(color[5]));
                break;
            case 7:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]),getResources().getColor(color[4]),
                        getResources().getColor(color[5]),getResources().getColor(color[6]));
                break;
            case 8:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]),getResources().getColor(color[4]),
                        getResources().getColor(color[5]),getResources().getColor(color[6]),getResources().getColor(color[7]));
                break;
            case 9:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]),getResources().getColor(color[4]),
                        getResources().getColor(color[5]),getResources().getColor(color[6]),getResources().getColor(color[7]),
                        getResources().getColor(color[8]));
                break;
            case 10:
                set1.setColors(getResources().getColor(color[0]),getResources().getColor(color[1]),
                        getResources().getColor(color[2]),getResources().getColor(color[3]),getResources().getColor(color[4]),
                        getResources().getColor(color[5]),getResources().getColor(color[6]),getResources().getColor(color[7]),
                        getResources().getColor(color[8]),getResources().getColor(color[9]));
                break;
            default:
                break;

        }*/
            /*set1.setColors(getResources().getColor(R.color.blue_sl_false),
                    getResources().getColor(R.color.colorPrimary),
                    getResources().getColor(R.color.bottom_sl));*/
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

    //初始化照片
    private void initPhoto(List<List<String>>list){
        if(list.size()>0){
            //下载图片
            for (int i=0;i<list.size();i++){
                final List<String>item=list.get(i);
                if(item.get(0).equals("A")){
                    if (!item.get(1).equals("")){
                        File file=new File(filePhath+"/Photos/"+item.get(1)+".JPG");
                        caozuo_text.setText(item.get(3));
                        cao_name_text.setText(item.get(2));
                        String[] str=item.get(6).split("&lt;br&gt;");
                        String rym="";
                        for (int j=0;j<str.length;j++){
                            rym=rym+str[j]+"\n";
                        }
                        labRym.setText(rym);


                        //文件缓存
                        if(file.exists()){
                            Glide.with(getContext()).load(filePhath+"/Photos/"+item.get(1)+".JPG").into(img_pho1);
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File dir=new File(filePhath+"/Photos/");
                                        if (!dir.exists()){
                                            dir.mkdir();
                                        }
                                        URL url=new URL(item.get(4));
                                        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                                        urlConnection.setDoInput(true);
                                        urlConnection.setUseCaches(false);
                                        urlConnection.setRequestMethod("GET");
                                        urlConnection.setConnectTimeout(5000);
                                        urlConnection.connect();
                                        InputStream in=urlConnection.getInputStream();
                                        OutputStream out=new FileOutputStream(filePhath+"/Photos/"+item.get(1)+".JPG",false);
                                        byte[] buff=new byte[1024];
                                        int size;
                                        while ((size = in.read(buff)) != -1) {
                                            out.write(buff, 0, size);
                                        }
                                        Message msg=handler.obtainMessage();
                                        msg.what=0x106;
                                        msg.obj=filePhath+"/Photos/"+item.get(1)+".JPG";
                                        handler.sendMessage(msg);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }else {
                       handler.sendEmptyMessage(0x108);
                    }
                }else if(item.get(0).equals("B")){
                    if (!item.get(1).equals("")){
                        File file=new File(filePhath+"/Photos/"+item.get(1)+".JPG");
                        jisu_text.setText(item.get(3));
                        ji_name_text.setText(item.get(2));

                        //文件缓存
                        if(file.exists()){
                            Glide.with(getContext()).load(filePhath+"/Photos/"+item.get(1)+".JPG").into(img_pho2);
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File dir=new File(filePhath+"/Photos/");
                                        if (!dir.exists()){
                                            dir.mkdir();
                                        }
                                        URL url=new URL(item.get(4));
                                        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                                        urlConnection.setDoInput(true);
                                        urlConnection.setUseCaches(false);
                                        urlConnection.setRequestMethod("GET");
                                        urlConnection.setConnectTimeout(15000);
                                        urlConnection.connect();
                                        InputStream in=urlConnection.getInputStream();
                                        OutputStream out=new FileOutputStream(filePhath+"/Photos/"+item.get(1)+".JPG",false);
                                        byte[] buff=new byte[1024];
                                        int size;
                                        while ((size = in.read(buff)) != -1) {
                                            out.write(buff, 0, size);
                                        }
                                        Message msg=handler.obtainMessage();
                                        msg.what=0x107;
                                        msg.obj=filePhath+"/Photos/"+item.get(1)+".JPG";
                                        handler.sendMessage(msg);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
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

    //定时刷新
    private void updateDataOntime(){
        updateTimer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                getNetDate();
                //Log.e("updateDataOntime","updateDataOntime go");
            }
        };
        updateTimer.schedule(timerTask,Integer.parseInt(getString(R.string.base_info_update_time)),Integer.parseInt(getString(R.string.base_info_update_time)));
    }

    private void getNetDate(){
        //工单信息

        if (sharedPreferences.getString("isBaseInfoFinish","OK").equals("OK")){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("isBaseInfoFinish","NO");
            editor.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_OrderInfo  '"+jtbh+"'");
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
                        AppUtils.uploadNetworkError("Exec PAD_Get_OrderInfo NetWorkError",jtbh,mac);
                        handler.sendEmptyMessage(0x110);
                    }




                    List<List<String>>list2= NetHelper.getQuerysqlResult("Exec PAD_Get_JtmZtInfo '"+jtbh+"'");
                    if(list2!=null){
                        handler.sendEmptyMessage(0x111);
                        if (list2.size()>0){
                            if (list2.get(0).size()>11){
                                Message msg=handler.obtainMessage();
                                msg.what=0x104;
                                msg.obj=list2;
                                handler.sendMessage(msg);
                            }
                        }
                    }else {
                        AppUtils.uploadNetworkError("Exec PAD_Get_JtmZtInfo NetWordError",jtbh,mac);
                        //handler.sendEmptyMessage(0x101);
                        handler.sendEmptyMessage(0x110);
                    }


                    List<List<String>>list3= NetHelper.getQuerysqlResult("Exec PAD_Get_FhChartInfo '"+jtbh+"'");
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
                        AppUtils.uploadNetworkError("Exec PAD_Get_FhChartInfo NetWordError",jtbh,mac);
                        handler.sendEmptyMessage(0x110);
                    }


                    List<List<String>>list4= NetHelper.getQuerysqlResult("Exec PAD_Get_PhotoInfo '"+jtbh+"'");
                    if(list4!=null){
                        handler.sendEmptyMessage(0x111);
                        if (list4.get(0).size()>6){
                            Message msg=handler.obtainMessage();
                            msg.what=0x105;
                            msg.obj=list4;
                            handler.sendMessage(msg);
                        }
                    }else {
                        AppUtils.uploadNetworkError("Exec PAD_Get_PhotoInfo NetWordError",jtbh,mac);
                        handler.sendEmptyMessage(0x110);
                    }

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("isBaseInfoFinish","OK");
                    editor.commit();
                }
            }).start();
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
                getNetDate();
                break;
        }
    }

}
