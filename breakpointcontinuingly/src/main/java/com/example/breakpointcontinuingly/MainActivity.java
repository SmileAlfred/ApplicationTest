package com.example.breakpointcontinuingly;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProgressListener, View.OnClickListener {

    private String PACKAGE_URL = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk";
    private ProgressBar progressBar;
    private Button btn_download, btn_cancel, btn_continue;
    private long breakPoints;
    private ProgressDownloader downloader;
    private File file;
    private long totalBytes;
    private long contentLength;
    private boolean isLoading = false;
    private final String TAG = "\t\tMainActivity\t\t";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.verifyPermissions(MainActivity.this, PERMISSIONS_STORAGE, 101);

        progressBar = findViewById(R.id.progressBar);
        btn_download = findViewById(R.id.btn_download);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_continue = findViewById(R.id.btn_continue);

        btn_download.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
    }

    /**
     * onPreExecute方法
     * 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
     */

    @Override
    public void onPreExecute(long contentLength) {
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;
            progressBar.setMax((int) (contentLength / 1024));
        }
    }

    /**
     * update方法
     */

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        progressBar.setProgress((int) (totalBytes + breakPoints) / 1024);
        if (done) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "run: 下载完成");
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                Log.e(TAG, "onClick: ");
                if (isLoading) break;
                breakPoints = 0L;
                String path = PathUtils. getExternalDownloadsPath();
                file = new File(path, "sample.apk");
                Log.e(TAG, "onClick: filePath "+file.getAbsolutePath() );
                Toast.makeText(MainActivity.this, "开始下载? ", Toast.LENGTH_SHORT).show();
                downloader = new ProgressDownloader(PACKAGE_URL, file, MainActivity.this);
                downloader.download(0L);
                isLoading = true;
                break;
            case R.id.btn_continue:
                if (isLoading) break;
                Toast.makeText(MainActivity.this, "继续暂停", Toast.LENGTH_SHORT).show();
                downloader.download(breakPoints);
                isLoading = true;
                break;
            case R.id.btn_cancel:
                if (!isLoading) break;
                downloader.pause();
                Toast.makeText(MainActivity.this, "下载暂停", Toast.LENGTH_SHORT).show();
                // 存储此时的totalBytes，即断点位置。
                breakPoints = totalBytes;
                isLoading = false;
                break;
            default:
                break;
        }
    }
}