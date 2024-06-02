package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
            handleConnect(msg, demoService);
        } else if (msg.what == DemoConst.ACTION_ADD_WINDOW) {
            handleAddWindow(msg, demoService);
        } else if (msg.what == DemoConst.ACTION_REMOVE_WINDOW) {
            handleRemoveWindow(msg, demoService);
        }
    }

    private void handleConnect(@NonNull Message msg, @NonNull DemoService demoService) {
        Messenger clientMessenger = msg.replyTo;
        Bundle connectInfo = msg.getData();
        try {
            DemoClientInfo clientInfo = (DemoClientInfo) connectInfo.getSerializable(DemoConst.Key.CLIENT_INFO);
            if (clientMessenger != null && clientInfo != null) {
                Log.d(DemoConst.TAG, "server <=== client msg");
                demoService.registerClient(clientInfo, clientMessenger);
                Message connectionMsg = Message.obtain();
                connectionMsg.what = DemoConst.ACTION_CONNECT_ESTABLISHED;
                clientMessenger.send(connectionMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAddWindow(@NonNull Message msg, @NonNull DemoService demoService) {
        Log.d(DemoConst.TAG, "ACTION_ADD_WINDOW");
        Messenger clientMessenger = doCheckClient(msg, demoService);
        if (clientMessenger == null) {
            return;
        }

        // 客户端校验通过，继续执行
        try {
            Bundle params = msg.getData();
            Class windowClass = (Class) params.getSerializable(DemoConst.Key.WINDOW_CLASS);
            demoService.addFloatWindow(windowClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRemoveWindow(@NonNull Message msg, @NonNull DemoService demoService) {
        Log.d(DemoConst.TAG, "ACTION_REMOVE_WINDOW");
        Messenger clientMessenger = doCheckClient(msg, demoService);
        if (clientMessenger == null) {
            return;
        }

        // 客户端校验通过，继续执行
        try {
            demoService.removeFloatWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用校验client方法，当service校验出现异常时，返回错误信息给当前发信息的client
     * @param msg
     * @param demoService
     * @return 合法客户端的messenger对象
     */
    private Messenger doCheckClient(@NonNull Message msg, @NonNull DemoService demoService) {
        Messenger msgMessenger = msg.replyTo;
        String errorMsg = null;
        try {
            Bundle params = msg.getData();
            return demoService.checkClient(params);
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = e.getMessage();
        }

        if (!TextUtils.isEmpty(errorMsg) && msgMessenger != null) {
            Bundle errorData = new Bundle();
            errorData.putString(DemoConst.Callback.ERROR_MSG, errorMsg);
            Message error = Message.obtain();
            error.what = DemoConst.Callback.ERROR;
            error.setData(errorData);
            try {
                msgMessenger.send(error);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}