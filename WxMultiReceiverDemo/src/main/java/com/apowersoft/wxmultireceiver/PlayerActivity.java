package com.apowersoft.wxmultireceiver;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.apowersoft.wxvncreceiver.api.WxVncMultiReceiver;
import com.apowersoft.wxvncreceiver.api.callback.WxVncPlayerCallback;
import com.apowersoft.wxvncreceiver.widget.WxVncMirrorLayout;


public class PlayerActivity extends AppCompatActivity {
    private MirrorGrideView mirrorGrideView;
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player);
        mirrorGrideView = findViewById(R.id.content);

        // 画面渲染回调
        WxVncMultiReceiver.getInstance()
                .registerWxVncPlayer(this, new WxVncPlayerCallback() {
                    @Override
                    public void addView(WxVncMirrorLayout pcMirrorLayout, String ip) {
                        mirrorGrideView.addView(pcMirrorLayout);
                        size++;
                    }

                    @Override
                    public void removeView(WxVncMirrorLayout pcMirrorLayout, String ip) {
                        mirrorGrideView.removeView(pcMirrorLayout);
                        size--;
                        if (size==0){
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        WxVncMultiReceiver.getInstance().closeAllWxVncChannel();
        WxVncMultiReceiver.getInstance().unregisterWxVncPlayer();
        super.onDestroy();
    }
}