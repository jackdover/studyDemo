package com.dover.rxdemo.apis.request;

/**
 * Created by d on 2017/9/7.
 */
public class RegisterRequest {

    String account = "15888888888"; //账号
    String password = "123456";

    int regType;    //0 或者 不写 默认为手机号注册
    String lang = "zh_CN";      //语言类型
    String mid = "12121212";     //设备序列号
}
