package com.dover.viewpager.firstsimple;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by d on 2017/9/20.
 */
public class SimpleFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> mList;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<T> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
