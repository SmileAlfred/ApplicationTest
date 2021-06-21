package com.apowersoft.wxmultireceiver;

import android.app.Application;

import com.apowersoft.wxvncreceiver.api.WxVncMultiReceiver;


/**
 * @CreatedBy: Terry
 * @CreateDate: 2021/3/18
 * @Desc: WxMultiReceiverDemo
 */
public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String appSecret = "1FBFA-C9E82-E0D25-217B5";
        String appId = "wQlnSyBrIW60b6f74d4c9c5754408329";
        WxVncMultiReceiver.init(this,appId,appSecret);
    }
}
