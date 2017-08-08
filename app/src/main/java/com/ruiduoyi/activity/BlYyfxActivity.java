package com.ruiduoyi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruiduoyi.Fragment.BlfxFragment;
import com.ruiduoyi.Fragment.InfoFragment;
import com.ruiduoyi.Fragment.StatusFragment;
import com.ruiduoyi.Fragment.TestFragment;
import com.ruiduoyi.Fragment.YyfxFragment;
import com.ruiduoyi.R;
import com.ruiduoyi.adapter.ViewPagerAdapter;
import com.ruiduoyi.utils.AppUtils;

public class BlYyfxActivity extends BaseActivity implements View.OnClickListener {
    private TextView sjsx_text,zzdh_text,gddh_text,scph_text,mjbh_text,cpbh_text,pmgg_text,mjmc_text,
                blfx_text,yyfx_text,title_text,jhsl_text,lpsl_text,blpsl_text;
    private String sjsx_str,zzdh_str,gddh_str,scph_str,mjbh_str,cpbh_str,pmgg_str,jhsl_str,lpsl_str,
            blpsl_str;
    private Button cancle_btn,save_btn;
    private FrameLayout blfx_btn,yyfx_btn;
    private String type,zldm,zlmc,mjmc_str;
    private Animation animation;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BlfxFragment blfxFragment;
    private YyfxFragment yyfxFragment;
    private String zldming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_yyfx);
        initData();
        initView();
    }


    private void initView(){
        sjsx_text=(TextView)findViewById(R.id.dq_1);
        zzdh_text=(TextView)findViewById(R.id.dq_2);
        gddh_text=(TextView)findViewById(R.id.dq_3);
        scph_text=(TextView)findViewById(R.id.dq_4);
        mjbh_text=(TextView)findViewById(R.id.dq_5);
        mjmc_text=(TextView)findViewById(R.id.dq_6);
        cpbh_text=(TextView)findViewById(R.id.dq_7);
        pmgg_text=(TextView)findViewById(R.id.dq_8);
        jhsl_text=(TextView)findViewById(R.id.dq_9);
        lpsl_text=(TextView)findViewById(R.id.dq_10);
        blpsl_text=(TextView)findViewById(R.id.dq_11);
        blfx_text=(TextView)findViewById(R.id.blfx_text);
        yyfx_text=(TextView)findViewById(R.id.yyfx_text);
        title_text=(TextView)findViewById(R.id.title);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        save_btn=(Button)findViewById(R.id.save_btn);
        blfx_btn=(FrameLayout) findViewById(R.id.blfx_btn);
        yyfx_btn=(FrameLayout)findViewById(R.id.yyfx_btn);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        blfx_btn.setOnClickListener(this);
        yyfx_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);

        sjsx_text.setText(sjsx_str);
        zzdh_text.setText(zzdh_str);
        gddh_text.setText(gddh_str);
        scph_text.setText(scph_str);
        mjbh_text.setText(mjbh_str);
        cpbh_text.setText(cpbh_str);
        pmgg_text.setText(pmgg_str);
        mjmc_text.setText(mjmc_str);
        jhsl_text.setText(jhsl_str);
        lpsl_text.setText(lpsl_str);
        blpsl_text.setText(blpsl_str);
        title_text.setText(zlmc);

        initViewPager();
    }


    private void initViewPager(){
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(yyfxFragment,"原因分析");
        viewPagerAdapter.addFragment(blfxFragment,"不良分析");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.w("onPageScrolled",position+"   "+positionOffset+"   "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                AppUtils.sendCountdownReceiver(BlYyfxActivity.this);
               switch (position){
                   case 0:
                       yyfx_btn.setBackgroundColor(getResources().getColor(R.color.g_true));
                       yyfx_text.setTextColor(Color.WHITE);
                       blfx_btn.setBackgroundColor(getResources().getColor(R.color.g_false));
                       blfx_text.setTextColor(getResources().getColor(R.color.color_9));
                       break;
                   case 1:
                       blfx_btn.setBackgroundColor(getResources().getColor(R.color.g_true));
                       blfx_text.setTextColor(Color.WHITE);
                       yyfx_btn.setBackgroundColor(getResources().getColor(R.color.g_false));
                       yyfx_text.setTextColor(getResources().getColor(R.color.color_9));
                       break;
               }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    private void initData(){
        sjsx_str=sharedPreferences.getString("sjsx","");
        zzdh_str=sharedPreferences.getString("zzdh","");
        gddh_str=sharedPreferences.getString("gddh","");
        scph_str=sharedPreferences.getString("scph","");
        mjbh_str=sharedPreferences.getString("mjbh","");
        cpbh_str=sharedPreferences.getString("cpbh","");
        pmgg_str=sharedPreferences.getString("pmgg","");
        mjmc_str=sharedPreferences.getString("mjmc","");
        jhsl_str=sharedPreferences.getString("jhsl","");
        lpsl_str=sharedPreferences.getString("lpsl","");
        blpsl_str=sharedPreferences.getString("blsl","");
        zldming=sharedPreferences.getString("zldm_ss","");
        Intent intent_from=getIntent();
        zldm=intent_from.getStringExtra("zldm");
        zlmc=intent_from.getStringExtra("title");
        type=intent_from.getStringExtra("type");
        animation= AnimationUtils.loadAnimation(this,R.anim.scale_anim);
        blfxFragment=new BlfxFragment();
        yyfxFragment=new YyfxFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0){
            String wkno=data.getStringExtra("wkno");
            blfxFragment.upLoadData(wkno);
            yyfxFragment.upLoadData(wkno);
            Intent intent=new Intent();
            intent.setAction("UpdateInfoFragment");
            sendBroadcast(intent);
            if (!(zlmc.equals("结束")|zlmc.equals("人员上岗")|zlmc.equals("品管巡机"))){
                Intent intent2=new Intent();
                intent2.setAction("com.Ruiduoyi.returnToInfoReceiver");
                sendBroadcast(intent2);
            }
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(BlYyfxActivity.this);
        switch (v.getId()){
            case R.id.cancle_btn:
                cancle_btn.startAnimation(animation);
                finish();
                break;
            case R.id.save_btn:
                save_btn.startAnimation(animation);
                if (yyfxFragment.isReady()&&blfxFragment.isReady()){
                    Intent intent=new Intent(BlYyfxActivity.this,DialogGActivity.class);
                    if (zldming.equals("50")|zldming.equals("51")|zldming.equals("52")|zldming.equals("53")|zldming.equals("54")|
                            zldming.equals("55")|zldming.equals("56")|zldming.equals("57")|zldming.equals("58")|
                            zldming.equals("59")|zldming.equals("60")|zldming.equals("61")|zldming.equals("62")|
                            zldming.equals("63")|zldming.equals("64")|zldming.equals("65")|zldming.equals("66")|
                            zldming.equals("67")|zldming.equals("68")|zldming.equals("69")|zldming.equals("70")){
                        intent.putExtra("zldm",getResources().getString(R.string.js));
                        intent.putExtra("title","结束");
                    }else {
                        intent.putExtra("zldm",zldm);
                        intent.putExtra("title",zlmc);
                    }
                    intent.putExtra("type","OPR");
                    intent.putExtra("isFromBlyyfx",true);
                    startActivityForResult(intent,0);
                }
                break;
            case R.id.blfx_btn:
                blfx_btn.setBackgroundColor(getResources().getColor(R.color.g_true));
                blfx_text.setTextColor(Color.WHITE);
                yyfx_btn.setBackgroundColor(getResources().getColor(R.color.g_false));
                yyfx_text.setTextColor(getResources().getColor(R.color.color_9));
                viewPager.setCurrentItem(1);
                break;
            case R.id.yyfx_btn:
                yyfx_btn.setBackgroundColor(getResources().getColor(R.color.g_true));
                yyfx_text.setTextColor(Color.WHITE);
                blfx_btn.setBackgroundColor(getResources().getColor(R.color.g_false));
                blfx_text.setTextColor(getResources().getColor(R.color.color_9));
                viewPager.setCurrentItem(0);
                break;
        }
    }

    public String getZzdh() {
        return zzdh_str;
    }

    public String getZldm() {
        return zldm;
    }

    public String getLpsl_str() {
        return lpsl_str;
    }

    public String getBlpsl_str() {
        return blpsl_str;
    }
}
