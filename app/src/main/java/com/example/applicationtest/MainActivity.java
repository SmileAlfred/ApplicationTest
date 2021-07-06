package com.example.applicationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    public final String TAG = MainActivity.class.getSimpleName().toString();
    public final String pathName = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + ".Secret";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etKey = findViewById(R.id.et_key);
        final EditText etValue = findViewById(R.id.et_value);
        final TextView tvKey = findViewById(R.id.tv_key);
        final TextView tvValue = findViewById(R.id.tv_value);

        Button btn_Save = findViewById(R.id.btn_save);
        Button btn_Get = findViewById(R.id.btn_get);
        Button btn_Clear = findViewById(R.id.btn_clear);
        verifyStoragePermissions(this);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMap(etKey.getText().toString(), etValue.getText().toString());
            }
        });
        btn_Get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = etKey.getText().toString();
                tvKey.setText("Key:" + key);
                tvValue.setText(getMap(key));
            }
        });
        btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPro();
            }
        });

    }

    /**
     * 仅删除了 内存中的pro，并未删除本地文件；
     */
    private void clearPro() {
        File file = new File(pathName);
        try {
            FileInputStream fis = new FileInputStream(file);

            Properties p1 = new Properties();
            p1.load(fis);
            p1.clear();
            Log.i(TAG, "clearPro: p1 = " + p1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) file.delete();
    }

    public String getMap(String key) {
        try {
            File file = new File(pathName);
            if(!file.exists())return null;
            FileInputStream fis = new FileInputStream(file);
            Properties p1 = new Properties();
            p1.load(fis);
            String value = (String) p1.get(key);
            Log.i(TAG, "getMap: p1 = " + p1 + " ; key = " + key + " ; value = " + value);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void saveMap(String key, String value) {
        try {
            File file = new File(pathName);
            FileOutputStream fos = new FileOutputStream(file);

            Properties p = new Properties();
            p.setProperty(key, value);
            p.store(fos, "");
            Log.i(TAG, "saveMap: filePath = " + file.getAbsolutePath() + " ; fileSize存在？ = " + file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 校验权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
