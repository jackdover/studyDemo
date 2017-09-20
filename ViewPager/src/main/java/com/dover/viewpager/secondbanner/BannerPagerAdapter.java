package com.dover.viewpager.secondbanner;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by d on 2017/9/20.
 */
public class BannerPagerAdapter<T extends View> extends PagerAdapter {
    private static final String TAG = "BannerPagerAdapter";

    private List<T> mList;
    private BannerClickListener mBannerClickListener;
    private int mPosition;

    public BannerPagerAdapter(List<T> list) {
        this.mList = list;
    }

    public BannerPagerAdapter(List<T> list, BannerClickListener bannerClickListener) {
        this.mList = list;
        this.mBannerClickListener = bannerClickListener;
    }

    //返回需要展示页面的数量
    @Override
    public int getCount() {
//        Integer.MAX_VALUE
        return 100 * 10000; //100W个
    }

    //判断view是否可以复用，直接写“return view == object;”即可
    //其实就是比较 当前的 view 和 instantiateItem 返回的view是否是同一个
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //负责初始化指定位置的页面。完成两步骤 1.添加view到容器中，2返回view给适配器
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //对ViewPager页号求模取出View列表中要显示的项
        mPosition = position % (mList.size());
        if (mPosition < 0) {
            mPosition = mList.size() + mPosition;
        }
        Log.d(TAG, "mPosition=" + mPosition);

        //生成view
        View view = mList.get(mPosition);

        //添加view
        container.addView(view);

        //add listeners here if necessary
        if (mBannerClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBannerClickListener.onClick(mPosition);
                }
            });
        }
        return view;
    }

    //负责移除指定位置的页面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //点击监听
    public interface BannerClickListener {
        void onClick(int position);
    }
}