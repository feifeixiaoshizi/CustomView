package com.study.customview.customview.chatview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.study.customview.customview.R;


/**
 * Created by jianshengli on 2019/2/16.
 */

public abstract class BaseChatView extends LinearLayout {
    private ViewGroup fl_content;
    private View leftView;
    private View rightView;

    public BaseChatView(Context context) {
        super(context);
    }

    public BaseChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public BaseChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    public BaseChatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        //如果为false，表示不会直接添加到root布局上，在界面上不会显示。
        //LayoutInflater.from(context).inflate(R.layout.item_chat_view_left,this,false);

        //如果为true，表示把xml变为View对象，并直接添加到root上，会直接显示。
        //默认显示左边的布局,attachToRoot为true返回的view是root（新建view的父view），false返回的新创建的view
        leftView = LayoutInflater.from(context).inflate(R.layout.item_chat_view_left,this,false);
        rightView = LayoutInflater.from(context).inflate(R.layout.item_chat_view_right,this,false);
        //默认显示左view
        addView(leftView);

        fl_content = findViewById(R.id.fl_content);
        onInflateChildView(fl_content);
    }

    /**
     * 创建子view
     * @param childParentView 子View的容器
     */
    public abstract void onInflateChildView(ViewGroup childParentView);


    public void setShowLeftView(){
        this.removeAllViews();
        this.addView(leftView);
        fl_content = findViewById(R.id.fl_content);
        onInflateChildView(fl_content);
    }


    public void setShowRightView(){
        this.removeAllViews();
        this.addView(rightView);
        fl_content = findViewById(R.id.fl_content);
        onInflateChildView(fl_content);
    }


}
