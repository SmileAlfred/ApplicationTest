package com.example.exoplayertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.media2.exoplayer.external.ExoPlayerFactory;
import androidx.media2.exoplayer.external.source.MediaSource;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * ???未通过【播放库】 ExoPlayer 的测试
 */
public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.ACCESS_NETWORK_STATE};

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermissions(this);
        playerView = findViewById(R.id.playview);

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(MainActivity.this).build();
        playerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                MainActivity.this,
                Util.getUserAgent(MainActivity.this, "yourApplicationName"));

        String  mp4uri = "https://media.vued.vanthink.cn/CJ7%20-%20Trailer.mp4";
        ProgressiveMediaSource videoSource  = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mp4uri));
        player.prepare(videoSource);


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
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED);

        if (permissions != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        }
    }
}