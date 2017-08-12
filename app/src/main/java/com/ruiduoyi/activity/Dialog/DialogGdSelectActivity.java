package com.ruiduoyi.activity.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ruiduoyi.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogGdSelectActivity extends BaseDialogActivity {
    private JSONArray array;
    private ListView listView;
    private Button cancle_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_gd_select);
        initData();
        initView();
    }

    private void initView(){
        listView=(ListView)findViewById(R.id.list_1);
        cancle_btn=(Button)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
        try {
            initListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initListView() throws JSONException {
        List<Map<String,String>>data=new ArrayList<>();
        if (array!=null){
            for (int i=0;i<array.length();i++){
                Map<String,String>map=new HashMap<>();
                map.put("lab_1",array.getJSONObject(i).getString("moe_jtbh"));
                map.put("lab_2",array.getJSONObject(i).getString("moe_wldm"));
                map.put("lab_3",array.getJSONObject(i).getString("itm_pmgg"));
                map.put("lab_4",array.getJSONObject(i).getString("moe_zzdh"));
                map.put("lab_5",array.getJSONObject(i).getString("moe_mjbh"));
                map.put("lab_6",array.getJSONObject(i).getString("mjm_xs"));
                map.put("lab_7",array.getJSONObject(i).getString("moe_xs"));
                map.put("lab_8",array.getJSONObject(i).getString("itm_czdm"));
                map.put("lab_9",array.getJSONObject(i).getString("ysm_ysmc"));
                map.put("lab_10",array.getJSONObject(i).getString("moe_chktime"));
                data.add(map);
            }
            SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.list_item_pzxj_gd,
                    new String[]{"lab_1","lab_2","lab_3","lab_4","lab_5","lab_6","lab_7","lab_8","lab_9","lab_10"},
                    new int[]{R.id.lab_1,R.id.lab_2,R.id.lab_3,R.id.lab_4,R.id.lab_5,R.id.lab_6,R.id.lab_7,R.id.lab_8,R.id.lab_9,R.id.lab_10});
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intent=new Intent();
                        intent.putExtra("gd_json",array.getJSONObject(position).toString());
                        setResult(1,intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void initData(){
        try {
            Intent intent_from=getIntent();
            array=new JSONArray(intent_from.getStringExtra("gd_array"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
