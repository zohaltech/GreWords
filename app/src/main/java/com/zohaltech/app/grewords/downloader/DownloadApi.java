package com.zohaltech.app.grewords.downloader;

import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownloadApi {
    private Retrofit retrofit;
    
    public DownloadApi(String baseUrl, DownloadProgressListener listener) {
        
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);
        
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    
    public void downloadAPK(@NonNull String url, final File file, Observer<InputStream> observer) {
        retrofit.create(DownloadService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBodyResponse -> responseBodyResponse.body().byteStream())
                .observeOn(Schedulers.computation())
                .doOnNext(inputStream -> {
                    //FileUtils.writeFile(inputStream, file);
                    try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;
                        while ((read = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}