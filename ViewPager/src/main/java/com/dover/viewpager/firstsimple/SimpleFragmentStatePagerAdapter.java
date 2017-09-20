package com.dover.viewpager.firstsimple;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by d on 2017/9/20.
 */
public class SimpleFragmentStatePagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> mList;

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<T> list) {
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
