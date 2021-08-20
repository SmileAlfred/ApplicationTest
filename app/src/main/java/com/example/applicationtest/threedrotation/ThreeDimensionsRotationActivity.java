package com.example.applicationtest.threedrotation;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * android 3D 效果
 * https://juejin.cn/post/6998123707025588238
 */
public class ThreeDimensionsRotationActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;
    private TdRenderer tdRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tdRenderer=new TdRenderer(this);
        // 创建预览视图，并将其设置为Activity的内容
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(tdRenderer);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tdRenderer.start();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tdRenderer.stop();
        mGLSurfaceView.onPause();
    }

}