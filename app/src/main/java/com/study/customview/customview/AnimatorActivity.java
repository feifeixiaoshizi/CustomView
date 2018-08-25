package com.study.customview.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/*
*
* detachViewFromParent（） + requestLayout（） = removeView（）
*
* detachViewFromParent（） + invalidate（） = 子view的invisible效果，子view不显示了但是占位仍在。
*
* 1：一个属性多个关键帧（KeyFrame）
* 2：一个动画同时执行多个属性变化（PropertyValuesHolder）
* 3：多个动画同时执行（AnimatorSet）
* 4：动画插值器用于计算动画执行过程的比例值（0-1）
* 5：动画估值器用于利用插值器提供的比例来计算具体的属性值。
*
* */
public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {
    View view;
    View image;
    private TextView translateX, translateY, translateXY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animator);
        view = findViewById(R.id.object_animator);
        image = findViewById(R.id.image);
        translateX = (TextView) findViewById(R.id.translateX);
        translateX.setOnClickListener(this);
        translateY = (TextView) findViewById(R.id.translateY);
        translateY.setOnClickListener(this);
        translateXY = (TextView) findViewById(R.id.translateXY);
translateXY.setOnClickListener(this);

// 创建 ObjectAnimator 对象
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", 0, 85);
        animator.setDuration(5000);
        // animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setInterpolator(new AnticipateOvershootInterpolator());
// 执行动画
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewTranslateX();
                imageViewTranslateY();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });


    }

    /*-------------平移动画-------------------------*/
    private void imageViewTranslateX() {
        image.clearAnimation();
        Log.d("AnimatorActivity", "方法执行了");
        //是相对于起始位置的偏移量。（*******）
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(image, "translationX", 200);
        objectAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();

    }

    private void imageViewTranslateY() {
        image.clearAnimation();
        Log.d("AnimatorActivity", "方法执行了");
        //是相对于起始位置的偏移量,如果当前View已经偏移到了200，则在执行动画时无效果
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(image, "translationY", 200);
        objectAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();

    }

    private void imageViewTranslateXY() {
        image.clearAnimation();
        PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.ofFloat("translationX", 10, 100, 200);
        PropertyValuesHolder propertyValuesHolderY = PropertyValuesHolder.ofFloat("translationY", 10, 100, 400);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(image, propertyValuesHolderX, propertyValuesHolderY);
        objectAnimator.setDuration(2000);
        objectAnimator.start();


    }


    private void imageViewTanslateSetXY(){
        //是相对于起始位置的偏移量。（*******）
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(image, "translationX", 200);
        objectAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator.setDuration(2000);
        objectAnimator.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(image, "translationY", 200);
        objectAnimator1.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator1.setDuration(2000);
        objectAnimator1.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(objectAnimator,objectAnimator1);
        animatorSet.playTogether(objectAnimator,objectAnimator1);
        animatorSet.play(objectAnimator).before(objectAnimator1);
        animatorSet.start();

    }


    private void keyFrame(){
        //关键帧
        Keyframe keyframe = Keyframe.ofFloat(0.5f,100);
        Keyframe keyframe1 = Keyframe.ofFloat(1f,200);

        //属性holder
        PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.ofKeyframe("translationX",keyframe,keyframe1);

        //属性动画对象
        ObjectAnimator objectAnimator= ObjectAnimator.ofPropertyValuesHolder(image,propertyValuesHolderX);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatCount(ValueAnimator.RESTART);
        objectAnimator.start();




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.translateX:
                imageViewTranslateX();
                break;
            case R.id.translateY:
                imageViewTranslateY();
                break;
            case R.id.translateXY:
                imageViewTranslateXY();
                break;
        }

    }
}
