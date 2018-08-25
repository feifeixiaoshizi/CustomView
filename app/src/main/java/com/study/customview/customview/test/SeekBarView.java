package com.study.customview.customview.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.study.customview.customview.R;


/**
 * 在View的dispatchTouchEvent方法里面，对事件的分发分为两种情况：
 * 1：如果存在OnTouchListener,则首先把事件分发给OnTouchListener中的onTouch（）方法。
 * 2：否则把事件分发给View中的onTouchEvent()方法
 * <p>
 * <p>
 * 自定义控件分为两个方面：
 * 1：View的呈现：测量(measure->onMeasure) 布局(layout->onLayout) 绘制(draw->onDraw) 三个步骤
 * 1.1 View
 * 1.2 ViewGroup
 * <p>
 * 2：View的事件传递：事件分发（dispatchTouchEvent）  事件拦截(onInterceptTouchEvent)   事件处理(onTouchEvent)
 * <p>
 * 从Activity的PhoneWindow开始传递事件到decorView,然后再分发给子View。
 * <p>
 * 重点理解：ViewGroup也是View对象，也可以直接作为View使用，要使用View的中的方法，
 * 只需要是super.xxx()即可调用覆盖前的View的方法。（*****）
 * 例如：子View的onTouchEvent()方法返回为false的时候，需要调用ViewGroup的onTouchEvent（）方法，
 * ViewGroup作为View调用其dispatchTouchEvent（）方法，super.dispatchTouchEvent(),然后就可以调用到ViewGroup中的onTouchEvent方法。
 * （子类调用父类中的方法，需要使用super关键字即可，super.xxx()即可实现子类调用父类中的方法，如果想调用未重写前的方法即可使用该方调用原来的方法）
 * 坐标细节问题：
 * 坐标：以屏幕为参考物坐标（getRawX）   以父View参考物为坐标(left)     以event所属的View为参考物坐标(event.getX)
 * 1:自定义控件：onmeasure  onlayout（X）  ondraw
 * 2：事件 dispatchEvent onInterceptEvent（X） onTochEvent(MotionEvent  down   move  up  cancel)
 * 3：自定义的属性
 *    3.1 在attrs.xml的文件中通过declare-styleable name="SeekBar"定义属性，declare-styleable会生成一个名字为SeekBar的int[]数组，存放了属性名称。
 *    3.2 在布局文件中声明一个任意名字的命名空间 xmlns:seekbar="http://schemas.android.com/apk/res-auto"
 *    3.3 在控件上使用 seekbar:circleRadius="8dp"属性
 *    3.4 在初始化中通过TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBar);取出AttributeSet中自定义的属性部分，
 *    通过TypedArray根据属性索引取出属性对应的值。
 */
public class SeekBarView extends View {
    public String TAG = "SeekBarView";


    public int width;
    public int height;

    public float backGroudBarWidth;
    public float backGroudBarHeight;
    public int backGroudBarColor;

    public float frontBarWidth;
    public float frontBarHeight;
    public int frontBarColor;

    public float circleCenterX;
    public float circleCenterY;
    public float circleRadius;


    public SeekBarView(Context context) {
        super(context);

    }

