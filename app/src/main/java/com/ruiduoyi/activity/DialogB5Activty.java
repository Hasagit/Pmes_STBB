package com.ruiduoyi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DialogB5Activty extends BaseActivity implements View.OnClickListener{
    private Button cancle_btn;
    private PDFView pdfView;
    private String path;
    private ProgressDialog dialog;
    private String type;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_b5);
        initView();
        initData();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x100:
                    List<List<String>>list=(List<List<String>>)msg.obj;
                    initPDF(list);
                    break;
                case 0x101:
                    Toast.makeText(DialogB5Activty.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    break;
                case 0x102:
                    String fileName=(String)msg.obj;
                    File file=new File(path+"/pdf/"+type+"_"+fileName);
                    pdfView.fromFile(file).defaultPage(1).swipeVertical(true).load();
                    //dialog.dismiss();
                    break;
                case 0x103:
                    //dialog.show();
                    break;
                default:
                    break;
            }
        }
    };


    private void initView(){
        pdfView=(PDFView)findViewById(R.id.pdfview);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        title=(TextView)findViewById(R.id.title);
        cancle_btn.setOnClickListener(this);
    }

    private void initData(){
        dialog=new ProgressDialog(DialogB5Activty.this);
        dialog.setTitle("提示");
        dialog.setMessage("下载中...");
        path=getCacheDir().getPath();
        File file=new File(path+"/pdf/");
        if (!file.exists()){
            file.mkdir();
        }
        Intent intent_from=getIntent();
        type=intent_from.getStringExtra("type");
        if(type.equals("gycs")){
            title.setText("工艺参数");
        }else {
            title.setText("作业标准");
        }
        thread.start();
    }


    private void initPDF(List<List<String>>list){
        if (list.size()<1){
            Toast.makeText(DialogB5Activty.this,"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            final List<String>item=list.get(0);
            String url_str=item.get(0);
            String[] temp=url_str.split("\\\\");
            final String fileName=temp[temp.length-1];
            File file=new File(path+"/pdf/"+type+"_"+fileName);
            if (file.exists()){
                pdfView.fromFile(file).defaultPage(1).swipeVertical(true).load();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handler.sendEmptyMessage(0x103);
                            URL url=new URL(getString(R.string.service_ip)+"/Pdf/"+fileName);
                            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                            urlConnection.setDoInput(true);
                            urlConnection.setUseCaches(false);
                            urlConnection.setConnectTimeout(5000);
                            urlConnection.connect();
                            InputStream in=urlConnection.getInputStream();
                            OutputStream out=new FileOutputStream(path+"/pdf/"+type+"_"+fileName,false);
                            byte[] buff=new byte[1024];
                            int size;
                            while ((size = in.read(buff)) != -1) {
                                out.write(buff, 0, size);
                            }
                            Message msg=handler.obtainMessage();
                            msg.obj=fileName;
                            msg.what=0x102;
                            handler.sendMessage(msg);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }


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



    //请求工艺参数pdf地址线程
    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            String mojuID=sharedPreferences.getString("mojuID","");
            String wuliaoID=sharedPreferences.getString("wuliaoID","");
            String jtbh=sharedPreferences.getString("jtbh","");
            Log.e("","");
            while (sharedPreferences.getString("mojuID","").equals("")|sharedPreferences.getString("wuliaoID","").equals("")|sharedPreferences.getString("jtbh","").equals("")){
                try {
                    Thread.currentThread().sleep(3000);
                    mojuID=sharedPreferences.getString("mojuID","");
                    wuliaoID=sharedPreferences.getString("wuliaoID","");
                    jtbh=sharedPreferences.getString("jtbh","");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            List<List<String>>list= NetHelper.getQuerysqlResult("Select dbo.Func_GetDocPath('A','"+mojuID+
                    "' ,'"+wuliaoID+"' ,'"+jtbh+"' )");
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
}
