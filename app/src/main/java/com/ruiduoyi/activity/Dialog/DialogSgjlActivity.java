package com.ruiduoyi.activity.Dialog;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ruiduoyi.R;
import com.ruiduoyi.model.NetHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogSgjlActivity extends BaseDialogActivity {
    private Button cancle_btn;
    private ListView listView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_sgjl);
        initData();
        initView();
    }

    private void  initView(){
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        listView=(ListView)findViewById(R.id.list_1);
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData(){
        sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        jtbh=sharedPreferences.getString("jtbh","");
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x100:
                        initList((JSONArray) msg.obj);
                        break;
                    case 0x101:
                        Toast.makeText(DialogSgjlActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        getListData();
    }

    private void getListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql=String.format("Exec Pad_Get_Xbl_list '%S'",jtbh );
                JSONArray array= NetHelper.getQuerysqlResultJsonArray(sql);
                if (array!=null){
                    Message msg=handler.obtainMessage();
                    msg.what=0x100;
                    msg.obj=array;
                    handler.sendMessage(msg);
                }else {
                    handler.sendEmptyMessage(0x101);
                    NetHelper.uploadNetworkError(sql,jtbh,sharedPreferences.getString("mac",""));
                }
            }
        }).start();
    }

    private void initList(JSONArray array){
        try {
            List<Map<String,String>>data=new ArrayList<>();
            for (int i=0;i<array.length();i++){
                Map<String,String>map=new HashMap<>();
                map.put("lab_1",array.getJSONObject(i).getString("xbl_jtbh"));
                map.put("lab_2",array.getJSONObject(i).getString("xbl_rq"));
                map.put("lab_3",array.getJSONObject(i).getString("xbl_name"));
                map.put("lab_4",array.getJSONObject(i).getString("xbl_kssj"));
                map.put("lab_5",array.getJSONObject(i).getString("xbl_jssj"));
                map.put("lab_6",array.getJSONObject(i).getString("xbl_ys"));
                data.add(map);
            }
            SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.list_item_sgjl,
                    new String[]{"lab_1","lab_2","lab_3","lab_4","lab_5","lab_6"},
                    new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3,R.id.lab_4,R.id.lab_5,R.id.lab_6});
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
