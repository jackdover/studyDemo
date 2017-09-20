package com.dover.rxdemo.rxapi;

import android.support.annotation.NonNull;

import com.dover.rxdemo.BuildConfig;
import com.dover.rxdemo.utils.NetUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by d on 2017/9/6.
 */
public class RxRetrofit {

    private static final String BASE_URL = "http://192.168.0.120:8080/";

    /**
     * 创建 Retrofit
     *
     * @return Retrofit
     */
    public static Retrofit newRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL) // Base URL: 总是以 /结尾
                .client(initOkHttpClient()) //设置okhttp
                .addConverterFactory(GsonConverterFactory.create()) //解析数据
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Service接口现在可以作为Observable返回了
                .build();
    }


    /**
     * 设置 OkHttpClient 的初始化
     *
     * @return OkHttpClient
     */
    @NonNull
    private static OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(9, TimeUnit.SECONDS);    //连接超时
        builder.readTimeout(10, TimeUnit.SECONDS);      //读取超时
        builder.writeTimeout(20, TimeUnit.SECONDS);     //写入超时


        // addNetworkInterceptor添加的是网络拦截器，在request和resposne是分别被调用一次
        // addinterceptor添加的是aplication应用拦截器，只会在response被调用一次
//
//        应用拦截器
//
//        不需要担心中间响应，如重定向和重试。
//        总是调用一次，即使从缓存中提供HTTP响应。
//        遵守应用程序的原始意图。不关心OkHttp注入的头像If-None-Match。
//        允许短路而不打电话Chain.proceed()。
//        允许重试并进行多次呼叫Chain.proceed()。

//        网络拦截器
//
//        能够对重定向和重试等中间响应进行操作。
//        没有为缓存的响应调用网络短路。
//        观察数据，就像通过网络传输一样。
//        访问Connection该请求。

        if (BuildConfig.DEBUG) {    // 第三方的日志拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);    //全部信息
            builder.addInterceptor(interceptor);
        }

//        builder.addInterceptor(appIntercepter);         // 自定义的应用拦截器: 设置公共参数
//        builder.addInterceptor(cacheIntercepter);       // 自定义的应用拦截器: 设置缓存策略//这里大家一定要注意了是addNetworkOnterceptor别搞错了啊
//        builder.addNetworkInterceptor(netIntercepter);  // 自定义的网络拦截器: 重试或者重连次数等

//        builder.addInterceptor(new BridgeInterceptor(cookiejar));
//        builder.addInterceptor(new CallServerInterceptor(boolean forWebSocket));
        /*builder.addInterceptor(new CacheInterceptor(new InternalCache() {
            @Override
            public Response get(Request request) throws IOException {
                return null;
            }

            @Override
            public CacheRequest put(Response response) throws IOException {
                return null;
            }

            @Override
            public void remove(Request request) throws IOException {

            }

            @Override
            public void update(Response cached, Response network) {

            }

            @Override
            public void trackConditionalCacheHit() {

            }

            @Override
            public void trackResponse(CacheStrategy cacheStrategy) {

            }
        }));*/

//        builder.addInterceptor(new ConnectInterceptor(new OkHttpClient()));

        //builder.retryOnConnectionFailure(true); //连接失败后是否重新连接
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }


    //应用拦截器：主要用于设置公共参数，头信息，日志拦截等,有点类似Retrofit的Converter
    private static Interceptor appIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = processRequest(chain.request());
            Response response = processResponse(chain.proceed(request));
            return response;
        }
    };

    //访问网络之前，处理Request(这里统一添加了Cookie)
    private static Request processRequest(Request request) {
//        String session = CacheManager.restoreLoginInfo(BaseApplication.getContext()).getSession();
        String session = "restoreLoginInfo.getSession";
        return request
                .newBuilder()
                .addHeader("Cookie", "JSESSIONID=" + session)
                .build();
    }

    //访问网络之后，处理Response(这里可以考虑token失效等统一处理)
    private static Response processResponse(Response response) {
        return response;
    }


    //应用拦截器：设置缓存策略
//    noCache ：不使用缓存，全部走网络
//    noStore ： 不使用缓存，也不存储缓存
//    onlyIfCached ： 只使用缓存
//    maxAge ：设置最大失效时间，失效则不使用
//    maxStale ：设置最大失效时间，失效则不使用
//    minFresh ：设置最小有效时间，失效则不使用
//    FORCE_NETWORK ： 强制走网络
//    FORCE_CACHE ：强制走缓存
    //注意:
    //max-stale在请求头设置有效，在响应头设置无效。
    //max-stale和max-age同时设置的时候，缓存失效的时间按最长的算。

    private static Interceptor cacheIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            //无网的时候强制使用缓存
            if (NetUtil.getNetState() == NetUtil.NetState.NET_NO) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//缓存策略
                        .build();
            }

            Response response = chain.proceed(request);

            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            if (NetUtil.getNetState() != NetUtil.NetState.NET_NO) {
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return response.newBuilder()
                        //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    //网络拦截器：主要用于重试或重写
    private static Interceptor netIntercepter = new Interceptor() {

        private int sMaxTryCount = 3;   //最多尝试次数

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < sMaxTryCount) {
                tryCount++;
                response = chain.proceed(request);
            }
            return response;
        }
    };


}
