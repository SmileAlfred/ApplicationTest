package com.apowersoft.wxamcastadvancedemo;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apowersoft.amcast.advanced.api.AMCastReceiverAdvanced;
import com.apowersoft.amcast.advanced.api.callback.AMCastViewCallback;
import com.apowersoft.amcast.advanced.receiver.AndroidMirrorLayout;

/**
 * author : Terry.Tao
 * date   : 2019/12/3
 * desc   :
 */
public class AMCastMirrorPlayActivity extends AppCompatActivity {
    private ImageView ivExit;
    private LinearLayout llMainLayout;
    private String ipAddress;
    private RelativeLayout rlTopMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_amcast_mirror_play);
        initView();
        initPlayer();
    }

    private void initView() {
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
            }
        });
    }

    private void initPlayer() {
        AMCastReceiverAdvanced.getInstance().initPlayer(AMCastMirrorPlayActivity.this,new AMCastViewCallback() {
            @Override
            public void addView(AndroidMirrorLayout androidMirrorLayout, String ip) {
                ipAddress = ip;
                //添加解码播放部分
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                llp.weight = 1;
                try {
                    llMainLayout.addView(androidMirrorLayout, llp);
                } catch (Exception e) {

                }
            }

            @Override
            public void removeView(AndroidMirrorLayout androidMirrorLayout, String ip) {
                try {
                    llMainLayout.removeView(androidMirrorLayout);
                    finish();
                } catch (Exception e) {

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        AMCastReceiverAdvanced.getInstance().closeConnect(ipAddress);
        AMCastReceiverAdvanced.getInstance().releasePlayer();
        super.onDestroy();
    }
}
