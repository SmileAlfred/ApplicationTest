package com.example.applicationtest.camerademo;

import android.hardware.Camera;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.applicationtest.camerademo.CameraUtils.getOutputMediaFile;

public class CameraCallBack implements Camera.PictureCallback {
    private final String TAG = CameraCallBack.class.getSimpleName().toString();

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //1 为图片， 2 为 视频
        File pictureFile = getOutputMediaFile(1);
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}
