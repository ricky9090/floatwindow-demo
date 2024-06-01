package com.example.floatwindowdemo.business.bar;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.floatwindowdemo.R;
import com.example.floatwindowdemo.sdk.BaseFloatWindow;

public class BarFloatWindow extends BaseFloatWindow {

    @Override
    public View onCreateView(Context context) {
        TextView floatingView = new TextView(context);
        floatingView.setText("悬浮窗 [" + globalIndex.getAndIncrement() + "]");
        floatingView.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
        floatingView.setTextColor(context.getResources().getColor(R.color.white));

        return floatingView;
    }
}
