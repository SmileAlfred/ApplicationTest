package com.example.breakpointcontinuingly;

/**
 * 下载进度
 * https://blog.csdn.net/weixin_37730482/article/details/72897322
 */
public interface ProgressListener {

    void onPreExecute(long contentLength);

    void update(long totalBytes, boolean done);

}
