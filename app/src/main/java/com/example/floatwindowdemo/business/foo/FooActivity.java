package com.example.floatwindowdemo.business.foo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.floatwindowdemo.sdk.DemoClient;
import com.example.floatwindowdemo.sdk.DemoClientHelper;
import com.example.floatwindowdemo.sdk.DemoClientInfo;
import com.example.floatwindowdemo.sdk.DemoConst;
import com.example.floatwindowdemo.R;

/**
 * 业务页面1 主进程
 */
public class FooActivity extends AppCompatActivity {

    DemoClient client;

    EditText logView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_foo_activity);

        logView = findViewById(R.id.foo_log_view);
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

        initFloatWindowClient();
    }

    private void initFloatWindowClient() {
        client = DemoClientHelper.bind(this, DemoClientHelper.ClientParams
                .create("foo", "FooActivity")
                .callback(new DemoClient.ClientCallback() {
                    @Override
                    public void onConnected(String message) {

                    }

                    @Override
                    public void onReceive(String message) {
                        logView.append(message);
                        logView.append("\n");
                    }
                }));
    }

    private void doAddWindow() {
        Bundle params = new Bundle();
        params.putSerializable(DemoConst.Key.WINDOW_CLASS, FooFloatWindow.class);
        client.openFloatWindow(params);
    }

    private void doRemoveWindow() {
        client.closeFloatWindow();
    }
}
