package com.example.floatwindowdemo.sdk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DemoClientHelper {

    public static class ClientParams {
        String business;
        String pageId;

        DemoClient.ClientCallback callback;

        public static ClientParams create(String business, String pageId) {
            ClientParams params = new ClientParams();
            params.business = business;
            params.pageId = pageId;
            return params;
        }

        public ClientParams callback(DemoClient.ClientCallback callback) {
            this.callback = callback;
            return this;
        }
    }

    private static final String CLIENT_FRAGMENT_TAG = "client_fragment_tag";

    public static DemoClient bind(AppCompatActivity activity, ClientParams clientParams) {
        if (activity == null) {
            return null;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment old = fragmentManager.findFragmentByTag(CLIENT_FRAGMENT_TAG);
        DemoClientFragment clientFragment = new DemoClientFragment();
        if (clientParams != null) {
            clientFragment.client.initFromParam(clientParams);
        }
        if (old == null) {
            fragmentManager.beginTransaction()
                    .add(clientFragment, CLIENT_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction()
                    .remove(old)
                    .add(clientFragment, CLIENT_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        }
        return clientFragment.client;
    }

    static void doLog(DemoClient client, String log) {
        Log.d(DemoConst.TAG, log);
        if (client.clientCallback != null) {
            client.clientCallback.onLog(log);
        }
    }
}
