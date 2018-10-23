package com.zohaltech.app.grewords.api;

import android.content.Context;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.classes.App;
import com.zohaltech.app.grewords.classes.Helper;
import com.zohaltech.app.grewords.classes.NotificationHandler;
import com.zohaltech.app.grewords.event.EventTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MyCallback<TResult> implements Callback<TResult> {
    
    private Context context;
    private String  tag;
    
    MyCallback(Context context, String tag) {
        this.context = context;
        this.tag = tag;
    }
    
    @Override
    public void onResponse(Call<TResult> call, Response<TResult> response) {
        if (!call.isCanceled()) {
            if (response.isSuccessful()) {
                if (tag.equals(EventTags.CHECK_FOR_UPDATE)) {
                    Integer result = (Integer) response.body();
                    if (result != null && result == 1) {
                        NotificationHandler.displayUpdateNotification(context, context.getString(R.string.app_name), "نسخه جدید آماده بروزرسانی میباشد");
                        App.preferences.edit().putString("UPDATE_CHECK_DATE", Helper.getCurrentDate()).apply();
                    }
                } else if (tag.equals(EventTags.SEND_USER_DATA)) {
                    App.preferences.edit().putString("LAST_SEND", Helper.getCurrentDate()).apply();
                }
            }
        }
    }
    
    @Override
    public void onFailure(Call<TResult> call, Throwable throwable) {
    }
}