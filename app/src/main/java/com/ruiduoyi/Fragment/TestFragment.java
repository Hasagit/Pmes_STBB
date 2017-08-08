package com.ruiduoyi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ruiduoyi.R;
import com.ruiduoyi.activity.DialogB5Activty;


public class TestFragment extends Fragment implements View.OnClickListener{
    private CardView card_1,card_2;
    private Animation anim;



    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        anim= AnimationUtils.loadAnimation(getContext(),R.anim.scale_anim);
        card_1=(CardView)view.findViewById(R.id.card_1);
        card_2=(CardView)view.findViewById(R.id.card_2);
        card_1.setOnClickListener(this);
        card_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_1:
                card_1.startAnimation(anim);
                Intent intent_gycs=new Intent(getContext(), DialogB5Activty.class);
                intent_gycs.putExtra("type","gycs");
                startActivity(intent_gycs);
                break;
            case R.id.card_2:
                card_2.startAnimation(anim);
                Intent intent_zybz=new Intent(getContext(), DialogB5Activty.class);
                intent_zybz.putExtra("type","zybz");
                startActivity(intent_zybz);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
