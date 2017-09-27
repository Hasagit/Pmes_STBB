package com.ruiduoyi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.BlYyfxActivity;
import com.ruiduoyi.activity.Dialog.DialogSgjlActivity;
import com.ruiduoyi.activity.Dialog.SbxxActivity;
import com.ruiduoyi.activity.MjxxActivity;
import com.ruiduoyi.activity.PzglActivity;
import com.ruiduoyi.activity.ScrzActivity;
import com.ruiduoyi.activity.OeeActivity;
import com.ruiduoyi.activity.Dialog.DialogGActivity;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class StatusFragment extends Fragment implements View.OnClickListener{
    private CardView cardView_hm,cardView_tiaoji,cardView_hs,cardView_sj,cardView_pzyc,cardView_xm,
            cardView_jtwx,cardView_tingji,cardView_dl,cardView_sm,cardView_hl,cardView_by,cardView_dr,
            cardView_jhtj,cardView_gdzt,cardView_rysg,cardView_pzxj,cardView_js,cardView_ts,cardView_hj,cardView_jshj,
            cardView_gdgl,cardView_blfx,cardView_ycfx,cardView_pzgl,cardView_sgjl,cardView_xsbg,
            cardView_sbxx,cardView_mjxx,cardView_scrz,cardView_oee;
    private Animation anim;
    private SharedPreferences sharedPreferences;
    private PopupDialog dialog;
    private ProgressBar wait_progress;
    public StatusFragment() {

    }


    public static StatusFragment newInstance() {
        StatusFragment fragment = new StatusFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anim= AnimationUtils.loadAnimation(getContext(),R.anim.scale_anim);
        sharedPreferences=getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_status, container, false);
        initView(view);
        return view;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    wait_progress.setVisibility(View.GONE);
                    break;
                case 0x101:
                    wait_progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"网络异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x102:
                    wait_progress.setVisibility(View.GONE);
                    break;
            }
        }
    };



    public void initView(View view){
        cardView_sbxx=(CardView)view.findViewById(R.id.sbxx);
        cardView_mjxx=(CardView)view.findViewById(R.id.mjxx);
        cardView_gdgl=(CardView)view.findViewById(R.id.gdgl);
        cardView_pzgl=(CardView)view.findViewById(R.id.pzgl);
        cardView_scrz=(CardView)view.findViewById(R.id.scrz);
        cardView_ycfx=(CardView)view.findViewById(R.id.ycfx);
        cardView_blfx=(CardView)view.findViewById(R.id.blfx);
        cardView_oee=(CardView)view.findViewById(R.id.oee);
        cardView_xsbg=(CardView)view.findViewById(R.id.jtjqsbg);
        cardView_sgjl=(CardView)view.findViewById(R.id.sgjl);

        cardView_hm=(CardView)view.findViewById(R.id.hm);
        cardView_tiaoji=(CardView)view.findViewById(R.id.tiaoji);
        cardView_hs=(CardView)view.findViewById(R.id.hs);
        cardView_sj=(CardView)view.findViewById(R.id.sj);
        cardView_pzyc=(CardView)view.findViewById(R.id.pzyc);
        cardView_xm=(CardView)view.findViewById(R.id.xm);
        cardView_jtwx=(CardView)view.findViewById(R.id.jtwx);
        cardView_tingji=(CardView)view.findViewById(R.id.tingji);
        cardView_dl=(CardView)view.findViewById(R.id.dl);
        cardView_sm=(CardView)view.findViewById(R.id.sm);
        cardView_hl=(CardView)view.findViewById(R.id.hl);
        cardView_by=(CardView)view.findViewById(R.id.by);
        cardView_dr=(CardView)view.findViewById(R.id.dr);
        cardView_jhtj=(CardView)view.findViewById(R.id.jhtj);
        cardView_gdzt=(CardView)view.findViewById(R.id.gdzt);
        cardView_rysg=(CardView)view.findViewById(R.id.rysg);
        cardView_pzxj=(CardView)view.findViewById(R.id.pgxj) ;
        cardView_ts=(CardView)view.findViewById(R.id.ts);
        cardView_js=(CardView)view.findViewById(R.id.js) ;
        cardView_hj=(CardView)view.findViewById(R.id.hj);
        cardView_jshj=(CardView)view.findViewById(R.id.jshj);
        wait_progress=(ProgressBar)view.findViewById(R.id.wait_progress);


        cardView_hj.setOnClickListener(this);
        cardView_jshj.setOnClickListener(this);
        cardView_sbxx.setOnClickListener(this);
        cardView_mjxx.setOnClickListener(this);
        cardView_gdgl.setOnClickListener(this);
        cardView_pzgl.setOnClickListener(this);
        cardView_scrz.setOnClickListener(this);
        cardView_ycfx.setOnClickListener(this);
        cardView_blfx.setOnClickListener(this);
        cardView_oee.setOnClickListener(this);
        cardView_xsbg.setOnClickListener(this);
        cardView_sgjl.setOnClickListener(this);

        cardView_hm.setOnClickListener(this);
        cardView_tiaoji.setOnClickListener(this);
        cardView_hs.setOnClickListener(this);
        cardView_sj.setOnClickListener(this);
        cardView_pzyc.setOnClickListener(this);
        cardView_xm.setOnClickListener(this);
        cardView_jtwx.setOnClickListener(this);
        cardView_tingji.setOnClickListener(this);
        cardView_dl.setOnClickListener(this);
        cardView_sm.setOnClickListener(this);
        cardView_hl.setOnClickListener(this);
        cardView_by.setOnClickListener(this);
        cardView_dr.setOnClickListener(this);
        cardView_jhtj.setOnClickListener(this);
        cardView_gdzt.setOnClickListener(this);
        cardView_rysg.setOnClickListener(this);
        cardView_pzxj.setOnClickListener(this);
        cardView_ts.setOnClickListener(this);
        cardView_js.setOnClickListener(this);
        dialog=new PopupDialog(getActivity(),400,350);
        dialog.setTitle("提示");
        dialog.getCancle_btn().setVisibility(View.GONE);
        dialog.getOkbtn().setText("确定");
        dialog.getOkbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setMessage("正在指令状态下，请先结束指令");
        /*dialog=new AppDialog(getActivity());
        dialog.setTitle("提示");
        dialog.setOkbtn("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setMessage("正在指令状态下，请先结束指令");*/
    }

    private void startActivityByNetResult(final String zldm, final String title, final String type){
        wait_progress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'A','"+sharedPreferences.getString("jtbh","")+"','"+zldm+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>2){
                            Message msg=handler.obtainMessage();
                            msg.obj=list.get(0);
                            msg.what=0x100;
                            handler.sendMessage(msg);
                            Intent intent;
                            switch (list.get(0).get(2)){
                                case "A":
                                    intent=new Intent(getContext(), BlYyfxActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    startActivity(intent);
                                    break;
                                case "B":
                                    intent=new Intent(getContext(), DialogGActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    break;
                                case "C":
                                    intent=new Intent(getContext(), DialogGActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    break;
                                default:
                                    intent=new Intent(getContext(), DialogGActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    }
                }else {
                    handler.sendEmptyMessage(0x101);
                }*/
                try {
                    JSONArray list= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_ZlmYywh 'A','"+sharedPreferences.getString("jtbh","")+"','"+zldm+"'");
                    if (list!=null){
                        if (list.length()>0){
                            Message msg=handler.obtainMessage();
                            msg.what=0x100;
                            handler.sendMessage(msg);
                            Intent intent;
                            switch (list.getJSONObject(0).getString("v_yytype")){
                                case "A":
                                    intent=new Intent(getContext(), BlYyfxActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    startActivity(intent);
                                    break;
                                case "B":
                                    intent=new Intent(getContext(), DialogGActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    break;
                                case "C":
                                    intent=new Intent(getContext(), DialogGActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    break;
                                default:
                                    intent=new Intent(getContext(), DialogGActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    }else {
                        handler.sendEmptyMessage(0x101);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private boolean isReady(){
        String zldming=sharedPreferences.getString("zldm_ss","");
        if (zldming.equals("50")|zldming.equals("51")|zldming.equals("52")|zldming.equals("53")|zldming.equals("54")|
                zldming.equals("55")|zldming.equals("56")|zldming.equals("57")|zldming.equals("58")|    // zldming.equals("59")|
                zldming.equals("60")|zldming.equals("61")|zldming.equals("62")|
                zldming.equals("63")|zldming.equals("64")|zldming.equals("65")|     // zldming.equals("66")|
                zldming.equals("67")|zldming.equals("68")|zldming.equals("69")|zldming.equals("70")){
            dialog.show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        AppUtils.sendCountdownReceiver(getContext());
        switch (v.getId()){
            case R.id.sbxx: //设备信息
                cardView_sbxx.startAnimation(anim);
                Intent intent=new Intent(getContext(), SbxxActivity.class);
                startActivity(intent);
                break;
            case R.id.mjxx:     // 模具信息
                cardView_mjxx.startAnimation(anim);
                Intent intent_b2=new Intent(getContext(), MjxxActivity.class);
                startActivity(intent_b2);
                break;
            case R.id.gdgl:     // 工单管理
                cardView_gdgl.startAnimation(anim);
                Intent intent_b3=new Intent(getContext(),DialogGActivity.class );
                intent_b3.putExtra("title","工单管理");
                intent_b3.putExtra("zldm",getContext().getString(R.string.gdgl));
                intent_b3.putExtra("type","DOC");
                startActivity(intent_b3);
                break;
            case R.id.pzgl:         // 品质管理
                cardView_pzgl.startAnimation(anim);
                Intent intent_b4=new Intent(getContext(), PzglActivity.class);
                startActivity(intent_b4);
                break;
            case R.id.scrz:         // 生产日志
                cardView_scrz.startAnimation(anim);
                Intent intent_b6=new Intent(getContext(), ScrzActivity.class);
                startActivity(intent_b6);
                break;
            case R.id.ycfx:     // 异常分析
                cardView_ycfx.startAnimation(anim);
                Intent intent_b7=new Intent(getContext(),DialogGActivity.class );
                intent_b7.putExtra("title","异常分析");
                intent_b7.putExtra("zldm",getContext().getString(R.string.ycfx));
                intent_b7.putExtra("type","DOC");
                startActivity(intent_b7);
                break;
            case R.id.blfx:     //  不良分析
                cardView_blfx.startAnimation(anim);
                Intent intent_b8=new Intent(getContext(), DialogGActivity.class);
                intent_b8.putExtra("title","不良分析");
                intent_b8.putExtra("zldm",getContext().getString(R.string.blfx));
                intent_b8.putExtra("type","DOC");
                startActivity(intent_b8);
                break;
            case R.id.oee:      // OEE分析
                cardView_oee.startAnimation(anim);
                Intent intent_b9=new Intent(getContext(), OeeActivity.class);
                startActivity(intent_b9);
                break;
            case R.id.jtjqsbg:      // 穴数变更
                cardView_xsbg.startAnimation(anim);
                Intent intent_10=new Intent(getContext(), DialogGActivity.class);
                intent_10.putExtra("title","穴数变更");
                intent_10.putExtra("zldm",getResources().getString(R.string.jtjqsbg));
                intent_10.putExtra("type","DOC");
                startActivity(intent_10);
                break;
            case R.id.sgjl:
                cardView_sgjl.startAnimation(anim);
                Intent intent_sgjl=new Intent(getContext(), DialogSgjlActivity.class);
                startActivity(intent_sgjl);
                break;
            case R.id.jshj:
                cardView_jshj.startAnimation(anim);
                Intent intent_jshj=new Intent(getContext(),DialogGActivity.class);
                intent_jshj.putExtra("title","结束呼叫");
                startActivity(intent_jshj);
                break;
            case R.id.hj:
                cardView_hj.startAnimation(anim);
                Intent intent_hj=new Intent(getContext(),DialogGActivity.class);
                intent_hj.putExtra("title","呼叫");
                startActivity(intent_hj);
                break;



            case  R.id.hm:
                if (isReady()){
                    cardView_hm.startAnimation(anim);
                    startActivityByNetResult("50",getContext().getString(R.string.zlmc_50),"OPR");
                }
                break;
            case  R.id.tiaoji:
                if (isReady()){
                    cardView_tiaoji.startAnimation(anim);
                    startActivityByNetResult("51",getContext().getString(R.string.zlmc_51),"OPR");
                }
                break;
            case  R.id.hs:
                if (isReady()){
                    cardView_hs.startAnimation(anim);
                    startActivityByNetResult("52",getContext().getString(R.string.zlmc_52),"OPR");
                }
                break;
            case  R.id.sj:
                if (isReady()){
                    cardView_sj.startAnimation(anim);
                    startActivityByNetResult("53",getContext().getString(R.string.zlmc_53),"OPR");
                }
                break;
            case  R.id.pzyc:
                if (isReady()){
                    cardView_pzyc.startAnimation(anim);
                    startActivityByNetResult("54",getContext().getString(R.string.zlmc_54),"OPR");
                }
                break;
            case  R.id.xm:
                if (isReady()){
                    cardView_xm.startAnimation(anim);
                    startActivityByNetResult("55",getContext().getString(R.string.zlmc_55),"OPR");
                }
                break;
            case  R.id.jtwx:
                if (isReady()){
                    cardView_jtwx.startAnimation(anim);
                    startActivityByNetResult("56",getContext().getString(R.string.zlmc_56),"OPR");
                }
                break;
            case  R.id.tingji:
                if (isReady()){
                    cardView_tingji.startAnimation(anim);
                    startActivityByNetResult("57",getContext().getString(R.string.zlmc_57),"OPR");
                }
                break;
            case  R.id.dl:
                if (isReady()){
                    cardView_dl.startAnimation(anim);
                    startActivityByNetResult("58",getContext().getString(R.string.zlmc_58),"OPR");
                }
                break;
            case  R.id.sm:
                if (isReady()){
                    cardView_sm.startAnimation(anim);
                    startActivityByNetResult("60",getContext().getString(R.string.zlmc_60),"OPR");
                }
                break;
            case  R.id.hl:
                if (isReady()){
                    cardView_hl.startAnimation(anim);
                    startActivityByNetResult("61",getContext().getString(R.string.zlmc_61),"OPR");
                }
                break;
            case  R.id.by:
                if (isReady()){
                    cardView_by.startAnimation(anim);
                    startActivityByNetResult("62",getContext().getString(R.string.zlmc_62),"OPR");
                }
                break;
            case  R.id.dr:
                if (isReady()){
                    cardView_dr.startAnimation(anim);
                    startActivityByNetResult("63",getContext().getString(R.string.zlmc_63),"OPR");
                }
                break;
            case  R.id.jhtj:
                if (isReady()){
                    cardView_jhtj.startAnimation(anim);
                    startActivityByNetResult("64",getContext().getString(R.string.zlmc_64),"OPR");
                }
                break;
            case  R.id.gdzt:
                if (isReady()){
                    cardView_gdzt.startAnimation(anim);
                    startActivityByNetResult("65",getContext().getString(R.string.zlmc_65),"OPR");
                }
                break;
            case R.id.rysg:
                cardView_rysg.startAnimation(anim);
                startActivityByNetResult("*",getContext().getString(R.string.zlmc_x),"OPR");
                break;
            case R.id.pgxj:
                cardView_pzxj.startAnimation(anim);
                //startActivityByNetResult(getContext().getString(R.string.pgxj),getContext().getString(R.string.zlmc_xj),"OPR");
                Intent intent_pzxj=new Intent(getContext(), DialogGActivity.class);
                intent_pzxj.putExtra("title","品质巡机");
                startActivity(intent_pzxj);
                break;
            case R.id.ts:
                cardView_ts.startAnimation(anim);
                startActivityByNetResult("67",getContext().getString(R.string.zlmc_67),"OPR");
                break;
            case R.id.js:
                cardView_js.startAnimation(anim);
                jsBtnEven();
                break;
        }
    }

    private void jsBtnEven(){
        wait_progress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*List<List<String>>list2= NetHelper.getQuerysqlResult("Exec PAD_Get_JtmZtInfo '"+sharedPreferences.getString("jtbh","")+"'");
                if(list2!=null){
                    handler.sendEmptyMessage(0x102);
                    if (list2.size()>0){
                        if (list2.get(0).size()>11){
                            String zldm_ss=list2.get(0).get(1);
                            String zlmc=getZlmcByZldm(zldm_ss);
                            String waring=list2.get(0).get(5);
                            if (waring.equals("1")){//如果超时了则必须要弹出蓝框
                                Intent intent_blyyfx=new Intent(getContext(),BlYyfxActivity.class);
                                intent_blyyfx.putExtra("title",zlmc);
                                intent_blyyfx.putExtra("zldm",zldm_ss);
                                startActivity(intent_blyyfx);
                            }else {//如果没有超时则根据启动类型来判断
                                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'A','"+sharedPreferences.getString("jtbh","")+"','"+zldm_ss+"'");
                                if (list!=null){
                                    if (list.size()>0){
                                        if (list.get(0).size()>2){
                                            String startType=list.get(0).get(2);
                                            switch (startType){
                                                case "A":
                                                    Intent intent_g21=new Intent(getContext(), DialogGActivity.class);
                                                    intent_g21.putExtra("zldm",getContext().getString(R.string.js));
                                                    intent_g21.putExtra("title","结束");
                                                    intent_g21.putExtra("type","OPR");
                                                    startActivity(intent_g21);
                                                    break;
                                                case "B":
                                                    Intent intent1=new Intent(getContext(), BlYyfxActivity.class);
                                                    intent1.putExtra("title",zlmc);
                                                    intent1.putExtra("zldm",zldm_ss);
                                                    startActivity(intent1);
                                                    break;
                                                case "C":
                                                    Intent intent2=new Intent(getContext(), BlYyfxActivity.class);
                                                    intent2.putExtra("title",zlmc);
                                                    intent2.putExtra("zldm",zldm_ss);
                                                    startActivity(intent2);
                                                    break;
                                                default:
                                                    Intent intent_g3=new Intent(getContext(), DialogGActivity.class);
                                                    intent_g3.putExtra("zldm",getContext().getString(R.string.js));
                                                    intent_g3.putExtra("title","结束");
                                                    intent_g3.putExtra("type","OPR");
                                                    startActivity(intent_g3);
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else {
                    NetHelper.uploadNetworkError("Exec PAD_Get_JtmZtInfo NetWordError",sharedPreferences.getString("jtbh","")
                            ,sharedPreferences.getString("mac",""));
                    handler.sendEmptyMessage(0x101);
                    //handler.sendEmptyMessage(0x110);
                }*/
                try {
                    JSONArray list2= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_JtmZtInfo '"+sharedPreferences.getString("jtbh","")+"'");
                    if(list2!=null){
                        handler.sendEmptyMessage(0x102);
                        if (list2.length()>0){
                            String zldm_ss=list2.getJSONObject(0).getString("kbl_zldm");
                            String zlmc=getZlmcByZldm(zldm_ss);
                            String waring=list2.getJSONObject(0).getString("kbl_waring");
                            if (waring.equals("1")){//如果超时了则必须要弹出蓝框
                                Intent intent_blyyfx=new Intent(getContext(),BlYyfxActivity.class);
                                intent_blyyfx.putExtra("title",zlmc);
                                intent_blyyfx.putExtra("zldm",zldm_ss);
                                startActivity(intent_blyyfx);
                            }else {//如果没有超时则根据启动类型来判断
                                JSONArray list= NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_ZlmYywh 'A','"+sharedPreferences.getString("jtbh","")+"','"+zldm_ss+"'");
                                if (list!=null){
                                    if (list.length()>0){
                                        String startType=list.getJSONObject(0).getString("v_yytype");
                                        switch (startType){
                                            case "A":
                                                Intent intent_g21=new Intent(getContext(), DialogGActivity.class);
                                                intent_g21.putExtra("zldm",getContext().getString(R.string.js));
                                                intent_g21.putExtra("title","结束");
                                                intent_g21.putExtra("type","OPR");
                                                startActivity(intent_g21);
                                                break;
                                            case "B":
                                                Intent intent1=new Intent(getContext(), BlYyfxActivity.class);
                                                intent1.putExtra("title",zlmc);
                                                intent1.putExtra("zldm",zldm_ss);
                                                startActivity(intent1);
                                                break;
                                            case "C":
                                                Intent intent2=new Intent(getContext(), BlYyfxActivity.class);
                                                intent2.putExtra("title",zlmc);
                                                intent2.putExtra("zldm",zldm_ss);
                                                startActivity(intent2);
                                                break;
                                            default:
                                                Intent intent_g3=new Intent(getContext(), DialogGActivity.class);
                                                intent_g3.putExtra("zldm",getContext().getString(R.string.js));
                                                intent_g3.putExtra("title","结束");
                                                intent_g3.putExtra("type","OPR");
                                                startActivity(intent_g3);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        NetHelper.uploadNetworkError("Exec PAD_Get_JtmZtInfo NetWordError",sharedPreferences.getString("jtbh","")
                                ,sharedPreferences.getString("mac",""));
                        handler.sendEmptyMessage(0x101);
                        //handler.sendEmptyMessage(0x110);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private String getZlmcByZldm(String zldm){
        switch (zldm){
            case "50":
                return getResources().getString(R.string.zlmc_50);
            case "51":
                return getResources().getString(R.string.zlmc_51);
            case "52":
                return getResources().getString(R.string.zlmc_52);
            case "53":
                return getResources().getString(R.string.zlmc_53);
            case "54":
                return getResources().getString(R.string.zlmc_54);
            case "55":
                return getResources().getString(R.string.zlmc_55);
            case "56":
                return getResources().getString(R.string.zlmc_56);
            case "57":
                return getResources().getString(R.string.zlmc_57);
            case "58":
                return getResources().getString(R.string.zlmc_58);
            case "59":
                return getResources().getString(R.string.zlmc_59);
            case "60":
                return getResources().getString(R.string.zlmc_60);
            case "61":
                return getResources().getString(R.string.zlmc_61);
            case "62":
                return getResources().getString(R.string.zlmc_62);
            case "63":
                return getResources().getString(R.string.zlmc_63);
            case "64":
                return getResources().getString(R.string.zlmc_64);
            case "65":
                return getResources().getString(R.string.zlmc_65);
            case "66":
                return getResources().getString(R.string.zlmc_66);
            case "67":
                return getResources().getString(R.string.zlmc_67);
            case "68":
                return getResources().getString(R.string.zlmc_68);
            case "69":
                return getResources().getString(R.string.zlmc_69);
            case "70":
                return getResources().getString(R.string.zlmc_70);
        }
        return "";
    }


}



