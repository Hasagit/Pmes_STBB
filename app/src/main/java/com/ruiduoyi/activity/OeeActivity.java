package com.ruiduoyi.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ruiduoyi.R;
import com.ruiduoyi.adapter.OeeAdapter;
import com.ruiduoyi.model.NetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OeeActivity extends BaseActivity implements View.OnClickListener{
    private Button cancle_btn;
    private PieChart p_chart;
    private HorizontalBarChart h_char;
    private String jtbh;
    private ListView listView;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oee);
        initView();
        initData();
    }


    private void initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        p_chart=(PieChart)findViewById(R.id.p_chart);
        h_char=(HorizontalBarChart)findViewById(R.id.h_barchart);
        listView=(ListView)findViewById(R.id.list_oee);
        cancle_btn.setOnClickListener(this);

    }

    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        thread_oee1.start();
        thread_oee2.start();
        thread_oee3.start();
        animation= AnimationUtils.loadAnimation(this,R.anim.apha_anim);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    if(list.size()<1){
                        Toast.makeText(OeeActivity.this,"数据异常",Toast.LENGTH_SHORT).show();
                    }else {
                        initHorizontalBarChat(h_char,list);
                        h_char.startAnimation(animation);
                    }
                    break;
                case 0x101:
                    break;
                case 0x102:
                    List<List<String>>list2=(List<List<String>>)msg.obj;
                    if(list2.size()<1){
                        Toast.makeText(OeeActivity.this,"数据异常",Toast.LENGTH_SHORT).show();
                    }else {
                        initPieChat(p_chart,list2);
                        p_chart.startAnimation(animation);
                    }
                    break;
                case 0x103:
                    List<List<String>>list3=(List<List<String>>)msg.obj;
                    if(list3.size()<1){
                        Toast.makeText(OeeActivity.this,"数据异常",Toast.LENGTH_SHORT).show();
                    }else {
                        initListView(list3);
                        listView.startAnimation(animation);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //初始化水平柱形图
    private void initHorizontalBarChat(HorizontalBarChart mHorizontalBarChart,List<List<String>>list){
        //正负堆叠条形图

        //mHorizontalBarChart.setOnChartValueSelectedListener(this);
        // 扩展现在只能分别在x轴和y轴
        mHorizontalBarChart.setPinchZoom(false);
        mHorizontalBarChart.setBackgroundColor(getResources().getColor(R.color.color_9));
        mHorizontalBarChart.setTouchEnabled(false);
        mHorizontalBarChart.setDrawBarShadow(false);
        mHorizontalBarChart.setDrawValueAboveBar(false);
        mHorizontalBarChart.setHighlightFullBarEnabled(false);

        mHorizontalBarChart.getAxisLeft().setEnabled(true);
        mHorizontalBarChart.getAxisLeft().setAxisMaximum(25f);
        mHorizontalBarChart.getAxisLeft().setAxisMinimum(0f);
        mHorizontalBarChart.getAxisLeft().setZeroLineWidth(5f);
        mHorizontalBarChart.getAxisRight().setAxisMaximum(25f);
        mHorizontalBarChart.getAxisRight().setAxisMinimum(0f);
        mHorizontalBarChart.getAxisRight().setDrawGridLines(true);
        mHorizontalBarChart.getAxisRight().setDrawZeroLine(true);
        mHorizontalBarChart.getAxisRight().setLabelCount(7, false);
        mHorizontalBarChart.getAxisRight().setTextSize(9f);

        XAxis xAxis = mHorizontalBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(20f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value==1.5){
                    return "A02";
                }
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setTextSize(9f);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(3);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisLineColor(Color.WHITE);

        // 重要:当使用负值在堆叠酒吧,总是确保-值数组中的第一个
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        float[] floats=new float[list.size()];
        String[] color=new String[list.size()];
        for (int i=0;i<list.size();i++){
            List<String>item=list.get(i);
            floats[i]=Float.parseFloat(item.get(5));
            color[i]=item.get(6);
        }
        yValues.add(new BarEntry(2, floats));

        BarDataSet set = new BarDataSet(yValues, "时间片分析");
        //set.setValueFormatter(new CustomFormatter());
        set.setValueTextSize(7f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColors(traToColor(color));
        set.setStackLabels(new String[color.length]);
        set.setFormLineWidth(10f);
        BarData data = new BarData(set);
        data.setBarWidth(2.5f);
        mHorizontalBarChart.setData(data);
        mHorizontalBarChart.animateY(2500, Easing.EasingOption.EaseInOutQuad);
        //mHorizontalBarChart.invalidate();
    }


    //初始化饼形图
    private void initPieChat(PieChart mPieChart,List<List<String>>list){
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setUsePercentValues(true);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        mPieChart.setDrawSliceText(false);
        //绘制中间文字
        //mPieChart.setCenterText(generateCenterSpannableText());
        //mPieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        //mPieChart.setDrawHoleEnabled(true);
        //mPieChart.setHoleColor(Color.WHITE);

        //mPieChart.setTransparentCircleColor(Color.WHITE);
        //mPieChart.setTransparentCircleAlpha(110);

        //mPieChart.setHoleRadius(58f);
        //mPieChart.setTransparentCircleRadius(61f);

        //mPieChart.setDrawCenterText(true);

        //mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);


        // 添加一个选择监听器
        //mPieChart.setOnChartValueSelectedListener(this);

        //模拟数据
        String[] color=new String[list.size()];
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for(int i=0;i<list.size();i++){
            List<String>item=list.get(i);
            color[i]=item.get(1);
            entries.add(new PieEntry(Float.parseFloat(item.get(3)),""));
        }
        /*entries.add(new PieEntry(40, "优秀"));
        entries.add(new PieEntry(20, "满分"));
        entries.add(new PieEntry(30, "及格"));
        entries.add(new PieEntry(10, "不及格"));*/
        //设置数据
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(traToColor(color));

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);

        // 撤销所有的亮点
        mPieChart.highlightValues(null);

        mPieChart.invalidate();

        //默认动画
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }


    //初始化列表
    private void initListView(List<List<String>>lists_3){
        List<Map<String,String>>data=new ArrayList<>();
        for (int i=0;i<lists_3.size();i++){
            List<String>item=lists_3.get(i);
            Map<String,String>map=new HashMap<>();
            map.put("lab_1",item.get(1));
            map.put("lab_2",item.get(2));
            map.put("lab_3",item.get(3));
            map.put("lab_4",item.get(4));
            map.put("lab_5",item.get(5));
            map.put("color",item.get(6));
            data.add(map);
        }
        OeeAdapter adapter=new OeeAdapter(this,R.layout.list_item_oee,data);
        listView.setAdapter(adapter);

    }


    //根据字符串返回相对应的颜色
    private int[] traToColor(String[] str){
        int[] color=new int[str.length];
        for (int i=0;i<str.length;i++){
            String temp=str[i];
            if(temp.trim().equals("W")){
                color[i]=Color.WHITE;
            }else if (temp.trim().equals("G")){
                color[i]=Color.GREEN;
            }
            else if (temp.trim().equals("Y")){
                color[i]=Color.YELLOW;
            }else if (temp.trim().equals("N")){
                color[i]=getResources().getColor(R.color.N);
            }
        }
        return color;
    }

    //按键点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle_btn:
                finish();
                break;
            default:
                break;
        }
    }

    //柱状图数据请求
    Thread thread_oee1=new Thread(new Runnable() {
        @Override
        public void run() {
            List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_bod202p1New '"+jtbh+"',1");
            Message msg=handler.obtainMessage();
            if(list!=null){
                msg.what=0x100;
                msg.obj=list;
            }else {
                msg.what=0x101;
            }
            handler.sendMessage(msg);
        }
    });

    //饼图数据请求
    Thread thread_oee2=new Thread(new Runnable() {
        @Override
        public void run() {
            List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_bod202p1New '"+jtbh+"',2");
            Message msg=handler.obtainMessage();
            if(list!=null){
                msg.what=0x102;
                msg.obj=list;
            }else {
                msg.what=0x101;
            }
            handler.sendMessage(msg);
        }
    });

    //表格数据请求
    Thread thread_oee3=new Thread(new Runnable() {
        @Override
        public void run() {
            List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_bod202p1New '"+jtbh+"',3");
            Message msg=handler.obtainMessage();
            if(list!=null){
                msg.what=0x103;
                msg.obj=list;
            }else {
                msg.what=0x101;
            }
            handler.sendMessage(msg);
        }
    });

}
