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

import com.ruiduoyi.R;
import com.ruiduoyi.activity.BlYyfxActivity;
import com.ruiduoyi.activity.Dialog.SbxxActivity;
import com.ruiduoyi.activity.MjxxActivity;
import com.ruiduoyi.activity.PzglActivity;
import com.ruiduoyi.activity.ScrzActivity;
import com.ruiduoyi.activity.OeeActivity;
import com.ruiduoyi.activity.Dialog.DialogGActivity;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;

import java.util.List;

public class StatusFragment extends Fragment implements View.OnClickListener{
    private CardView cardView_g1,cardView_g2,cardView_g3,cardView_g4,cardView_g5,cardView_g6,
            cardView_g7,cardView_g8,cardView_g9,cardView_g10,cardView_g11,cardView_g12,cardView_g13,
            cardView_g14,cardView_g15,cardView_g16,cardView_g17,cardView_g18,cardView_g19,cardView_g21,cardView_pzxj,
            cardView_b1,cardView_b2,cardView_b3,cardView_b4,cardView_b5,cardView_b6,cardView_b7,
            cardView_b8,cardView_b9;
    private String startType,startZldm,startZlmc;
    private Animation anim;
    private SharedPreferences sharedPreferences;
    private PopupDialog dialog;
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
                    List<String>list=(List<String>)msg.obj;
                    startType=list.get(2);
                    startZldm=list.get(0);
                    startZlmc=list.get(1);
                    break;
                case 0x101:
                    break;
            }
        }
    };


    private void getStartType(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.w("zldm_ss",sharedPreferences.getString("zldm_ss",""));
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'A','"+sharedPreferences.getString("jtbh","")+"','"+sharedPreferences.getString("zldm_ss","")+"'");
                if (list!=null){
                    if (list.size()>0){
                        if (list.get(0).size()>2){
                            String startType=list.get(0).get(2);
                            String startZlmc=list.get(0).get(1);
                            String startZldm=list.get(0).get(0);
                            switch (startType) {
                                case "A":
                                    Intent intent_g21 = new Intent(getContext(), DialogGActivity.class);
                                    intent_g21.putExtra("zldm", getContext().getString(R.string.js));
                                    intent_g21.putExtra("title", "结束");
                                    intent_g21.putExtra("type", "OPR");
                                    startActivity(intent_g21);
                                    break;
                                case "B":
                                    Intent intent1 = new Intent(getContext(), BlYyfxActivity.class);
                                    intent1.putExtra("title", startZlmc);
                                    intent1.putExtra("zldm", startZldm);
                                    startActivity(intent1);
                                    break;
                                case "C":
                                    Intent intent2 = new Intent(getContext(), BlYyfxActivity.class);
                                    intent2.putExtra("title", startZlmc);
                                    intent2.putExtra("zldm", startZldm);
                                    startActivity(intent2);
                                    break;
                                default:
                                    Intent intent_g3 = new Intent(getContext(), DialogGActivity.class);
                                    intent_g3.putExtra("zldm", getContext().getString(R.string.js));
                                    intent_g3.putExtra("title", "结束");
                                    intent_g3.putExtra("type", "OPR");
                                    startActivity(intent_g3);
                                    break;
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void initView(View view){
        cardView_b1=(CardView)view.findViewById(R.id.sbxx);
        cardView_b2=(CardView)view.findViewById(R.id.mjxx);
        cardView_b3=(CardView)view.findViewById(R.id.gdgl);
        cardView_b4=(CardView)view.findViewById(R.id.pzgl);
        cardView_b5=(CardView)view.findViewById(R.id.scrz);
        cardView_b6=(CardView)view.findViewById(R.id.ycfx);
        cardView_b7=(CardView)view.findViewById(R.id.blfx);
        cardView_b8=(CardView)view.findViewById(R.id.oee);
        cardView_b9=(CardView)view.findViewById(R.id.jtjqsbg);

        cardView_g1=(CardView)view.findViewById(R.id.zl50);
        cardView_g2=(CardView)view.findViewById(R.id.zl51);
        cardView_g3=(CardView)view.findViewById(R.id.zl52);
        cardView_g4=(CardView)view.findViewById(R.id.zl53);
        cardView_g5=(CardView)view.findViewById(R.id.zl54);
        cardView_g6=(CardView)view.findViewById(R.id.zl55);
        cardView_g7=(CardView)view.findViewById(R.id.zl56);
        cardView_g8=(CardView)view.findViewById(R.id.zl57);
        cardView_g9=(CardView)view.findViewById(R.id.zl58);
        cardView_g10=(CardView)view.findViewById(R.id.zl60);
        cardView_g11=(CardView)view.findViewById(R.id.zl61);
        cardView_g12=(CardView)view.findViewById(R.id.zl62);
        cardView_g13=(CardView)view.findViewById(R.id.zl63);
        cardView_g14=(CardView)view.findViewById(R.id.zl64);
        cardView_g15=(CardView)view.findViewById(R.id.zl65);
        cardView_g16=(CardView)view.findViewById(R.id.zlx);
//        cardView_g17=(CardView)view.findViewById(R.id.zyg) ;
//        cardView_g18=(CardView)view.findViewById(R.id.cm) ;
//        cardView_g19=(CardView)view.findViewById(R.id.rysg) ;
        cardView_pzxj=(CardView)view.findViewById(R.id.pgxj) ;
        cardView_g21=(CardView)view.findViewById(R.id.zlF3) ;
        cardView_g1.setOnClickListener(this);
        cardView_g2.setOnClickListener(this);
        cardView_g3.setOnClickListener(this);
        cardView_g4.setOnClickListener(this);
        cardView_g5.setOnClickListener(this);
        cardView_g6.setOnClickListener(this);
        cardView_g7.setOnClickListener(this);
        cardView_g8.setOnClickListener(this);
        cardView_g9.setOnClickListener(this);
        cardView_g10.setOnClickListener(this);
        cardView_g11.setOnClickListener(this);
        cardView_g12.setOnClickListener(this);
        cardView_g13.setOnClickListener(this);
        cardView_g14.setOnClickListener(this);
        cardView_g15.setOnClickListener(this);
        cardView_g16.setOnClickListener(this);
//        cardView_g17.setOnClickListener(this);
//        cardView_g18.setOnClickListener(this);
//        cardView_g19.setOnClickListener(this);
        //cardView_g20.setOnClickListener(this);
        cardView_g21.setOnClickListener(this);
        cardView_b1.setOnClickListener(this);
        cardView_b2.setOnClickListener(this);
        cardView_b3.setOnClickListener(this);
        cardView_b4.setOnClickListener(this);
        cardView_b5.setOnClickListener(this);
        cardView_b6.setOnClickListener(this);
        cardView_b7.setOnClickListener(this);
        cardView_b8.setOnClickListener(this);
        cardView_b9.setOnClickListener(this);
        cardView_pzxj.setOnClickListener(this);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list= NetHelper.getQuerysqlResult("Exec PAD_Get_ZlmYywh 'A','"+sharedPreferences.getString("jtbh","")+"','"+zldm+"'");
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
                                    intent=new Intent(getContext(), BlYyfxActivity.class);
                                    intent.putExtra("title",title);
                                    intent.putExtra("zldm",zldm);
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
                cardView_b1.startAnimation(anim);
                Intent intent=new Intent(getContext(), SbxxActivity.class);
                startActivity(intent);
                break;
            case R.id.mjxx:     // 模具信息
                cardView_b2.startAnimation(anim);
                Intent intent_b2=new Intent(getContext(), MjxxActivity.class);
                startActivity(intent_b2);
                break;
            case R.id.gdgl:     // 工单管理
                cardView_b3.startAnimation(anim);
                Intent intent_b3=new Intent(getContext(),DialogGActivity.class );
                intent_b3.putExtra("title","工单管理");
                intent_b3.putExtra("zldm",getContext().getString(R.string.gdgl));
                intent_b3.putExtra("type","DOC");
                startActivity(intent_b3);
                break;
            case R.id.pzgl:         // 品质管理
                cardView_b4.startAnimation(anim);
                Intent intent_b4=new Intent(getContext(), PzglActivity.class);
                startActivity(intent_b4);
                break;
            case R.id.scrz:         // 生产日志
                cardView_b5.startAnimation(anim);
                Intent intent_b6=new Intent(getContext(), ScrzActivity.class);
                startActivity(intent_b6);
                break;
            case R.id.ycfx:     // 异常分析
                cardView_b6.startAnimation(anim);
                Intent intent_b7=new Intent(getContext(),DialogGActivity.class );
                intent_b7.putExtra("title","异常分析");
                intent_b7.putExtra("zldm",getContext().getString(R.string.ycfx));
                intent_b7.putExtra("type","DOC");
                startActivity(intent_b7);
                break;
            case R.id.blfx:     //  不良分析
                cardView_b7.startAnimation(anim);
                Intent intent_b8=new Intent(getContext(), DialogGActivity.class);
                intent_b8.putExtra("title","不良分析");
                intent_b8.putExtra("zldm",getContext().getString(R.string.blfx));
                intent_b8.putExtra("type","DOC");
                startActivity(intent_b8);
                break;
            case R.id.oee:      // OEE分析
                cardView_b8.startAnimation(anim);
                Intent intent_b9=new Intent(getContext(), OeeActivity.class);
                startActivity(intent_b9);
                break;
            case R.id.jtjqsbg:      // 穴数变更
                cardView_b9.startAnimation(anim);
                Intent intent_10=new Intent(getContext(), DialogGActivity.class);
                intent_10.putExtra("title","穴数变更");
                intent_10.putExtra("zldm",getResources().getString(R.string.jtjqsbg));
                intent_10.putExtra("type","DOC");
                startActivity(intent_10);
                break;
            case  R.id.zl50:
                if (isReady()){
                    cardView_g1.startAnimation(anim);
                    startActivityByNetResult("50",getContext().getString(R.string.zlmc_50),"OPR");
                }
                break;
            case  R.id.zl51:
                if (isReady()){
                    cardView_g2.startAnimation(anim);
                    startActivityByNetResult("51",getContext().getString(R.string.zlmc_51),"OPR");
                }
                break;
            case  R.id.zl52:
                if (isReady()){
                    cardView_g3.startAnimation(anim);
                    startActivityByNetResult("52",getContext().getString(R.string.zlmc_52),"OPR");
                }
                break;
            case  R.id.zl53:
                if (isReady()){
                    cardView_g4.startAnimation(anim);
                    startActivityByNetResult("53",getContext().getString(R.string.zlmc_53),"OPR");
                }
                break;
            case  R.id.zl54:
                if (isReady()){
                    cardView_g5.startAnimation(anim);
                    startActivityByNetResult("54",getContext().getString(R.string.zlmc_54),"OPR");
                }
                break;
            case  R.id.zl55:
                if (isReady()){
                    cardView_g6.startAnimation(anim);
                    startActivityByNetResult("55",getContext().getString(R.string.zlmc_55),"OPR");
                }
                break;
            case  R.id.zl56:
                if (isReady()){
                    cardView_g7.startAnimation(anim);
                    startActivityByNetResult("56",getContext().getString(R.string.zlmc_56),"OPR");
                }
                break;
            case  R.id.zl57:
                if (isReady()){
                    cardView_g8.startAnimation(anim);
                    startActivityByNetResult("57",getContext().getString(R.string.zlmc_57),"OPR");
                }
                break;
            case  R.id.zl58:
                if (isReady()){
                    cardView_g9.startAnimation(anim);
                    startActivityByNetResult("58",getContext().getString(R.string.zlmc_58),"OPR");
                }
                break;
            case  R.id.zl60:
                if (isReady()){
                    cardView_g10.startAnimation(anim);
                    startActivityByNetResult("60",getContext().getString(R.string.zlmc_60),"OPR");
                }
                break;
            case  R.id.zl61:
                if (isReady()){
                    cardView_g11.startAnimation(anim);
                    startActivityByNetResult("61",getContext().getString(R.string.zlmc_61),"OPR");
                }
                break;
            case  R.id.zl62:
                if (isReady()){
                    cardView_g12.startAnimation(anim);
                    startActivityByNetResult("62",getContext().getString(R.string.zlmc_62),"OPR");
                }
                break;
            case  R.id.zl63:
                if (isReady()){
                    cardView_g13.startAnimation(anim);
                    startActivityByNetResult("63",getContext().getString(R.string.zlmc_63),"OPR");
                }
                break;
            case  R.id.zl64:
                if (isReady()){
                    cardView_g14.startAnimation(anim);
                    startActivityByNetResult("64",getContext().getString(R.string.zlmc_64),"OPR");
                }
                break;
            case  R.id.zl65:
                if (isReady()){
                    cardView_g15.startAnimation(anim);
                    startActivityByNetResult("65",getContext().getString(R.string.zlmc_65),"OPR");
                }
                break;
            case R.id.zlx:
                cardView_g16.startAnimation(anim);
                startActivityByNetResult("*",getContext().getString(R.string.zlmc_x),"OPR");
                break;
            case R.id.pgxj:
                cardView_pzxj.startAnimation(anim);
                //startActivityByNetResult(getContext().getString(R.string.pgxj),getContext().getString(R.string.zlmc_xj),"OPR");
                Intent intent_pzxj=new Intent(getContext(), DialogGActivity.class);
                intent_pzxj.putExtra("title","品管巡机");
                startActivity(intent_pzxj);
                break;
            case R.id.zlF3:
                cardView_g21.startAnimation(anim);
                jsBtnEven();
                break;
        }
    }

    private void jsBtnEven(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<List<String>>list2= NetHelper.getQuerysqlResult("Exec PAD_Get_JtmZtInfo '"+sharedPreferences.getString("jtbh","")+"'");
                if(list2!=null){
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
                                if (startType!=null){
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
                                            intent1.putExtra("title",startZlmc);
                                            intent1.putExtra("zldm",startZldm);
                                            startActivity(intent1);
                                            break;
                                        case "C":
                                            Intent intent2=new Intent(getContext(), BlYyfxActivity.class);
                                            intent2.putExtra("title",startZlmc);
                                            intent2.putExtra("zldm",startZldm);
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
                                }else {
                                    getStartType();
                                }
                            }
                        }
                    }
                }else {
                    AppUtils.uploadNetworkError("Exec PAD_Get_JtmZtInfo NetWordError",sharedPreferences.getString("jtbh","")
                            ,sharedPreferences.getString("mac",""));
                    //handler.sendEmptyMessage(0x101);
                    handler.sendEmptyMessage(0x110);
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



