package com.study.customview.customview.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 1: 得到SurfaceHolder
 * 2：得到Canvans
 * 3：在子线程中实现绘制的方法
 *
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;
    private Paint paint1;
    private Path path;


    public MySurfaceView(Context context) {
        super(context);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private void initView() {
        surfaceHolder = getHolder();
        //设置回调函数
        surfaceHolder.addCallback(this);
        //设置可点击事件
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        path = new Path();

        //初始化画笔
        paint = new Paint();
        //paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        //初始化画笔
        paint1 = new Paint();
        //paint.setStyle(Paint.Style.STROKE);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(2);
        paint1.setAntiAlias(true);
        paint1.setColor(Color.BLUE);



        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //在监听中surfaceCreated之后就可以开启子线程在画布上开始绘制了
        //开启线程开始绘制
        new Thread(this).start();



    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        for (int i = 1; i < 101; i++) {
            draw(i);
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    //绘制圆形,理解：每次调用draw方法都是一次重新的绘制，之前的绘制到画布上的内容会丢失，要在一个空白的画布上重新绘制。
    private void draw(float radius) {
        try {
            //锁定画布并返回画布对象
            //每次的绘制都要先lockCanvas然后绘制完毕后再unlockCanvas
            canvas = surfaceHolder.lockCanvas();
            //设置画布的中心点
            canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            //接下去就是在画布上进行一下draw
            canvas.drawColor(Color.WHITE);
            drawCircle(canvas,radius);
            drawBoder(canvas,radius);

        } catch (Exception e) {
        } finally {
            //当画布内容不为空时，才post，避免出现黑屏的情况。
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawCircle(Canvas canvas,float radius){
        canvas.save();
        canvas.rotate(-360*radius/100);
        //路径重置，就是清空原来的添加到路径对象中的路径。（*****）否则会把原来的路径加上现在的路径一起绘制起来。
        path.reset();
        path.addCircle(0, 0, radius, Path.Direction.CCW);
        //clearCanvans();
        //canvas.drawPath(path, paint);
        canvas.drawCircle(0,0,radius,paint);

        RectF rectF = new RectF(-radius/2,radius/2,radius/2,-radius/2);
        path.reset();
        path.addRect(rectF, Path.Direction.CCW);
        // clearCanvans();
        canvas.drawPath(path, paint1);
        canvas.restore();

    }


    private void drawBoder(Canvas canvas,float radius){
        //路径重置，就是清空原来的添加到路径对象中的路径。（*****）否则会把原来的路径加上现在的路径一起绘制起来。
        path.reset();
        //旋转后对之前的绘制没有影响，但是对后续的绘制会造成影响。原来的内容不会旋转但是后续的内容会旋转（*******）
        canvas.rotate(360*radius/100);
        RectF rectF = new RectF(-radius,radius,radius,-radius);
        path.addRect(rectF, Path.Direction.CCW);
        //测量path的工具类
        PathMeasure pathMeasure = new PathMeasure(path,true);
        Path dpath = new Path();
        //startWithMoveTo：是否把截取的path，moveto到起始点。如果为true则移动到起始点，否则移动到坐标原点。（*****）
        pathMeasure.getSegment(0,pathMeasure.getLength()*radius/100,dpath,true);
        //pathMeasure.getSegment(0,pathMeasure.getLength()*radius/100,dpath,false);
        // clearCanvans();
        canvas.drawPath(dpath, paint1);


    }

    private void clearCanvans() {
        Paint p = new Paint();
        //清屏
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }


}
