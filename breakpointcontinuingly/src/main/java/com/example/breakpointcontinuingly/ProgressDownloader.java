package com.example.breakpointcontinuingly;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttpClient操作下载类
 */
public class ProgressDownloader {

    private ProgressListener progressListener;
    private String url;
    private OkHttpClient client;
    private File destination;
    private Call call;

    /**
     * 构造方法
     */
    public ProgressDownloader(String url, File destination, ProgressListener progressListener) {
        this.url = url;
        this.destination = destination;
        this.progressListener = progressListener;
        client = getProgressClient();//获取OkHttpClient对象
    }

    /**
     * 获取OkHttpClient对象
     */
    public OkHttpClient getProgressClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownloaderInterceptor(progressListener))//拦截器
                .build();
    }

    /**
     * 下载
     *
     * @param startsPoint：开始下载的位置
     */
    public void download(final long startsPoint) {
        call = newCall(startsPoint);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                save(response, startsPoint);
            }
        });
    }

    /**
     * newCall方法
     */
    private Call newCall(long startPoints) {
        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + startPoints + "-")//断点续传要用到的，指示下载的区间
                .build();
        return client.newCall(request);
    }

    /**
     * 保存文件
     */
    private void save(Response response, long startsPoint) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(destination, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (call != null) {
            call.cancel();
        }
    }

}