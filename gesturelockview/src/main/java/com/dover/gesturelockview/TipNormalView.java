package com.dover.gesturelockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by d on 2017/7/25.
 */
public class TipNormalView extends View {

    private int colorId = 0;    //外圈颜色
    private int strokeWidth = 0;    //外圈宽度
    private float radius;   //圆半径

    private Context context;
    private Paint paint;

    public TipNormalView(Context context) {
        super(context);
        this.context = context;
    }

    public TipNormalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TipNormalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        drawTipNormal(canvas);
    }

    private void drawTipNormal(Canvas canvas) {
        canvas.drawCircle(getRadius() / 2, getRadius() / 2, getRadius() / 2, paint);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);   //抗锯齿
        paint.setStyle(Paint.Style.STROKE); //圆环

        paint.setStrokeWidth(getStrokeWidth());  //圆环宽度
        paint.setColor(context.getResources().getColor(getColorId()));  //圆环颜色
    }


    private int getStrokeWidth() {
        return strokeWidth > 0 ? strokeWidth : 3;   //圆环宽度(默认 3)
    }

    //外部调用可修改宽度
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    private int getColorId() {
        return colorId == 0 ? R.color.tip_normal : colorId; //圆环颜色(默认tip_normal)
    }

    //外部调用可修改颜色
    public void setColorId(int colorId) {
        this.colorId = colorId;
        invalidate();
    }


    private float getRadius() {
        return radius > 0 ? radius : 20;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

}
