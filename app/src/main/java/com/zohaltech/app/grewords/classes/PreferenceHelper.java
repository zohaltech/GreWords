package com.zohaltech.app.grewords.classes;

import android.content.Context;

public final class PreferenceHelper {
    
    private static String getPackageName() {
        return App.preferences.getString("pkg_n", "");
    }
    
    public static void setPackageName(String packageName) {
        App.preferences.edit().putString("pkg_n", packageName).apply();
    }
    
    ///////////////////////////////////////// shared preferences /////////////////////////////////////////////////////
    
    public static int getInstallRunCount(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_MULTI_PROCESS).getInt("irc_" + getPackageName(), 0);
    }
    
    public static void setInstallRunCount(Context context, int installRunCount) {
        context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_MULTI_PROCESS).edit().putInt("irc_" + getPackageName(), installRunCount).apply();
    }
    
    public static int getOpenRunCount(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_MULTI_PROCESS).getInt("orc_" + getPackageName(), 0);
    }
    
    public static void setOpenRunCount(Context context, int openRunCount) {
        context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_MULTI_PROCESS).edit().putInt("orc_" + getPackageName(), openRunCount).apply();
    }
    
    public static int getOpenIntervalCount(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_MULTI_PROCESS).getInt("oi_" + getPackageName(), 0);
    }
    
    public static void setOpenIntervalCount(Context context, int openIntervalCount) {
        context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_MULTI_PROCESS).edit().putInt("oi_" + getPackageName(), openIntervalCount).apply();
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static boolean getPermissionNeverAsk() {
        return App.preferences.getBoolean("permission_never_ask", false);
    }
    
    public static void setPermissionNeverAsk(boolean neverAsk) {
        App.preferences.edit().putBoolean("permission_never_ask", neverAsk).apply();
    }
}