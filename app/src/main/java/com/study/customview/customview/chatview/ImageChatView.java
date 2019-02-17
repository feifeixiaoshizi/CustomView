package com.study.customview.customview.chatview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.customview.customview.R;

/**
 * Created by jianshengli on 2019/2/16.
 */

public class ImageChatView extends BaseChatView {
    private View childView;

    public ImageChatView(Context context) {
        super(context);
    }

    public ImageChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageChatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onInflateChildView(ViewGroup childParentView) {
        //尽量使用false+addview的方式。
        childView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_view_image, childParentView, true);

    }
}
