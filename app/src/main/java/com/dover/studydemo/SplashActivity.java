package com.dover.studydemo;

import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private DataLoop dataLoop;
    private ThreadLoop threadLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = (TextView) findViewById(R.id.tv);

//        new SoundPool.Builder().build();  //api 21

        initSoundPool();

//        dataLoop = new DataLoop();
        threadLoop = new ThreadLoop();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataLoop != null) {
                    dataLoop.startLoop();
                }
                if (threadLoop != null) {
                    threadLoop.startLoop();
                }
            }
        });
    }

    private void initSoundPool() {

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (dataLoop != null) {
            dataLoop.stopLoop();
        }
        if (threadLoop != null) {
            threadLoop.stopLoop();
        }
    }
}
