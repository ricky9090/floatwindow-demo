package com.example.floatwindowdemo.sdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DemoService extends Service {

    private static final String TAG = "Service-0531";

    private Handler serverHandler;
    private Messenger serverMessenger;
    private DemoFloatWindowManager demoFloatWindowManager;

    private Messenger clientMessenger;

    private final Map<DemoClientInfo, Messenger> clientMap = new HashMap<>();

    public static void start(Context context) {
        if (context != null) {
            Intent intent = new Intent();
            intent.setClass(context, DemoService.class);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(DemoConst.TAG, "onCreate, pid: " + Process.myPid() + ", thread: " + Thread.currentThread().getName());

        demoFloatWindowManager = new DemoFloatWindowManager(getApplicationContext());
        serverHandler = new DemoServiceHandler(Looper.getMainLooper(), this);

        serverMessenger = new Messenger(serverHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DemoConst.TAG, "onStartCommand, pid: " + Process.myPid() + ", thread: " + Thread.currentThread().getName());
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(DemoConst.TAG, "onBind, pid: " + Process.myPid() + ", thread: " + Thread.currentThread().getName());
        if (serverMessenger != null) {
            return serverMessenger.getBinder();
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(DemoConst.TAG, "onUnbind, pid: " + Process.myPid() + ", thread: " + Thread.currentThread().getName());
        boolean autoStop = true;
        if (demoFloatWindowManager.mCurrentWindow == null && autoStop) {
            Log.d(DemoConst.TAG, "onUnbind 当前没有任何浮窗");
            serverHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(DemoConst.TAG, "自动关闭悬浮窗服务");
                    stopSelf();
                }
            });
        }
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(DemoConst.TAG, "onRebind, pid: " + Process.myPid() + ", thread: " + Thread.currentThread().getName());
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(DemoConst.TAG, "onDestroy, pid: " + Process.myPid() + ", thread: " + Thread.currentThread().getName());
        super.onDestroy();
        removeFloatWindow();
    }

    Messenger checkClient(Bundle params) throws Exception {
        if (params == null) {
            throw new Exception("No params from client");
        }
        Object clientInfoObj = params.getSerializable(DemoConst.Key.CLIENT_INFO);
        if (clientInfoObj instanceof DemoClientInfo) {
            DemoClientInfo clientInfo = (DemoClientInfo) clientInfoObj;
            if (!clientMap.containsKey(clientInfo)) {
                throw new Exception("Not register client");
            }
            return clientMap.get(clientInfo);
        }
        throw new Exception("No client info");

    }

    void addFloatWindow(Class windowClass) {
        if (demoFloatWindowManager != null) {
            demoFloatWindowManager.addFloatWindow(windowClass);
        }
    }

    void removeFloatWindow() {
        if (demoFloatWindowManager != null) {
            demoFloatWindowManager.removeFloatWindow();
        }
    }

    public void registerClient(DemoClientInfo clientInfo, Messenger clientMessenger) {
        this.clientMap.put(clientInfo, clientMessenger);
    }
}
