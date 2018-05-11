package com.rongyun.rongyuntest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/5/7.
 */

public class TestView extends View {

    private int mWidth;
    private int mHeight;
    private Path mPath;
    private Paint mPaint;
    private RectF mRectF;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath.addRect(100, 200, 500, 400, Path.Direction.CW);
        mPath.setLastPoint(100,300);
        mRectF = new RectF(0,0,100,100);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.save();
        mPaint.setColor(Color.RED);
        canvas.drawRect(mRectF,mPaint);
        canvas.translate(110,110);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(mRectF,mPaint);
        canvas.translate(130,130);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(mRectF,mPaint);
        //mPath.close();
       //canvas.drawPath(mPath,mPaint);
      //  canvas.drawBitmap(getBitmap(),0,0,mPaint);
        //canvas.restoreToCount(sc);
        canvas.restore();

        canvas.translate(300,300);
        //顺时针旋转，默认是左上角为原点旋转
        canvas.rotate(30);
        canvas.drawRect(mRectF,mPaint);

    }


    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(700,700, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(700/2f,700/2f);
        canvas.drawColor(Color.BLUE);
        canvas.drawPath(mPath,mPaint);
        return bitmap;
    }


    
}
