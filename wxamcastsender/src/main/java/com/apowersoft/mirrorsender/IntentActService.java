package com.apowersoft.mirrorsender;

import android.app.IntentService;
import android.content.Intent;

/**
 * 协助PendingIntent跳转
 */
public class IntentActService extends IntentService {


    private static final String TAG = "IntentActService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public IntentActService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent intent1 = new Intent(getApplication(), MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
}
