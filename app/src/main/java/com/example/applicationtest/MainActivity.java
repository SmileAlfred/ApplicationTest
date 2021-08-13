package com.example.applicationtest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.applicationtest.camerademo.CameraActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends Activity  implements View.OnClickListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int LOCATION_PERMISSION_CODE = 2;
    public final String TAG = MainActivity.class.getSimpleName().toString();
    //public final String pathName = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + ".Secret";
    public final String pathName ="";

    EditText etKey,etValue;
    TextView tvKey, tvValue;
    Button btn_Save,btn_Get,btn_Clear,btn_camerademo2,btn_bubble;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKey = findViewById(R.id.et_key);
        etValue = findViewById(R.id.et_value);
        tvKey = findViewById(R.id.tv_key);
        tvValue = findViewById(R.id.tv_value);
        btn_Save = findViewById(R.id.btn_save);
        btn_Get = findViewById(R.id.btn_get);
        btn_Clear = findViewById(R.id.btn_clear);
        btn_bubble = findViewById(R.id.btn_bubble);
        btn_camerademo2 = findViewById(R.id.btn_camerademo2);

        btn_camerademo2.setOnClickListener(this);
        btn_Save.setOnClickListener(this);
        btn_Get.setOnClickListener(this);
        btn_Clear.setOnClickListener(this);
        btn_bubble.setOnClickListener(this);



        verifyPermissions(this);

    }

    public int add(int num1, int num2) {
        Toast.makeText(MainActivity.this,"测试单元",Toast.LENGTH_SHORT );
        Log.i(TAG, "add: 单元测试～");
        return num1 + num2 - 1;
    }

    public String printUUID() {
        System.out.println("MainActivity 中");
        return MUtils.generateNewUUId();
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
            if (!file.exists()) return null;
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
     * 校验 存储 权限
     *
     * @param activity
     */
    public void verifyPermissions(Activity activity) {
        // Check if we have write permission
        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissions != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.btn_camerademo2:
             Intent intent = new Intent(MainActivity.this, CameraActivity.class);
             startActivity(intent);
             break;
         case R.id.btn_bubble:
             break;
         case R.id.btn_save:
             saveMap(etKey.getText().toString(), etValue.getText().toString());
             break;
         case R.id.btn_get:
             String key = etKey.getText().toString();
             tvKey.setText("Key:" + key);
             tvValue.setText(getMap(key));
             break;
         case R.id.btn_clear:
             clearPro();
             break;
         default:
             break;
     }

    }

}
