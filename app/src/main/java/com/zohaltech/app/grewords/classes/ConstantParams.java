package com.zohaltech.app.grewords.classes;


import com.zohaltech.app.grewords.R;

public final class ConstantParams {

    private static String apiSecurityKey  = App.context.getString(R.string.jan);

    public static String getApiSecurityKey() {
        return apiSecurityKey;
    }
}
