package com.dover;

import android.app.Application;
import android.util.Log;

import com.dover.alog.utils.PackageUtils;
import com.dover.dlog.DLogUtils;

/**
 * Created by d on 2017/9/8.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化日志
        DLogUtils.initDLog(this);
//        ALogUtils.initALog(this);

        android.util.Log.i("MyApplication", PackageUtils.getAppName(this));
    }


}
