package com.study.customview.customview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.study.customview.customview.chatview.BaseChatView;
import com.study.customview.customview.viewgroup.TagsLayout;

/*
*
* detachViewFromParent（） + requestLayout（） = removeView（）
*
* detachViewFromParent（） + invalidate（） = 子view的invisible效果，子view不显示了但是占位仍在。
* */
public class PaintActivity extends AppCompatActivity {
    private BaseChatView image_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paint);
        image_chat = findViewById(R.id.image_chat);
        image_chat.setShowRightView();
        image_chat.setShowLeftView();
        image_chat.setShowRightView();

    }
}
