package com.dover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dover.baseimageloader.R;
import com.dover.glide.MainActivity;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_glide:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                break;
            case R.id.btn_picaso:
                break;
            case R.id.btn_fresco:
                break;
        }
    }
}
