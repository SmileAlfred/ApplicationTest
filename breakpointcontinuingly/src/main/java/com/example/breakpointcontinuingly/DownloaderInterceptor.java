package com.example.breakpointcontinuingly;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * OkHttp拦截器
 */

public class DownloaderInterceptor implements Interceptor {

    private ProgressListener progressListener;

    public DownloaderInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (null == chain || null == progressListener) {
            return null;
        }

        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                .build();
    }
}
