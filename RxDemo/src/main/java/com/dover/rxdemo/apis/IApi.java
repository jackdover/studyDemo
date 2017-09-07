package com.dover.rxdemo.apis;

import com.dover.rxdemo.apis.request.BaseRequestBean;
import com.dover.rxdemo.apis.response.BaseResponseBean;

import retrofit2.Callback;

/**
 * Created by d on 2017/9/6.
 */
public interface IApi {

    // 域名
//    String BASE_URL = "http://123.56.199.233:8088/";  //外网
    String BASE_URL = "http://192.168.0.120:8080/";   //内网


    // 登录
    void login(BaseRequestBean bean, Callback<BaseResponseBean> callback);





}
