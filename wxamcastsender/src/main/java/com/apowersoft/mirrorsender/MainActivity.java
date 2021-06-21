package com.apowersoft.mirrorsender;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apowersoft.mirrorcast.api.DeviceBean;
import com.apowersoft.mirrorcast.api.MirrorCastSender;
import com.apowersoft.mirrorcast.api.OnDeviceListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDeviceListener {
    private final String[] permission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION_CODE = 0x1001;
    private ImageView ivSearch;
    private TextView tvResearch;
    private RecyclerView reDevices;

    private MirrorCastSender mirrorCastSender;
    private List<DeviceBean> deviceBeans;
    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceBeans = new ArrayList<>();

        initView();
        checkPermissions();
    }

    private void checkPermissions() {
        //申请权限
        if (PermissionsChecker.lacksPermissions(this, permission)) {
            ActivityCompat.requestPermissions(this, permission, REQUEST_PERMISSION_CODE);
        } else {
            initMirrorCast();
        }
    }

    private void initMirrorCast() {
        if (AppApplication.init) {
            mirrorCastSender = MirrorCastSender.getInstance();
            mirrorCastSender.startMirrorService(PortConstant.SERVICE_PORT);
            mirrorCastSender.addOnDeviceListener(this);

            discoverReceiver();
        }
    }

    /**
     * 发现设备
     */
    private void discoverReceiver() {
        if (AppApplication.init) {
            tvResearch.setVisibility(View.GONE);
            ivSearch.setVisibility(View.VISIBLE);
            mirrorCastSender.discoverReceiver(PortConstant.MULTICAST_PORT, PortConstant.MULTICAST_PORT_BACK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvResearch.setVisibility(View.VISIBLE);
                    ivSearch.setVisibility(View.GONE);
                    mirrorCastSender.stopDiscoverReceiver();
                }
            }, 3000);
        }
    }


    private void initView() {
        reDevices = findViewById(R.id.re_devices);
        ivSearch = findViewById(R.id.iv_search);
        tvResearch = findViewById(R.id.tv_re_search);
        //搜索动画
        Glide.with(this).asGif().load(R.drawable.gif_device_search).into(ivSearch);

        reDevices.setLayoutManager(new LinearLayoutManager(this));
        deviceAdapter = new DeviceAdapter(deviceBeans);
        reDevices.setAdapter(deviceAdapter);

        deviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                if (TextUtils.isEmpty(deviceAdapter.getConnectIp())) {
                    mirrorCastSender.startMirror(deviceBeans.get(position).getIpAddress());
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.only_link_one_device_tips), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deviceAdapter.setOnStopClickListener(new DeviceAdapter.OnStopClickListener() {
            @Override
            public void stopClick(View view, int position) {
                mirrorCastSender.stopMirror(deviceAdapter.getConnectIp());
            }
        });

        tvResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverReceiver();
            }
        });
    }

    /**
     * 设备上线
     *
     * @param s
     * @param s1
     * @param i
     */
    @Override
    public void onDeviceUp(String s, String s1, int i) {
        for (DeviceBean deviceBean : deviceBeans) {
            if (deviceBean.getIpAddress().equals(s1)) {
                return;
            }
        }
        DeviceBean deviceBean = new DeviceBean();
        deviceBean.setDeviceName(s);
        deviceBean.setIpAddress(s1);
        deviceBean.setDeviceType(i);
        deviceBeans.add(deviceBean);
        deviceAdapter.notifyDataSetChanged();
    }

    /**
     * 设备下线
     *
     * @param s
     * @param s1
     * @param i
     */
    @Override
    public void onDeviceDown(String s, String s1, int i) {
        for (DeviceBean deviceBean : deviceBeans) {
            if (deviceBean.getIpAddress().equals(s1)) {
                deviceBeans.remove(deviceBean);
                deviceAdapter.notifyDataSetChanged();
                return;
            }
        }

    }

    /**
     * 投屏连接
     *
     * @param s
     */
    @Override
    public void onDeviceConnect(String s) {
        deviceAdapter.setConnectIp(s);
    }

    /**
     * 断开投屏连接
     */
    @Override
    public void onDeviceDisconnect() {
        deviceAdapter.setConnectIp("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mirrorCastSender.removeDeviceListener(this);
        mirrorCastSender.stopDiscoverReceiver();
        if (TextUtils.isEmpty(deviceAdapter.getConnectIp())) {
            mirrorCastSender.stopMirror(deviceAdapter.getConnectIp());
        }
        mirrorCastSender.stopMirrorService();
    }

    /**
     * 权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE && PermissionsChecker.hasAllPermissionsGranted(grantResults)) {
            initMirrorCast();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
