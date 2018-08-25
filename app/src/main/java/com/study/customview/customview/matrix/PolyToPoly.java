package com.study.customview.customview.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.study.customview.customview.R;

/**
 * Author: GcsSloop
 * <p>
 * Created Date: 16/8/26
 * <p>
 * Copyright (C) 2016 GcsSloop.
 * <p>
 * GitHub: https://github.com/GcsSloop
 */
public class PolyToPoly extends View{
    private static final String TAG = "SetPolyToPoly";

    private int testPoint = 0;
    private int triggerRadius = 180;    // 触发半径为180px

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix

    private float[] src = new float[8];
    private float[] dst = new float[8];

    private Paint pointPaint;

    public PolyToPoly(Context context) {
        this(context, null);
    }

    public PolyToPoly(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolyToPoly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmapAndMatrix();
    }

    private void initBitmapAndMatrix() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.matrix);

        float[] temp = {0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下
        src = temp.clone();
        dst=temp.clone();
        dst[0]=dst[0]+100;
        dst[1]=dst[1]+100;
        dst[3]=dst[3]+200;
        dst[4]=dst[4]+200;
       /* Matrix matrix = new Matrix();
        matrix.setScale(0.8f,0.5f);
        matrix.mapPoints(dst,src);*/

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        mPolyMatrix = new Matrix();
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, 3);
        /*

        setPolyToPoly（）方法的核心理解：最多可以提供四个操控点，在src中设置操控点的坐标，提供的操控点不是非得全部都使用，可以选择使用的个数。
        1：表示使用src中定义的第一个点。
        2：表示使用src中定义的前两个点。
        3：表示使用src中定义的前三个点。
        4：表示使用src中定义的前四个点。

        * pointCount:表示要使用src和dst中的几个点坐标，最多四个点。
        * 使用一个点：只能做相对于这个原来的位置的移动变换操作。
        * 使用两个点：其中一个点围绕另外一个点做旋转变化操作。
        * 使用三个点：移动其中一个点时，围绕另外两个点组成的直线做变换操作。
        * 使用四个点：移动其中一个点时，围绕另外三个点构成的面进行变换操作。
        *
        * */

    }

    @Override
    protected void onDraw(Canvas canvas) {
       canvas.translate(10,10);

        // 绘制坐标系
        //CanvasAidUtils.setCoordinateLen(900, 0, 1200, 0);
        //CanvasAidUtils.set2DAxisLength(900,0,1200,0);
        //CanvasAidUtils.draw2DCoordinateSpace(canvas);

        // 根据Matrix绘制一个变换后的图片
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);

        float[] dst = new float[8];
        mPolyMatrix.mapPoints(dst,src);


    }


}
