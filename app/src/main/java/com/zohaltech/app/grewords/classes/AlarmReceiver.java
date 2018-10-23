package com.zohaltech.app.grewords.classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.activities.VocabularyDetailsActivity;
import com.zohaltech.app.grewords.data.SystemSettings;
import com.zohaltech.app.grewords.entities.SystemSetting;
import com.zohaltech.app.grewords.serializables.Reminder;

public class AlarmReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Reminder reminder = (Reminder) intent.getSerializableExtra("reminder");
    
        String channelId = "channel_id_word";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel wordUsageChannel = new NotificationChannel(channelId, "word reminder", NotificationManager.IMPORTANCE_DEFAULT);
            wordUsageChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(wordUsageChannel);
        }
    
        NotificationCompat.Builder builder =
                new android.support.v4.app.NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(reminder.getTitle())
                        .setContentText(reminder.getMessage())
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
        
        Intent resultIntent = new Intent(context, VocabularyDetailsActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(VocabularyDetailsActivity.VOCAB_ID, reminder.getVocabularyId());
    
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, reminder.getVocabularyId(), resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);
    
        Notification notification = builder.build();
        notificationManager.notify((int) (reminder.getTime().getTime()), notification);
        
        ReminderManager.setLastReminder(reminder);
        ReminderManager.registerNextReminder(context, reminder.getVocabularyId(), reminder.doesTriggersNext());
    }
}