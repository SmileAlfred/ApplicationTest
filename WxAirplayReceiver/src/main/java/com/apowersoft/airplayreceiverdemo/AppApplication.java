package com.apowersoft.airplayreceiverdemo;

import android.app.Application;
import android.os.Build;

import com.apowersoft.airplay.advanced.api.AirplayReceiverAdvanced;


/**
 * author : Terry.Tao
 * date   : 2019/11/6
 * desc   :
 */
public class AppApplication extends Application {
    public static boolean init;
    @Override
    public void onCreate() {
        super.onCreate();

        String label = getResources().getString(R.string.lable);
        String appSecret = "1FBFA-C9E82-E0D25-217B5";
        String appId = "wQlnSyBrIW60b6f74d4c9c5754408329";
        String deviceName = label+"["+ Build.MODEL +"]";
        try {
            AirplayReceiverAdvanced.init(this,deviceName,appId,appSecret);
            init = true;
        }catch (Exception e){
            //密钥过期
            init = false;
        }

    }
}
