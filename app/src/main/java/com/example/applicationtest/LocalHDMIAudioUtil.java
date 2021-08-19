package com.example.applicationtest;


import java.util.concurrent.ArrayBlockingQueue;

/**
 * ？？？未实现
 */
public class LocalHDMIAudioUtil {
    private int audioLength = 80;
    private ArrayBlockingQueue<byte[]> audioQueue = new ArrayBlockingQueue<>(audioLength);
    private SendThread thread = new SendThread();
    public LocalHDMIAudioUtil() {
        isStart = true;
        thread.start();
    }

    int size = 0;
    long lastPutDataTime = 0;
    public void putData(byte[] data) {
        size = (size + 1) % 3;

        lastPutDataTime = System.currentTimeMillis();
        try {
            audioQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isStart;
    long t1;
    long t2;
    int num = 0;
    int total = 0;
    int interval = 18;

    class SendThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isStart) {
                total += t2;
                int sleepTime = 0;
                if (audioQueue.size() < 10) {
                    interval = 22;
                } else {
                    interval = 18;
                }
                if (interval > total) {
                    sleepTime = interval - 1 - total;
                }
                total = 0;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                t1 = System.currentTimeMillis();

                byte[] data = new byte[0];
                try {
                    data = audioQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (callBack != null) {
                    callBack.playLocalHDMIAudio(data);
                }
                if (t1 != 0)
                    t2 = System.currentTimeMillis() - t1;

            }
        }
    }

    PlayLocalHDMIAudioCallBack callBack;

    public void setCallBack(PlayLocalHDMIAudioCallBack callBack) {
        this.callBack = callBack;
    }

    public interface PlayLocalHDMIAudioCallBack {
        void playLocalHDMIAudio(byte[] data);
    }

    public void stop() {
        callBack = null;
        while (audioQueue.size()>0)
            audioQueue.remove();
        t1 = 0;
        t2 = 0;
        num = 0;
        total = 0;
        thread.interrupt();
        isStart = false;
    }

    byte[] Mix2(byte[] sourseFile1, byte[] sourseFile2, int len) {
        //归一化混音
        int MAX = 32767;
        int MIN = -32768;

        double f = 1;
        byte[] outBuffer = new byte[len];
        int output;
        for (int i = 0; i < len; i += 2) {
            int temp = 0;
            temp += (sourseFile1[i + 1] << 8) | sourseFile1[i];
            temp += (sourseFile2[i + 1] << 8) | sourseFile2[i];

            output = (int) (temp * f);
            if (output > MAX) {
                f = (double) MAX / (double) (output);
                output = MAX;
            }
            if (output < MIN) {
                f = (double) MIN / (double) (output);
                output = MIN;
            }
            if (f < 1) {
                f += ((double) 1 - f) / (double) 32;
            }
            outBuffer[i] = (byte) (output & 0xFF);
            outBuffer[i + 1] = (byte) (output >> 8);
        }
        return outBuffer;
    }
}
