package com.dover.newapp.utils;

import com.dover.newapp.base.BaseApplication;

/**
 * Created by d on 2017/7/24.
 */
public class UrlUtil {

    //todo 线上(false),线下(true)切换
    public static final boolean isdebug = BaseApplication.isdebug;

    public static final String BASE_URL = "http://dover.com/";
    public static final String BASE_URL_DEBUG = "http://10.10.10.146:8080/";

    public static String getBaseUrl() {
        return isdebug ? BASE_URL_DEBUG : BASE_URL;
    }

}
