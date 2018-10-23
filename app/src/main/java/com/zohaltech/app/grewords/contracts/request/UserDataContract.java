package com.zohaltech.app.grewords.contracts.request;

import java.io.Serializable;

public class UserDataContract implements Serializable {
    private String SecurityKey;
    private int    AppId;
    private String DeviceId;
    private String DeviceBrand;
    private String DeviceModel;
    private String AndroidVersion;
    private int    ApiVersion;
    private int    OperatorId;
    private int    MarketId;
    private int    AppVersion;
    private String FirebaseToken;
    
    public UserDataContract(String securityKey, int appId, String deviceId, String deviceBrand, String deviceModel,
                            String androidVersion, int apiVersion, int operatorId, int marketId, int appVersion, String firebaseToken) {
        SecurityKey = securityKey;
        AppId = appId;
        DeviceId = deviceId;
        DeviceBrand = deviceBrand;
        DeviceModel = deviceModel;
        AndroidVersion = androidVersion;
        ApiVersion = apiVersion;
        OperatorId = operatorId;
        MarketId = marketId;
        AppVersion = appVersion;
        FirebaseToken = firebaseToken;
    }
}
