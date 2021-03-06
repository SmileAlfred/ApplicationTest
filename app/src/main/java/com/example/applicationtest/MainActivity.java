package com.example.applicationtest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

import com.example.applicationtest.camerademo.CameraActivity;
import com.example.applicationtest.threedrotation.ThreeDimensionsRotationActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int LOCATION_PERMISSION_CODE = 2;
    public final String TAG = MainActivity.class.getSimpleName().toString();
    //public final String pathName = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + ".Secret";
    public final String pathName = "";

    EditText etKey, etValue;
    TextView tvKey, tvValue;
    Button btn_Save, btn_Get, btn_Clear, btn_camerademo2, btn_bubble, btn_material, btn_hdmi,btn_ring,btn_3d;
    ToggleButton toggleButton;

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
        toggleButton = findViewById(R.id.togglebtn);
        btn_material = findViewById(R.id.btn_material);
        btn_hdmi = findViewById(R.id.btn_hdmi);
        btn_ring = findViewById(R.id.btn_ring);
        btn_3d = findViewById(R.id.btn_3d);

        btn_camerademo2.setOnClickListener(this);
        btn_Save.setOnClickListener(this);
        btn_Get.setOnClickListener(this);
        btn_Clear.setOnClickListener(this);
        btn_bubble.setOnClickListener(this);
        btn_material.setOnClickListener(this);
        btn_hdmi.setOnClickListener(this);
        btn_ring.setOnClickListener(this);
        btn_3d.setOnClickListener(this);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "isChecked", 500).show();
                } else {
                    // The toggle is disabled
                    Toast.makeText(MainActivity.this, "no Checked", 500).show();
                }
            }
        });

        verifyPermissions(this);

    }

    public int add(int num1, int num2) {
        Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT);
        Log.i(TAG, "add: ???????????????");
        return num1 + num2 - 1;
    }

    public String printUUID() {
        System.out.println("MainActivity ???");
        return MUtils.generateNewUUId();
    }

    /**
     * ???????????? ????????????pro??????????????????????????????
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
            Log.i(TAG, "saveMap: filePath = " + file.getAbsolutePath() + " ; fileSize????????? = " + file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * ?????? ?????? ??????
     *
     * @param activity
     */
    public void verifyPermissions(Activity activity) {
        // Check if we have write permission
        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED);

        if (permissions != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.btn_material:
                intent = new Intent(MainActivity.this, MaterialBtnActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hdmi:
                LocalHDMIAudioUtil localHDMIAudioUtil = new LocalHDMIAudioUtil();
                break;
            case R.id.btn_ring:
                LocalRingManagement mLocalRingManagement = LocalRingManagement.getInstance();
                if(mLocalRingManagement.isPlaying()){
                    mLocalRingManagement.stop();
                    break;
                }
                mLocalRingManagement.playMusic(R.raw.called,MainActivity.this);
                break;
            case R.id.btn_3d:
                startActivity(new Intent(MainActivity.this, ThreeDimensionsRotationActivity.class));
                break;
            default:
                break;
        }

    }

}
