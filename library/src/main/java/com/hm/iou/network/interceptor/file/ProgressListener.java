package com.hm.iou.network.interceptor.file;

public interface ProgressListener {
    void onProgress(final long addedBytes, final long contentLength, final boolean done);
}