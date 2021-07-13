package com.example.applicationtest.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.applicationtest.R;

import java.util.List;


/**
 * 1. 检测和访问相机 - 创建代码以检查设备是否配有相机并请求访问权限。
 * 2. 创建预览类 - 创建一个扩展 SurfaceView 并实现 SurfaceHolder 接口的相机预览类。此类预览来自摄像机的实时图像。
 * 3. 构建预览布局 - 拥有相机预览类后，创建一个融合预览和您想要的界面控件的视图布局。
 * 4. 为捕获设置侦听器 - 为界面控件连接侦听器，以便启用图像或视频捕获来响应用户操作，例如按下按钮。
 * 5. 捕获并保存文件 - 创建用于捕获图片或视频并保存输出的代码。
 * 6. 释放相机 - 在用完相机之后，您的应用必须正确地释放相机，以便其他应用使用。
 *
 * 人脸检测参考了 https://blog.csdn.net/renlei0109/article/details/49911695
 */
public class CameraActivity extends Activity {

    private final String TAG = CameraActivity.class.getSimpleName();
    private CameraPreview mPreview;
    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private MyHandler mHandler = new MyHandler();
    private CameraFaceView cameraFaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkCameraPermission();
        initCamera();


        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        cameraFaceView = findViewById(R.id.face_view);
        // Add a listener to the Capture button
        ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 // get an image from the camera
                                                 mCamera.takePicture(null, null, new CameraCallBack());
                                             }
                                         }
        );
    }

    private void initCamera() {
        if (checkCameraHardware(this)) {
            mCamera = getCameraInstance();
            //方法获取有关相机功能的详细信息，
            if (mCamera != null) {
                mCamera.setFaceDetectionListener(new CameraFaceDetectionListener(mHandler));
                Camera.Parameters cameraParameters = mCamera.getParameters();
                checkFunctionAvailable(cameraParameters);
                Log.i(TAG, "onCreate: 相机参数：" + cameraParameters.toString());

                // set the focus mode；自主对焦；前置摄像头不可
                //cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                //旋转 90度
                cameraParameters.setRotation(90);

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
        //释放相机
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();
    }

    /**
     * 动态申请相机的权限
     */
    private void checkCameraPermission() {
        //添加权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: 获取到相机权限");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Log.i(TAG, "onCreate: 请求到相机权限");
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
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
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