package com.dover.rxdemo.rxapi;

import com.dover.rxdemo.apis.request.DeviceInfoRequest;
import com.dover.rxdemo.apis.request.LoginRequest;
import com.dover.rxdemo.apis.request.RegisterRequest;
import com.dover.rxdemo.apis.request.UserInfoRequest;
import com.dover.rxdemo.apis.response.DeviceInfoResponse;
import com.dover.rxdemo.apis.response.LoginResponse;
import com.dover.rxdemo.apis.response.RegisterResponse;
import com.dover.rxdemo.apis.response.UserInfoResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by d on 2017/9/6.
 */
public interface IRxApi {

    //注册
    @POST("/yds/app/user/v1/register?/")
    Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);

    //登录
    @POST("/yds/app/user/v1/login?/")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    //获取用户信息
    @POST("/yds/app/user/v1/login?/")
    Observable<UserInfoResponse> getUserInfo(@Body UserInfoRequest userInfoRequest);
//    @GET("/yds/app/user/v1/info?/")
//    Call<UserInfoResultBean> getUserInfo(@Header("token") String token, @Header("lang") String lang);

    //获取配置信息
    @POST("/yds/app/user/v1/login?/")
    Observable<DeviceInfoResponse> getDeviceInfo(@Body DeviceInfoRequest deviceInfoRequest);


}
