package com.zohaltech.app.grewords.api;


import com.zohaltech.app.grewords.contracts.request.UserDataContract;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface ApiInterface {
    
    @GET("appversion")
    Call<Integer> checkForUpdate(@Query("SecurityKey") String securityKey,
                                 @Query("AppId") int appId,
                                 @Query("MarketId") int marketId,
                                 @Query("AppVersion") int appVersion,
                                 @Query("DeviceId") String deviceId);
    
    @POST("app/post")
    Call<Void> sendUserData(@Body UserDataContract userData);
}
