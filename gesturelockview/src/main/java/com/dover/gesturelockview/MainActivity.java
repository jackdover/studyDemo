package com.dover.gesturelockview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GestureLockViewGroup mGestureLockViewGroup;
    private LockViewConfig config;
    private LockViewGroup lockViewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
        mGestureLockViewGroup.setAnswer(new int[]{1, 2, 3, 4, 5});
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        Toast.makeText(MainActivity.this, "错误5次...",
                                Toast.LENGTH_SHORT).show();
                        mGestureLockViewGroup.setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        Toast.makeText(MainActivity.this, matched + "",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }
                });


        lockViewGroup = (LockViewGroup) findViewById(R.id.lvp);

        config = new LockViewConfig();

        config.setNormalColor(0xFF0F8EE8);
        config.setFingerOnColor(0xFF2177C7);
        config.setErrorColor(0xFFFF0000);
        // LockView
        config.setRadiusRate(0.3f);
        config.setArrowRate(0.25f);
        config.setStrokeWidth(6);

        lockViewGroup.setmConfig(config);

//        lockViewGroup.setAnswer();

        lockViewGroup.setMaxTryTimes(5);

        lockViewGroup.setOnLockListener(new LockViewGroup.OnLockListener() {
            @Override
            public void onLockSelected(int id) {
                Toast.makeText(MainActivity.this, "当前连接的是:" + id,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLess4Points() {
                Toast.makeText(MainActivity.this, "至少连接4个点, 请重新输入",
                        Toast.LENGTH_SHORT).show();
                lockViewGroup.clear2ResetDelay(1200L);
            }

            @Override
            public void onSaveFirstAnswer(int[] answer) {

                Toast.makeText(MainActivity.this, "保存的答案是" + answer.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucessed() {
                Toast.makeText(MainActivity.this, "验证成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTryTimes(int mTryTimes) {
                Toast.makeText(MainActivity.this,
                        "与上一次绘制不一致, 请重新绘制\n剩余尝试次数:" + mTryTimes,
                        Toast.LENGTH_SHORT).show();
                lockViewGroup.clear2ResetDelay(1400L);


//                mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
//                // 左右移动动画
//                Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
//                mTextTip.startAnimation(shakeAnimation);
//                // 保持绘制的线，1.5秒后清除
//                mGestureContentView.clearDrawlineState(1300L);
            }
        });

    }
}
