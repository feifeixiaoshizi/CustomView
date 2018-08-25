package com.study.customview.customview.view.animator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.study.customview.customview.R;



public class ObjectAnimatorDemo extends View {

    Paint paint ;
    float progress = 0;

    public ObjectAnimatorDemo(Context context) {
        super(context);
        init();
    }

    public ObjectAnimatorDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObjectAnimatorDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void  init(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        paint.setStrokeCap(Paint.Cap.ROUND);

    }
    // 创建 getter 方法
    public float getProgress() {
        return progress;
    }

    // 创建 setter 方法
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth()/2,getHeight()/2);
        RectF arcRectF = new RectF(-getWidth()/2+50,-getHeight()/2+50,getWidth()/2-50,getHeight()/2-50);
        canvas.drawArc(arcRectF, 135, progress * 2.7f, false, paint);


    }
}




