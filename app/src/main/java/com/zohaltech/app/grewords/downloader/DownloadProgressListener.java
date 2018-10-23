package com.zohaltech.app.grewords.downloader;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
