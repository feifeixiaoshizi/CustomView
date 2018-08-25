package com.study.customview.customview.view.animator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.study.customview.customview.R;

import java.util.zip.Inflater;

/**
 * Created by jianshengli on 2018/5/26.
 * 1：scroller移动的是其直接的内容
 *
 * 2：子view可以在父View的范围外，但是无法显示，这时候可以使用Scroller来实现内容的移动，
 * 来显示父View范围外的子View。
 *
 * 3:在onFinishInflate()方法中可以找到具体的子View。
 *
 *
 */

public class ScrollerLinearLayout extends LinearLayout {
    private LayoutInflater layoutInflater;
    private View child0;
    private View child1;
    Scroller scroller ;
    private String TAG = "ScrollerLinearLayout";

    public ScrollerLinearLayout(Context context) {
        this(context,null);
    }

    public ScrollerLinearLayout(Context context, @Nullable AttributeSet attrs) {
       this(context,attrs,0);
    }

    public ScrollerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
       this(context,attrs,0,0);
    }

    public ScrollerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        layoutInflater = LayoutInflater.from(context);
        scroller= new Scroller(context);
    }


    public void smoothScrollTo(int destX,int destY){
        int scrollX=getScrollX();
        int delta= destX-scrollX;
        //开始滚动
        scroller.startScroll(scrollX,0,delta,0,2000);
        //触发computeScroll方法的调用
        invalidate();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG,"childCount:"+getChildCount());
        child0 = findViewById(R.id.child0);
        child1 = findViewById(R.id.child1);
    }

    public View getRightView(){
        return child1;
    }

    public View getLeftView(){
        return child0;
    }


    //递归调用该方法进行不断的获取值来进行滚动
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller!=null&&scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            //触发下一次调用该方法
            postInvalidate();
        }
    }
}
