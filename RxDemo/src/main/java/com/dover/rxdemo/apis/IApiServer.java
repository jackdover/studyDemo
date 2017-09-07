package com.dover.rxdemo.apis;

import com.dover.rxdemo.apis.request.BaseRequestBean;
import com.dover.rxdemo.apis.response.BaseResponseBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by d on 2017/9/6.
 */
public interface IApiServer {

    // 登录
    @POST("/yds/app/user/v1/login?/")
    Call<BaseResponseBean> login(@Body BaseRequestBean request);


//
//    @GET("/yds/app/user/v1/pre-register?/")
//    Call<CheckRegisterResultBean> checkRegisterCall(@Query("account") String phone, @Query("regType") int type, @Query("lang") String lang);
//
//    @POST("/yds/app/user/v1/register?/")
//    Call<RegisterResultBean> register(@Body RegisterBodyBean bean);
//
//    @POST("/yds/app/user/v1/login?/")
//    Call<LoginResultBean> login(@Body LoginBodyBean bean);
//
//    @GET("/yds/app/user/v1/logout?/")
//    Call<LoginOutResultBean> loginOut(@Header("token") String token, @Header("lang") String lang);

}
