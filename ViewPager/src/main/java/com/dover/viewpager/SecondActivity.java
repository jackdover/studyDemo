package com.dover.viewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dover.viewpager.dlog.DLog;
import com.dover.viewpager.firstsimple.SimplePagerAdapter;
import com.dover.viewpager.secondbanner.BannerPagerAdapter;
import com.dover.viewpager.secondbanner.VpSpeedUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告条 banner ---自动轮播, 无限循环
 */
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";

    int[] TEXT = {1, 2, 3, 4, 5, 6};
    int[] IDS = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d,
            R.mipmap.a, R.mipmap.b
    };

    private ViewPager viewPager;
    private List<View> viewList;//view数组
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        viewPager = (ViewPager) findViewById(R.id.vp);


        initDataViews();

        viewPager.setAdapter(new BannerPagerAdapter(viewList, new BannerPagerAdapter.BannerClickListener() {
            @Override
            public void onClick(int position) {
                Log.d(TAG, "onBannerClickListener : " + position);
            }
        }));

        viewPager.setCurrentItem(50 * 10000 - 50 % viewList.size());

        //修改viewpager切换速度
//        VpSpeedUtil.setSpeed(this, viewPager, 3000);

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                DLog.d("onPageScrolled : " + position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                DLog.d("onPageScrolled : " + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                //state ==1的时表示正在滑动，state==2的时表示滑动完毕了，state==0的时表示什么都没做。
//                DLog.d("onPageScrollStateChanged : " + state);
//            }
//        });

    }

    private void initDataViews() {
        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < TEXT.length; i++) {
            View view = layoutInflater.inflate(R.layout.item_viewpager, null);
            LinearLayout layout_right = (LinearLayout) view.findViewById(R.id.layout_right);
            TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_num.setText("" + TEXT[i]);
            tv_total.setText("" + TEXT.length);
            TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
            TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
            TextView tv_3 = (TextView) view.findViewById(R.id.tv_3);
            tv_3.setText("去看看");
            ImageView iv_body = (ImageView) view.findViewById(R.id.iv_body);
            iv_body.setImageResource(IDS[i]);
            viewList.add(view);
        }
    }

}
