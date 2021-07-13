package com.example.applicationtest.camerademo;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtils {
    public static final int RECEIVE_FACE_MSG = 1;
    private static final String TAG = CameraUtils.class.getSimpleName().toString();
    private static int cameraId = 1;

    public static void setCameraId(int mCameraId) {
        cameraId = mCameraId;
    }

    public static int getCameraId() {
        return cameraId;
    }

    //1。 存储模块
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     * Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) - 应用被卸载，媒体文件不会遭到移除
     * Context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) - 应用被卸载，媒体文件 会 遭到移除
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        Log.i(TAG, "getOutputMediaFile: path = " + mediaStorageDir.getAbsolutePath().toString());
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "MyCameraApp " + "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
