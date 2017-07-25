package com.dover.newapp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by d on 2017/7/24.
 */
public class BaseApplication extends Application {

    public static boolean isdebug = true;//测试模式//todo 线上(false),线下(true)切换

    //Application extends ContextWrapper, ContextWrapper extends Context
    private static BaseApplication sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;

        // Application通过此接口提供了一套回调方法，用于让开发者对Activity的生命周期事件进行集中处理
        // Activity生命周期的事件进行集中处理 比如 统计 等处理
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(activity.getClass().getSimpleName(), "onActivityCreated with: savedInstanceState = [" + savedInstanceState + "]");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(activity.getClass().getSimpleName(), "onActivitySaveInstanceState with: outState = [" + outState + "]");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "onActivityDestroyed");
            }
        });
    }


}