package com.zohaltech.app.grewords.api;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zohaltech.app.grewords.BuildConfig;
import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.classes.App;
import com.zohaltech.app.grewords.classes.ConstantParams;
import com.zohaltech.app.grewords.classes.Cryptography;
import com.zohaltech.app.grewords.classes.Helper;
import com.zohaltech.app.grewords.contracts.request.UserDataContract;
import com.zohaltech.app.grewords.event.EventTags;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Webservice {
    
    private ApiInterface getApiInterface(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Cryptography.decrypt(context.getString(R.string.b_u_b_e)))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiInterface.class);
    }
    
    public void callCheckForUpdate(Context context) {
        try {
            String lastUpdateCheckDate = App.preferences.getString("UPDATE_CHECK_DATE", null);
            if (TextUtils.isEmpty(lastUpdateCheckDate) || !lastUpdateCheckDate.equals(Helper.getCurrentDate())) {
                ApiInterface apiInterface = getApiInterface(context);
                Call<Integer> call = apiInterface.checkForUpdate(ConstantParams.API_SECRET_KEY,
                        4,
                        App.market,
                        BuildConfig.VERSION_CODE,
                        Helper.getDeviceId(context));
                String tag = EventTags.CHECK_FOR_UPDATE;
                MyCallback<Integer> callback = new MyCallback<>(context, tag);
                call.enqueue(callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void callSendUserData(Context context) {
        try {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        String token = "";
                        if (task.isSuccessful()) {
                            token = task.getResult() == null ? "" : task.getResult().getToken();
                        }
                        UserDataContract userData = new UserDataContract(ConstantParams.API_SECRET_KEY,
                                4,
                                Helper.getDeviceId(context),
                                Build.MANUFACTURER,
                                Build.MODEL,
                                Build.VERSION.RELEASE,
                                Build.VERSION.SDK_INT,
                                Helper.getOperator(context).ordinal(),
                                App.market,
                                BuildConfig.VERSION_CODE,
                                token);
                        ApiInterface apiInterface = getApiInterface(context);
                        Call<Void> call = apiInterface.sendUserData(userData);
                        String tag = EventTags.SEND_USER_DATA;
                        MyCallback<Void> callback = new MyCallback<>(context, tag);
                        call.enqueue(callback);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}