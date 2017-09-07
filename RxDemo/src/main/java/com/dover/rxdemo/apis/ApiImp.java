package com.dover.rxdemo.apis;

import com.dover.rxdemo.apis.request.BaseRequestBean;
import com.dover.rxdemo.apis.response.BaseResponseBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by d on 2017/9/6.
 */
public class ApiImp implements IApi {

    private Retrofit mRetrofit;

    public ApiImp() {
        initRetrofit();
    }

    private void initRetrofit() {
        final Gson gson = new GsonBuilder().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Override
    public void login(BaseRequestBean bean, Callback<BaseResponseBean> callback) {
        IApiServer server = mRetrofit.create(IApiServer.class);
        Call<BaseResponseBean> call = server.login(bean);
        call.enqueue(callback);
    }



//
//    new ApiImp().login(bodyBean, new Callback<LoginResultBean>() {
//        @Override
//        public void onResponse(Call<LoginResultBean> call, Response<LoginResultBean> response) {
//            if (response == null) {
//                MyLog.d(TAG, "login response == null");
//            }
//            LoginResultBean bean = response.body();
//            if (bean == null) {
//                MyLog.d(TAG, "login bean==null");
//                return;
//            }
//
//            LoginResultBean.DataBean dataBean = bean.getData();
//            if (bean.getStatus() != 0 || dataBean == null) {
//                MyLog.d(TAG, "login dataBean == null");
//                Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            MyLog.d(TAG, "uid == " + dataBean.getUid());
//            MyLog.d(TAG, "token == " + dataBean.getToken());
//            MyLog.d(TAG, "自动登录成功");
//
//            UserInfo.getInstance().setUid(bean.getData().getUid());
//            UserInfo.getInstance().setToken(bean.getData().getToken());
//
//            UserInfo.getInstance().setNumDistance(bean.getData().getDistance());    //距离
//
//            if (dataBean.getInfoBean() != null) {
//                InitUserInfo(dataBean.getInfoBean()); //初始化用户信息
//            } else {
//                getUserInfo();//获取用户信息
//            }
//            new RecordUtil().initSportData();// 获取运动数据  (初始化距离)
//            //保存登录信息
//            LoginCache.setLoginOver(getContext(), true);
//            LoginCache.pushUserName(getContext(), userName);
//            LoginCache.pushUserPassword(getContext(), password);
//        }
//
//        @Override
//        public void onFailure(Call<LoginResultBean> call, Throwable t) {
//            MyLog.d(TAG, " login onFailure ");
//            t.printStackTrace();
//        }
//    });
//
}
