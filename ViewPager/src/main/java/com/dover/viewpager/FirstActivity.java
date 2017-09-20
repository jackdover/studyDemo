package com.dover.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//简单使用
public class FirstActivity extends AppCompatActivity {

    int[] TEXT = {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};
    int[] IDS = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d,
            R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d,
            R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d,
            R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d
    };
    List<Fragment> Fragments;

    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //fbc
        viewPager = (ViewPager) findViewById(R.id.vp);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.colorAccent));//下划线颜色
        //数据源
        initData();

        viewPager.setAdapter(new MyAdapter());

        //fragmentPagerAdapter 使用
        //注意; FragmentManager 是否使用 v4包中的
//        FragAdapter fragAdapter = new FragAdapter(getSupportFragmentManager(), Fragments);
//        viewPager.setAdapter(fragAdapter);
    }

    private void initData() {
//        int[] TEXT = {1, 2, 3, 4};
//        int[] ids = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d};
    }

    class MyAdapter extends PagerAdapter {

        //返回需要展示页面的数量
        @Override
        public int getCount() {
            return TEXT.length;
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
//            return super.instantiateItem(container, position);
            TextView textView = new TextView(FirstActivity.this);
            textView.setText("this is " + TEXT[position] + " pager");
            textView.setBackground(container.getResources().getDrawable(IDS[position]));
            container.addView(textView);
            return textView;
        }

        //负责移除指定位置的页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "pager: " + TEXT[position];
        }
    }


    public class FragAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


}
