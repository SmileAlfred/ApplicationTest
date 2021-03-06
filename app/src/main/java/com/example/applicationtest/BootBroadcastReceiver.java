package com.example.applicationtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/***
 *开机广播
 **/
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent in) {
        if (in.getAction().equals(ACTION)) {
            Intent intents = new Intent(context, MainActivity.class);  // 要启动的Activity
            //1.如果自启动APP，参数为需要自动启动的应用包名
            Log.d("bootBroadcast", "onReceive: 收到开机广播");
            //Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            //下面这句话必须加上才能开机自动运行app的界面
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //2.如果自启动Activity
            context.startActivity(intents);
            //3.如果自启动服务
            //context.startService(intents);
        }
    }
}
