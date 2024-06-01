package com.example.floatwindowdemo.sdk;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class DemoClientFragment extends Fragment {

    static class ClientHandler extends Handler {

        WeakReference<DemoClient> clientRef;

        public ClientHandler(@NonNull Looper looper, DemoClient client) {
            super(looper);
            this.clientRef = new WeakReference<>(client);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == DemoConst.ACTION_CONNECT_ESTABLISHED) {
                Log.d(DemoConst.TAG, "server ===> client msg");
                Log.d(DemoConst.TAG, "双向通讯建立");
            }
        }
    }

    public static final String TAG = "[DemoClientFragment] ";

    private Messenger serverMessenger = null;
    protected DemoClient client = new DemoClient(this);

    private ClientHandler clientHandler;
    private Messenger clientMessenger;

    private final ServiceConnection demoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serverMessenger = new Messenger(iBinder);

            Log.d(DemoConst.TAG, TAG + "连接悬浮窗服务");
            try {
                Message connectMsg = Message.obtain();
                connectMsg.what = DemoConst.ACTION_CONNECT;
                connectMsg.replyTo = clientMessenger;
                serverMessenger.send(connectMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(DemoConst.TAG, TAG + "悬浮窗服务异常关闭");
            serverMessenger = null;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientHandler = new ClientHandler(Looper.getMainLooper(), client);
        clientMessenger = new Messenger(clientHandler);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();

        Context context = getContext();
        if (context != null) {
            Log.d(DemoConst.TAG, TAG + "启动悬浮窗服务");
            DemoService.start(getContext());
            Log.d(DemoConst.TAG, TAG + "绑定悬浮窗服务");
            Intent serverIntent = new Intent();
            serverIntent.setClass(getContext(), DemoService.class);
            context.bindService(
                    serverIntent, demoServiceConnection, Service.BIND_AUTO_CREATE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Context context = getContext();
        if (context != null) {
            Log.d(DemoConst.TAG, TAG + "解绑悬浮窗服务");
            context.unbindService(demoServiceConnection);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendMessage(int msg, Bundle params) {
        if (serverMessenger != null) {
            Message message = Message.obtain();
            message.what = msg;
            message.setData(params);
            try {
                serverMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
