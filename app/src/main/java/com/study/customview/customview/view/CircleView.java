package com.study.customview.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**X
 * Created by Administrator on 2018/2/8 0008.
 * 1: 自定义控件：onmeasure  onlayout（X）  ondraw
 * 2: 事件 dispatchEvent onInterceptEvent（X） onTochEvent
 *
 */

public class CircleView extends View {

    Paint paint;
    Paint paint1;

    int space=8;//间隔度数
    int count=3;


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        paint1.setStrokeWidth(1);
        paint1.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Toast.makeText(this.getContext(),"width:"+getMeasuredWidth(),Toast.LENGTH_LONG).show();
        //左上右下的坐标（控制弧形的范围大小）
        RectF rectF1 = new RectF(1,1,500,500);
        RectF rectF = new RectF(1+paint.getStrokeWidth()/2,1+paint.getStrokeWidth()/2,500-paint.getStrokeWidth()/2,500-paint.getStrokeWidth()/2);
        canvas.drawRect(rectF1,paint1);
        paint1.setColor(Color.WHITE);
        canvas.drawRect(rectF,paint1);
        //false不闭合，true闭合
        //canvas.drawArc(rectF,0,89,false,paint);
        //canvas.drawArc(rectF,91,90,false,paint1);
        int arcAngle=(360-space*count)/count;
        for(int i=0;i<count;i++){
            if(i%2==0){
                paint.setColor(Color.RED);
            }else {
                paint.setColor(Color.BLUE);
            }
            //如果弧度的线条特变宽，仅仅是线条的中间点和rectF的边紧贴，线条的一半宽度会越出rectF的边界。
            //rectF和Stroke
            canvas.drawArc(rectF,0+i*(arcAngle+space),arcAngle,false,paint);

        }


    }
}




