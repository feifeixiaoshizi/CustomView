package com.study.customview.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.study.customview.customview.R;

import static android.animation.ValueAnimator.INFINITE;

/**
 * Created by ky on 2017/6/20.
 * 属性动画里最主要的是有一个时间因子，类型估值器利用这个时间因子计算动画过程中的值。
 *
 * 对画布的操作会影响到后续的绘制，对于原来绘制好的不受影响，例如画布旋转了30度，沿着x轴绘制了一条直线，然后
 * 把画布在旋转回来，则沿着x轴绘制的直线就会相对于现在的x轴旋转了30度。也就是说画布旋转回去，但是绘制的直线不会随着
 * 画布旋转的。（*****）
 *
 * 对画布的操作只是改变和画布的坐标系，与之前具体的绘制的内容无关，但是因为坐标系变了所以会影响后续的绘制。（***）
 *
 * 思路： 画布 + 画笔 + 路径 + 动画 + 不断静态就是动态
 *
 * ps：尽量使用画布+path来绘制各种图形
 *
 */
public class LoadingView extends View {
    float distance;

    private Paint mArrowPaint;//箭头的画笔

    private Paint mArcPaint;//外圆环的画笔
    private RectF mArcRectF;//用来画圆环用

    private float mStartSweepValue;//圆环开始角度
    private float mTargetPercent = 100f;//设置目标的百分比(也就是后台返回的数据)
    private float mCurrentPercent;//当前百分比
    private float mRadius = 80;//圆的半径

    private int arrowColor;
    private int ringColor;

    private int width;
    private int height;
    private ValueAnimator valueAnimator;
    //是否完成开始
    private boolean isStarted = false;
    private String TAG = "TestCanvansView";

    public LoadingView(Context context) {
        super(context);
        //初始化数据
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 自定义属性，attr
        // 使用TypedArray <declare-styleable name="LoadingRing">
        //自定义属性 values/attr
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingRing);
        //箭头颜色  默认 - 白色
        arrowColor = typedArray.getColor(R.styleable.LoadingRing_arrow_color, 0xff000000);
        //外圆环的颜色  默认 - 灰色
        ringColor = typedArray.getColor(R.styleable.LoadingRing_ring_color, 0xff000000);
        mRadius = typedArray.getDimension(R.styleable.LoadingRing_radius_size, 30);
        Log.d(TAG, "mRadius:" + mRadius);
        typedArray.recycle();
        //初始化数据
        init(context);
    }


    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        //圆环开始角度 -90° 正北方向
        mStartSweepValue = -80;

        //当前百分比
        mCurrentPercent = 0;

        //设置箭头的画笔
        mArrowPaint = new Paint();
        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setColor(arrowColor);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setStrokeWidth((float) (0.075 * mRadius));

        //设置外圆环的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(ringColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth((float) (0.075 * mRadius));
        initAnimator();
        //valueAnimator.start();


    }

    //主要是测量wrap_content时候的宽和高，因为宽高一样，只需要测量一次宽即可，高等于宽
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(widthMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (2 * mRadius > Math.min(w, h)) {
            mRadius = Math.min(w, h) / 2;
        }


    }

    //当wrap_content的时候，view的大小根据半径大小改变，但最大不会超过屏幕
    private int measure(int measureSpec) {
        int result = 0;
        //1、先获取测量模式 和 测量大小
        //2、如果测量模式是MatchParent 或者精确值，则宽为测量的宽
        //3、如果测量模式是WrapContent ，则宽为 直径值 与 测量宽中的较小值；否则当直径大于测量宽时，会绘制到屏幕之外；
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (1.075 * mRadius * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;

    }

    //开始画中间圆、文字和外圆环
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(Color.BLUE);
        if (isStarted) {
            drawFreshing(canvas);
        } else {
            drawStarting(canvas);
        }

    }

    /*绘制正在刷新的效果*/
    private void drawFreshing(Canvas canvas) {
        canvas.translate(width / 2, height / 2);
        //默认的画布的坐标原点是左上角，通过位移把坐标原点设置到画布的中心，然后保存此时画布坐标原点为画布中心的状态。（*****）
        canvas.save();
        drawArrow(canvas);
        //绘制完箭头后，把画布的状态返回到刚才保留的状态，即画布的坐标原点位于画布的中心，然后在此状态下继续绘制后续的图形。（*****）
        canvas.restore();
        //旋转的角度
        canvas.rotate(360 * distance);
        mArcRectF = new RectF(-mRadius, -mRadius, +mRadius, +mRadius);

        Path pathArc = new Path();
        pathArc.addArc(mArcRectF, -80, 340);
        canvas.drawPath(pathArc, mArcPaint);
        //canvas.drawArc(mArcRectF, mStartSweepValue ,mCurrentAngle, false, mArcPaint);

    }

    private void drawStarting(Canvas canvas) {
        //绘制箭头
        canvas.translate(width / 2, height / 2);
        canvas.save();
        drawArrow(canvas);
        canvas.restore();

        Path pathArc = new Path();
        //不断的绘制
        mArcRectF = new RectF(-mRadius, -mRadius, +mRadius, +mRadius);
        pathArc.addArc(mArcRectF, -80, 340);
        PathMeasure pathMeasure = new PathMeasure(pathArc, false);
        //判断当前百分比是否小于设置目标的百分比
        if (mCurrentPercent <= mTargetPercent) {
            Path dest = new Path();
            float length = pathMeasure.getLength();
            //获取完整路径的一部分路径作为最终要绘制的路径。
            pathMeasure.getSegment(0, length * mCurrentPercent / mTargetPercent, dest, true);
            canvas.drawPath(dest, mArcPaint);
        }
    }

    private void drawArrow(Canvas canvas) {
        //定义单位1
        float unit = mRadius / 3;
        //开始绘制箭头
        Path path = new Path();
        path.moveTo(-unit, unit);
        path.lineTo(0, unit * 2);
        path.lineTo(unit, unit);
        path.moveTo(0, unit * 2);
        path.lineTo(0, -unit * 2);
        canvas.drawPath(path, mArrowPaint);

    }

    public void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
        valueAnimator.setRepeatCount(INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                invalidate();


            }
        });


    }

    /**
     * @param percent 取值范围0-100
     */
    public void setPercent(float percent) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 100) {
            percent = 100;
        }
        this.mCurrentPercent = percent;
        //每次设置了比例后要重新绘制。
        invalidate();
    }


    public void startAnimation() {
        isStarted = true;
        if (valueAnimator != null && !valueAnimator.isStarted()) {
            valueAnimator.start();
        }
    }

    //停止动画
    public void stopAnimation() {
        isStarted = false;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public int getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        mArrowPaint.setColor(arrowColor);
        invalidate();
    }

    public int getRingColor() {
        return ringColor;
    }

    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
        mArcPaint.setColor(ringColor);
        invalidate();
    }

}
