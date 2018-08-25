package com.study.customview.customview.coordinatorlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.study.customview.customview.R;

import java.util.List;

/**
 * Created by jianshengli on 2018/6/10.
 */

public class RecyclerViewBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    private  final  String TAG = "RecyclerViewBehavior";
    public RecyclerViewBehavior() {
    }

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    //测量
    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, RecyclerView child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        parent.onMeasureChild(child,parentWidthMeasureSpec,widthUsed,parentHeightMeasureSpec,heightUsed);
        Log.d(TAG,"measureWidth:"+child.getMeasuredWidth()+"measureHeight:"+child.getMeasuredHeight());
        return true;
        //return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    //布局的过程
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
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

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof FrameLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        Log.d(TAG,"childHeight:"+child.getHeight()+"top:"+dependency.getTop());
        int top = dependency.getTop();
        child.setTranslationY(top);
        return super.onDependentViewChanged(parent, child, dependency);
    }


}

