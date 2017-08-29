package com.dover.signanimview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by d on 2017/8/29.
 */
public class SignAnimView extends View {

    private Paint circleBgPaint;
    private Paint ringBgPaint;
    private Paint ringPaint;
    private Paint textPaint;

    private Path path;

    private int ringBgColor = Color.YELLOW;
    private int ringColor = Color.CYAN;
    private int textColor = Color.WHITE;
    private int circleColor = Color.CYAN;

    private int textSize = 48;

    private int mRadius = 120;
    private int space = 6;
    private String contentText = "签到";

    private int ringRadius = mRadius;
    private int ringWidth = mRadius / 5;

    private int narrowDown = 5;
    private int value = 0;
    private float result;

    private boolean startDrawLine = false;
    private float angle = 0;

    private OnViewClick onViewClick;
    private boolean enable = true;

    private ValueAnimator animator;
    private ValueAnimator animatorValue;
    private ValueAnimator angleAnimator;


    public SignAnimView(Context context) {
        super(context);
        init();
    }

    public SignAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        circleBgPaint = new Paint();
        circleBgPaint.setAntiAlias(true);
        circleBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        ringBgPaint = new Paint();

        ringBgPaint.setColor(ringBgColor);
        ringBgPaint.setAntiAlias(true);
        ringBgPaint.setStrokeWidth(ringWidth);
        ringBgPaint.setStyle(Paint.Style.STROKE);

        ringPaint = new Paint();
        ringPaint.setColor(ringColor);
        ringPaint.setAntiAlias(true);
        ringPaint.setStrokeWidth(ringWidth);
        ringPaint.setStyle(Paint.Style.STROKE);

        path = new Path();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int resultWidth = widthSize;
        int resultHeight = heightSize;
        if (mRadius * 2 < textPaint.measureText(contentText)) {
            mRadius = (int) textPaint.measureText(contentText);
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            int contentWidth = (mRadius + space + ringWidth) * 2 + getPaddingLeft() + getPaddingRight();
            resultWidth = (contentWidth < widthSize) ? resultWidth : contentWidth;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            int contentHeight = (mRadius + space + ringWidth) * 2 + getPaddingTop() + getPaddingBottom();
            resultHeight = (contentHeight < heightSize) ? resultHeight : contentHeight;
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆
        ringRadius = mRadius - DpUtils.dip2px(getContext(), value / 2);
        circleBgPaint.setColor(circleColor);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, ringRadius, circleBgPaint);
        //用户按键时开始画圆环
        if (startDrawLine) {
            //计算外环的半径，记得要减去外环的宽度的一半
            result = ringRadius + space + ringWidth / 2;
            //画完整的进度条
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, result, ringBgPaint);
            //画进度条路径
            path.reset();
            //计算画路径的矩形
            float left = getWidth() / 2 - result;
            float top = getHeight() / 2 - result;
            float right = getWidth() / 2 + result;
            float bottom = getHeight() / 2 + result;
            RectF rect = new RectF(left, top, right, bottom);
            path.addArc(rect, -90, angle);

            //画圆环的路径
            canvas.drawPath(path, ringPaint);
        }
        //画文字
        canvas.drawText(contentText, getWidth() / 2, getHeight() / 2, textPaint);
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (!enable && event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                if (animator != null) {
                    animator.removeAllUpdateListeners();
                }
                animatorValue = ValueAnimator.ofInt(0, narrowDown);
                animatorValue.setDuration(50);
                animatorValue.setInterpolator(new LinearInterpolator());
                animatorValue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        value = (int) valueAnimator.getAnimatedValue();
                        if (value == narrowDown) {
                            startDrawLine = true;
                            animatorValue.removeAllUpdateListeners();
                        }
                        postInvalidate();
                    }
                });

                animatorValue.start();
                angleAnimator = ValueAnimator.ofFloat(0, 360f);
                angleAnimator.setDuration(2000);
                angleAnimator.setInterpolator(new LinearInterpolator());
                angleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        angle = (float) valueAnimator.getAnimatedValue();
                        if (angle == 360) {
                            angleAnimator.removeAllUpdateListeners();
                            onViewClick.onFinish(SignAnimView.this);

                        }
                        postInvalidate();
                    }
                });
                angleAnimator.start();
            }

            break;
            case MotionEvent.ACTION_UP: {
                restoreShape();
            }
            startDrawLine = false;
            break;
        }
        return true;
    }

    private void restoreShape() {
        angleAnimator.removeAllUpdateListeners();
        animatorValue.removeAllUpdateListeners();
        animator = ValueAnimator.ofInt(value, 0);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                value = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }


    public interface OnViewClick {
        void onFinish(View view);
    }

    public void setOnViewClick(OnViewClick viewClick) {
        this.onViewClick = viewClick;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // super.setEnabled(enabled);
        this.enable = enabled;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    //设置背景颜色
    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

}
