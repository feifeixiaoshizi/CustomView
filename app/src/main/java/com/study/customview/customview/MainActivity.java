package com.study.customview.customview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.study.customview.customview.coordinatorlayout.CoordinatorActivity;
import com.study.customview.customview.viewgroup.TagsLayout;
/*
*
* detachViewFromParent（） + requestLayout（） = removeView（）
*
* detachViewFromParent（） + invalidate（） = 子view的invisible效果，子view不显示了但是占位仍在。
* */
public class MainActivity extends AppCompatActivity {
    View test_detachViewFromParent ;
    View test_removeView ;
    TagsLayout imageViewGroup;
    View testView ;
    View testGroup;

    View test1;
    View test2;

    View matrix;
    View matrix1;
    View paint;
    View canvas;
    View animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        testView = findViewById(R.id.testview);
        test1 = findViewById(R.id.test1);
        test2 = findViewById(R.id.test2);
        matrix = findViewById(R.id.matrix);
        matrix1 = findViewById(R.id.matrix1);
        paint = findViewById(R.id.paint);
        canvas = findViewById(R.id.canvas);
        animator = findViewById(R.id.animator);
        matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到矩阵界面
                Intent intent = new Intent(MainActivity.this,MatrixActivity.class);
                startActivity(intent);
            }
        });
        matrix1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到矩阵界面
                Intent intent = new Intent(MainActivity.this,MatrixActivity1.class);
                startActivity(intent);
            }
        });
        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到矩阵界面
                Intent intent = new Intent(MainActivity.this,PaintActivity.class);
                startActivity(intent);
            }
        });

        //跳转到画布测试界面
        canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,CanvasActivity.class);
                Intent intent = new Intent(MainActivity.this,CoordinatorActivity.class);
                startActivity(intent);
            }
        });

        //跳转到画布测试界面
        animator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,AnimatorActivity.class);
                Intent intent = new Intent(MainActivity.this,ScrollerActivity.class);
                startActivity(intent);
            }
        });

       test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"test1点击了",Toast.LENGTH_SHORT).show();
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"test2点击了",Toast.LENGTH_SHORT).show();
            }
        });



        testGroup = findViewById(R.id.testgroup);

        imageViewGroup = (TagsLayout) findViewById(R.id.image_layout);
        test_detachViewFromParent = findViewById(R.id.test_detachViewFromParent);
        test_removeView = findViewById(R.id.test_removeView);
        test_detachViewFromParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = imageViewGroup.getChildAt(0);
                //仅仅只是从ViewGroup的内部数组中删除了子view但是不会触发view的重新布局。
                imageViewGroup.detachViewFromParent(child);
                //触发ViewGroup重新布局和绘制
                //imageViewGroup.requestLayout();

                //仅仅ViewGroup从新绘制，分离的View不再显示了，但是在ViewGroup中的位置仍在。
                imageViewGroup.invalidate();

            }
        });

        test_removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = imageViewGroup.getChildAt(0);
                //removeView的方法会触发requestLayout并从ViewGroup中删除该子View
                imageViewGroup.removeView(child);
            }
        });
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String[] string={"从我写代码那天起，我就没有打算写代码","从我写代码那天起","我就没有打算写代码","没打算","写代码"};
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setText(string[i]);
            textView.setTextColor(Color.GREEN);
            textView.setBackgroundResource(R.drawable.backgroud);
            imageViewGroup.addView(textView, lp);


        }


    }
}
