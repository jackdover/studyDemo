package com.dover.viewpager.secondbanner;

import android.content.Context;
import android.support.v4.view.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by d on 2017/9/20.
 * viewpager切换速度控制工具类
 */
public class VpSpeedUtil {

    public static void setSpeed(Context context, ViewPager viewPager, int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerSpeedScroller scroller = new ViewPagerSpeedScroller(context);
            scroller.setmDuration(duration);
            field.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
