package com.study.customview.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.study.customview.customview.R;

/**X
 * Created by Administrator on 2018/2/8 0008.
 * 1:自定义控件：onmeasure  onlayout（X）  ondraw
 * 2：事件 dispatchEvent onInterceptEvent（X） onTochEvent
 *
 */

public class PathView extends View {

    Paint paint;
    Paint paint1;

    int space=8;//间隔度数
    int count=3;


    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }
    Matrix mMatrix;
    Bitmap mBitmap;
    public  void init(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);

        paint1 = new Paint();
        paint1.setColor(Color.GREEN);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(1);
        paint1.setAntiAlias(true);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        canvas.translate(getWidth()/2,getHeight()/2);
        //draw1(canvas,path);
      //  draw2(canvas,path);
        //draw3(canvas,path);
        draw4(canvas,path);

    }



    public void draw1(Canvas canvas,Path path){
        RectF rectF= new RectF(-250,-250,250,250);
        //：addArc是在原来的基础上接着添加路径。（*****）
        path.addArc(rectF,0,90); //addArc()不受rotate（）方法影响。
        canvas.rotate(30);
        path.addArc(rectF,0,90);//(***)
        //在canvas旋转前添加了路径，再旋转后添加后又重新添加了路径，因为路径坐标一致，导致最终绘制的时候两个路径是重叠的。
        //但是重合的两条路径依然是两条路径。

        //PathMeasure和path关联时，path一定是已经准备好了，不再发生变化了。否则要setPath下。（***）
        PathMeasure pathMeasure = new PathMeasure(path,false);
        Log.d("PahtView","length1:"+pathMeasure.getLength());//getLength得到的是一条线段的长度。
        boolean next = pathMeasure.nextContour();//通过nextContour跳转到下一条线段来测量长度。
        Log.d("PahtView","next1:"+next+"length1:"+pathMeasure.getLength());
        canvas.drawPath(path,paint);

    }

    public void draw2(Canvas canvas,Path path){
        RectF rectF= new RectF(-250,-250,250,250);
        //addArc是在原来的基础上接着添加路径。（*****）
        path.addArc(rectF,0,90); //addArc()不受rotate（）方法影响。
        canvas.drawPath(path,paint);//先把第一个圆弧画在画布上。
        canvas.rotate(90);

        path.reset();//可以把path中原有的先清除掉，再添加新的路径。
        //旋转画布后，在路径中添加第二个圆弧，此时就是画布旋转后，在path中添加了一个（0，90）一个（10,90）的圆弧，两个圆弧重叠了。（***）
        path.addArc(rectF,10,90);
        paint.setColor(Color.GREEN);
        canvas.drawPath(path,paint);//此时画的是第一个圆弧(0,90)和第二个圆弧(10,90)交集后形成的路径(0,90)。

        //PathMeasure和path关联时，path一定是已经准备好了，不再发生变化了。否则要setPath下。（***）
        PathMeasure pathMeasure = new PathMeasure(path,false);
        Log.d("PahtView","length2:"+pathMeasure.getLength());//getLength得到的是一条线段的长度。
        boolean next = pathMeasure.nextContour();//通过nextContour跳转到下一条线段来测量长度。
        Log.d("PahtView","next2:"+next+"length2:"+pathMeasure.getLength());


    }



    float[] pos=new float[2] ;
    float[] tan=new float[2];
    float currentValue=0;


    public void draw3(Canvas canvas,Path path){
        RectF rectF= new RectF(-250,-250,250,250);
        //addArc是在原来的基础上接着添加路径。（*****）
        path.addArc(rectF,0,180); //addArc()不受rotate（）方法影响。
        currentValue += 0.005;// 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }
        //PathMeasure和path关联时，path一定是已经准备好了，不再发生变化了。否则要setPath下。（***）
        PathMeasure pathMeasure = new PathMeasure(path,true);
        float length = pathMeasure.getLength();
        boolean get=  pathMeasure.getPosTan((length*currentValue),pos,tan);
        Log.d("PahtView","get:"+get+"x:"+pos[0]+"y:"+pos[1]+"tanx:"+tan[0]+"tany:"+tan[1]);

        //图片在轨迹上的每个点都有其坐标和相对于起始的旋转角度。（*****）
        mMatrix.reset();// 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度

        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);   // 旋转图片
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合

        canvas.drawPath(path, paint);// 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, paint);


        invalidate();
    }
 public void draw4(Canvas canvas,Path path){
        RectF rectF= new RectF(-250,-250,250,250);
        //addArc是在原来的基础上接着添加路径。（*****）
        path.addArc(rectF,0,180); //addArc()不受rotate（）方法影响。
        currentValue += 0.005;// 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }
        //PathMeasure和path关联时，path一定是已经准备好了，不再发生变化了。否则要setPath下。（***）
        PathMeasure pathMeasure = new PathMeasure(path,true);
        float length = pathMeasure.getLength();
        boolean get=  pathMeasure.getPosTan((length*currentValue),pos,tan);
        Log.d("PahtView","get:"+get+"x:"+pos[0]+"y:"+pos[1]+"tanx:"+tan[0]+"tany:"+tan[1]);
        //图片在轨迹上的每个点都有其坐标和相对于起始的旋转角度。（*****）
        mMatrix.reset();// 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度

        pathMeasure.getMatrix(length*currentValue, mMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        //mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);   // 旋转图片
        //mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合

         //利用矩阵实现对图片的变化操作（*****）
         mMatrix.preScale(0.5f,0.5f);
         //preTranslate(float x, float y)按照之前的缩放比例缩放偏移量。
         mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);
         canvas.drawPath(path, paint);// 绘制 Path
         canvas.drawBitmap(mBitmap, mMatrix, paint);
         invalidate();
    }



}




