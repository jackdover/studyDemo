package com.dover.gesturelockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by d on 2017/7/26.
 */
public class MyLockViewGroup extends RelativeLayout {

    private static final String TAG = "ViewGroup";

    private Paint mPaint;
    private Path mPath;

    private float mStrokeWidth = 20; //画笔宽度


    // 宽度, 高度  (LockViewGroup 的大小)
    private int mWidth, mHeight;
    // 宽度, 高度  (子view--LockView 的大小)
    private int mLockViewWidth;
    // 间距 (子view--LockView 的间距)
    private int mLockViewMargin;
    // 间距与宽度的比例 (mLockViewMargin = mLockViewWidth * mLockMarginRate)
    private float mRateMargin = 0.75f;


    private int numRow;         // 行
    private int numCol;         // 列
    private int mCount = 3;   // 每个边上的 LockView 的个数

    // 保存所有的 LockView
    private LockView[] mLockViews;  //总个数 = mCount * mCount


    public MyLockViewGroup(Context context) {
        super(context);
        init(context);   //初始化
    }

    public MyLockViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);   //初始化
    }

    public MyLockViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // init(context, attrs, defStyleAttr); //初始化自定义属性
        init(context);   //初始化
    }

    /**
     * 1. 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // 1.1 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        // mPaint.setStrokeWidth(mStrokeWidth);           //设置 画笔宽度
        // mPaint.setColor(Color.parseColor("#aaffffff"));  //设置画笔颜色
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        // 1.2 初始化路径
        mPath = new Path();


    }


    // 2. 测量 ViewGroup大小, 并根据其计算 子view大小和间距
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 2.1 测量viewgroup 大小
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 2.2 获取最小值
        mWidth = mHeight = mWidth < mHeight ? mWidth : mHeight;

        // 2.3 根据宽高,计算 子view大小和间距
        // 2.3.1 计算每个 LockView 的宽度
        mLockViewWidth = (int) (mWidth / (mCount + mRateMargin * (mCount + 1)));
        mLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
        // 2.3.2 计算每个 LockView 的间距
        mLockViewMargin = (int) (mLockViewWidth * mRateMargin);

        // 2.3.3 设置画笔的宽度（不喜欢的话，随便设）
        mStrokeWidth = mLockViewWidth * 0.02f < 2 ? 2 : mLockViewWidth * 0.02f;//最小值2
        mPaint.setStrokeWidth(mStrokeWidth);//mLockViewWidth * 0.29f

        Log.e(TAG, "onMeasure---" + "mWidth/mHeight=" + mWidth + "/" + mHeight
                + ",mLockWidth=" + mLockViewWidth + ",mLockMargin=" + mLockViewMargin
                + ",mStroke=" + mStrokeWidth);


        //2.4 初始化 mLockViews, 并设置相应的界面位置
        if (mLockViews == null) {
            initmLockViews();
        }
    }

    private void initmLockViews() {
        mLockViews = new LockView[mCount * mCount];
        // 添加子view 并布局
        for (int i = 0; i < mLockViews.length; i++) {
            //初始化每个LockView
            mLockViews[i] = new LockView(getContext(), null);
            mLockViews[i].setId(i + 1);


            //设置参数，主要是定位LockView间的位置
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(mLockViewWidth, mLockViewWidth);

            // 不是每行的第一个，则设置位置为前一个的右边
            if (i % mCount != 0) {
                params.addRule(RelativeLayout.RIGHT_OF,
                        mLockViews[i - 1].getId());
            }
            // 从第二行开始，设置为上一行同一位置View的下面
            if (i > mCount - 1) {
                params.addRule(RelativeLayout.BELOW,
                        mLockViews[i - mCount].getId());
            }
            //设置右下左上的边距
            int rightMargin = mLockViewMargin;
            int bottomMargin = mLockViewMargin;
            int leftMagin = 0;
            int topMargin = 0;
            /**
             * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
             */
            if (i >= 0 && i < mCount)// 第一行
            {
                topMargin = mLockViewMargin;
            }
            if (i % mCount == 0)// 第一列
            {
                leftMagin = mLockViewMargin;
            }

            params.setMargins(leftMagin, topMargin, rightMargin,
                    bottomMargin);
            mLockViews[i].setStatus(LockView.Status.STATUS_NORMAL);


            addView(mLockViews[i]);

        }
    }


    // 3. 布局子 view
    // 添加子view 并布局
     /*   if (mLockViews != null) {
            for (int i = 0; i < mLockViews.length; i++) {
                int row = i / mCount;   //第几行
                int col = i % mCount;   //第几列

                // 计算实际的布局边界
                int left = (int) (mLockViewMargin + col * (mLockViewWidth + mLockViewMargin));
                int top = (int) (mLockViewMargin + row * (mLockViewWidth + mLockViewMargin));
                int right = (int) (left + mLockViewWidth);
                int bottom = (int) (top + mLockViewWidth);

//                //初始化每个LockView
//                mLockViews[i] = new LockView(getContext(), null);
//                mLockViews[i].setId(i + 1);
//                // 添加到 viewgroup 中
////                addView(mLockViews[i]);
//                // 布局到相应位置
                mLockViews[i].layout(left, top, right, bottom);
                // 添加到 viewgroup 中
//                addView(mLockViews[i]);
            }
        }*/

//        for (int i = 0, size = getChildCount(); i < size; i++) {
//            int row = i / mCount;   //第几行
//            int col = i % mCount;   //第几列
//
//            // 计算实际的布局边界
//            int left = (int) (mLockViewMargin + col * (mLockViewWidth + mLockViewMargin));
//            int top = (int) (mLockViewMargin + row * (mLockViewWidth + mLockViewMargin));
//            int right = (int) (left + mLockViewWidth);
//            int bottom = (int) (top + mLockViewWidth);
//
//            // 布局到相应位置
//            getChildAt(i).layout(left, top, right, bottom);
//
//        }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
