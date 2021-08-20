package com.example.keepalive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 通过无声音乐，保活；问题来了，怎么证明他活着？
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, MusicService.class));
    }
}
