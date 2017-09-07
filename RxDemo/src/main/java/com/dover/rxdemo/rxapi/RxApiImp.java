package com.dover.rxdemo.rxapi;

/**
 * Created by d on 2017/9/6.
 */
public class RxApiImp {


    public static IRxApi getRxApi() {
        return RxRetrofit.newRetrofit().create(IRxApi.class);
    }


}
