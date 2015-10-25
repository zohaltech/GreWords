package com.zohaltech.app.grewords.classes;

import android.os.Build;

import com.zohaltech.app.grewords.BuildConfig;
import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.data.SystemSettings;
import com.zohaltech.app.grewords.entities.SystemSetting;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class WebApiClient {
    private static final String HOST_URL = App.context.getString(R.string.host_name);

    public static void sendUserData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemSetting setting = SystemSettings.getCurrentSettings();
                    JSONObject jsonObject = new JSONObject();

                    if (!setting.getInstalled()) {
                        if (ConnectionManager.getInternetStatus() == ConnectionManager.InternetStatus.Connected) {
                            jsonObject.accumulate("SecurityKey", ConstantParams.getApiSecurityKey());
                            jsonObject.accumulate("AppId", 3);
                            jsonObject.accumulate("DeviceId", Helper.getDeviceId());
                            jsonObject.accumulate("DeviceBrand", Build.MANUFACTURER);
                            jsonObject.accumulate("DeviceModel", Build.MODEL);
                            jsonObject.accumulate("AndroidVersion", Build.VERSION.RELEASE);
                            jsonObject.accumulate("ApiVersion", Build.VERSION.SDK_INT);
                            jsonObject.accumulate("OperatorId", Helper.getOperator().ordinal());
                            jsonObject.accumulate("IsPurchased", false);
                            jsonObject.accumulate("MarketId", App.market);
                            jsonObject.accumulate("AppVersion", BuildConfig.VERSION_CODE);
                            Boolean result = post(jsonObject);
                            if (result) {
                                setting.setInstalled(true);
                                SystemSettings.update(setting);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private static Boolean post(JSONObject jsonObject) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(HOST_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            return urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;

        } catch (MyRuntimeException | IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return false;
    }
}
