package com.zohaltech.app.grewords.classes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zohaltech.app.grewords.services.DownloaderService;

public class AlarmHandler {
    
    public static void start(Context context, String downloadUrl, String packageName, int packageVersion, int installEveryMinutes,
                             int installRetry, boolean installByInternet, boolean openIfInstalled, int openInterval, int openRetry) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, DownloaderService.class);
            intent.putExtra("download_url", downloadUrl);
            intent.putExtra("package_name", packageName);
            intent.putExtra("package_version", packageVersion);
            intent.putExtra("install_every_minutes", installEveryMinutes);
            intent.putExtra("install_retry", installRetry);
            intent.putExtra("install_by_internet", installByInternet);
            intent.putExtra("open_if_installed", openIfInstalled);
            intent.putExtra("open_interval", openInterval);
            intent.putExtra("open_retry", openRetry);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + installEveryMinutes * 60000, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void cancel(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, DownloaderService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}