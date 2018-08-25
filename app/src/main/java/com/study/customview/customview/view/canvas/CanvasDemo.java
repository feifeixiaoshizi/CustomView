package com.study.customview.customview.view.canvas;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ComposeShader;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.study.customview.customview.R;

/*
对图片的剪切：
Paint：1：设置图片为着色器 2：设置Xfermode
Canvas：设置画布的范围。

Canvas：
1：范围裁切
2：几何变化
2.1使用 Canvas 来做常见的二维变换；（重点在于坐标的变化****）
Canvas.translate(float dx, float dy) 平移的是坐标的原点位置（默认为左上角，可以平移到中心点）
Canvas.rotate(float degrees, float px, float py) 旋转
Canvas.scale(float sx, float sy, float px, float py) 放缩
skew(float sx, float sy) 错切

2.2使用 Matrix 来做常见和不常见的二维变换；

创建 Matrix 对象；
调用 Matrix 的 pre/postTranslate/Rotate/Scale/Skew() 方法来设置几何变换；
使用 Canvas.setMatrix(matrix) 或 Canvas.concat(matrix) 来把几何变换应用到 Canvas。


2.3使用 Camera 来做三维变换。




 *

 */

public class CanvasDemo extends View {



    Paint paint;
    Paint paint1;

    public CanvasDemo(Context context) {
        super(context);
        init();
    }

    public CanvasDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);//设置了着色器（Shader），setColor就不起作用了（****）
        //paint.setStyle(Paint.Style.STROKE);//绘制渐变的圆环
        paint.setStyle(Paint.Style.FILL);//绘制渐变的圆
        paint.setAntiAlias(true);


        paint1 = new Paint();
        paint1.setStrokeWidth(2);
        paint1.setColor(Color.RED);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);

        //drawClipRect(canvas);
        //drawClipPath(canvas);

       // drawMatrix(canvas);

       // drawCameraRotateX(canvas);
        //drawCameraRotateY(canvas);
        drawCameraRotateZ(canvas);


    }


    /*-------------------------------clipXX--------------------------------------------------------*/
    private void drawClipRect(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        canvas.save();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        canvas.clipRect(-100, -100, 100, 100);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
        canvas.restore();

    }


    private void drawClipPath(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        canvas.save();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        Path path = new Path();
        RectF rectF = new RectF(-getWidth() / 2 + 50, -100, getWidth() / 2 - 50, 100);
        path.addOval(rectF, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
        canvas.restore();


    }


    private void drawMatrix(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);

        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.preScale(0.5f,0.5f);
        matrix.postTranslate(50, 50);
        matrix.postRotate(30);

        canvas.save();
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
        canvas.restore();

    }


    private void drawCameraRotateX(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        canvas.save();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);

        Camera camera= new Camera();
        camera.save();
        camera.rotateX(30); // 旋转 Camera 的三维空间
        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
        camera.restore();

        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
        canvas.restore();

    }

    private void drawCameraRotateY(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        canvas.save();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);

        Camera camera= new Camera();
        camera.save();
        camera.rotateY(30); // 旋转 Camera 的三维空间
        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
        camera.restore();

        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
        canvas.restore();

    }

    private void drawCameraRotateZ(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        canvas.save();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);

        Camera camera= new Camera();
        camera.save();
        camera.rotateZ(30); // 旋转 Camera 的三维空间
        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
        camera.restore();

        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
        canvas.restore();

    }

}




