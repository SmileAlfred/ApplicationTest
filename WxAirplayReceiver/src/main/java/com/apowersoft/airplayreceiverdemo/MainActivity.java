package com.apowersoft.airplayreceiverdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apowersoft.airplay.advanced.api.AirplayReceiverAdvanced;
import com.apowersoft.airplay.advanced.api.callback.AirplayCallBack;
import com.apowersoft.airplayreceiver.api.AirplayResolution;


public class MainActivity extends AppCompatActivity {
    private RadioGroup rgResolution;
    private RadioGroup rgDecode;
    private TextView tvDeviceName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgResolution = findViewById(R.id.rg_resolution);
        rgResolution.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (AppApplication.init) {
                    switch (checkedId) {
                        case R.id.rb_low_resolution:
                            AirplayReceiverAdvanced.getInstance().setAirplayResolution(AirplayResolution.LOW_RESOLUTION_TYPE);
                            break;
                        case R.id.rb_high_resolution:
                            AirplayReceiverAdvanced.getInstance().setAirplayResolution(AirplayResolution.HIGH_RESOLUTION_TYPE);
                            break;
                    }
                }
            }
        });

        rgDecode = findViewById(R.id.rg_decode);
        rgDecode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (AppApplication.init) {
                    switch (checkedId) {
                        case R.id.rb_soft_decode:
                            AirplayReceiverAdvanced.getInstance().setSoftDecode(true);
                            break;
                        case R.id.rb_hard_decode:
                            AirplayReceiverAdvanced.getInstance().setSoftDecode(false);
                            break;
                    }
                }
            }
        });

        String label = getResources().getString(R.string.lable);
        String deviceName = label+"["+ Build.MODEL +"]";
        tvDeviceName = findViewById(R.id.tv_device_name);
        tvDeviceName.setText(deviceName);
        if (AppApplication.init) {
            initAirplay();
        }

    }

    private void initAirplay() {
        AirplayReceiverAdvanced.getInstance().startAirplayReceiver();
        AirplayReceiverAdvanced.getInstance().addAirplayCallBack(new AirplayCallBack() {
            @Override
            public void onOpenFail() {

            }

            @Override
            public void onMirrorStop(String ip) {

            }

            @Override
            public void onMirrorStart(String ip) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, AirplayMirrorPlayActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onOpenSuccess() {

            }

            @Override
            public void onWaitTimeOut() {

            }
        });


    }

    @Override
    protected void onDestroy() {
        AirplayReceiverAdvanced.getInstance().clearAirplayCallBack();
        AirplayReceiverAdvanced.getInstance().stopAirplay();
        super.onDestroy();
    }
}
