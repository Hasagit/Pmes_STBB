package com.ruiduoyi.Fragment;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.DialogB5Activty;
import com.ruiduoyi.activity.OeeActivity;
import com.ruiduoyi.activity.MainActivity;

import java.util.ArrayList;

public class ManageFragment extends Fragment implements View.OnClickListener {
    //private CustomViewPager viewPager;
    private CardView card_1,card_2,card_3;
    private Animation anim;
    private LocalActivityManager manager;
    private FrameLayout layout_1,layout_2,layout_3;
    private ArrayList<View> list;
    public ManageFragment() {

    }

    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=new LocalActivityManager((MainActivity)getActivity(),true);
        manager.dispatchCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_manage, container, false);
        initView(view);

        return view;
    }


    private void initView(View view){
        //viewPager=(CustomViewPager)view.findViewById(R.id.viewPager);
        layout_1=(FrameLayout)view.findViewById(R.id.layout_1);
        layout_2=(FrameLayout)view.findViewById(R.id.layout_2);
        layout_3=(FrameLayout)view.findViewById(R.id.layout_3);
        card_1=(CardView)view.findViewById(R.id.card_1);
        card_2=(CardView)view.findViewById(R.id.card_2);
        card_3=(CardView)view.findViewById(R.id.card_3);
        anim= AnimationUtils.loadAnimation(getContext(),R.anim.scale_anim);
        card_1.setOnClickListener(this);
        card_2.setOnClickListener(this);
        card_3.setOnClickListener(this);
        initViewPager();
    }



    private void initViewPager(){
        list= new ArrayList<View>();
        Intent intent_gycs=new Intent(getContext(), DialogB5Activty.class);
        intent_gycs.putExtra("type","gycs");
        Intent intent_zybz=new Intent(getContext(), DialogB5Activty.class);
        intent_zybz.putExtra("type","zybz");
        Intent intent_oee=new Intent(getContext(), OeeActivity.class);
        list.add(getView("gycs",intent_gycs));
        list.add(getView("zybz",intent_zybz));
        list.add(getView("oee",intent_oee));
        layout_1.addView(list.get(0), FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        layout_2.addView(list.get(1), FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        layout_3.addView(list.get(2), FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
       /* viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new ActivityViewPagerAdater(list));
        viewPager.setCurrentItem(0);
        viewPager .setOnTouchListener( new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;  //修改为true
            }

        });*/
    }



    private View getView(String id, Intent intent) {
        //先获取当前Window对象,获取当前Activity对应的view
        return manager.startActivity(id, intent).getDecorView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_1:
                card_1.startAnimation(anim);
                layout_1.setVisibility(View.VISIBLE);
                layout_2.setVisibility(View.GONE);
                layout_3.setVisibility(View.GONE);
                break;
            case R.id.card_2:
                card_2.startAnimation(anim);
                layout_1.setVisibility(View.GONE);
                layout_2.setVisibility(View.VISIBLE);
                layout_3.setVisibility(View.GONE);
                break;
            case R.id.card_3:
                card_3.startAnimation(anim);
                layout_1.setVisibility(View.GONE);
                layout_2.setVisibility(View.GONE);
                layout_3.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
