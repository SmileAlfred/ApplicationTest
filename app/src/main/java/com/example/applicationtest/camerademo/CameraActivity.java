package com.example.applicationtest.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.applicationtest.R;

import java.io.IOException;
import java.util.List;

import static com.example.applicationtest.camerademo.CameraUtils.MEDIA_TYPE_VIDEO;
import static com.example.applicationtest.camerademo.CameraUtils.getOutputMediaFile;


/**
 * 1. 检测和访问相机 - 创建代码以检查设备是否配有相机并请求访问权限。
 * 2. 创建预览类 - 创建一个扩展 SurfaceView 并实现 SurfaceHolder 接口的相机预览类。此类预览来自摄像机的实时图像。
 * 3. 构建预览布局 - 拥有相机预览类后，创建一个融合预览和您想要的界面控件的视图布局。
 * 4. 为捕获设置侦听器 - 为界面控件连接侦听器，以便启用图像或视频捕获来响应用户操作，例如按下按钮。
 * 5. 捕获并保存文件 - 创建用于捕获图片或视频并保存输出的代码。
 * 6. 释放相机 - 在用完相机之后，您的应用必须正确地释放相机，以便其他应用使用。
 * <p>
 * 人脸检测参考了 https://blog.csdn.net/renlei0109/article/details/49911695
 * BUG:相机只可以单次使用，拍照后自动停止了预览，无法再次拍照；
 */
public class CameraActivity extends Activity {

    private final String TAG = CameraActivity.class.getSimpleName();
    private CameraPreview mPreview;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private MyHandler mHandler = new MyHandler();
    private CameraFaceView cameraFaceView;
    private volatile boolean isVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkCameraPermission();
        checkAudioPermission();
        initCamera();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        cameraFaceView = findViewById(R.id.face_view);
        // Add a listener to the Capture button
        TextView tv_pai = findViewById(R.id.tv_captrue);
        TextView tv_lu = findViewById(R.id.tv_lu);
        tv_pai.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          // get an image from the camera
                                              mCamera.takePicture(null, null, new CameraCallBack());
                                          Toast.makeText(CameraActivity.this, "照片已保存", Toast.LENGTH_SHORT).show();
                                      }
                                  }
        );
        tv_lu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 点击录制 ～");
                if (!isVideo) {
                    initVideo();
                    try {
                        mMediaRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "onClick: 视频准备失败：" + e.getMessage());
                    }
                    mCamera.unlock();
                    mMediaRecorder.start();
                    Toast.makeText(CameraActivity.this, "开始视频录制", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CameraActivity.this, "视频录制结束", Toast.LENGTH_SHORT).show();
                    //停止 MediaRecorder -
                    mMediaRecorder.stop();
                    //重置 MediaRecorder - （可选）从录制器中移除配置设置
                    //mMediaRecorder.reset();
                    ///释放 MediaRecorder -
                    //mMediaRecorder.release();
                    //停止预览 - 当 Activity 结束对相机的使用时，使用 其 停止预览。
                    //mCamera.stopPreview();
                    //释放相机 - 释放相机以便其他应用可通过调用 其 使用
                    //mCamera.release();
                }
                isVideo = !isVideo;
            }
        });

    }

    private void initVideo() {
        //为您的应用指定 SurfaceView 预览布局元素。使用您在连接预览部分中指定的相同对象。
        //mMediaRecorder.setPreviewDisplay(this);

        //2.配置 MediaRecorder
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //获取配置文件实例。
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CameraUtils.getCameraId(), CamcorderProfile.QUALITY_720P);
        //设置视频的输出格式和编码
        mMediaRecorder.setProfile(camcorderProfile);
        //设置输出文件，使用保存媒体文件部分示例方法中的
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
    }

    private void initCamera() {
        if (checkCameraHardware(this)) {
            if(mCamera == null)mCamera = getCameraInstance();
            //方法获取有关相机功能的详细信息，
            if (mCamera != null) {
                mCamera.setFaceDetectionListener(new CameraFaceDetectionListener(mHandler));
                Camera.Parameters cameraParameters = mCamera.getParameters();
                checkFunctionAvailable(cameraParameters);
                Log.i(TAG, "onCreate: 相机参数：" + cameraParameters.toString());

                // set the focus mode；自主对焦；前置摄像头不可
                //cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                //旋转 90度
                cameraParameters.setRotation(0);

                //区域测光和对焦: 前置摄像头不可！
                //if (cameraParameters.getMaxNumMeteringAreas() > 0) { // check that metering areas are supported
                //    List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                //    Rect areaRect1 = new Rect(10, 10, 100, 100);    // specify an area in center of image
                //    meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
                //    Rect areaRect2 = new Rect(600, 600, 690, 690);  // specify an area in upper right of image
                //    meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
                //    cameraParameters.setMeteringAreas(meteringAreas);
                //} else Log.e(TAG, "initCamera: 未设置区域测光和对焦");

                //set Camera parameters
                mCamera.setParameters(cameraParameters);
            } else Log.i(TAG, "onCreate: cameraInstance == null");
        } else Log.e(TAG, "initCamera: Fail: " + "硬件不支持");

        mMediaRecorder = new MediaRecorder();
    }


    /**
     * 检测 相机功能 是否可用；即检测硬件支持的功能
     *
     * @param cameraParameters
     */
    private void checkFunctionAvailable(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        Log.i(TAG, "checkFunctionAvailable: \n" + focusModes);
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // Autofocus mode is supported
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //释放相机；
        //releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        //releaseCamera();
    }

    /**
     * 动态申请 相机 的权限
     */
    private void checkCameraPermission() {
        //添加权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: 获取到相机权限");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Log.i(TAG, "onCreate: 请求到相机权限");
        }
    }

    /**
     * 动态申请 录音 的权限
     */
    private void checkAudioPermission() {
        //添加权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: 获取到 录音 权限");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            Log.i(TAG, "onCreate: 请求到  录音 权限");
        }
    }

    /**
     * 1. Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Log.i(TAG, "checkCameraHardware: 设备有 " + numberOfCameras + " 个相机");

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    /**
     * 2. A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            // 0 表示后置摄像头 cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK
            c = Camera.open(CameraUtils.getCameraId()); // attempt to get a Camera instance;
        } catch (Exception e) {
            Log.e(TAG, "getCameraInstance: 报错：" + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }


    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            int what = msg.what;
            switch (what) {
                case CameraUtils.RECEIVE_FACE_MSG:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Camera.Face[] faces = (Camera.Face[]) msg.obj;
                            cameraFaceView.setFaces(faces);
                        }
                    });

                    break;
            }
            super.handleMessage(msg);
        }
    }


}