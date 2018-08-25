package com.study.customview.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.animation.ValueAnimator.INFINITE;

/**X
 * Created by Administrator on 2018/2/8 0008.
 * 1:自定义控件：onmeasure  onlayout（X）  ondraw
 * 2：事件 dispatchEvent onInterceptEvent（X） onTochEvent
 *
 */

public class CircleView1 extends View {

    Paint paint;
    Paint paint1;

    int space=2;//间隔度数
    int count=5;

    Path path;
    Path path1;
    Path path2;


    public CircleView1(Context context) {
        super(context);
        init();
    }

    public CircleView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    public  void init(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(80);
        paint.setAntiAlias(true);

        paint1 = new Paint();
        paint1.setColor(Color.GREEN);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(100);
        paint1.setAntiAlias(true);

        //initAnimator();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Toast.makeText(this.getContext(),"width:"+getMeasuredWidth(),Toast.LENGTH_LONG).show();
        path = new Path();
        path1 = new Path();
        //移动圆心到中心
        canvas.translate(getWidth()/2,getHeight()/2);
        //左上右下的坐标（控制弧形的范围大小）
        RectF rectF = new RectF(-250,-250,250,250);
       /* path.addArc(rectF,0,60);
        path1.addArc(rectF,0,60);
        canvas.save();
        canvas.drawPath(path,paint);
        canvas.restore();
        canvas.rotate(63);
        canvas.drawPath(path1,paint1);*/
        canvas.rotate(360*distance);

        int arcAngle=(360-space*count)/count;
        for(int i=0;i<count;i++){
            canvas.save();
            //圆弧
            path.addArc(rectF,0,arcAngle);
            PathMeasure pathMeasure = new PathMeasure(path,false);
            Log.d("CircleView1","length:"+pathMeasure.getLength());
            if(i%2==0){
                paint.setColor(Color.RED);
                //rectF和Stroke
                //canvas.rotate(0+i*(arcAngle+space));
                canvas.drawPath(path,paint);
            }else {
                paint.setColor(Color.BLUE);
                //rectF和Stroke
                //canvas.rotate(0+i*(arcAngle+space));
                canvas.drawPath(path,paint1);
            }
            //如果弧度的线条特变宽，仅仅是线条的中间点和rectF的边紧贴，线条的一半宽度会越出rectF的边界。

            //soomthDrawPath(canvas,path);
            canvas.restore();

        }




    }





    public void soomthDrawPath(Canvas canvas,Path path){
        PathMeasure pathMeasure = new PathMeasure();
        pathMeasure.setPath(path,false);
        Path dst = new Path();
        float length = pathMeasure.getLength();
        pathMeasure.getSegment(0, (float) (length*0.8),dst,true);
        canvas.drawPath(dst,paint);

    }


    float distance;
    ValueAnimator valueAnimator;
    public void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
        valueAnimator.setRepeatCount(1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                invalidate();


            }
        });
        valueAnimator.start();


    }


}




