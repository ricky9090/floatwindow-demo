package com.example.floatwindowdemo.business;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.floatwindowdemo.R;
import com.example.floatwindowdemo.business.bar.BarActivity;
import com.example.floatwindowdemo.business.foo.FooActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View foo = findViewById(R.id.foo_activity_btn);
        foo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fooIntent = new Intent();
                fooIntent.setClass(MainActivity.this, FooActivity.class);
                startActivity(fooIntent);
            }
        });

        View bar = findViewById(R.id.bar_activity_btn);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barIntent = new Intent();
                barIntent.setClass(MainActivity.this, BarActivity.class);
                startActivity(barIntent);
            }
        });
    }
}