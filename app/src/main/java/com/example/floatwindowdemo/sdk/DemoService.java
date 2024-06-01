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

public class DemoService extends Service {

    private static class ServerHandler extends Handler {

        WeakReference<DemoService> serverRef;

        public ServerHandler(@NonNull Looper looper, DemoService service) {
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
                Messenger clientMessenger = msg.replyTo;
                if (clientMessenger != null) {
                    try {
                        Log.d(DemoConst.TAG, "server <=== client msg");
                        demoService.setClientMessenger(clientMessenger);
                        Message connectionMsg = Message.obtain();
                        connectionMsg.what = DemoConst.ACTION_CONNECT_ESTABLISHED;
                        clientMessenger.send(connectionMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (msg.what == DemoConst.ACTION_ADD_WINDOW) {
                Bundle params = msg.getData();
                Class windowClass = (Class) params.getSerializable(DemoConst.KEY_PARAMS_WINDOW_CLASS);
                Log.d(DemoConst.TAG, "ACTION_ADD_WINDOW");
                demoService.addFloatWindow(windowClass);
            } else if (msg.what == DemoConst.ACTION_REMOVE_WINDOW) {
                Log.d(DemoConst.TAG, "ACTION_REMOVE_WINDOW");
                demoService.removeFloatWindow();
            }
        }
    }

    private static final String TAG = "Service-0531";

    private Handler serverHandler;
    private Messenger serverMessenger;
    private DemoFloatWindowManager demoFloatWindowManager;

    private Messenger clientMessenger;

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
        serverHandler = new ServerHandler(Looper.getMainLooper(), this);

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

    public Messenger getClientMessenger() {
        return clientMessenger;
    }

    public void setClientMessenger(Messenger clientMessenger) {
        this.clientMessenger = clientMessenger;
    }
}
