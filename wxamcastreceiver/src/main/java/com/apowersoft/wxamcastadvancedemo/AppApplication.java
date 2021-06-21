package com.apowersoft.wxamcastadvancedemo;

import android.app.Application;
import android.os.Build;

import com.apowersoft.amcast.advanced.api.AMCastReceiverAdvanced;

/**
 * @CreatedBy: Terry
 * @CreateDate: 2020/9/15
 * @Desc: WxAmcastAdvanceDemo
 */
public class AppApplication extends Application {
    public static boolean init;

    @Override
    public void onCreate() {
        super.onCreate();

        String label = getResources().getString(R.string.lable)+"["+ Build.MODEL +"]";
        String appSecret = "1FBFA-C9E82-E0D25-217B5";
        String appId = "wQlnSyBrIW60b6f74d4c9c5754408329";
        try {
            AMCastReceiverAdvanced.init(this, label, appId,appSecret);
            init = true;
        } catch (Exception e) {
            //密钥过期
            init = false;
        }
    }
}
