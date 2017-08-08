package com.ruiduoyi.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PzglActivity extends BaseActivity implements View.OnClickListener{
    private Button btn_1,btn_2,btn_3,btn_4,cancle_btn;
    private TextView fcry_text,fcsj_text,pdry_text,pdsj_text,ys_text,zzdh_text,
        gddh_text,scph_text,mjbh_text,mjmc_text,mjqs_text,cxzq_text,
        cpbh_text,pmgg_text,bzjz_text,sjjz_text,bzsk_text,sjsk_text,
        pdjg_text,blyy_text,bzxx_text;
    private BarChart barChart;
    private LineChart lineChart;
    private LinearLayout layout;
    private String zzdh;
    private String jtbh;
    private ListView xuncha_lisView,listView;
    private LinearLayout layout_4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pzgl);
        initView();
        initData();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    List<List<String>>list= (List<List<String>>) msg.obj;
                    List<String>item=list.get(0);
                    fcry_text.setText(item.get(0));
                    fcsj_text.setText(item.get(1));
                    pdry_text.setText(item.get(2));
                    pdsj_text.setText(item.get(3));
                    ys_text.setText(item.get(4));
                    zzdh_text.setText(item.get(5));
                    gddh_text.setText(item.get(6));
                    scph_text.setText(item.get(7));
                    mjbh_text.setText(item.get(8));
                    mjmc_text.setText(item.get(9));
                    mjqs_text.setText(item.get(10));
                    cxzq_text.setText(item.get(11));
                    cpbh_text.setText(item.get(12));
                    pmgg_text.setText(item.get(13));
                    bzjz_text.setText(item.get(14));
                    sjjz_text.setText(item.get(15));
                    bzsk_text.setText(item.get(16));
                    sjsk_text.setText(item.get(17));
                    pdjg_text.setText(item.get(18));
                    blyy_text.setText(item.get(19));
                    bzxx_text.setText(item.get(20));
                    break;
                case 0x101:
                    break;
                case 0x102:
                    List<List<String>>list1=(List<List<String>>)msg.obj;
                    initLineChar(list1,lineChart);
                    break;
                case 0x103:
                    List<List<String>>list2=(List<List<String>>)msg.obj;
                    initBartChart(barChart,list2);
                    break;
                case 0x104:
                    List<List<String>>list3=(List<List<String>>)msg.obj;
                    initListView(list3);
                    break;
                case 0x105:
                    List<List<String>>list4=(List<List<String>>)msg.obj;
                    initRightListView(list4);
                    break;
                default:
                    break;
            }
        }
    };


    private void initView(){
        fcry_text=(TextView)findViewById(R.id.ksname);
        fcsj_text =(TextView)findViewById(R.id.kssj);
        pdry_text=(TextView)findViewById(R.id.jsname);
        pdsj_text=(TextView)findViewById(R.id.jssj);
        ys_text=(TextView)findViewById(R.id.min);
        zzdh_text=(TextView)findViewById(R.id.zzdh);
        gddh_text=(TextView)findViewById(R.id.sodh);
        scph_text=(TextView)findViewById(R.id.ph);
        mjbh_text=(TextView)findViewById(R.id.mjbh);
        mjmc_text=(TextView)findViewById(R.id.mjmc);
        mjqs_text=(TextView)findViewById(R.id.bzxs);
        cxzq_text=(TextView)findViewById(R.id.bzcxsj);
        cpbh_text=(TextView)findViewById(R.id.wldm);
        pmgg_text=(TextView)findViewById(R.id.pmgg);
        bzjz_text=(TextView)findViewById(R.id.bzjz);
        sjjz_text=(TextView)findViewById(R.id.jz);
        bzsk_text=(TextView)findViewById(R.id.bzskzl);
        sjsk_text=(TextView)findViewById(R.id.skzl);
        pdjg_text=(TextView)findViewById(R.id.pdjg);
        blyy_text=(TextView)findViewById(R.id.yymc);
        bzxx_text=(TextView)findViewById(R.id.desc);
        btn_1=(Button)findViewById(R.id.btn_1);
        btn_2=(Button)findViewById(R.id.btn_2);
        btn_3=(Button)findViewById(R.id.btn_3);
        btn_4=(Button)findViewById(R.id.btn_4);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        barChart=(BarChart)findViewById(R.id.barChart);
        lineChart=(LineChart)findViewById(R.id.lineChart);
        layout=(LinearLayout)findViewById(R.id.xuncha_layout);
        xuncha_lisView=(ListView)findViewById(R.id.xuncha_listView);
        layout_4=(LinearLayout)findViewById(R.id.layout_4);
        listView=(ListView)findViewById(R.id.listView_2) ;
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
    }

    //初始化折线图质量监测
    private void initLineChar(List<List<String>>lists,LineChart mDoubleLineChar){
        //设置数值选择监听
        //mDoubleLineChar.setOnChartValueSelectedListener(this);
        // 没有描述的文本
        mDoubleLineChar.getDescription().setEnabled(false);
        // 支持触控手势
        mDoubleLineChar.setTouchEnabled(false);
        mDoubleLineChar.setDragDecelerationFrictionCoef(0.9f);
        // 支持缩放和拖动
        mDoubleLineChar.setDragEnabled(false);
        mDoubleLineChar.setScaleEnabled(false);
        mDoubleLineChar.setDrawGridBackground(false);
        mDoubleLineChar.setHighlightPerDragEnabled(false);
        // 如果禁用,扩展可以在x轴和y轴分别完成
        mDoubleLineChar.setPinchZoom(true);
        // 设置背景颜色(灰色)
        //mDoubleLineChar.setBackgroundColor(Color.BLACK);



        //设置数据
        final List<String>xVals=new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < lists.size(); i++) {
            List<String>item=lists.get(i);
            xVals.add(item.get(0));
            yVals.add(new Entry(i,Integer.parseInt(item.get(1))));
        }

        LineDataSet set;

        set = new LineDataSet(yVals, "");
        //set1.isDrawValuesEnabled();
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.RED);
        set.setCircleColor(getResources().getColor(R.color.touming));
        set.setLineWidth(3f);
        //set2.setCircleRadius(3f);
        //set2.setFillAlpha(65);
        //set2.setFillColor(Color.RED);
        set.setDrawCircleHole(false);
        //set2.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextSize(10f);
        // 创建一个数据集的数据对象
        LineData data = new LineData(set);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);


        //设置数据
        mDoubleLineChar.setData(data);
        //默认x动画
        mDoubleLineChar.animateX(2500);

        //获得数据
        Legend l = mDoubleLineChar.getLegend();

        //修改
        l.setEnabled(false);
        /*l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);*/

        //x轴
        XAxis xAxis = mDoubleLineChar.getXAxis();
        xAxis.setLabelCount(xVals.size()-1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index=(int)value;
                if (value>xVals.size()){
                    return "";
                }else {
                    return xVals.get(index);
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });


        //左边y轴
        YAxis leftAxis = mDoubleLineChar.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        //右边
        YAxis rightAxis = mDoubleLineChar.getAxisRight();
        rightAxis.setTextColor(getResources().getColor(R.color.touming));
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
        //mDoubleLineChar.animateX(2500);
    }

    //初始化不良统计
    private void initBarChar(List<List<String>>lists,BarChart mBarChart){
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMaxVisibleValueCount(40);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(false);
        mBarChart.setTouchEnabled(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.setHighlightFullBarEnabled(false);


        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        //初始化数据
        final List<String>xVals=new ArrayList<>();
        List<BarEntry>yVals=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            xVals.add(item.get(1));
            yVals.add(new BarEntry(i,Float.parseFloat(item.get(2))));
        }
        BarDataSet set=new BarDataSet(yVals,"");
        set.setColor(Color.RED);
        BarData data=new BarData(set);
        data.setBarWidth(0.15f);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLUE);
        data.setDrawValues(true);
        mBarChart.setData(data);
        // 改变y标签的位置
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        mBarChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mBarChart.getXAxis();
        xLabels.setLabelCount(xVals.size());
        xLabels.setTextSize(15f);
        xLabels.setDrawAxisLine(false);
        xLabels.setDrawGridLines(false);
        xLabels.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index=(int)value;
                return xVals.get(index);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
    }


    //初始化条形图
    private void initBartChart(BarChart mBarChart,List<List<String>>lists){
        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        //mBarChart.setOnChartValueSelectedListener(this);
        mBarChart.setTouchEnabled(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        // 如果60多个条目显示在图表,drawn没有值
        mBarChart.setMaxVisibleValueCount(60);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(false);
        //是否显示表格颜色
        mBarChart.setDrawGridBackground(false);
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        // 只有1天的时间间隔
        //xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //这个替换setStartAtZero(true)
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setAxisMaximum(50f);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(50f);

        // 设置标示，就是那个一组y的value的
        Legend l = mBarChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //样式
        l.setForm(Legend.LegendForm.SQUARE);
        //字体
        l.setFormSize(9f);
        //大小
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        //模拟数据
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        final List<String>xVal=new ArrayList<>();
        List<String>yVal=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            xVal.add(item.get(1));
            yVal.add(item.get(2));
            yVals1.add(new BarEntry(i,Integer.parseInt(item.get(2))));
        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        //设置有四种颜色
        set1.setColor(Color.RED);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.3f);
        //设置数据
        mBarChart.setData(data);


        xAxis.setLabelCount(yVals1.size());
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index= (int) value;
                if (index<xVal.size()){
                    return xVal.get(index);
                }else {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
    }

    private void initBarChart(List<List<String>>lists,BarChart mBarChart){
        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        // 如果60多个条目显示在图表,drawn没有值
        mBarChart.setMaxVisibleValueCount(60);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(false);
        //是否显示表格颜色
        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        // 只有1天的时间间隔
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setAxisMaximum(50f);
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return null;
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });


        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //这个替换setStartAtZero(true)
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(50f);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(50f);

        // 设置标示，就是那个一组y的value的
        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //样式
        l.setForm(Legend.LegendForm.SQUARE);
        //字体
        l.setFormSize(9f);
        //大小
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        //设置数据
        List<String>xVal=new ArrayList<>();
        List<String>yVal=new ArrayList<>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            xVal.add(item.get(1));
            yVal.add(item.get(2));
            yVals.add(new BarEntry(i,Integer.parseInt(item.get(2))));
        }

        BarDataSet set1=new BarDataSet(yVals,"");
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        mBarChart.setData(data);



    }

    //初始化巡查记录
    private void initListView(List<List<String>>lists){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("lab_1",item.get(0));
            map.put("lab_2",item.get(1));
            map.put("lab_3",item.get(2));
            map.put("lab_4",item.get(3));
            map.put("lab_5",item.get(4));
            map.put("lab_6",item.get(5));
            map.put("lab_7",item.get(6));
            map.put("lab_8",item.get(7));
            map.put("lab_9",item.get(8));
            map.put("lab_10",item.get(9));
            map.put("lab_11",item.get(10));
            map.put("lab_12",item.get(11));
            data.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.list_item_b4_1,
                new String[]{"lab_1","lab_2","lab_3","lab_4","lab_5","lab_6","lab_7","lab_8","lab_9","lab_10","lab_11","lab_12"},
                new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3,R.id.lab_4,R.id.lab_5,R.id.lab_6,R.id.lab_7,
                        R.id.lab_8,R.id.lab_9,R.id.lab_10,R.id.lab_11,R.id.lab_12});
        xuncha_lisView.setAdapter(adapter);
    }

    //初始化从工单信息表格
    private void initRightListView(List<List<String>>lists){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<lists.size();i++){
            List<String>item=lists.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("lab_1",item.get(0));
            map.put("lab_2",item.get(1));
            map.put("lab_3",item.get(2));
            map.put("lab_4",item.get(3));
            map.put("lab_5",item.get(4));
            map.put("lab_6",item.get(5));
            data.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.list_item_b4_2,
                new String[]{"lab_1","lab_2","lab_3","lab_4","lab_5","lab_6"},
                new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3,R.id.lab_4,R.id.lab_5,R.id.lab_6});
        listView.setAdapter(adapter);
    }

    private void initData(){
        zzdh=sharedPreferences.getString("zzdh","");
        jtbh=sharedPreferences.getString("jtbh","");
        getNetData();
    }

    private void a(){
        /*//获取文本框的值
        Thread thread_1=new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_PzmInfNew 'A','"+jtbh+"','"+moeid+"'");
                Message msg=handler.obtainMessage();
                if (list!=null){
                    msg.what=0x100;
                    msg.obj=list;
                }else {
                    msg.what=0x101;
                }
                handler.sendMessage(msg);
            }
        });

        //获取质量检测的值
        Thread thread_2=new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_PzmInfNew 'B01','"+jtbh+"','"+moeid+"'");
                Message msg=handler.obtainMessage();
                if (list!=null){
                    msg.what=0x102;
                    msg.obj=list;
                }else {
                    msg.what=0x101;
                }
                handler.sendMessage(msg);
            }
        });

        //获取不良统计的值
        Thread thread_3=new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_PzmInfNew 'B02','"+jtbh+"','"+moeid+"'");
                Message msg=handler.obtainMessage();
                if (list!=null){
                    msg.what=0x103;
                    msg.obj=list;
                }else {
                    msg.what=0x101;
                }
                handler.sendMessage(msg);
            }
        });

        //获取巡查记录的值
        Thread thread_4=new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_PzmInfNew 'B03','"+jtbh+"','"+moeid+"'");
                Message msg=handler.obtainMessage();
                if (list!=null){
                    msg.what=0x104;
                    msg.obj=list;
                }else {
                    msg.what=0x101;
                }
                handler.sendMessage(msg);
            }
        });

        //获取质量检测右边表的值
        Thread thread_5=new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_PzmInfNew 'A01','"+jtbh+"','"+moeid+"'");
                Message msg=handler.obtainMessage();
                if (list!=null){
                    msg.what=0x105;
                    msg.obj=list;
                }else {
                    msg.what=0x101;
                }
                handler.sendMessage(msg);
            }
        });*/
    }


    private void getNetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //首件信息
                List<List<String>>list1=NetHelper.getQuerysqlResult("Exec  PAD_Get_PzmInf 'A','"+zzdh+"','"+jtbh+"'");
                if (list1!=null){
                    if (list1.size()>0){
                        if (list1.get(0).size()>20){
                            Message msg=handler.obtainMessage();
                            msg.obj=list1;
                            msg.what=0x100;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec  PAD_Get_PzmInf",jtbh,sharedPreferences.getString("mac",""));
                }


                //质量检测
                List<List<String>>list2= NetHelper.getQuerysqlResult("Exec  PAD_Get_PzmInf 'B01','"+zzdh+"','"+jtbh+"'");
                if (list2!=null){
                    if (list2.size()>0){
                        if (list2.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.obj=list2;
                            msg.what=0x102;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec  PAD_Get_PzmInf 'B01'",jtbh,sharedPreferences.getString("mac",""));
                }

                /*//质量检测
                List<List<String>>list3= NetHelper.getQuerysqlResult("Exec  PAD_Get_PzmInf 'B01','"+zzdh+"','"+jtbh+"'");
                if (list3!=null){
                    if (list3.size()>0){
                        if (list2.get(0).size()>1){
                            Message msg=handler.obtainMessage();
                            msg.obj=list2;
                            msg.what=0x102;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec  PAD_Get_PzmInf 'B01'",jtbh,sharedPreferences.getString("mac",""));
                }*/

                //不良统计
                List<List<String>>list4= NetHelper.getQuerysqlResult("Exec  PAD_Get_PzmInf 'B02','"+zzdh+"','"+jtbh+"'");
                if (list4!=null){
                    if (list4.size()>0){
                        if (list4.get(0).size()>2){
                            Message msg=handler.obtainMessage();
                            msg.what=0x103;
                            msg.obj=list4;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec  PAD_Get_PzmInf 'B02'",jtbh,sharedPreferences.getString("mac",""));
                }

                //巡查记录
                List<List<String>>list5= NetHelper.getQuerysqlResult("Exec  PAD_Get_PzmInf 'B03','"+zzdh+"','"+jtbh+"'");
                if (list5!=null){
                    if (list5.size()>0){
                        if (list5.get(0).size()>11){
                            Message msg=handler.obtainMessage();
                            msg.what=0x104;
                            msg.obj=list5;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec  PAD_Get_PzmInf 'B03'",jtbh,sharedPreferences.getString("mac",""));
                }

                //从工单信息
                List<List<String>>list6= NetHelper.getQuerysqlResult("Exec  PAD_Get_PzmInf 'G','"+zzdh+"','"+jtbh+"'");
                if (list6!=null){
                    if (list6.size()>0){
                        if (list6.get(0).size()>5){
                            Message msg=handler.obtainMessage();
                            msg.what=0x105;
                            msg.obj=list6;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec  PAD_Get_PzmInf 'G'",jtbh,sharedPreferences.getString("mac",""));
                }



            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(PzglActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.btn_1:
                lineChart.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                layout_4.setVisibility(View.GONE);
                break;
            case R.id.btn_2:
                lineChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                layout_4.setVisibility(View.GONE);
                break;
            case R.id.btn_3:
                lineChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                layout_4.setVisibility(View.GONE);
                break;
            case R.id.btn_4:
                lineChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                layout_4.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
