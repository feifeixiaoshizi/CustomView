package com.study.customview.customview.chatview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.customview.customview.R;

/**
 * Created by jianshengli on 2019/2/16.
 */

public class TextChatView extends BaseChatView {
    private TextView text;
    private View childView;

    public TextChatView(Context context) {
        super(context);
    }

    public TextChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextChatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onInflateChildView(ViewGroup childParentView) {
        childView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_view_text, childParentView, true);
    }
}
