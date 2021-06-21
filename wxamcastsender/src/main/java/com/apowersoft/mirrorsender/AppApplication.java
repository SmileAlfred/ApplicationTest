package com.apowersoft.mirrorsender;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.apowersoft.mirrorcast.api.MirrorCastSender;


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
        Intent intent = new Intent(this, IntentActService.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String deviceName = getResources().getString(R.string.app_name)+"["+ Build.MODEL +"]";
        String appSecret = "1FBFA-C9E82-E0D25-217B5";
        String appId = "wQlnSyBrIW60b6f74d4c9c5754408329";
        try {
            MirrorCastSender.init(this,intent,deviceName,appId,appSecret);
            init = true;
        }catch (Exception e){
            //密钥过期
            init = false;
        }

    }
}
