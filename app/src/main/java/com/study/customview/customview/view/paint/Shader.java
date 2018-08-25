package com.study.customview.customview.view.paint;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ComposeShader;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
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
import android.widget.Toast;

import com.study.customview.customview.R;

import static android.R.attr.bitmap;

/*
 *Paint：
 * 1：设置基本颜色和着色器（Shader） :颜色就是你要绘制的图形是用颜色填充的，着色器就是你要绘制的图形是用着色填充的。
 * 2：setColorFilter(ColorFilter colorFilter) 控制图片中颜色的值。
 * 3：setXfermode：重叠的部分如何显示。

 */

public class Shader extends View {

    Paint paint;
    Paint paint1;

    public Shader(Context context) {
        super(context);
        init();

    }

    public Shader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Shader(Context context, AttributeSet attrs, int defStyleAttr) {
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
        //drawLinearGradient(canvas);
        //drawRadialGradient(canvas);
        //drawSweepGradient(canvas);
        //drawBitmapShader(canvas);
        //drawComposeShader(canvas);


        // drawColorFilter(canvas);
        //drawColorMatrixColorFilter(canvas);


       // drawXfermode(canvas);


       // drawPathEffect(canvas);
       // drawCornerPathEffect(canvas);
       // drawDiscretePathEffect(canvas);



        //drawMaskFilter(canvas);
        //drawMaskFilter1(canvas);
        drawEmbossMaskFilter(canvas);



    }
/*-------------------------------------------------------着色器--------------------------------------------------*/

    private void drawLinearGradient(Canvas canvas) {
        canvas.drawRect(-200, -200, 200, 200, paint1);
        //线性着色器（Shader）
        LinearGradient linearGradient = new LinearGradient(-200, -200, 200, 200, Color.RED, Color.BLUE, android.graphics.Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawCircle(0, 0, 200, paint);

    }


    private void drawRadialGradient(Canvas canvas) {
        canvas.drawRect(-200, -200, 200, 200, paint1);
        //辐射渐变着色器（Shader）
        RadialGradient linearGradient = new RadialGradient(0, 0, 200, Color.RED, Color.BLUE, android.graphics.Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawCircle(0, 0, 200, paint);

    }

    private void drawSweepGradient(Canvas canvas) {
        canvas.drawRect(-200, -200, 200, 200, paint1);
        //扫描渐变着色器（Shader）
        SweepGradient linearGradient = new SweepGradient(0, 0, Color.RED, Color.BLUE);
        paint.setShader(linearGradient);
        canvas.drawCircle(0, 0, 200, paint);

    }

    private void drawBitmapShader(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        BitmapShader bitmapShader = new BitmapShader(bitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        canvas.drawRect(-200, -200, 200, 200, paint1);
        paint.setShader(bitmapShader);
        //用图片填充绘制的圆，效果就相当于截取了一个圆形图片。
        canvas.drawCircle(0, 0, 200, paint);

    }

    private void drawComposeShader(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        BitmapShader bitmapShader = new BitmapShader(bitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        canvas.drawRect(-200, -200, 200, 200, paint1);
        LinearGradient linearGradient = new LinearGradient(-100, -100, 100, 100, Color.RED, Color.BLUE, android.graphics.Shader.TileMode.CLAMP);

        ComposeShader composeShader = new ComposeShader(linearGradient, bitmapShader, PorterDuff.Mode.SRC_OVER);
        paint.setShader(composeShader);
        //用图片填充绘制的圆，效果就相当于截取了一个圆形图片。
        canvas.drawCircle(0, 0, 200, paint);

    }

    /*-------------------------------------------------------ColorFilter--------------------------------------------------*/

    private void drawColorFilter(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        ColorFilter lightingColorFilter = new LightingColorFilter(0x00ffff, 0x000000);
        paint.setColorFilter(lightingColorFilter);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
    }


    //颜色矩阵过滤器
    private void drawColorMatrixColorFilter(Canvas canvas) {
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);

        //红 绿 蓝 透明度
        float[] colorArray = new float[]{
                0.5f, 0, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0.4f, 0
        };

        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorArray);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);
    }

/*-------------------------------------------Xfermode--------------------------------------------------*/

    private void drawXfermode(Canvas canvas){
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);

        //1：实现离屏渲染  2：注意先后顺序
        int saved = canvas.saveLayer(null, null);
        //SRC:显示源  SRC_OVER：目标和源重叠的部分，源在上面    SRC_ATOP源在目标上，重叠之外的源丢弃    SRC_IN：源在目标里面的部分    SRC_OUT ：源在目标外的部分
        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        canvas.drawCircle(0, 0,100, paint); // 画圆（target）

        paint.setXfermode(xfermode); // 设置 Xfermode
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);//src
        paint.setXfermode(null); // 用完及时清除 Xfermode

        canvas.restoreToCount(saved);
    }


/*-------------------------------------------PathEffect--------------------------------------------------*/

    private void drawPathEffect(Canvas canvas){
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        PathEffect pathEffect = new DashPathEffect(new float[]{5, 5}, 5);
        paint.setPathEffect(pathEffect);
        canvas.drawCircle(0,0,200,paint);
    }

    private void drawCornerPathEffect(Canvas canvas){
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        PathEffect pathEffect = new CornerPathEffect(60);
        paint.setPathEffect(pathEffect);
        Path path = new Path();
        path.moveTo(-getWidth()/2,0);
        path.lineTo(-200,-100);
        path.lineTo(-100,0);
        path.lineTo(100,-100);
        path.lineTo(200,0);
        path.lineTo(300,-100);
        canvas.drawPath(path,paint);
    }

    private void drawDiscretePathEffect(Canvas canvas){
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        PathEffect pathEffect = new DiscretePathEffect(20, 8);
        paint.setPathEffect(pathEffect);
        Path path = new Path();
        path.moveTo(-getWidth()/2,0);
        path.lineTo(-200,-100);
        path.lineTo(-100,0);
        path.lineTo(100,-100);
        path.lineTo(200,0);
        path.lineTo(300,-100);
        canvas.drawPath(path,paint);
    }


/*--------------------------------MaskFilter--------------------------------------------------------*/

    private void drawMaskFilter(Canvas canvas){
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        paint.setMaskFilter(new BlurMaskFilter(200, BlurMaskFilter.Blur.OUTER));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);//src

    }

    private void drawMaskFilter1(Canvas canvas){
        //关闭硬件加速，setMaskFilter才会起作用。（****）
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        paint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));
        canvas.drawCircle(0,0,100,paint);



    }

    private void drawEmbossMaskFilter(Canvas canvas){
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint1);
        paint.setMaskFilter(new EmbossMaskFilter(new float[]{0, 10, 10}, 0.2f, 8, 10));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        canvas.drawBitmap(bitmap, -getWidth() / 2, -getHeight() / 2, paint);//src

    }





}




