package com.example.floatwindowdemo.sdk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DemoClientHelper {

    private static final String CLIENT_FRAGMENT_TAG = "client_fragment_tag";

    public static DemoClient bind(AppCompatActivity activity) {
        if (activity == null) {
            return null;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment old = fragmentManager.findFragmentByTag(CLIENT_FRAGMENT_TAG);
        DemoClientFragment clientFragment = new DemoClientFragment();
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
}
