package com.zohaltech.app.grewords.classes;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zohaltech.app.grewords.BuildConfig;

import java.util.Locale;

public class App extends Application {
    
    public static final int MARKET_BAZAAR = 0;
    public static final int MARKET_CANDO  = 1;
    public static final int MARKET_MYKET  = 2;
    public static final int MARKET_PLAY   = 3;
    
    public static SharedPreferences preferences;
    public static Typeface          persianFont;
    public static Typeface          persianFontBold;
    public static Handler           handler;
    public static int               screenWidth;
    public static int               screenHeight;
    public static Locale            locale;
    public static int               market;
    public static String            marketName;
    public static String            marketPackage;
    public static String            marketAction;
    public static String            marketUri;
    public static String            marketWebsiteUri;
    public static String            marketDeveloperUri;
    //public static String            marketDeveloperWebsiteUri;
    public static String            marketPollUri;
    public static String            marketPollIntent;
    
    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        persianFont = Typeface.createFromAsset(getAssets(), "fonts/iransans_light.ttf");
        persianFontBold = Typeface.createFromAsset(getAssets(), "fonts/iransans_light.ttf");
        handler = new Handler();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        setAppLocal(this);
        
        //todo : set market here
        setTargetMarket(MARKET_BAZAAR);
        
        try {
            for (int i = 106; i < BuildConfig.VERSION_CODE; i++) {
                try {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("" + i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FirebaseMessaging.getInstance().subscribeToTopic("public");
            FirebaseMessaging.getInstance().subscribeToTopic("" + BuildConfig.VERSION_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setAppLocal(Context context) {
        locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
    
    private void setTargetMarket(int marketId) {
        switch (marketId) {
            case MARKET_BAZAAR:
                market = MARKET_BAZAAR;
                marketName = "Bazaar";
                marketPackage = "com.farsitel.bazaar";
                marketAction = "ir.cafebazaar.pardakht.InAppBillingService.BIND";
                marketUri = "bazaar://details?id=" + getPackageName();
                marketWebsiteUri = "http://cafebazaar.ir/app/" + getPackageName();
                marketDeveloperUri = "bazaar://collection?slug=by_author&aid=zohaltech";
                marketPollUri = "bazaar://details?id=" + getPackageName();
                marketPollIntent = Intent.ACTION_EDIT;
                break;
            case MARKET_CANDO:
                market = MARKET_CANDO;
                marketName = "Cando";
                marketPackage = "com.ada.market";
                marketAction = "com.ada.market.service.payment.BIND";
                marketUri = "cando://details?id=" + getPackageName();
                marketWebsiteUri = "http://cando.asr24.com/app.jsp?package=" + getPackageName();
                marketDeveloperUri = "cando://publisher?id=zohaltech@gmail.com";
                marketPollUri = "cando://leave-review?id=" + getPackageName();
                marketPollIntent = Intent.ACTION_VIEW;
                break;
            case MARKET_MYKET:
                market = MARKET_MYKET;
                marketName = "Myket";
                marketPackage = "ir.mservices.market";
                marketAction = "ir.mservices.market.InAppBillingService.BIND";
                marketUri = "myket://application/#Intent;scheme=myket;package=" + getPackageName() + ";end";
                marketWebsiteUri = "http://myket.ir/Appdetail.aspx?id=" + getPackageName();
                marketDeveloperUri = "http://myket.ir/DeveloperApps.aspx?Packagename=" + getPackageName();
                marketPollUri = "myket://comment/#Intent;scheme=comment;package=" + getPackageName() + ";end";
                marketPollIntent = Intent.ACTION_VIEW;
                break;
            case MARKET_PLAY:
                market = MARKET_PLAY;
                marketName = "Google Play";
                marketPackage = "com.android.vending";
                marketAction = "com.android.vending.billing.InAppBillingService.BIND";
                marketUri = "market://details?id=" + getPackageName();
                marketWebsiteUri = "http://play.google.com/store/apps/details?id=" + getPackageName();
                marketDeveloperUri = "http://play.google.com/store/search?q=pub:ZohalTech";
                //marketDeveloperWebsiteUri = "http://play.google.com/store/search?q=pub:ZohalTech";
                marketPollUri = "market://details?id=" + getPackageName();
                marketPollIntent = Intent.ACTION_VIEW;
                break;
        }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}