    public SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,-1);

    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);

    }


    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        //获取AttributeSet中的有关在styleable中定义的属性和属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBar);
        backGroudBarColor = typedArray.getColor(R.styleable.SeekBar_backGroundColor, Color.GRAY);
        backGroudBarWidth = typedArray.getDimension(R.styleable.SeekBar_backGroundWidth, 20);
        Log.d(TAG,"backGroudBarWidth:"+backGroudBarWidth+"backGroudBarColor："+backGroudBarColor);
        //10dp  -- backGroudBarWidth:30.0 根据日志可以看出得到是dp转换后的的具体数值。


        frontBarColor = typedArray.getColor(R.styleable.SeekBar_frontGroundColor, Color.RED);
        frontBarWidth = typedArray.getDimension(R.styleable.SeekBar_frontGroundWidth, 22);


        circleRadius = typedArray.getDimension(R.styleable.SeekBar_circleRadius,28);
        Log.d(TAG,"frontBarWidth:"+frontBarWidth+"circleRadius:"+circleRadius);
        // frontBarWidth:33.0circleRadius:30.0 根据日志可以看出得到是dp转换后的的具体数值。

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        this.width = width;
        this.height = height;
        initBar();

        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();
        Log.d(TAG, "widthMode:" + widthMode + "width:" + width + "heightMode:" + heightMode + "height:" + height + "measureWidth:" + measureWidth + "measureHeight:" + measureHeight);


    }

    Bar bar = new Bar();

    private void initBar() {
        bar.backGroudBarColor=backGroudBarColor;
        bar.backGroudBarWidth = backGroudBarWidth;
        bar.backGroudBarHeight = height - 40;

        bar.frontBarColor=frontBarColor;
        bar.frontBarWidth = frontBarWidth;
        bar.frontBarHeight = height / 2;

        bar.circleRadius = circleRadius;

        bar.initPaint();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //把坐标起始点移动到左下角
        canvas.translate(0, height);
        canvas.save();
        canvas.translate(80, 0);


        bar.draw(canvas);

       /* canvas.translate(80, 0);
        bar.draw(canvas);*/
        canvas.restore();

    }


    public static class Bar {
        public float backGroudBarWidth;
        public float backGroudBarHeight;
        public int backGroudBarColor;

        public float frontBarWidth;
        public float frontBarHeight;
        public int frontBarColor;

        public float circleCenterX;
        public float circleCenterY;
        public float circleRadius;

        public Paint paintBar;
        public Paint paintBar1;


        public Bar() {

        }

        public void initPaint() {
            paintBar = new Paint();
            paintBar.setColor(backGroudBarColor);
            paintBar.setStyle(Paint.Style.FILL);
            //paintBar.setStrokeWidth(40);
            paintBar.setAntiAlias(true);


            paintBar1 = new Paint();
            paintBar1.setColor(frontBarColor);
            paintBar1.setStyle(Paint.Style.FILL);
            //paintBar1.setStrokeWidth(44);
            paintBar1.setAntiAlias(true);


        }

        public void draw(Canvas canvas) {
            drawBackgroud(canvas);
            drawFront(canvas);
            drawCircle(canvas);
        }

        public void drawBackgroud(Canvas canvas) {
            RectF rectF = new RectF((frontBarWidth - backGroudBarWidth) / 2, -backGroudBarHeight, backGroudBarWidth + (frontBarWidth - backGroudBarWidth) / 2, 0);
            canvas.drawRect(rectF, paintBar);
        }


        public void drawFront(Canvas canvas) {
            RectF rectF = new RectF(0, -frontBarHeight, frontBarWidth, 0);
            canvas.drawRect(rectF, paintBar1);
        }


        public void drawCircle(Canvas canvas) {
            canvas.drawCircle(frontBarWidth / 2, -frontBarHeight - circleRadius / 2, circleRadius, paintBar1);
        }


    }

    float downX;
    float downY;

    /*
    * 计算偏移量，在ViewGrou和View中是有区别的。只要记住Event针对的触摸点距离触摸点所属的View（View/ViewGroup）的距离即可。（****）
    * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d(TAG,"testView:onTouchEvent:"+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //在这里不能使用getX（）来计算偏移量，因为getX()是触摸点在View内，距离View左边的距离，无法把偏移量用在View上。（****）
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //更新Bar的高度信息
                upadateBarByMove(event);
                //每次移动完毕，重新设置起点。（****）
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //更新Bar的高度信息
                upadateBarByUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                break;
        }
        invalidate();
        return true;

    }


    private void upadateBarByUp(MotionEvent event) {
        //手指抬起时的触摸点的坐标
        float x = event.getX();
        float y = event.getY();
        Log.d(TAG, "x:" + x + "y:" + y);

        if (y < bar.backGroudBarHeight) {
            bar.frontBarHeight = bar.backGroudBarHeight - y;

        }


    }

    private void upadateBarByMove(MotionEvent event) {
        //使用的位移距离而不是具体的坐标（使用差值而不使用具体的点坐标）
        //计算触摸点在整个屏幕上的真实偏移量
        final float xDistance = event.getRawX() - downX;
        final float yDistance = event.getRawY() - downY;
        bar.frontBarHeight -= yDistance;
        if (bar.frontBarHeight >= bar.backGroudBarHeight - 1 * bar.circleRadius) {
            bar.frontBarHeight = bar.backGroudBarHeight - 1 * bar.circleRadius;
        }
        if (bar.frontBarHeight <= bar.circleRadius / 2) {
            bar.frontBarHeight = bar.circleRadius / 2;
        }

    }
}
