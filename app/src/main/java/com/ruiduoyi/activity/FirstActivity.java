package com.ruiduoyi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruiduoyi.R;
import com.ruiduoyi.activity.Dialog.ReSetServerIpActivity;
import com.ruiduoyi.model.NetHelper;
import com.ruiduoyi.utils.AppUtils;
import com.ruiduoyi.view.PopupDialog;
import com.ruiduoyi.view.PopupWindowSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class FirstActivity extends BaseActivity{
    private boolean isServiceOpen=false;
    private boolean isJtbh=false;
    private boolean isNewVersion=false;
    private String jtbh;
    private ImageView welc_img;
    View contenView,contenView2;
    PopupWindow dialog,dialog2;
    private Button spiner_btn;
    private TextView jtbh_tip;
    PopupDialog dialogAutoUpdate;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
        initData();
    }


    private void initView(){
        welc_img=(ImageView) findViewById(R.id.welcome_img);
        Glide.with(this).load(R.drawable.welcome).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(welc_img);
       // Glide.with(this).load("file:///android_asset/welcome.gif").into(welc_img);
        contenView= LayoutInflater.from(FirstActivity.this).inflate(R.layout.popup_dialog3,null);
        dialog=new PopupWindow(contenView, 450, 350);
        contenView2= LayoutInflater.from(FirstActivity.this).inflate(R.layout.popup_dialog2,null);
        dialog2=new PopupWindow(contenView2,  450, 400);
        spiner_btn=(Button)contenView2.findViewById(R.id.spinner_btn);
        jtbh_tip=(TextView)contenView2.findViewById(R.id.jtbh_tip);
    }


    private void initData(){
        File file=new File(Environment.getExternalStorageDirectory().getPath()+"/RdyPmes.apk");
        boolean is=file.exists();
        if(is){
            file.delete();
        }
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        if (sharedPreferences.getString("service_ip","").equals("")){
            NetHelper.URL="http://"+getString(R.string.service_ip)+":8080/Service1.asmx";
        }else {
            NetHelper.URL="http://"+sharedPreferences.getString("service_ip","")+":8080/Service1.asmx";
        }


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100://服务器已经连接成功，机台编号的获取
                        dialog.dismiss();
                        isServiceOpen=true;
                        getNetData(1);
                        break;
                    case 0x101:
                        isServiceOpen=false;
                        if(!dialog.isShowing()){
                            Button cancle_btn=(Button)contenView.findViewById(R.id.cancle_btn);
                            Button try_again=(Button)contenView.findViewById(R.id.again_btn);
                            TextView msg_text=(TextView)contenView.findViewById(R.id.msg_text);
                            msg_text.setText("服务器连接异常\n系统每隔十秒重连一次 \n重连次数："+msg.arg1);
                            cancle_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            try_again.setVisibility(View.GONE);

                            dialog.setOutsideTouchable(false);
                            dialog.setBackgroundDrawable(new ColorDrawable(0xffffff));
                            dialog.showAtLocation(FirstActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                        }else {
                            TextView msg_text=(TextView)contenView.findViewById(R.id.msg_text);
                            msg_text.setText("服务器连接异常\n系统每隔十秒重连一次 \n重连次数："+msg.arg1);
                        }
                        break;
                    case 0x102:
                        isJtbh=false;
                        if (!dialog2.isShowing()){
                            Button cancle_btn2=(Button)contenView2.findViewById(R.id.cancle_btn);
                            final Button try_again2=(Button)contenView2.findViewById(R.id.again_btn);
                            TextView msg_text2=(TextView)contenView2.findViewById(R.id.msg_text);
                            msg_text2.setText("【设备物理地址】："+sharedPreferences.getString("mac","")+"\n未设置机台，请重新设置");
                            cancle_btn2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                    finish();
                                }
                            });
                            try_again2.setText("机台设置");
                            try_again2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (spiner_btn.getText().toString().equals("选择机台号")){
                                        Toast.makeText(FirstActivity.this,"请先选择机台号",Toast.LENGTH_SHORT).show();
                                    }else {
                                        //dialog2.dismiss();
                                        try_again2.setText("系统设置中...");
                                        try_again2.setEnabled(false);
                                        new Thread(new Runnable() {//设置机台编号
                                            @Override
                                            public void run() {
                                                String service_ip="";
                                                if (sharedPreferences.getString("service_ip","").equals("")){
                                                    service_ip=getResources().getString(R.string.service_ip);
                                                }else {
                                                    service_ip=sharedPreferences.getString("service_ip","");
                                                }
                                                boolean result=NetHelper.getRunsqlResult("Exec PAD_Set_JtmJtbh '"+
                                                        sharedPreferences.getString("mac","")+"','"+spiner_btn.getText().toString()+"',"+"'"+service_ip+"'");
                                                if (result){
                                                    getNetData(1);
                                                }
                                            }
                                        }).start();
                                    }
                                }
                            });

                            dialog2.setOutsideTouchable(false);
                            dialog2.setBackgroundDrawable(new ColorDrawable(0xffffff));
                            if (!FirstActivity.this.isFinishing()){
                                dialog2.showAtLocation(FirstActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                            }
                            new Thread(new Runnable() {//获取机台编号列表
                                @Override
                                public void run() {
                                    JSONArray array=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_JtmMstr ''");
                                    if (array!=null){
                                        Message msg=handler.obtainMessage();
                                        msg.what=0x105;
                                        msg.obj=array;
                                        handler.sendMessage(msg);
                                    }else {
                                        NetHelper.uploadNetworkError("Exec PAD_Get_WebAddr NetWordError",
                                                jtbh,sharedPreferences.getString("mac",""));
                                    }
                                }
                            }).start();

                        }
                        break;
                    case 0x103:
                        try {
                            JSONArray array= (JSONArray) msg.obj;
                            if (array.length()>0){
                                jtbh=array.getJSONObject(0).getString("jtm_jtbh");
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("jtbh",jtbh);
                                editor.commit();
                                isJtbh=true;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case 0x105:
                        try {
                            JSONArray array= (JSONArray) msg.obj;
                            if (array.length()>0){
                                final List<String>data=new ArrayList<>();
                                for (int i=0;i<array.length();i++){
                                    data.add(array.getJSONObject(i).getString("v_jtbh"));
                                }
                                spiner_btn.setVisibility(View.VISIBLE);
                                jtbh_tip.setVisibility(View.VISIBLE);
                                spiner_btn.setText("选择机台号");
                                spiner_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final PopupWindowSpinner spinner=new PopupWindowSpinner(FirstActivity.this,data,R.layout.spinner_list_b7,R.id.lab_1,200);
                                        spinner.showUpOn(spiner_btn);
                                        spinner.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                spiner_btn.setText(data.get(position));
                                                spinner.dismiss();
                                                //dialog2.dismiss();
                                            }
                                        });
                                    }
                                });

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                    case 0x106://服务器时间
                        try {
                            JSONArray array= (JSONArray) msg.obj;
                            if (array.length()>0){
                                String time=array.getJSONObject(0).getString("Column1");
                                String ymd_hm=time.substring(0,4)+time.substring(5,7)+time.substring(8,10)
                                        +"."+time.substring(11,13)+time.substring(14,16)+time.substring(17,19);
                                AppUtils.setSystemTime(FirstActivity.this,ymd_hm);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                    case 0x107:
                        isNewVersion=false;
                        dialogAutoUpdate=new PopupDialog(FirstActivity.this,400,300);
                        dialogAutoUpdate.setTitle("温馨提示");
                        dialogAutoUpdate.setMessage("系统自动更新中...");
                        dialogAutoUpdate.getOkbtn().setVisibility(View.GONE);
                        dialogAutoUpdate.getCancle_btn().setVisibility(View.GONE);
                        dialogAutoUpdate.show();
                        break;
                    case 0x108:
                        isNewVersion=true;
                        break;
                    case 0x109:
                        dialogAutoUpdate.dismiss();
                        break;
                    case 0x110:
                        Intent intent=new Intent(FirstActivity.this, ReSetServerIpActivity.class);
                        startActivityForResult(intent,0);
                        //TextView message=(TextView)contenView.findViewById(R.id.msg_text);
                        //message.setText("系统无法连接服务器，请联系电脑部解决");
                        break;
                    case 0x111:
                        dialog.dismiss();
                        break;

                }
            }
        };


        getNetData(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time=0;
                while (!(isServiceOpen&isJtbh&isNewVersion)){
                    try {
                        time=time+1;
                        if(time==1){
                            Thread.currentThread().sleep(12000);
                        }else {
                            Thread.currentThread().sleep(4000);
                        }
                        if (isServiceOpen&isJtbh&isNewVersion){
                            break;
                        }else {
                            if(!isServiceOpen){
                                //handler.sendEmptyMessage(0x101);
                            }else if(!isNewVersion){
                                //handler.sendEmptyMessage(0x107);
                            }else if(!isJtbh) {
                                handler.sendEmptyMessage(0x102);
                            }
                        }
                        Log.e("ser,jt,ver",isServiceOpen+""+isJtbh+""+isNewVersion+"");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                intent.putExtra("jtbh",jtbh);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_alpha_in,R.anim.activity_alpha_out);
                finish();
            }
        }).start();




    }
    
    private void getNetData(final int type){
            new Thread(new Runnable() {//服务器是否开启
                @Override
                public void run() {
                    if(NetHelper.isUrl(NetHelper.URL)){
                        if (type==0){
                            int i=0;
                            while (!NetHelper.isServerConnected(NetHelper.URL)){
                                Message msg=handler.obtainMessage();
                                msg.what=0x101;
                                i=i+1;
                                if(i>5) {
                                    handler.sendEmptyMessage(0x110);
                                    break;
                                };
                                msg.arg1=i;
                                handler.sendMessage(msg);
                                try {
                                    Thread.currentThread().sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (NetHelper.isServerConnected(NetHelper.URL)){
                                handler.sendEmptyMessage(0x100);
                            }else {
                                handler.sendEmptyMessage(0x111);
                                return;
                            }

                        }

                        //获取机台编号
                        String mac = "";
                        WifiManager wifiManager=((WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE));
                        String mac_temp=wifiManager.getConnectionInfo().getMacAddress();
                        //mac_temp="c0:21:0d:94:26:f7";
                        if(mac_temp==null&&sharedPreferences.getString("mac","").equals("")) {
                            // Toast.makeText(FirstActivity.this,"获取网卡物理地址失败，请连接wifi",Toast.LENGTH_LONG).show();
                        }else {
                            String[] mac_sz = mac_temp.split(":");
                            for (int i = 0; i < mac_sz.length; i++) {
                                mac = mac + mac_sz[i];
                            }
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("mac",mac);
                            editor.commit();
                            JSONArray array=NetHelper.getQuerysqlResultJsonArray("Select * from jtm_mstr where jtm_flag=1 and jtm_wgip='"+mac+"'");
                            Message msg=handler.obtainMessage();
                            if(array!=null){
                                if (array.length()>0){
                                    msg.what=0x103;
                                    msg.obj=array;
                                    handler.sendMessage(msg);
                                }else {

                                }
                            }else {
                                NetHelper.uploadNetworkError("获取机台编号 NetWordError",
                                        jtbh,sharedPreferences.getString("mac",""));
                                msg.what=0x104;
                                handler.sendMessage(msg);
                            }
                        }


                        //发送重启信号
                        //mac_temp="c0:21:0d:94:26:f4";
                   /* String[] mac_sz = mac_temp.split(":");
                    for (int i = 0; i < mac_sz.length; i++) {
                        mac = mac + mac_sz[i];
                    }*/
                        NetHelper.uploadErrorMsg("重启","",mac,"5");

                        //获取系统时间
                        JSONArray array=NetHelper.getQuerysqlResultJsonArray("select GETDATE()");
                        Message msg=handler.obtainMessage();
                        if(array!=null){
                            msg.obj=array;
                            msg.what=0x106;
                            handler.sendMessage(msg);
                        }else {
                            NetHelper.uploadNetworkError("select GETDATE() NetWordError",
                                    jtbh,sharedPreferences.getString("mac",""));
                            //msg.what=0x104;
                        }


                        //获取版本信息
                        try {
                            JSONArray array_info=NetHelper.getQuerysqlResultJsonArray("Exec PAD_Get_WebAddr");
                            if (array_info!=null){
                                if (array_info.length()>0){
                                    String oldVersionName= AppUtils.getAppVersionName(FirstActivity.this);
                                    String newVersionName=array_info.getJSONObject(0).getString("v_WebAppVer");
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString("countdownNum",array_info.getJSONObject(0).getString("v_WebAppMin"));
                                    editor.commit();
                                    if (!oldVersionName.equals(newVersionName)){
                                        handler.sendEmptyMessage(0x107);
                                        NetHelper.downLoadFileByUrl(array_info.getJSONObject(0).getString("v_WebAppPath"),
                                                Environment.getExternalStorageDirectory().getPath(),"RdyPmes.apk");
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/RdyPmes.apk")),
                                                "application/vnd.android.package-archive");
                                        startActivity(intent);
                                        handler.sendEmptyMessage(0x109);
                                    }else {
                                        handler.sendEmptyMessage(0x108);
                                    }
                                }
                            } else {
                                NetHelper.uploadNetworkError("PAD_Get_WebAddr NetWordError",
                                        jtbh,sharedPreferences.getString("mac",""));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else {
                        handler.sendEmptyMessage(0x110);
                    }
                }
            }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                NetHelper.URL="http://"+sharedPreferences.getString("service_ip","")+":8080/Service1.asmx";
                getNetData(0);
                break;
            case 2:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog2.isShowing()){
            dialog2.dismiss();
        }
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
