package com.example.floatwindowdemo.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

public class DemoServiceHandler extends Handler {

    WeakReference<DemoService> serverRef;

    public DemoServiceHandler(@NonNull Looper looper, DemoService service) {
        super(looper);
        serverRef = new WeakReference<>(service);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Log.d(DemoConst.TAG, "服务端接受信息: " + msg.what);
        DemoService demoService = serverRef.get();
        if (demoService == null) {
            return;
        }
        if (msg.what == DemoConst.ACTION_CONNECT) {
            DemoServiceRouter.handleConnect(msg, demoService);
        } else if (msg.what == DemoConst.ACTION_DISCONNECT) {
            DemoServiceRouter.handleDisconnect(msg, demoService);
        } else if (msg.what == DemoConst.ACTION_ADD_WINDOW || msg.what == DemoConst.ACTION_AUTO_PLAY) {
            DemoServiceRouter.handleAddWindow(msg, demoService);
        } else if (msg.what == DemoConst.ACTION_REMOVE_WINDOW) {
            DemoServiceRouter.handleRemoveWindow(msg, demoService);
        }
    }
}