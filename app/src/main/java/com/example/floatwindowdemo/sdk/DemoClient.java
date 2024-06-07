package com.example.floatwindowdemo.sdk;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoClient {

    static final AtomicInteger globalIndex = new AtomicInteger(0);

    final DemoClientInfo clientInfo = new DemoClientInfo();

    final DemoClientFragment clientFragment;
    ClientCallback clientCallback;

    public DemoClient(DemoClientFragment fragment) {
        clientFragment = fragment;
    }

    public void initFromParam(DemoClientHelper.ClientParams clientParams) {
        if (clientParams == null) {
            return;
        }

        clientInfo.business = clientParams.business;
        clientInfo.pageId = clientParams.pageId;

        clientCallback = clientParams.callback;
    }

    public void openFloatWindow(DemoOpenParams params) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DemoConst.Key.CLIENT_INFO, clientFragment.client.clientInfo);
        bundle.putSerializable(DemoConst.Key.OPEN_PARAMS, params);
        clientFragment.sendMessage(DemoConst.ACTION_ADD_WINDOW, bundle);
    }

    public void closeFloatWindow() {
        Bundle params = new Bundle();
        params.putSerializable(DemoConst.Key.CLIENT_INFO, clientFragment.client.clientInfo);
        clientFragment.sendMessage(DemoConst.ACTION_REMOVE_WINDOW, params);
    }

    void onReceiveMessage(@NonNull DemoResult result) {
        if (clientCallback != null) {
            clientCallback.onResult(result);
        }
    }

    public interface ClientCallback {

        void onConnected(String message);
        void onLog(String log);
        void onResult(DemoResult result);

        DemoOpenParams onGetAutoOpenParams();
    }
}
