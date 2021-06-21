package com.apowersoft.wxmultireceiver;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.apowersoft.wxvncreceiver.api.WxVncMultiReceiver;
import com.apowersoft.wxvncreceiver.api.callback.WxVncMediaChannelCallback;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WxVncMultiReceiver.getInstance().startWxVncServer(13828);
        WxVncMultiReceiver.getInstance()
                .addWxVncMediaChannelCallback(new WxVncMediaChannelCallback() {
                    @Override
                    public void onDeviceConnect(String ip) {
                        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onDeviceDisconnect(String ip) {

                    }
                });
    }
}