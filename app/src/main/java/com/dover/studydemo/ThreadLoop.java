package com.dover.studydemo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/**
 * Created by d on 2017/8/10.
 */
public class ThreadLoop {

    private static final String TAG = "ThreadLoop";

    // UI-Handler
    private Handler mUiHandler = new Handler();
    // Thread-Handler
    private Handler mThreadHandler;
    // 创建一个线程,线程名字：handler-thread
    private HandlerThread mBackThread;
    // 是否继续
    private boolean isContinue = false;//默认不, 当调用startLoop方法时, 才开始

    private static final int MSG_CONTINUE_INFO = 0x110; //发送消息继续轮询


    public ThreadLoop() {
        initBackThread();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {     //主线程 -UI
            //主线程
            doUpdateUi();
        }
    };


    //创建后台线程
    private void initBackThread() {
        mBackThread = new HandlerThread("handler-thread");
        mBackThread.start();
        mThreadHandler = new Handler(mBackThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
//                Log.d("mThreadHandler", "消息： " + msg.what + "  线程： " + Thread.currentThread().getName());
//                long preTime = System.currentTimeMillis();
//                //工作在子线程, 可以耗时操作
//                doUpdateThread();
//                long afterTime = System.currentTimeMillis();
//                //如果耗时时间超过250ms, 立即执行, 否则,延迟到250ms
//                long delay = (afterTime - preTime) < 250 ? 250 - (afterTime - preTime) : 0;
//                mUiHandler.postDelayed(mRunnable, delay);
//                if (isContinue) {
//                    Log.d("mThreadHandler", "持续轮询" + Thread.currentThread().getName());
//                    mThreadHandler.sendEmptyMessage(MSG_CONTINUE_INFO);//持续轮询
//                }


                doUpdateThread();
            }
        };
    }

    //开始轮询
    public void startLoop() {
        //开始轮询
        isContinue = true;
        mThreadHandler.sendEmptyMessageDelayed(MSG_CONTINUE_INFO, 3000);//开始轮询
        mUiHandler.postDelayed(mRunnable, 250);
    }


    //停止轮询
    public void stopLoop() {
        //停止查询
        isContinue = false;
        mThreadHandler.removeMessages(MSG_CONTINUE_INFO);
        mThreadHandler.removeCallbacksAndMessages(null);
        //释放资源
        mBackThread.quit();

        mUiHandler.removeCallbacks(mRunnable);
        mUiHandler.removeCallbacksAndMessages(null);
    }


    /**
     * 执行耗时操作
     */
    private void doUpdateThread() {
        Log.d(TAG, "耗时操作--------------开始");
        try {
            //模拟耗时
            Thread.sleep(5000);
            Log.d(TAG, "耗时操作------------结束---" + count2++);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int count;
    int count2;

    /**
     * 执行 UI操作
     */
    private void doUpdateUi() {
        Log.d(TAG, "doUpdateUi----主线程");


        if (isContinue) {
            Log.d("mThreadHandler", "持续轮询--" + count++ + "--" + Thread.currentThread().getName());

            mThreadHandler.sendEmptyMessageDelayed(MSG_CONTINUE_INFO, 1000);//开始轮询
            mUiHandler.postDelayed(mRunnable, 1000);
        }
    }

}
