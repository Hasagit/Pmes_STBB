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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MjxxActivity extends BaseActivity implements View.OnClickListener{
    private Button ok_btn,cancle_btn;
    private ListView listView;
    private SimpleAdapter adapter;
    private TextView mjbh_text,mjmc_text,mjcc_text,mjdw_text,mjqs_text,cxzq_text,mjah_text,cfwz_text,tmcz_text,zrry_text,ptsb_text;
    private String mjbh,zzdh,jtbh;
    private LineChart mDoubleLineChar;
    private Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mjxx);
        initView();
        initDate();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    List<List<String>>list1=( List<List<String>>)msg.obj;
                    mjbh_text.setText(list1.get(0).get(0));
                    mjmc_text.setText(list1.get(0).get(1));
                    mjcc_text.setText(list1.get(0).get(2));
                    mjah_text.setText(list1.get(0).get(3));
                    mjdw_text.setText(list1.get(0).get(4));
                    mjqs_text.setText(list1.get(0).get(5));
                    cxzq_text.setText(list1.get(0).get(6));
                    cfwz_text.setText(list1.get(0).get(7));
                    tmcz_text.setText(list1.get(0).get(8));
                    zrry_text.setText(list1.get(0).get(9));
                    ptsb_text.setText(list1.get(0).get(10));
                    break;
                case 0x101:
                    //Toast.makeText(MjxxActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x102:
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    List<Map<String,String>>listview_data=new ArrayList<>();
                    for (int i=0;i<list.size();i++){
                        List<String>items=list.get(i);
                        Map<String,String>map=new HashMap<>();
                        map.put("lab_1",items.get(0));
                        map.put("lab_2",items.get(1));
                        map.put("lab_3",items.get(2));
                        listview_data.add(map);
                    }
                    initListView(listview_data);
                    listView.startAnimation(anim);
                    break;
                case 0x103:
                    List<List<String>>list_scdt=(List<List<String>>)msg.obj;
                    initLineChar(list_scdt);
                    mDoubleLineChar.startAnimation(anim);
                    break;
                default:
                    break;
            }
        }
    };


    private void initDate(){
        mjbh=sharedPreferences.getString("mjbh","");
        zzdh=sharedPreferences.getString("zzdh","");
        jtbh=sharedPreferences.getString("jtbh","");
        anim= AnimationUtils.loadAnimation(this,R.anim.apha_anim);
        getNetData();
    }


    private void initView(){
        mjbh_text=(TextView)findViewById(R.id.mjbh);
        mjmc_text=(TextView)findViewById(R.id.mjmc);
        mjcc_text=(TextView)findViewById(R.id.mjgg);
        mjdw_text=(TextView)findViewById(R.id.dw);
        mjqs_text=(TextView)findViewById(R.id.xs);
        cxzq_text=(TextView)findViewById(R.id.cxsj);
        mjah_text=(TextView)findViewById(R.id.mjah);
        cfwz_text=(TextView)findViewById(R.id.cwdm);
        tmcz_text=(TextView)findViewById(R.id.tmcz);
        zrry_text=(TextView)findViewById(R.id.zrry);
        ptsb_text=(TextView)findViewById(R.id.mjsb);
        ok_btn=(Button)findViewById(R.id.ok_btn);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        listView=(ListView)findViewById(R.id.list_b2);
        mDoubleLineChar=(LineChart)findViewById(R.id.lineChart);
        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
    }


    private void initListView(List<Map<String,String>>listview_data){
        adapter=new SimpleAdapter(this,listview_data,R.layout.list_item_b2,new String[]{"lab_1","lab_2","lab_3"},
                new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3});
        listView.setAdapter(adapter);
    }

    private void initLineChar(List<List<String>>lists){
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
        setData(lists);
        //默认x动画
        mDoubleLineChar.animateX(2500);

        //获得数据
        Legend l = mDoubleLineChar.getLegend();

        //修改
        l.setEnabled(false);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(17f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        //x轴
        XAxis xAxis = mDoubleLineChar.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index=(int)value;
                return index+"";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });


        //左边y轴
        YAxis leftAxis = mDoubleLineChar.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setAxisMaximum(6000000);
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

    private void setData(List<List<String>>lists) {
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for (int i = 0; i < lists.size(); i++) {
            List<String>item=lists.get(i);
            yVals2.add(new Entry(i,Float.parseFloat(item.get(1))));
            yVals3.add(new Entry(i,Float.parseFloat(item.get(0))));
        }

        /*for (int i = 0; i < count - 1; i++) {
            float mult = range;
            float val = (float) (Math.random() * mult) + 450;
            yVals2.add(new Entry(i, val));
        }

        for (int i = 0; i < count; i++) {
            float mult = range;
            float val = (float) (Math.random() * mult) + 500;
            yVals3.add(new Entry(i, val));
        }*/

        LineDataSet set2, set3;

        if (mDoubleLineChar.getData() != null &&
                mDoubleLineChar.getData().getDataSetCount() > 0) {
            set2 = (LineDataSet) mDoubleLineChar.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mDoubleLineChar.getData().getDataSetByIndex(2);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mDoubleLineChar.getData().notifyDataChanged();
            mDoubleLineChar.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
           /* set1 = new LineDataSet(yVals1, "鸡群");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.isDrawValuesEnabled();
            set1.setColor(ColorTemplate.getHoloBlue());
            //set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);*/

            //创建一个数据集,并给它一个类型
            set2 = new LineDataSet(yVals2, "实际成型时间");
            //set1.isDrawValuesEnabled();
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(getResources().getColor(R.color.colorPrimary));
            set2.setCircleColor(getResources().getColor(R.color.touming));
            set2.setLineWidth(3f);
            //set2.setCircleRadius(3f);
            //set2.setFillAlpha(65);
            //set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            //set2.setHighLightColor(Color.rgb(244, 117, 117));

            set3 = new LineDataSet(yVals3, "标准成型时间");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(getResources().getColor(R.color.color_7));
            //set1.isDrawValuesEnabled();
            set3.setCircleColor(getResources().getColor(R.color.touming));
            set3.setLineWidth(3f);
            //set3.setCircleRadius(3f);
            //set3.setFillAlpha(65);
            //set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
            set3.setDrawCircleHole(false);
            //set3.setHighLightColor(Color.rgb(244, 117, 117));

            // 创建一个数据集的数据对象
            LineData data = new LineData( set2, set3);
            data.setValueTextColor(getResources().getColor(R.color.touming));
            mDoubleLineChar.getXAxis().setLabelCount(yVals2.size());
            //设置数据
            mDoubleLineChar.setData(data);
        }
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(MjxxActivity.this);
        switch (v.getId()) {
            case R.id.cancle_btn:
                finish();
                break;
            case R.id.ok_btn:
                finish();
                break;
            default:
                break;
        }
    }

    private void getNetData(){

        //模具信息线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list1=NetHelper.getQuerysqlResult("Exec PAD_Get_MjmMstr '"+mjbh+"'");
                if (list1!=null){
                    if (list1.size()>0){
                        if (list1.get(0).size()>10){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            msg.obj=list1;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_MjmMstr",jtbh,
                            sharedPreferences.getString("mac",""));
                }

                //生产动态线程
                List<List<String>>list2=NetHelper.getQuerysqlResult("Exec PAD_Get_MjmRzm '"+zzdh+"','"+mjbh+"'");
                if (list2!=null){
                    if (list2.size()>0){
                        if (list2.get(0).size()>2){
                            Message msg=handler.obtainMessage();
                            msg.what=0x103;
                            msg.obj=list2;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_MjmRzm",jtbh,
                            sharedPreferences.getString("mac",""));
                }


                //生产履历线程
                List<List<String>>list3=NetHelper.getQuerysqlResult("Exec PAD_Get_MjmHml '"+mjbh+"'");
                if (list3!=null){
                    if (list3.size()>0){
                        if (list3.get(0).size()>2){
                            Message msg=handler.obtainMessage();
                            msg.what=0x102;
                            msg.obj=list3;
                            handler.sendMessage(msg);
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("PAD_Get_MjmHml",jtbh,sharedPreferences.getString("mac",""));
                }



            }
        }).start();



    }


}
