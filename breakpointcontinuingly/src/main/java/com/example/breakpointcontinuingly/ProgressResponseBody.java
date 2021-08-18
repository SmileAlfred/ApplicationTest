package com.example.breakpointcontinuingly;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * ResponseBody继承类
 * https://blog.csdn.net/weixin_37730482/article/details/72897322
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;

    /**
     * 构造方法
     */

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        if (progressListener != null) {
            progressListener.onPreExecute(contentLength());
        }
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 获取Source
     */

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytes = 0L;

            @Override
            public long read(@NotNull okio.Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytes += bytesRead != -1 ? bytesRead : 0;
                if (null != progressListener) {
                    progressListener.update(totalBytes, bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }
}
