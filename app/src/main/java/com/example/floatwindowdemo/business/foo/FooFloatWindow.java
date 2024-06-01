package com.example.floatwindowdemo.business.foo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.floatwindowdemo.R;
import com.example.floatwindowdemo.sdk.BaseFloatWindow;

public class FooFloatWindow extends BaseFloatWindow {

    @Override
    public View onCreateView(Context context) {
        TextView floatingView = new TextView(context);
        floatingView.setText("悬浮窗 [" + globalIndex.getAndIncrement() + "]");
        floatingView.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
        floatingView.setTextColor(context.getResources().getColor(R.color.white));

        return floatingView;
    }
}
