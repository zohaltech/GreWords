package com.zohaltech.app.grewords.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zohaltech.app.grewords.api.Webservice;

public class ConnectivityStateReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String lastUserUpdate = App.preferences.getString("LAST_SEND", null);
        if (TextUtils.isEmpty(lastUserUpdate) || !lastUserUpdate.equals(Helper.getCurrentDate())) {
            if (intent.getAction().equals("android.net.wifi.STATE_CHANGE") ||
                    intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED") ||
                    intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                new Webservice().callSendUserData(context);
            }
        }
    }
}