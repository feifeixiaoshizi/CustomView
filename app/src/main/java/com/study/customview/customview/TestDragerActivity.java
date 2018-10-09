package com.study.customview.customview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
*
* detachViewFromParent（） + requestLayout（） = removeView（）
*
* detachViewFromParent（） + invalidate（） = 子view的invisible效果，子view不显示了但是占位仍在。
* */
public class TestDragerActivity extends AppCompatActivity {


    private View view;
    private ViewPager viewPager;
    ArrayList<View> viewContainter = new ArrayList<View>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drager);

        view=findViewById(R.id.tv);
        viewPager = findViewById(R.id.viewpager);
        View  textView1 =View.inflate(this,R.layout.viewpager_item,null);

        View view = textView1.findViewById(R.id.translateXY);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestDragerActivity.this,"click",Toast.LENGTH_LONG).show();
            }
        });

        View  textView2 =View.inflate(this,R.layout.viewpager_item,null);

        viewContainter.add(textView1);
        viewContainter.add(textView2);
        viewPager.setAdapter(new PagerAdapter() {



            //viewpager中的组件数量

            @Override

            public int getCount() {

                return viewContainter.size();

            }

            //滑动切换的时候销毁当前的组件

            @Override

            public void destroyItem(ViewGroup container, int position,

                                    Object object) {

                ((ViewPager) container).removeView(viewContainter.get(position));

            }

            //每次滑动的时候生成的组件

            @Override

            public Object instantiateItem(ViewGroup container, int position) {

                ((ViewPager) container).addView(viewContainter.get(position));

                return viewContainter.get(position);

            }



            @Override

            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;

            }



        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestDragerActivity.this,"onclick",Toast.LENGTH_LONG).show();
            }
        });

    }





}
