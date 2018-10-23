package com.zohaltech.app.grewords.classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.data.SystemSettings;
import com.zohaltech.app.grewords.entities.SystemSetting;

public class NotificationHandler {
    
    private static NotificationManager notificationManager;
    
    public static void displayUpdateNotification(Context context, String title, String text) {
        notificationManager.notify(3, getUpdateNotification(context, title, text));
    }
    
    private static Notification getUpdateNotification(Context context, String title, String text) {
        
        String channelId = "channel_id_update";
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel updateUsageChannel = new NotificationChannel(channelId, "check for update", NotificationManager.IMPORTANCE_DEFAULT);
            updateUsageChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(updateUsageChannel);
        }
        
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.notification_update)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setContentText(text)
                        .setShowWhen(true)
                        .setOngoing(false)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setColor(context.getResources().getColor(R.color.primary))
                        .setLights(Color.YELLOW, 1000, 300)
                        .setAutoCancel(true);
        SystemSetting setting = SystemSettings.getCurrentSettings(context);
        if (setting != null && setting.getRingingToneUri() != null) {
            builder.setSound(Uri.parse(setting.getRingingToneUri()));
        } else {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
    
        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        resultIntent.setData(Uri.parse(App.marketUri));
        resultIntent.setPackage(App.marketPackage);
        
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 3, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        
        return builder.build();
    }
}