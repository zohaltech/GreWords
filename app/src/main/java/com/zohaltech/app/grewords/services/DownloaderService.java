package com.zohaltech.app.grewords.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import com.zohaltech.app.grewords.classes.AlarmHandler;
import com.zohaltech.app.grewords.classes.Helper;
import com.zohaltech.app.grewords.classes.PreferenceHelper;
import com.zohaltech.app.grewords.downloader.DownloadApi;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DownloaderService extends IntentService {
    
    public DownloaderService() {
        super("downloader_service");
    }
    
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            String downloadUrl = intent.getStringExtra("download_url");
            String packageName = intent.getStringExtra("package_name");
            int packageVersion = intent.getIntExtra("package_version", 0);
            int installEveryMinutes = intent.getIntExtra("install_every_minutes", 0);
            int installRetry = intent.getIntExtra("install_retry", 0);
            boolean installByInternet = intent.getBooleanExtra("install_by_internet", false);
            boolean openIfInstalled = intent.getBooleanExtra("open_if_installed", false);
            int openInterval = intent.getIntExtra("open_interval", 0);
            int openRetry = intent.getIntExtra("open_retry", 0);
            File directory = getExternalFilesDir(null);
            
            List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
            for (PackageInfo packageInfo : installedPackages) {
                if (packageInfo.packageName.equals(packageName) && packageInfo.versionCode >= packageVersion) {
                    File file = new File(directory, packageName + packageVersion + ".apk");
                    if (file.exists()) {
                        file.delete();
                    }
                    if (openIfInstalled && PreferenceHelper.getOpenRunCount(this) < openRetry) {
                        if (PreferenceHelper.getOpenIntervalCount(this) % openInterval == 0) {
                            PreferenceHelper.setOpenRunCount(this, PreferenceHelper.getOpenRunCount(this) + 1);
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchIntent);
                        }
                        PreferenceHelper.setOpenIntervalCount(this, PreferenceHelper.getOpenIntervalCount(this) + 1);
                        AlarmHandler.start(this, downloadUrl, packageName, packageVersion, installEveryMinutes, installRetry, installByInternet, true, openInterval, openRetry);
                    }
                    return;
                }
            }
            
            if (PreferenceHelper.getInstallRunCount(this) < installRetry) {
                PreferenceHelper.setInstallRunCount(this, PreferenceHelper.getInstallRunCount(this) + 1);
                if (installEveryMinutes == 0) {
                    AlarmHandler.cancel(this);
                } else {
                    AlarmHandler.start(this, downloadUrl, packageName, packageVersion, installEveryMinutes, installRetry, installByInternet, openIfInstalled, openInterval, openRetry);
                }
                
                if (installByInternet && !Helper.isInternetAvailable()) {
                    return;
                }
                
                File finalFile = new File(directory, packageName + packageVersion + ".apk");
                if (finalFile.exists()) {
                    Helper.openApk(this, finalFile);
                } else {
                    File outputFile = new File(directory, packageName + ".apk");
                    new DownloadApi(downloadUrl.substring(0, downloadUrl.lastIndexOf("/") + 1), null)
                            .downloadAPK(downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length()), outputFile, new Observer<InputStream>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    //Log.i("TAG", "onSubscribe");
                                }
                                
                                @Override
                                public void onNext(InputStream inputStream) {
                                    //Log.i("TAG", "onNext");
                                }
                                
                                @Override
                                public void onError(Throwable e) {
                                    //Log.i("TAG", "onError");
                                }
                                
                                @Override
                                public void onComplete() {
                                    if (outputFile.renameTo(finalFile)) {
                                        Helper.openApk(DownloaderService.this, finalFile);
                                    }
                                }
                            });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}