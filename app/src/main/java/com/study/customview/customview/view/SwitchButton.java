package com.study.customview.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.study.customview.customview.R;


/**
 * Created by Administrator on 2016/4/15 0015.
 * 事件传递：
 *
 *
 */
public class SwitchButton extends View {

    private boolean currentState = true; // 当前开关的状态, 默认为: 关闭
    private Bitmap mSwitchBackground; // 开关的背景图片
    private Bitmap mSlideButtonBackground; // 滑动块的图片
    private int currentX; // 当前移动时x轴的值.
    private boolean isTouching = false; // 是否正在触摸滑动中, 默认为: 没有正在滑动

    private OnToggleStateChangedListener mOnToggleStateChangedListener; // 用户回调的接口

    /**
     * 当在java代码中创建此对象时, 需要使用此构造函数.
     * @param context
     */
    public SwitchButton(Context context) {
        super(context);
    }

    /**
     * 当在xml代码中引用此自定义控件时, 需要使用此构造函数.
     * @param context
     * @param attrs 在布局文件中声明的各种属性.
     */
    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSwitchBackgroundResource(R.mipmap.button_on);
        //初始化给定按钮的图像，在onmeasure()中进行使用该对象进行测量
        setSlideButtonBackgroundResource(R.mipmap.button_dot);



    }

    /**
     * 设置当前开关的背景图片id
     * @param switchBackground
     */
    public void setSwitchBackgroundResource(int switchBackground) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), switchBackground);
    }

    /**
     * 设置滑动块的图片id,
     * @param slideButtonBackground
     */
    public void setSlideButtonBackgroundResource(int slideButtonBackground) {
        mSlideButtonBackground = BitmapFactory.decodeResource(getResources(), slideButtonBackground);
    }

    /**
     * 设置当前开关的状态.
     * @param b
     */
    public void setCurrentState(boolean b) {
        this.currentState = b;
    }

    /**
     * 当前控件需要测量宽和高时, 触发此方法.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置当前控件的宽和高和背景图片的宽和高一样.
        setMeasuredDimension(mSwitchBackground.getWidth(), mSwitchBackground.getHeight());


    }

    /**
     * 当前控件需要绘制出来时, 调用此方法.
     *
     * @param canvas 画画板, 使用canvas所画出来的东西, 都是作为当前控件, 在屏幕上显示.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 1. 把背景图片平铺在当前控件中.
        canvas.drawBitmap(mSwitchBackground, 0, 0, null);

        if(isTouching) { // 当前正在拖动中, 应该根据currentX来处理滑动块的位置.
            // 根据当前x轴的值, 来动态的修改滑动块的位置.
            int left = currentX - mSlideButtonBackground.getWidth() / 2;
            int height =4;
            if(left < 0) { // 超出了左边界, 应该一直设置为0
                left = 2;
            } else if(left > mSwitchBackground.getWidth() - mSlideButtonBackground.getWidth()) {
                left = mSwitchBackground.getWidth() - mSlideButtonBackground.getWidth()-2;
                height=mSwitchBackground.getHeight()-mSlideButtonBackground.getHeight();
            }
            canvas.drawBitmap(mSlideButtonBackground, left, height/2, null);
        } else { // 当前没有触摸滑动中, 需要根据currentState来处理滑动块的位置.
            // 2. 根据当前的状态currentState来绘制滑动块的位置.
            if(currentState) {
                setSwitchBackgroundResource(R.mipmap.button_on);
                // 当前开关处于打开的状态, 把滑动块画在当前控件的右边.
                int left = mSwitchBackground.getWidth() - mSlideButtonBackground.getWidth()-2;
                int  height=mSwitchBackground.getHeight()-mSlideButtonBackground.getHeight();
                canvas.drawBitmap(mSlideButtonBackground, left, height/2, null);
            } else {
                setSwitchBackgroundResource(R.mipmap.button_off);
                int  height=mSwitchBackground.getHeight()-mSlideButtonBackground.getHeight();
                // 当前开关处于关闭的状态, 把滑动块画在当前控件的左边.
                canvas.drawBitmap(mSlideButtonBackground, 2, height/2, null);
            }
        }
    }

    /**
     * 当前控件被触摸时触发此方法.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                //点击点距离View左边的距离
                currentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                currentX = (int) event.getX();

                // 判断当前偏向于那一边

                int center = mSwitchBackground.getWidth() / 2;

                // 如果当前滑动块的中心点 > 背景图片中心点的位置时, 把当前状态置为true, 是打开的状态.
                // 如果当前滑动块的中心点 < 背景图片中心点的位置时, 把当前状态置为false, 是关闭的状态.
                boolean state = currentX > center; // 最新的状态
                if(state){
                    setSwitchBackgroundResource(R.mipmap.button_on);

                }else{
                    setSwitchBackgroundResource(R.mipmap.button_off);
                }

                // 当前是抬起, 判断当前状态是否已经改变了, 如果改变, 调用用户的回调接口把改变后的状态, 传递过去.
                if(state != currentState && mOnToggleStateChangedListener != null) {
                    // 当前状态已经被修改了, 需要回调用户.
                    mOnToggleStateChangedListener.onToggleStateChanged(state);
                }

                // 把最新的状态赋值给当前类的成员变量: 当前开关的状态.
                currentState = state;
                break;
            default:
                break;
        }

        // 手动触发onDraw方法的重绘.
        invalidate(); // 刷新当前控件, 会引起onDraw方法调用.
        return true; // 自己处理滑动的事件
    }

    public void setOnToggleStateChangedListener(OnToggleStateChangedListener listener) {
        this.mOnToggleStateChangedListener = listener;
    }

    /**
     * @author andong
     * 当开关状态改变时的监听事件.
     */
    public interface OnToggleStateChangedListener {

        /**
         * 开关状态改变时, 触发此方法.
         * @param currentState true 打开, false 关闭
         */
        public void onToggleStateChanged(boolean currentState);

    }
}
