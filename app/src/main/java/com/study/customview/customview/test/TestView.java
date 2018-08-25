package com.study.customview.customview.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 *
 在View的dispatchTouchEvent方法里面，对事件的分发分为两种情况：
 1：如果存在OnTouchListener,则首先把事件分发给OnTouchListener中的onTouch（）方法。
 2：否则把事件分发给View中的onTouchEvent()方法


 自定义控件分为两个方面：
 1：View的呈现：测量(measure->onMeasure) 布局(layout->onLayout) 绘制(draw->onDraw) 三个步骤
         1.1 View
         1.2 ViewGroup

 2：View的事件传递：事件分发（dispatchTouchEvent）  事件拦截(onInterceptTouchEvent)   事件处理(onTouchEvent)

 从Activity的PhoneWindow开始传递事件到decorView,然后再分发给子View。

 重点理解：ViewGroup也是View对象，也可以直接作为View使用，要使用View的中的方法，
 只需要是super.xxx()即可调用覆盖前的View的方法。（*****）
 例如：子View的onTouchEvent()方法返回为false的时候，需要调用ViewGroup的onTouchEvent（）方法，
 ViewGroup作为View调用其dispatchTouchEvent（）方法，super.dispatchTouchEvent(),然后就可以调用到ViewGroup中的onTouchEvent方法。
 （子类调用父类中的方法，需要使用super关键字即可，super.xxx()即可实现子类调用父类中的方法，如果想调用未重写前的方法即可使用该方调用原来的方法）


 坐标细节问题：
 坐标：以屏幕为参考物坐标（getRawX）   以父View参考物为坐标(left)     以自身为参考物坐标(event.getX)


 */
public class TestView extends TextView {
    private String TAG = "TestView";

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /*  if (li != null && li.mOnTouchListener != null
                    && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }

            if (!result && onTouchEvent(event)) {
                result = true;
            }

            在View的dispatchTouchEvent方法里面，对事件的分发分为两种情况：
            1：如果存在OnTouchListener,则首先把事件分发给OnTouchListener中的onTouch（）方法。
            2：否则把事件分发给View中的onTouchEvent()方法

            */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
       // Log.d(TAG,"testView:dispatchTouchEvent:"+ev.getAction());
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);

       /*
没有调用getParent().requestDisallowInterceptTouchEvent(true);时候的日志：
01-17 14:09:52.614 25021-25021/com.study.customview.customview D/Test: testGroup:dispatchTouchEvent:0
01-17 14:09:52.614 25021-25021/com.study.customview.customview D/Test: testGroup:onInterceptTouchEvent:0
01-17 14:09:52.614 25021-25021/com.study.customview.customview D/Test: testView:dispatchTouchEvent:0
01-17 14:09:52.614 25021-25021/com.study.customview.customview D/Test: testView:onTouchEvent:0

01-17 14:09:52.701 25021-25021/com.study.customview.customview D/Test: testGroup:dispatchTouchEvent:2
01-17 14:09:52.701 25021-25021/com.study.customview.customview D/Test: testGroup:onInterceptTouchEvent:2
01-17 14:09:52.701 25021-25021/com.study.customview.customview D/Test: testView:dispatchTouchEvent:2
01-17 14:09:52.701 25021-25021/com.study.customview.customview D/Test: testView:onTouchEvent:2

01-17 14:09:52.703 25021-25021/com.study.customview.customview D/Test: testGroup:dispatchTouchEvent:1
01-17 14:09:52.703 25021-25021/com.study.customview.customview D/Test: testGroup:onInterceptTouchEvent:1
01-17 14:09:52.703 25021-25021/com.study.customview.customview D/Test: testView:dispatchTouchEvent:1
01-17 14:09:52.703 25021-25021/com.study.customview.customview D/Test: testView:onTouchEvent:1

//如果父view不拦截，onInterceptTouchEvent（）方法返回为false。
每次事件的传递都是从父View的dispatchTouchEvent---》父view的onInterceptTouchEvent（）----》子view的dispatchTouchEvent（）--》子view的onTouchEvent（）


调用getParent().requestDisallowInterceptTouchEvent(true);时候的日志：
01-17 14:27:51.737 25021-25021/com.study.customview.customview D/Test: testGroup:dispatchTouchEvent:0
01-17 14:27:51.737 25021-25021/com.study.customview.customview D/Test: testGroup:onInterceptTouchEvent:0
01-17 14:27:51.737 25021-25021/com.study.customview.customview D/Test: testView:dispatchTouchEvent:0
01-17 14:27:51.738 25021-25021/com.study.customview.customview D/Test: testView:onTouchEvent:0

01-17 14:27:51.839 25021-25021/com.study.customview.customview D/Test: testGroup:dispatchTouchEvent:2
01-17 14:27:51.839 25021-25021/com.study.customview.customview D/Test: testView:dispatchTouchEvent:2
01-17 14:27:51.839 25021-25021/com.study.customview.customview D/Test: testView:onTouchEvent:2

01-17 14:27:51.840 25021-25021/com.study.customview.customview D/Test: testGroup:dispatchTouchEvent:1
01-17 14:27:51.840 25021-25021/com.study.customview.customview D/Test: testView:dispatchTouchEvent:1
01-17 14:27:51.840 25021-25021/com.study.customview.customview D/Test: testView:onTouchEvent:1

//如果父view不拦截，onInterceptTouchEvent（）方法返回为false。
只有第一次事件的传递都是从父View的dispatchTouchEvent---》父view的onInterceptTouchEvent（）----》子view的dispatchTouchEvent（）--》子view的onTouchEvent（）
后续down后的事件就不在调用父view的onInterceptTouchEvent（）方法了。

事件分为：down事件   move事件   up事件  每个事件都要逐个传递给子View。
父View尽量不要拦截Down事件，给子view一个机会，一旦父View拦截了down事件，那子View的所有和事件相关的方法均不会被调用。


*/

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
                //使用的位移距离而不是具体的坐标（使用差值而不使用具体的点坐标）
                //计算触摸点在整个屏幕上的真实偏移量
                final float xDistance = event.getRawX() - downX;
                final float yDistance = event.getRawY() - downY;

                //event.getX()得到的是触摸点距离该View左边的距离。（******）是该View内。
                Log.d(TAG,"getX:"+event.getX()+"getY:"+event.getY());
                Log.d(TAG,"downX:"+downX+"downY:"+downY);
                Log.d(TAG,"xDistance:"+xDistance+"yDistance:"+yDistance);
                int left = getLeft();
                int top = getTop();
                int right = getRight();
                int bottom= getBottom();
                Log.d(TAG,"testview:"+"left:"+left+"top:"+top+"top:"+right+"bottom:"+bottom);
                Log.d(TAG,"-------------------------------------------------------------------------------------");
                if (xDistance != 0 && yDistance != 0) {
                    //在原来位置的基础上加上移动的距离
                    int l = (int) (getLeft() + xDistance);
                    int r = (int) (getRight() + xDistance);
                    int t = (int) (getTop() + yDistance);
                    int b = (int) (getBottom() + yDistance);
                    this.layout(l, t, r, b);

                }
                //每次移动完毕，重新设置起点。（****）
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                break;
        }
        return true;

    }

}
