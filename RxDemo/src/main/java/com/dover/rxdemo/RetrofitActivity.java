package com.dover.rxdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dover.rxdemo.apis.bean.UserInfoBean;
import com.dover.rxdemo.apis.response.BaseResponseBean;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by d on 2017/9/11.
 */
public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity";
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        demo1_simple(); //简单网络请求: 采用 OkHttp3 进行演示，配合 map，doOnNext ，线程切换

//        demo2_cache(); //先读取缓存，如果缓存没数据再通过网络请求获取数据后更新UI    concat
        //concat 有序; 将多个上游连接成为一个, 只有前一个 Observable 调用 onComplete(), 下一个 Observable才会开始执行。
        //concat 泛型一致; 连接的多个 Observable 的泛型应当保持一致

        demo3_moreNet(); //多个网络请求依次依赖  (后一个必须要用前一个的数据)
        // 如:注册成功后自动登录
        // 如:账单列表 + 账单详情
        // 如:运动记录列表 +  运动数据详情
        //concatMap 与 FlatMap 的唯一区别: concatMap 保证了顺序,  因为网络是异步, 所以无需顺序,FlatMap足矣

        demo4_moreData();    //结合多个接口的数据更新UI   zip

        demo5_heartPackage();   //间隔任务实现心跳

    }

    private void demo5_heartPackage() {
        //即时通讯等需要轮训的任务在如今的 APP 中已是很常见，而 RxJava 2.x 的 interval 操作符可谓完美地解决了我们的疑惑。

        disposable = Flowable.interval(1, TimeUnit.SECONDS)  //每隔一秒, 执行一次, 默认在新线程
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: doOnNext : " + aLong);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //主线程 更新UI
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: 设置文本 ：" + aLong);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void demo4_moreData() {
        //在一个页面显示的数据来源于多个接口，这时候我们的 zip 操作符为我们排忧解难
        //zip 操作符可以将多个 Observable 的数据结合为一个数据源再发射出去

 /*       Observable<MobileAddress> observable1 = Rx2AndroidNetworking.get("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                .build()
                .getObjectObservable(MobileAddress.class);

        Observable<CategoryResult> observable2 = Network.getGankApi()
                .getCategoryData("Android",1,1);

        Observable.zip(observable1, observable2, new BiFunction<MobileAddress, CategoryResult, String>() {
            @Override
            public String apply(@NonNull MobileAddress mobileAddress, @NonNull CategoryResult categoryResult) throws Exception {
                return "合并后的数据为：手机归属地："+mobileAddress.getResult().getMobilearea()+"人名："+categoryResult.results.get(0).who;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e(TAG, "accept: 成功：" + s+"\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: 失败：" + throwable+"\n");
                    }
                });
*/

    }

    private void demo3_moreNet() {
        //例如用户注册成功后需要自动登录: 先通过注册接口注册用户，注册成功后马上调用登录接口进行自动登录即可

        //我们的 flatMap 恰好解决了这种应用场景，
        // flatMap 操作符可以将一个发射数据的 Observable 变换为多个 Observables ，
        // 然后将它们发射的数据合并后放到一个单独的 Observable，
        // 利用这个特性，我们很轻松地达到了我们的需求

        //concatMap 与 FlatMap 的唯一区别就是 concatMap 保证了顺序

        /*Rx2AndroidNetworking.get("http://www.tngou.net/api/food/list")
                .addQueryParameter("rows", 1 + "")
                .build()
                .getObjectObservable(FoodList.class) // 发起获取食品列表的请求，并解析到FootList
                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取食品列表的请求结果
                .doOnNext(new Consumer<FoodList>() {
                    @Override
                    public void accept(@NonNull FoodList foodList) throws Exception {
                        // 先根据获取食品列表的响应结果做一些操作
                        Log.d(TAG, "accept: doOnNext :" + foodList.toString());
                    }
                })
                .observeOn(Schedulers.io()) // 回到 io 线程去处理获取食品详情的请求
                .flatMap(new Function<FoodList, ObservableSource<FoodDetail>>() {
                    @Override
                    public ObservableSource<FoodDetail> apply(@NonNull FoodList foodList) throws Exception {
                        if (foodList != null && foodList.getTngou() != null && foodList.getTngou().size() > 0) {
                            return Rx2AndroidNetworking.post("http://www.tngou.net/api/food/show")
                                    .addBodyParameter("id", foodList.getTngou().get(0).getId() + "")
                                    .build()
                                    .getObjectObservable(FoodDetail.class);
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FoodDetail>() {
                    @Override
                    public void accept(@NonNull FoodDetail foodDetail) throws Exception {
                        Log.d(TAG, "accept: success ：" + foodDetail.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: error :" + throwable.getMessage());
                    }
                });
        */


    }

    private void demo2_cache() {
        //
        // 1）创建 cache缓存 Observable,获取缓存数据 infoBean
        // 1.1 如果缓存数据可用(做个标记), 直接 emitter.onNext(infoBean)发送数据给下游
        // 1.2 如果缓存数据不可用(做个标记),直接 emitter.onComplete() 结束当前, 让其进入下一个 执行获取网络数据的 Observable
        // 2）创建 net网络 Observable,获取网络数据 infoBean
        // 3）concat 连接 cache缓存 Observable + net网络 Observable
        // 4）下游处理结果:
        // 4.1 成功时: 根据标记, 如果是网络数据, 做下缓存;  处理界面, 更新UI
        // 4.2 抛异常时: 处理界面, 更新UI


//        很多时候（对数据操作不敏感时）都需要我们先读取缓存的数据，
// 如果缓存没有数据，再通过网络请求获取，随后在主线程更新我们的 UI。
//        concat 操作符简直就是为我们这种需求量身定做。
//        concat 可以做到不交错的发射两个甚至多个 Observable 的发射事件，
// 并且只有前一个 Observable 终止( onComplete() ) 后才会定义下一个 Observable。
//        利用这个特性，我们就可以先读取缓存数据，倘若获取到的缓存数据不是我们想要的，
// 再调用 onComplete() 以执行获取网络数据的 Observable，
// 如果缓存数据能应我们所需，则直接调用 onNext() ，防止过度的网络请求，浪费用户的流量。
/*
        Observable<UserInfoBean> cache =Observable.create(new ObservableOnSubscribe<UserInfoBean>() {
            @Override
            public void subscribe(ObservableEmitter<UserInfoBean> e) throws Exception {
                Log.e(TAG, "create当前线程:"+Thread.currentThread().getName() );
                UserInfoBean data = CacheManager.getInstance().getUserInfoBean();

                // 在操作符 concat 中，只有调用 onComplete 之后才会执行下一个 Observable
                if (data != null){ // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
                    isFromNet = false;
                    Log.e(TAG, "\nsubscribe: 读取缓存数据:" );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRxOperatorsText.append("\nsubscribe: 读取缓存数据:\n");
                        }
                    });

                    e.onNext(data);
                }else {
                    isFromNet = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRxOperatorsText.append("\nsubscribe: 读取网络数据:\n");
                        }
                    });
                    Log.e(TAG, "\nsubscribe: 读取网络数据:" );
                    e.onComplete();
                }
            }
        });

        Observable<FoodList> network = Rx2AndroidNetworking.get("http://www.tngou.net/api/food/list")
                .addQueryParameter("rows",10+"")
                .build()
                .getObjectObservable(FoodList.class);

        // 两个 Observable 的泛型应当保持一致

        Observable.concat(cache,network)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FoodList>() {
                    @Override
                    public void accept(@NonNull FoodList tngouBeen) throws Exception {
                        Log.e(TAG, "subscribe 成功:"+Thread.currentThread().getName() );
                        if (isFromNet){
                            mRxOperatorsText.append("accept : 网络获取数据设置缓存: \n");
                            Log.e(TAG, "accept : 网络获取数据设置缓存: \n"+tngouBeen.toString() );
                            CacheManager.getInstance().setFoodListData(tngouBeen);
                        }

                        mRxOperatorsText.append("accept: 读取数据成功:" + tngouBeen.toString()+"\n");
                        Log.e(TAG, "accept: 读取数据成功:" + tngouBeen.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "subscribe 失败:"+Thread.currentThread().getName() );
                        Log.e(TAG, "accept: 读取数据失败："+throwable.getMessage() );
                        mRxOperatorsText.append("accept: 读取数据失败："+throwable.getMessage()+"\n");
                    }
                });

       */

    }

    private void demo1_simple() {
//        1）通过 Observable.create() 方法，调用 OkHttp 网络请求, 发送请求结果 response
//        2）通过 map 操作符 结合 gson，将 Response 转换为 bean 类；
//        3）通过 doOnNext() 方法，解析 bean 中的数据，并进行数据库存储等操作；
//        4）调度线程，在子线程中进行耗时操作任务，在主线程中更新 UI ；
//        5）通过 subscribe()，根据请求成功或者失败来更新 UI 。

        Observable
                .create(new ObservableOnSubscribe<Response>() {
                    @Override
                    public void subscribe(ObservableEmitter<Response> e) throws Exception {
                        Request.Builder builder = new Request.Builder()
                                .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                                .get();
                        Request request = builder.build();
                        Call call = new OkHttpClient().newCall(request);
                        Response response = call.execute();
                        e.onNext(response);
                    }
                })
                .map(new Function<Response, BaseResponseBean>() {
                    @Override
                    public BaseResponseBean apply(Response response) throws Exception {
                        Log.d(TAG, "map 线程:" + Thread.currentThread().getName() + "\n");
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            if (body != null) {
                                Log.d(TAG, "map:转换前:" + response.body());
                                return new Gson().fromJson(body.string(), BaseResponseBean.class);
                            }
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<BaseResponseBean>() {
                    @Override
                    public void accept(BaseResponseBean responseBean) throws Exception {
                        Log.d(TAG, "doOnNext 线程:" + Thread.currentThread().getName() + "\n");
                        Log.d(TAG, "doOnNext: 保存成功：" + responseBean.toString() + "\n");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponseBean>() {
                    @Override
                    public void accept(BaseResponseBean responseBean) throws Exception {
                        Log.d(TAG, "subscribe 线程:" + Thread.currentThread().getName() + "\n");
                        Log.d(TAG, "成功:" + responseBean.toString() + "\n");
                        Log.d(TAG, "更新UI" + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "subscribe 线程:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                    }
                });
        //效果
// 09-12 02:26:05.512 3651-3669/? D/RetrofitActivity: map 线程:RxCachedThreadScheduler-1
// 09-12 02:26:05.512 3651-3669/? D/RetrofitActivity: map:转换前:okhttp3.internal.http.RealResponseBody@6ab0e1
// 09-12 02:26:05.518 3651-3651/? D/RetrofitActivity: doOnNext 线程:main
// 09-12 02:26:05.518 3651-3651/? D/RetrofitActivity: doOnNext: 保存成功：com.dover.rxdemo.apis.response.BaseResponseBean@1b0ca60
// 09-12 02:26:05.519 3651-3651/? D/RetrofitActivity: subscribe 线程:main
// 09-12 02:26:05.520 3651-3651/? D/RetrofitActivity: 成功:com.dover.rxdemo.apis.response.BaseResponseBean@1b0ca60
// 09-12 02:26:05.520 3651-3651/? D/RetrofitActivity: 更新UI
    }
}
