package com.dover.signanimview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fbc
        final SignAnimView signAnimView = (SignAnimView) findViewById(R.id.view_sav);
        signAnimView.setOnViewClick(new SignAnimView.OnViewClick() {
            @Override
            public void onFinish(View view) {
                Toast.makeText(MainActivity.this, "签到完成！", Toast.LENGTH_SHORT).show();
                //    imitateKeepButton.setCircleColor(Color.GRAY);
                signAnimView.setContentText("完成");
                signAnimView.setEnabled(false);
            }
        });


    }


}
