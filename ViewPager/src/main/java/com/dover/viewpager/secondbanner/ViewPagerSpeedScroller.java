package com.dover.viewpager.secondbanner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by d on 2017/9/20.
 */
public class ViewPagerSpeedScroller extends Scroller {

    private int mDuration = 1500;

    public ViewPagerSpeedScroller(Context context) {
        super(context);
    }

    public ViewPagerSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time) {
        mDuration = time;
    }

    public int getmDuration() {
        return mDuration;
    }
}
