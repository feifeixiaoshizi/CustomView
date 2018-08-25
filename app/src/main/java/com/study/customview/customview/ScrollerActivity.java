package com.study.customview.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.study.customview.customview.view.animator.ScrollerLinearLayout;

/*
*
* detachViewFromParent（） + requestLayout（） = removeView（）
*
* detachViewFromParent（） + invalidate（） = 子view的invisible效果，子view不显示了但是占位仍在。
* */
public class ScrollerActivity extends AppCompatActivity {
    private View start;
    private ScrollerLinearLayout scrollerLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrooller);
        start= findViewById(R.id.start);
        scrollerLinearLayout = (ScrollerLinearLayout) findViewById(R.id.sl);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollerLinearLayout.smoothScrollTo(scrollerLinearLayout.getRightView().getWidth(),0);

            }
        });

    }
}
