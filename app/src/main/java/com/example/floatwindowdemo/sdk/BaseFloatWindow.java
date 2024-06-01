package com.example.floatwindowdemo.sdk;

import android.content.Context;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseFloatWindow {

    public static final AtomicInteger globalIndex = new AtomicInteger(0);

    View mWindowView;

    public BaseFloatWindow() {
    }

    void createWindow(Context context) {
        mWindowView = onCreateView(context);
    }

    public View onCreateView(Context context) {
        return null;
    }

    void destroyWindow() {
        mWindowView = null;
    }
}
