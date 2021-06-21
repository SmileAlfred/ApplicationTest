package com.apowersoft.wxamcastadvancedemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apowersoft.amcast.advanced.api.AMCastReceiverAdvanced;
import com.apowersoft.amcast.advanced.api.callback.AMCastCallback;

public class MainActivity extends AppCompatActivity {
    public static final int AMCAST_PORT = 4487;
    public static final int AMCAST_BACK_PORT = 24486;
    private RadioGroup rgDecode;
    private TextView tvDeviceName;
    private RadioGroup rgRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgDecode = findViewById(R.id.rg_decode);
        tvDeviceName = findViewById(R.id.tv_device_name);
        rgRotation = findViewById(R.id.rg_rotation);
        tvDeviceName.setText(getString(R.string.lable) + "[" + Build.MODEL + "]");

        rgDecode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (AppApplication.init) {
                    //设置解码模式
                    switch (checkedId) {
                        case R.id.rb_soft_decode:
                            AMCastReceiverAdvanced.getInstance().setSoftDecode(true);
                            break;
                        case R.id.rb_hard_decode:
                            AMCastReceiverAdvanced.getInstance().setSoftDecode(false);
                            break;
                    }
                }
            }
        });
        rgRotation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (AppApplication.init) {
                    //设置播放方向
                    switch (checkedId) {
                        case R.id.rb_land:
                            AMCastReceiverAdvanced.getInstance().setPortrait(false);
                            break;
                        case R.id.rb_portrait:
                            AMCastReceiverAdvanced.getInstance().setPortrait(true);
                            break;
                    }
                }
            }
        });
        initAMCastReceiver();
    }

    private void initAMCastReceiver() {
        AMCastReceiverAdvanced.getInstance()
                .startAMCastService(AMCAST_PORT, AMCAST_BACK_PORT, new AMCastCallback() {
                    @Override
                    public void onVideoStart(String ip) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this,
                                        AMCastMirrorPlayActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    @Override
    protected void onDestroy() {
        AMCastReceiverAdvanced.getInstance().stopAMCastService();
        super.onDestroy();
    }
}
