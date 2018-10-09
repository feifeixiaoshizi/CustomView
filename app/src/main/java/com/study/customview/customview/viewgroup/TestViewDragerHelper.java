package com.study.customview.customview.viewgroup;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.study.customview.customview.R;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class TestViewDragerHelper extends LinearLayout {
    private static final String TAG="TestViewDragerHelper";
    private ViewDragHelper viewDragHelper ;

    public TestViewDragerHelper(Context context) {
        super(context);
        initViewDragHelper();
    }

    public TestViewDragerHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewDragHelper();
    }

    public TestViewDragerHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewDragHelper();
    }

    private void initViewDragHelper (){
        //1. 定义ViewDragHelper对象
        viewDragHelper= ViewDragHelper.create(this,1.0f,callback);
        //设置边缘检测范围
         viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    int initLeft;
    int initTop;
    View target;

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.d(TAG,"tryCaptureView->"+"child:"+child);
            if(child.getId()!= R.id.tv){

                return true;
            }
            return false;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            Log.d(TAG,"onViewCaptured->"+"capturedChild:"+capturedChild);
            initLeft = capturedChild.getLeft();
            initTop= capturedChild.getTop();
            target=capturedChild;
        }


        //如果想让该ViewGroup拦截移动事件，就要设置getViewHorizontalDragRange返回值大于0，表示子View可以移动，
        //那该父ViewGroup就会在move事件里拦截事件，然后执行移动，如果返回为0，表示子不可以移动，父也就没有拦截了。
        //如果要实现子View既可以点击事件也可以移动就要实现该方法，让点击事件的时候子view拦截处理，如果事件是move的时候
        //让父ViewGroup拦截交个父来处理，这样就可以根据事件来动态选择让子拦截处理还是父拦截处理。（******）
        @Override
        public int getViewHorizontalDragRange(View child) {
            Log.d(TAG,"getViewHorizontalDragRange->"+"child:"+child.getId());
            return 1;

        }

        @Override
        public int getViewVerticalDragRange(View child) {
            Log.d(TAG,"getViewVerticalDragRange->"+"child:"+child.getId());
            return 1;
        }

        /*
        * child:表示当前捕获的子View
        * left：表示该子View将要布局的位置距离父View左边的距离。
        * dx：表示从上次到将要布局位置的偏移量。
        * 日志分析：
        *
         clampViewPositionHorizontal->child:2131427426left:5dx:5
         clampViewPositionHorizontal->child:2131427426left:9dx:4
         clampViewPositionHorizontal->child:2131427426left:12dx:3

         从开始的（0,0）移动到距离左边为5的距离，偏移量为5。从（5,0）移动到（9，0）偏移量为4。

        * */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d(TAG,"clampViewPositionHorizontal->"+"child:"+child.getId()+"left:"+left+"dx:"+dx);
            int paddingLeft= getPaddingLeft();
            int paddingRight= getPaddingRight();
            int width = getWidth();
            return Math.min(width-paddingRight-child.getWidth(),Math.max(paddingLeft,left));
        }

        /*
        * 控制子View可以在父View中布局的范围和位置。（*****）
        * */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.d(TAG,"clampViewPositionVertical->"+"child:"+child.getId()+"top:"+top+"dy:"+dy);
            int  paddingTop =   getPaddingTop();
            int paddingBottom = getPaddingBottom();
            int height = getHeight();
            return Math.min(height-paddingBottom-child.getHeight(),Math.max(paddingTop,top));
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            Log.d(TAG,"onViewDragStateChanged->"+"state:"+state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.d(TAG,"onViewPositionChanged->"+"changedView:"+changedView.getId()+"left:"+left+"top:"+top+"dx:"+dx+"dy:"+dy);
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //通过Scroller提供数值，不断的重新绘制实现动画。
            //负责提供过程中的值
            //viewDragHelper.settleCapturedViewAt(initLeft,initTop);
            //当值改变后触发重新绘制，当draw完毕后，结合computeScroll（）重新触发下次的draw（）。（递归）
            //invalidate();

        }


        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            Log.d(TAG,"onEdgeDragStarted->"+"edgeFlags:"+edgeFlags+"pointerId:"+pointerId);
            //实现边缘检测功能
            if(target!=null){

            viewDragHelper.captureChildView(target,pointerId);
            }
        }


        @Override
        public boolean onEdgeLock(int edgeFlags) {
            Log.d(TAG,"onEdgeLock->"+"edgeFlags:"+edgeFlags);
            return super.onEdgeLock(edgeFlags);
        }


        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Log.d(TAG,"onEdgeTouched->"+"edgeFlags:"+edgeFlags+"pointerId:"+pointerId);
        }

    };


    /*
    * 在draw（）方法里会调用computeScroll();方法
    * */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            //invalidate（）方法会触发draw（）方法的调用。
            invalidate();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispatchTouchEvent:"+ev.getAction());
        if(getTargetView(ev)!=null){
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    // 2. 转交触摸事件拦截判断, 处理触摸事件
    /*
    * 默认ViewDragHelper是不拦截事件的，然后如果子View不处理，就是通过dispatchTouchEvent（）传递给该父View，
    * 然后调用onTouchEvent（）就在onTouchEvent中把事件转交给ViewDragHelper了。
    *
    * 然后在 ViewDragHelper的processTouchEvent方法里会调用Callback中的方法：
    *
    * if (dx != 0) {
            clampedX = mCallback.clampViewPositionHorizontal(mCapturedView, left, dx);
            ViewCompat.offsetLeftAndRight(mCapturedView, clampedX - oldLeft);
        }
        if (dy != 0) {
            clampedY = mCallback.clampViewPositionVertical(mCapturedView, top, dy);
            ViewCompat.offsetTopAndBottom(mCapturedView, clampedY - oldTop);
        }

        if (dx != 0 || dy != 0) {
            final int clampedDx = clampedX - oldLeft;
            final int clampedDy = clampedY - oldTop;
            mCallback.onViewPositionChanged(mCapturedView, clampedX, clampedY,
                    clampedDx, clampedDy);
        }
    * */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept =  viewDragHelper.shouldInterceptTouchEvent(ev);
        Log.d(TAG,"intercept:"+intercept+"ev:"+ev.getAction());
        return  intercept;
    };


    // 3. 转交触摸事件的处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getTargetView(event)==null){
            return  super.onTouchEvent(event);
        }

        try {
             viewDragHelper.processTouchEvent(event);
            Log.d(TAG,"onTouchEvent:"+event.getAction());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
