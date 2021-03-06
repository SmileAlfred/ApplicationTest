package com.example.screenshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.screenshare.network.MWebSocketClient;

import java.net.URI;

/**
 * @author by talon, Date on 2020/7/19.
 * note: 播放视频
 */
public class PlayActivity extends AppCompatActivity implements MWebSocketClient.CallBack {

    private MWebSocketClient webSocketClient;
    private ImageView iv_image;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        iv_image = findViewById(R.id.iv_image);

        Intent intent = getIntent();
        if (intent != null) {
            String host = intent.getStringExtra("host");
            try {
                URI url = new URI("ws://" + host + ":" + Config.ANDROID_SERVER_PORT);
                webSocketClient = new MWebSocketClient(url, this);
                webSocketClient.setConnectionLostTimeout(5 * 1000);
                boolean flag = webSocketClient.connectBlocking();
                Toast.makeText(PlayActivity.this, "链接状态：" + flag, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onClientStatus(boolean isConnected) {

    }

    @Override
    public void onBitmapReceived(final Bitmap bitmap) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                iv_image.setImageBitmap(bitmap);
            }
        });
    }
}
