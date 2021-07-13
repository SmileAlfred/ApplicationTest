package com.example.applicationtest.camerademo;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

public class CameraFaceDetectionListener implements Camera.FaceDetectionListener {
    private Handler mHandler;
    private final String TAG = CameraFaceDetectionListener.class.getSimpleName().toString();

    public CameraFaceDetectionListener(Handler handler){
        mHandler = handler;
    }
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length > 0) {
            Message msg = mHandler.obtainMessage();
            msg.what = CameraUtils.RECEIVE_FACE_MSG;
            msg.obj = faces;
            msg.sendToTarget();
            //Log.d(TAG, "face detected: "+ faces.length + " ; Face 1 Location X: " + faces[0].rect.centerX() + " ; Y: " + faces[0].rect.centerY() );
        }
    }
}
