package com.dover.viewpager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //viewpager 基本使用+简单封装
//        startActivity(new Intent(this, FirstActivity.class));

        //广告条 banner --- 无限循环 + 自动轮播
        startActivity(new Intent(this, SecondActivity.class));
    }
}
