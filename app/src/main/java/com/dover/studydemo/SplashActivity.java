package com.dover.studydemo;

import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        new SoundPool.Builder().build();  //api 21

        initSoundPool();


    }

    private void initSoundPool() {

    }
}
