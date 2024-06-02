package com.example.floatwindowdemo.sdk;

import android.os.Bundle;
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

    public void openFloatWindow(Bundle params) {
        params.putSerializable(DemoConst.Key.CLIENT_INFO, clientFragment.client.clientInfo);
        clientFragment.sendMessage(DemoConst.ACTION_ADD_WINDOW, params);
    }

    public void closeFloatWindow() {
        Bundle params = new Bundle();
        params.putSerializable(DemoConst.Key.CLIENT_INFO, clientFragment.client.clientInfo);
        clientFragment.sendMessage(DemoConst.ACTION_REMOVE_WINDOW, params);
    }

    void onReceiveMessage(String msg) {
        if (clientCallback != null && !TextUtils.isEmpty(msg)) {
            clientCallback.onReceive(msg);
        }
    }

    public interface ClientCallback {

        void onConnected(String message);
        void onReceive(String message);
    }
}
