package com.dover;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dover.dlog.DLog;
import com.dover.alog.R;
import com.dover.alog.utils.PackageUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alogdemo1();
    }

    private void alogdemo1() {
        DLog.v("verbose");
        DLog.d("debug");
        DLog.i("info");
        DLog.w("warn");
        DLog.e("error");
        DLog.a("assert");

        DLog.v("myCustomTag", "verbose");
        DLog.d("myCustomTag", "debug");
        DLog.i("myCustomTag", "info");
        DLog.w("myCustomTag", "warn");
        DLog.e("myCustomTag", "error");
        DLog.a("myCustomTag", "assert");

        DLog.v(null);
        DLog.d(null);
        DLog.i(null);
        DLog.w(null);
        DLog.e(null);
        DLog.a(null);

        DLog.v("customTag", "verbose0", "verbose1");
        DLog.d("customTag", "debug0", "debug1");
        DLog.i("customTag", "info0", "info1");
        DLog.w("customTag", "warn0", "warn1");
        DLog.e("customTag", "error0", "error1");
        DLog.a("customTag", "assert0", "assert1");


        String json = "{\"tools\": [{ \"name\":\"css format\" , \"site\":\"http://tools.w3cschool.cn/code/css\" },{ \"name\":\"json format\" , \"site\":\"http://tools.w3cschool.cn/code/json\" },{ \"name\":\"pwd check\" , \"site\":\"http://tools.w3cschool.cn/password/my_password_safe\" }]}";
        DLog.json(json);    //默认 D
        DLog.json(DLog.I, json);


        String xml = "<books><book><author>Jack Herrington</author><title>PHP Hacks</title><publisher>O'Reilly</publisher></book><book><author>Jack Herrington</author><title>Podcasting Hacks</title><publisher>O'Reilly</publisher></book></books>";
        DLog.xml(xml);      //默认 D
        DLog.xml(DLog.I, xml);

        DLog.i("getAppName", PackageUtils.getAppName(getApplicationContext()));
    }
}
