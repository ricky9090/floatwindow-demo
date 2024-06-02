package com.example.floatwindowdemo.business.bar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.floatwindowdemo.sdk.DemoClient;
import com.example.floatwindowdemo.sdk.DemoClientHelper;
import com.example.floatwindowdemo.sdk.DemoConst;
import com.example.floatwindowdemo.R;

/**
 * 业务页面2 独立进程
 */
public class BarActivity extends AppCompatActivity {

    DemoClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_bar_activity);

        View addWindowBtn = findViewById(R.id.add_window_btn);
        addWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddWindow();
            }
        });

        View removeWindowBtn = findViewById(R.id.remove_window_btn);
        removeWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRemoveWindow();
            }
        });

        String a = savedInstanceState == null ? "null" : "not null";
        Log.d(DemoConst.TAG, "savedInstanceState: " + a);
        client = DemoClientHelper.bind(this, DemoClientHelper.ClientParams
                .create("bar", "BarActivity")
                .callback(new DemoClient.ClientCallback() {
                    @Override
                    public void onConnected(String message) {

                    }

                    @Override
                    public void onReceive(String message) {

                    }
                }));
    }

    private void doAddWindow() {
        Bundle params = new Bundle();
        params.putSerializable(DemoConst.Key.WINDOW_CLASS, BarFloatWindow.class);
        client.openFloatWindow(params);
    }

    private void doRemoveWindow() {
        client.closeFloatWindow();
    }
}
