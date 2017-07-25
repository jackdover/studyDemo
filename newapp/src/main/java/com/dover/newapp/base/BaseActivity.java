package com.dover.newapp.base;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by d on 2017/7/24.
 * Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private Toast toast;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //0. 屏幕的相关初始化, 比如全屏等
        initBeforeSetContentView();
        //1. 初始化布局资源ID
        setContentView(getLayoutResID());
        //2 初始化第三方
        initThird();
        //3. 初始化布局view
        initView();
        //4. 初始化数据
        initData();
    }


    // 转场动画---这个方法在startActivity(Intent) or finish()之后被调用
    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }

    public void testNextActivity(){
//        startActivity(intent);
//// transaction animation
//        overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
    }



    //0. 屏幕的相关初始化, 比如全屏等
    protected void initBeforeSetContentView() {
        /*设置 no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*设置 full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //1. 初始化布局资源ID
    protected abstract int getLayoutResID();

    //2 初始化第三方
    protected void initThird() {
//        ButterKnife.bind(this);
    }

    //3. 初始化布局view
    protected abstract void initView();

    //4. 初始化数据
    protected abstract void initData();


    @Override
    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {
            if (toast == null) {
                toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }

            toast.show();
        }
    }
}
