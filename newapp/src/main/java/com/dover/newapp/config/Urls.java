package com.dover.newapp.config;

import com.dover.newapp.utils.UrlUtil;

/**
 * Created by d on 2017/7/24.
 * url 地址
 */
public interface Urls {

    String BASE_URL = UrlUtil.getBaseUrl();
    String MODUEL_USER = "user/";

    String login = "login";
}
