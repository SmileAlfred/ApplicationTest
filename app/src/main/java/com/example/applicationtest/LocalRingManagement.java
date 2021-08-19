package com.example.applicationtest;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class LocalRingManagement implements MediaPlayer.OnPreparedListener {

    private static volatile LocalRingManagement instance;
    private MediaPlayer mediaPlayer;

    private LocalRingManagement() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.MODE_CURRENT);// 设置媒体流类型
        mediaPlayer.setOnPreparedListener(this);
    }

    public static LocalRingManagement getInstance() {
        if (instance == null) {
            synchronized (LocalRingManagement.class) {
                if (instance == null) {
                    instance = new LocalRingManagement();
                }
            }
        }
        return instance;
    }

    public void playMusic(int raw, Context context) {
        // Context context = Appli.getContext();
        try {
            mediaPlayer.reset();
            AssetFileDescriptor file = context.getResources().openRawResourceFd(raw);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                    file.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            file.close();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    // 暂停
    public void pause() {
        mediaPlayer.pause();
    }

    public void play() {
        mediaPlayer.start();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void release() {
        mediaPlayer.release();
        mediaPlayer = null;
        instance = null;
    }
}
