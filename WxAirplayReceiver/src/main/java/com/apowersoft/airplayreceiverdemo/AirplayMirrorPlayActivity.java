package com.apowersoft.airplayreceiverdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apowersoft.airplay.advanced.api.AirplayReceiverAdvanced;
import com.apowersoft.airplay.advanced.api.callback.AirplayViewCallback;
import com.apowersoft.airplay.advanced.receiver.AirplayMirrorLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author : Terry.Tao
 * date   : 2019/12/3
 * desc   : 视频播放页
 */
public class AirplayMirrorPlayActivity extends AppCompatActivity {
    private ImageView ivExit;
    private LinearLayout llMainLayout;
    private RelativeLayout rlTopMenu;

    public static boolean bOpen;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HIDE_TOP_MENU) {
                rlTopMenu.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        bOpen = true;
        setContentView(R.layout.activity_play_airplay_video);
        initView();
        initPlayer();
    }


    private void initView() {
        startHideTimer();
        ivExit = findViewById(R.id.exit_img);
        llMainLayout = findViewById(R.id.main_layout);
        rlTopMenu = findViewById(R.id.rl_top_menu);
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        llMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlTopMenu.setVisibility(rlTopMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if (rlTopMenu.getVisibility() == View.VISIBLE) {
                    startHideTimer();
                } else {
                    stopHideTimer();
                }
            }
        });
    }

    private void initPlayer() {
        AirplayReceiverAdvanced.getInstance().initPlayer(this, airplayCallBack);
    }

    AirplayViewCallback airplayCallBack = new AirplayViewCallback() {
        @Override
        public void addView(AirplayMirrorLayout mirrorLayout, String ip) {
            //添加解码播放部分
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mirrorLayout.setLayoutParams(llp);
            try {
                llMainLayout.addView(mirrorLayout);
            } catch (Exception e) {

            }
        }

        @Override
        public void removeView(AirplayMirrorLayout mirrorLayout, String ip) {
            try {
                llMainLayout.removeView(mirrorLayout);
                finish();
            } catch (Exception e) {

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bOpen = false;
        stopHideTimer();
        AirplayReceiverAdvanced.getInstance().releasePlayer();
    }

    Timer mHideMenuTimer;
    final int MAX_TIME = 5;
    int now_time = 0;
    private final int HIDE_TOP_MENU = 0x0002;

    private void startHideTimer() {
        now_time = 0;
        if (mHideMenuTimer == null) {
            mHideMenuTimer = new Timer();
            mHideMenuTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    now_time++;
                    if (now_time >= MAX_TIME) {
                        stopHideTimer();
                        mHandler.sendEmptyMessage(HIDE_TOP_MENU);
                    }
                }
            }, 0, 1000);
        }
    }

    private void stopHideTimer() {
        if (mHideMenuTimer != null) {
            mHideMenuTimer.cancel();
            mHideMenuTimer = null;
        }
    }
}
