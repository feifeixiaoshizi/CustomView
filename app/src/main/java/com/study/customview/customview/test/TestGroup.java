package com.study.customview.customview.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 方法调用的顺序：
 * onFinishInflate：从xml--》Java对象
 * onMeasure:测量宽和高
 * onSizeChanged:W:840H:840oldW:0oldH:0：从原始宽和高（0,0）到测量后的宽和高（xx,xx）
 * onLayout:l:48t:336r:888b:1176:布局在父View上的位置。
 * <p>
 * 事件的传递分析：
 * <p>
 * 1：子View可以处理该事件对象
 * Activity的PhoneWindow的dispatchTouchEvent（） --》decoreView的dispatchTouchEvent（）---》
 * 父ViewGroup的dispatchTouchEvent---》父ViewGroup的onInterceptTouchEvent（）----》子View的dispatchTouchEvent（）--》
 * 子View的OnTouchListener.onTouch()/onTouchEvent（）
 * <p>
 * <p>
 * 2：子View无法处理事件对象
 * Activity的PhoneWindow的dispatchTouchEvent（） --》decoreView的dispatchTouchEvent（）---》
 * 父ViewGroup的dispatchTouchEvent---》父ViewGroup的onInterceptTouchEvent（）----》子View的dispatchTouchEvent（）--》
 * 子View的OnTouchListener.onTouch()/onTouchEvent（）--->如果子View的dispatchTouchEvent（）返回为false也就是子View处理不了该事件--》
 * 父ViewGroup会调用 super.dispatchTouchEvent(ev)；---》父ViewGroup作为View调用其View.dispatchTouchEvent（）--->
 * 父ViewGroup作为View调用其View.onTouchEvent（）来处理子View无法处理的事件。
 * <p>
 * 3：事件传递过程中最重要的事件是Down事件对象，尽量不要拦截该事件，一旦拦截了该事件，其下的子View将再也不会接收到任何事件。
 */

public class TestGroup extends ViewGroup {
    private String TAG = "Test";

    public TestGroup(Context context) {
        super(context);
    }

    public TestGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure:");
        this.widthMeasureSpec=widthMeasureSpec;
        this.heightMeasureSpec= heightMeasureSpec;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //遍历每个测量子控件，测量子View的时候，既要考虑到父的测量模式和可用空间，也要考虑到子View的具体的测量模式。（*****）
            //先测量好子控件的宽和高后在后续的layout过程中才可以使用测试的宽和高。
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }


    /*

    measureChild方法的分析：

    *  protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {

        //得到child布局中的layout_width和layout_height的参数值
        final LayoutParams lp = child.getLayoutParams();
        //根据父布局的测量限制和自身的布局中设置的width来计算出合适的width
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight, lp.width);

        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom, lp.height);

        //测量子view的大小
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    *
    *
    *
    *
    * 根据父控件的测量模式分类对子view的尺寸进行设置。
    * 在父控件的每种测量模式下，都要再考虑子view的每种可能的测量模式。
     public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
        // Parent has imposed an exact size on us
        //在父控件是精确的测量模式下，分类计算子view的测量模式
        case MeasureSpec.EXACTLY:
            //根据子view设置的测量模式，分别计算在可能出现测量模式下分别对应的测量值。
            //最后把子view的测量模式和测量的值压缩为MeasureSpec返回。
            if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size. So be it.
                resultSize = size;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

    *
    *
    * */





    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //在自定义控件的时候，尽量不要拦截Down事件，让子View也可以获取Down事件。如果不分事件类型直接全部拦截了，那么子
        //View以后是无法再接收到任何事件对象的。
        Log.d(TAG, "onSizeChanged:" + "W:" + w + "H:" + h + "oldW:" + oldw + "oldH:" + oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout:" + "l:" + l + "t:" + t + "r:" + r + "b:" + b);
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int height = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();
            int left = child.getLeft();
            int top = child.getTop();
            Log.d(TAG, "height:" + height + "width:" + width + "left:" + left + "top:" + top);
            if (i == 0) {
                //以父View的左上为顶点（0,0）坐标，开始布局子View在父View中的位置。（******）
                getChildAt(i).layout(0, 0, width, height);
            } else {
                View preChild = getChildAt(i - 1);
                int nextL = preChild.getRight() + 6;
                int nextT = preChild.getTop();
                int nextR = nextL + preChild.getMeasuredWidth();
                int nextB = nextT + preChild.getMeasuredHeight();
                child.layout(nextL, nextT, nextR, nextB);
            }

            Log.d(TAG, "height:" + height + "width:" + width + "left:" + left + "top:" + top);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "testGroup:dispatchTouchEvent:" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "testGroup:onInterceptTouchEvent:" + ev.getAction());
        //return true;
        return super.onInterceptTouchEvent(ev);
    }


    int downX;
    int downY;
    View target = null;
    int widthMeasureSpec;
    int heightMeasureSpec;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "testGroup:onTouchEvent:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点击点距离当前ViewGroup的左边的距离（ps：距离的是当前ViewGroup）
                //ps：在ViewGroup中可以使用getX来计算偏移量，然后用在子View上，但是在具体的View对象上要使用getRawX（）来计算偏移量。（*****）
                downX = (int) event.getX();
                downY = (int) event.getY();
                target = getTargetView(event);

                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) event.getX()-downX;
                int offsetY = (int) event.getY()-downY;
                if(target!=null){
                    int left = target.getLeft();
                    int top = target.getTop();
                    int right = target.getRight();
                    int bottom= target.getBottom();
                    Log.d(TAG,"target:"+"left:"+left+"top:"+top+"top:"+right+"bottom:"+bottom);
                            //计算偏移量
                    target.layout(target.getLeft()+offsetX,target.getTop()+offsetY,target.getRight()+offsetX,target.getBottom()+offsetY);

                }

                downX = (int) event.getX();
                downY = (int) event.getY();


                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        // 手动触发onDraw方法的重绘.
        //requestLayout(); // 刷新当前控件, 会引起onDraw方法调用.
        return true; // 自己处理滑动的事件
    }

    public View getTargetView(MotionEvent ev) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            if (inRangeOfView(getChildAt(i), ev)) {
                return getChildAt(i);
            }
        }
        return null;
    }

    public  boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        Log.d(TAG,"x:"+x+"y:"+y);
        Log.d(TAG,"X:"+ev.getX()+"Y:"+ev.getY());
        Log.d(TAG,"RawX:"+ev.getRawX()+"RawY:"+ev.getRawY());
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y
                || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }


}
