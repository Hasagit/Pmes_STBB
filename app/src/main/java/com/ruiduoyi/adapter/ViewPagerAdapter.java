package com.ruiduoyi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NewHT on 2016/10/5.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     *
     * @param fragment  添加Fragment
     * @param fragmentTitle Fragment标题
     */
    public void addFragment(Fragment fragment, String fragmentTitle){
        mFragments.add(fragment);
        mFragmentTitle.add(fragmentTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public CharSequence getPageTitle(int position){
        return mFragmentTitle.get(position);
    }
}
