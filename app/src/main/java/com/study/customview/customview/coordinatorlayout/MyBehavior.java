package com.study.customview.customview.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.study.customview.customview.R;

/**
 * AppBarLayout和RecyclerView的实现：
 * 在AppBarLayout中设置了Behavior，在里面设置了监听了滑动事件。
 * 在RecylcerView中设置了ScrollingViewBehavior，在里面RecyclerView依赖AppBarLayout的移动。
 *
 * 1：可以滑动控件，可以监视它的滑动事件。（AppBarLayout监听RecyclerView的滑动事件）
 * 2：不可以滑动的事件，可以监视它的移动事件。（RecyclerView监听AppBarLayout的移动事件）
 */
public class MyBehavior extends CoordinatorLayout.Behavior<FrameLayout> {
    public static  String   TAG = "MyBehavior";
    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //依赖判断
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FrameLayout child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    //依赖的view变化时
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FrameLayout child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
    //依赖的View删除了
    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FrameLayout child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    //测量
    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, FrameLayout child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        parent.onMeasureChild(child,parentWidthMeasureSpec,widthUsed,parentHeightMeasureSpec,heightUsed);
        Log.d(TAG,"measureWidth:"+child.getMeasuredWidth()+"measureHeight:"+child.getMeasuredHeight());
        return true;
        //return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    //布局的过程
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FrameLayout child, int layoutDirection) {
       RecyclerView recyclerView =  parent.findViewById(R.id.recyclerView);
       if(recyclerView !=null){
           //重新布局
           int rihgt = child.getMeasuredWidth();
           int height = child.getMeasuredHeight();
           child.layout(0,0,rihgt,height);
           //重新布局RecyclerView
           int recyclerViewHeight = recyclerView.getMeasuredHeight();
           //int l, int t, int r, int b
           //recyclerView.layout(0,height,rihgt,recyclerViewHeight);*/

            //把recyclerView放在headerView的下面
           recyclerView.offsetTopAndBottom(height);

        return true;
       }

       return super.onLayoutChild(parent, child, layoutDirection);
    }

    /**
     * @param coordinatorLayout 负责协调的父控件
     * @param child  和该Behavior关联的View，在这里就是FrameLayout
     * @param directTargetChild 该view要观察的View，且是CoordinatorLayout的直接View
     *（例如在ViewPager中的RecyclerView，在这里直接targetView是ViewPager但是真正的View的RecyclerView）
     * @param target 真正要观察的View（例如在ViewPager中的RecyclerView，在这里直接targetView是ViewPager但是真正的View的RecyclerView）
     * @param nestedScrollAxes
     * @return
     * 如果直接就是RecyclerView的话，那directTargetChild和target就是同一个View，即是RecyclerView.(*****)
     * 每次RecyclerView滑动的时候就会调用该方法，如果该方法返回为false，则后续的方法就不再执行了。（*****）
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG,"onStartNestedScroll:--->"+"directTargetChild:"+directTargetChild.getClass().getSimpleName()
                +"target:"+target.getClass().getSimpleName()+"nestedScrollAxes:"+nestedScrollAxes);
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        return  true;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, FrameLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG,"onNestedScrollAccepted:--->"+"directTargetChild:"+directTargetChild.getClass().getSimpleName()
                +"target:"+target.getClass().getSimpleName()+"nestedScrollAxes:"+nestedScrollAxes);
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }


    /**
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     *
    06-10 03:24:51.080 4951-4951/com.study.customview.customview D/MyBehavior: onStartNestedScroll:--->directTargetChild:RecyclerViewtarget:RecyclerViewnestedScrollAxes:2
    06-10 03:24:51.080 4951-4951/com.study.customview.customview D/MyBehavior: onNestedScrollAccepted:--->directTargetChild:RecyclerViewtarget:RecyclerViewnestedScrollAxes:2
    06-10 03:24:51.080 4951-4951/com.study.customview.customview D/MyBehavior: onStopNestedScroll:--->target:RecyclerView

    06-10 03:24:51.080 4951-4951/com.study.customview.customview D/MyBehavior: onStartNestedScroll:--->directTargetChild:RecyclerViewtarget:RecyclerViewnestedScrollAxes:2
    06-10 03:24:51.080 4951-4951/com.study.customview.customview D/MyBehavior: onNestedScrollAccepted:--->directTargetChild:RecyclerViewtarget:RecyclerViewnestedScrollAxes:2
    06-10 03:24:51.341 4951-4951/com.study.customview.customview D/MyBehavior: onNestedPreScroll:--->target:RecyclerViewdx:0dy:10
    06-10 03:24:51.359 4951-4951/com.study.customview.customview D/MyBehavior: onNestedPreScroll:--->target:RecyclerViewdx:4dy:22
    06-10 03:24:51.359 4951-4951/com.study.customview.customview D/MyBehavior: onNestedScroll:--->target:RecyclerViewdxConsumed:0dyConsumed:1dxUnconsumed:0dyUnconsumed:0

    06-10 03:24:51.376 4951-4951/com.study.customview.customview D/MyBehavior: onNestedPreScroll:--->target:RecyclerViewdx:4dy:11
    06-10 03:24:51.377 4951-4951/com.study.customview.customview D/MyBehavior: onNestedScroll:--->target:RecyclerViewdxConsumed:0dyConsumed:11dxUnconsumed:0dyUnconsumed:0
    06-10 03:24:51.633 4951-4951/com.study.customview.customview D/MyBehavior: onStopNestedScroll:--->target:RecyclerView

     *
     *
     *
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        Log.d(TAG,"onNestedPreScroll:--->"
                +"target:"+target.getClass().getSimpleName()+"dx:"+dx+"dy:"+dy);
       // 06-10 03:24:51.376 4951-4951/com.study.customview.customview D/MyBehavior: onNestedPreScroll:--->target:RecyclerViewdx:4dy:11
        //dx 和 dy 是target（RecyclerView）的滑动的距离

        Log.d(TAG,"onNestedPreScroll:--->"
                +"top:"+child.getTop()+"translationY:"+child.getTranslationY());
        //dy>0 向上滑动 dy<0 向下滑动

        //向上滑动RecyclerView
        if(dy>0){
            int scrollRange = child.getHeight();//最大滑动距离
            int currentChildTop = child.getTop();//当前headerView的top值
            if(currentChildTop<=-scrollRange){
                return;
            }
            int cansrolldy = scrollRange+currentChildTop;
            if(cansrolldy>dy){
                child.offsetTopAndBottom(-dy);
                //child.setTranslationY(-dy);
                consumed[1]=dy;
            }else {
                child.offsetTopAndBottom(-cansrolldy);
                //child.setTranslationY(-cansrolldy);
                consumed[1]=cansrolldy;
            }

        }else{
            int currentChildTop = child.getTop();//当前headerView的top值
            if(currentChildTop>=0){
                return;
            }
            int cansrolldy = -currentChildTop;
            if(cansrolldy>-dy){
                child.offsetTopAndBottom(-dy);
               // child.setTranslationY(-dy);
                consumed[1]=dy;
            }else {
                child.offsetTopAndBottom(cansrolldy);
                //child.setTranslationY(cansrolldy);
                consumed[1]=-cansrolldy;
            }

        }

    }

    /*
    * 如果在onNestedPreScroll（）childView就消耗了所有的dy，则不会在调用到该方法了。（****）
    * */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d(TAG,"onNestedScroll:--->"
                +"target:"+target.getClass().getSimpleName()+"dxConsumed:"+dxConsumed+"dyConsumed:"+dyConsumed
                +"dxUnconsumed:"+dxUnconsumed+"dyUnconsumed:"+dyUnconsumed);
        //06-10 03:24:51.377 4951-4951/com.study.customview.customview D/MyBehavior: onNestedScroll:--->target:RecyclerViewdxConsumed:0dyConsumed:11dxUnconsumed:0dyUnconsumed:0
        //滑动过后，dyConsumed是target（RecyclerView消费的距离），dyUnconsumed是target消费后剩余的距离。
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        Log.d(TAG,"onStopNestedScroll:--->"
                +"target:"+target.getClass().getSimpleName());
        //06-10 03:24:51.633 4951-4951/com.study.customview.customview D/MyBehavior: onStopNestedScroll:--->target:RecyclerView
        //停止滑动了
    }
}
