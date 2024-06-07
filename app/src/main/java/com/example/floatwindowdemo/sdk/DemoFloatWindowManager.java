package com.example.floatwindowdemo.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class DemoFloatWindowManager {

    private final Context context;
    private final WindowManager windowManager;

    ClientRecord mCurrentClient;
    BaseFloatWindow mCurrentWindow;

    private WindowManager.LayoutParams params;

    public DemoFloatWindowManager(Context context) {
        Log.d(DemoConst.TAG, "悬浮窗管理初始化 ...");
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressLint("SetTextI18n")
    public void addFloatWindow(ClientRecord clientRecord, Class windowClass) throws Exception {
        if (windowManager == null) {
            return;
        }
        if (mCurrentWindow != null && mCurrentClient.clientInfo.equals(clientRecord.clientInfo)) {
            throw new Exception("Duplicate request");
        }

        try {
            // 先移除旧的，内部捕获异常
            removeFloatWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        params = new WindowManager.LayoutParams();

        // 设置悬浮窗属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.format = PixelFormat.RGBA_8888; // 颜色格式
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不可获得焦点
        params.gravity = Gravity.START | Gravity.TOP; // 对齐方式
        params.width = 200; // 宽度
        params.height = 100; // 高度

        // 创建悬浮窗视图
        BaseFloatWindow windowObj;
        try {
            mCurrentWindow = (BaseFloatWindow) windowClass.newInstance();
            mCurrentWindow.createWindow(context);
            mCurrentClient = clientRecord;

            View windowView = mCurrentWindow.mWindowView;
            if (windowView != null) {
                // 将悬浮窗添加到WindowManager
                windowManager.addView(windowView, params);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }


    }

    // 移除悬浮窗
    public void removeFloatWindow() throws Exception {
        if (mCurrentWindow != null && mCurrentWindow.mWindowView != null) {
            windowManager.removeView(mCurrentWindow.mWindowView);
            mCurrentWindow.destroyWindow();
            mCurrentWindow = null;
            mCurrentClient = null;
        } else {
            throw new Exception("No displaying window");
        }
    }
}
