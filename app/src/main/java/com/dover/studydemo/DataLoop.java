package com.dover.studydemo;

import android.os.Handler;
import android.util.Log;

/**
 * Created by d on 2017/8/4.
 */
public class DataLoop {
    private static final String TAG = "DataLoop";

    int count;

    public DataLoop() {
//        startLoop();
    }

    public void startLoop() {
        Log.d(TAG, "startLoop---" + count);
        mHandler.postDelayed(mNormalRunnable, 3000);
    }


    private Handler mHandler = new Handler();


    private Runnable mNormalRunnable = new Runnable() {
        @Override
        public void run() {
//            sendSportData();
            Log.d(TAG, "mNormalRunnable---" + count++);
            //todo 没有问题吗???
            continueLoop(this);
        }

    };

    private void continueLoop(Runnable r) {
        mHandler.postDelayed(r, 250);
    }

    public void stopLoop() {
        mHandler.removeCallbacks(mNormalRunnable);
        mHandler.removeCallbacksAndMessages(null);
    }

}
