package com.study.customview.customview.event;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ImageView;

/**
 * Created by jianshengli on 2018/5/3.
 */

public class EventImageView extends AppCompatImageView {
    private final  String TAG = "EventImageView";


    public EventImageView(Context context) {
        super(context);
    }

    public EventImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //测试速度追踪
        //testVelocityTracker(event);
        testGestureDetector(event);
        return true;
    }

    VelocityTracker velocityTracker ;
    private void testVelocityTracker (MotionEvent event){
        int xVelocity=0;
        int yVelocity=0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //初始化
                velocityTracker = VelocityTracker.obtain();
                break;
            case MotionEvent.ACTION_MOVE:
                //追踪
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                xVelocity = (int) velocityTracker.getXVelocity();
                yVelocity = (int) velocityTracker.getYVelocity();

                Log.d(TAG,"x:"+xVelocity+"y:"+yVelocity);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //回收
                velocityTracker.clear();
                velocityTracker.recycle();
                break;
        }
    }

    GestureDetector gestureDetector ;
    private void testGestureDetector (MotionEvent event){
        gestureDetector= new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d(TAG,"onDown:"+e.getAction());
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.d(TAG,"onShowPress:"+e.getAction());

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG,"onSingleTapUp:"+e.getAction());
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG,"onScroll:"+e1.getAction());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG,"onLongPress:"+e.getAction());
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG,"onLongPress:"+e1.getAction());
                return false;
            }
        });
      boolean onTouchEvent =   gestureDetector.onTouchEvent(event);
      Log.d(TAG,"onTouchEvent:"+onTouchEvent);
        gestureDetector.setIsLongpressEnabled(false);
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });


    }



}
