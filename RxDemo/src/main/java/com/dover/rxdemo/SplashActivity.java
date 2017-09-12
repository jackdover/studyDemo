package com.dover.rxdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dover.rxdemo.apis.UserInfo;
import com.dover.rxdemo.apis.request.DeviceInfoRequest;
import com.dover.rxdemo.apis.request.LoginRequest;
import com.dover.rxdemo.apis.request.RegisterRequest;
import com.dover.rxdemo.apis.request.UserInfoRequest;
import com.dover.rxdemo.apis.response.DeviceInfoResponse;
import com.dover.rxdemo.apis.response.LoginResponse;
import com.dover.rxdemo.apis.response.RegisterResponse;
import com.dover.rxdemo.apis.response.UserInfoResponse;
import com.dover.rxdemo.rxapi.IRxApi;
import com.dover.rxdemo.rxapi.RxApiImp;
import com.dover.rxdemo.rxapi.RxRetrofit;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private IRxApi mRxApi;
    private ListView lv;
    private ArrayList<String> mList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        lv.setAdapter(adapter);




        //其他跳转
//        startActivity(new Intent(this,OperatActivity.class));//操作符相关

        startActivity(new Intent(this,RetrofitActivity.class));//网络使用

        startActivity(new Intent(this,RxSampleActivity.class));//实战




        mRxApi = RxApiImp.getRxApi();

//        rxdemo1();    //基础示范
//        rxdemo2();  //链式编程        Disposable.dispose(); 切断
//        rxdemo3();  //线程切换 --- 子线程发送事件, 主线程接收事件, (上游子线程, 下游主线程)

//        rxdemo4();    //网络请求 - 登录
//        rxdemo5();      //嵌套网络请求 - 新用户--- 注册+自动登录
//        rxdemo5_2();      //操作符改造 map flatmap concatmap   嵌套网络请求 - 新用户--- 注册+自动登录  (登录+获取信息)
//        rxdemo6();      //操作符 zip
//        rxdemo6_2();      //操作符 zip   -- 实战     (两个接口都请求到数据, 才显示)
//        rxdemo7();      //操作符 zip   -- 背压1 --- OOM
        // filter 过滤    只允许满足条件的事件通过
        // sample 取样    每隔指定的时间就从上游中取出一个事件发送给下游
//        rxdemo8();      //参考解决 背压1,OOM ---只使用Observable如何去解决上下游流速不均衡的问题

//        rxdemo9();  // Flowable
//        rxdemo10();  // Flowable 填坑 ---
        //下游 调用 mSubscription.request(n) 通知上游自己的处理能力
        //上游 调用 mFlowableEmitter.requested 就可以获取下游的处理能力
//        rxdemo11();  // Flowable 响应式拉取完整实现  ---实战之  大文本边读取边处理
    }

    Subscription mSubscription;

    private void rxdemo11() {
        //在下游中调用Subscription.request(n)就可以告诉上游，下游能够处理多少个事件
        //那么上游要根据下游的处理能力正确的去发送事件，那么上游是不是应该知道下游的处理能力是多少啊
        //那么上游从哪里得知下游的处理能力呢？

        //源码 FlowableEmitter
//        public interface FlowableEmitter<T> extends Emitter<T> {
//            void setDisposable(Disposable s);
//            void setCancellable(Cancellable c);
//            long requested();     //当前外部请求的数量
//            boolean isCancelled();
//            FlowableEmitter<T> serialize();

        //下游 调用 mSubscription.request(n) 通知上游自己的处理能力
        //上游 调用 mFlowableEmitter.requested 就可以获取下游的处理能力


        //1. 下游多次调用, 上游做 加法
        //2. 上游每发送一个next事件之后，requested就 减1
        //注意: 是next事件，complete 和 error 事件不会消耗 requested
        //3. 当减到0时，则代表下游没有处理能力了，
        // 这个时候你如果继续发送事件，会发生什么后果呢？
        // 根据背压策略, 例如 BackpressureStrategy.ERROR时,
        // 同步时报 MissingBackpressureException 异常
        // 异步时, 第一次是128, 之后,当下游每消费96个事件便会自动触发内部的request()去设置上游的requested的值

        //注意: 在发送事件前先判断当前的requested的值是否大于0，若等于0则说明下游处理不过来了，则需要等待

        //实战
        //读取一个文本文件，需要一行一行读取，然后处理并输出，
        // 如果文本文件很大的时候，比如几十M的时候，
        // 全部先读入内存肯定不是明智的做法，因此我们可以一边读取一边处理

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                try {
//                    FileReader reader = new FileReader("/sdcard/test.txt");
                    InputStreamReader reader = new InputStreamReader(getResources().getAssets().open("test.txt"));
                    BufferedReader br = new BufferedReader(reader);

                    String str;
                    while ((str = br.readLine()) != null && !emitter.isCancelled()) {
                        while (emitter.requested() == 0) {
                            if (emitter.isCancelled()) {
                                break;
                            }
                        }
                        emitter.onNext(str);
                    }
                    br.close();
                    reader.close();
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, s);
//
                        mList.add(s);
                        adapter.notifyDataSetChanged();

                        try {
                            Thread.sleep(1000);
                            mSubscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void rxdemo10() {
        //自己创建 Flowable 时, 可以指定背压策略
// 1.       BackpressureStrategy.ERROR      异步时, 水缸事件大小 128 个
// 2.       BackpressureStrategy.BUFFER     它没有大小限制, 可以存放许许多多的事件.
// 3.       BackpressureStrategy.DROP       直接把存不下的事件丢弃, 后边的不要了
// 4.       BackpressureStrategy.LATEST     只保留最新的事件, 最后一个肯定在

        //如果不是自己创建的, 也可以指定背压策略, 不指定,默认 ERROR
//        onBackpressureBuffer()
//        onBackpressureDrop()
//        onBackpressureLatest()

        //例如:
//        Flowable.interval(1, TimeUnit.MICROSECONDS)
//                .onBackpressureBuffer()     //指定背压策略
////                .onBackpressureDrop()
////                .onBackpressureLatest()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
////                        mSubscription = s;
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.d(TAG, "onNext: " + aLong);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });

    }

    private void rxdemo9() {
        //1. 创建一个上游     Observable --> Flowable
        //2. 创建一个下游
        //3. 建立连接

        //1. 上游
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);

                Log.d(TAG, "emit 2");
                emitter.onNext(2);

                Log.d(TAG, "emit 3");
                emitter.onNext(3);

                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); //增加了一个参数
// 1.       BackpressureStrategy.ERROR      异步时, 水缸事件大小 128 个
        //出现上下游流速不均衡的时候直接抛出一个 MissingBackpressureException 异常
// 2.       BackpressureStrategy.BUFFER     它没有大小限制, 可以存放许许多多的事件.
        //上游无限发送事件, 下游不处理或者处理过慢, 最终依然会 OOM

        // 数量解决方案
// 3.       BackpressureStrategy.DROP       直接把存不下的事件丢弃, 后边的不要了
// 4.       BackpressureStrategy.LATEST     只保留最新的事件, 最后一个肯定在

        //2. 下游
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                s.request(Long.MAX_VALUE);  //注意这句代码
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        //3. 建立链接
        flowable.subscribe(subscriber);

        //区别1
        //上游创建Flowable的时候增加了一个参数, 这个参数是用来选择背压,
        // 也就是出现上下游流速不均衡的时候应该怎么处理的办法,
        // 这里我们直接用 BackpressureStrategy.ERROR 这种方式,
        // 这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,
        // 这个异常就是著名的 MissingBackpressureException.

        //区别2
        //下游的onSubscribe方法中传给我们的不再是 Disposable 了, 而是Subscription,
        // 它俩有什么区别呢, 首先它们都是上下游中间的一个开关,
        // 之前我们说调用 Disposable.dispose() 方法可以切断水管,
        // 同样的调用 Subscription.cancel() 也可以切断水管,
        // 不同的地方在于 Subscription 增加了一个 void request(long n) 方法,
        // 这个方法有什么用呢,

        //不要行不行
        //不要1,同步,
//        Flowable
//                .create(new FlowableOnSubscribe<Integer>() {
//                    @Override
//                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                        Log.d(TAG, "emit 1");
//                        emitter.onNext(1);
//                        Log.d(TAG, "emit 2");
//                        emitter.onNext(2);
//                        Log.d(TAG, "emit 3");
//                        emitter.onNext(3);
//                        Log.d(TAG, "emit complete");
//                        emitter.onComplete();
//                    }
//                }, BackpressureStrategy.ERROR)
//                .subscribe(new Subscriber<Integer>() {
//
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "onNext: " + integer);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });
        // 效果: 在上游发送第一个事件之后, 下游就直接抛 MissingBackpressureException 异常, 并且下游没有收到任何其余的事件

        //不要2,异步,
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                Log.d(TAG, "emit 1");
//                emitter.onNext(1);
//                Log.d(TAG, "emit 2");
//                emitter.onNext(2);
//                Log.d(TAG, "emit 3");
//                emitter.onNext(3);
//                Log.d(TAG, "emit complete");
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR)
//                .subscribeOn(Schedulers.io())   //上游IO线程
//                .observeOn(AndroidSchedulers.mainThread())  //下游主线程
//                .subscribe(new Subscriber<Integer>() {
//
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
////                        mSubscription = s;
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "onNext: " + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });
        // 效果: 没有抛异常, 上游正确的发送了所有的事件, 但是下游一个事件也没有收到


        //为什么呢?
        //因为 Flowable 在设计的时候采用了一种新的思路也就是
        // 响应式拉取 的方式来更好的解决上下游流速不均衡的问题

        //我们把request当做是一种能力, 当成下游处理事件的能力, 下游能处理几个就告诉上游我要几个,
        // 这样只要上游根据下游的处理能力来决定发送多少事件, 就不会造成一窝蜂的发出一堆事件来, 从而导致OOM.
        // 这也就完美的解决之前我们所学到的两种方式的缺陷,
        // 过滤事件会导致事件丢失, 减速又可能导致性能损失.
        // 而这种方式既解决了事件丢失的问题, 又解决了速度的问题, 完美 !

        //注意:
        // 只有当上游正确的实现了如何根据下游的处理能力来发送事件的时候, 才能达到这种效果

        //解释不要1, 同步
        //为什么上游发送第一个事件后下游就抛出了MissingBackpressureException异常,
        // 这是因为下游没有调用request, 上游就认为下游没有处理事件的能力,
        // 而这又是一个同步的订阅, 既然下游处理不了, 那上游不可能一直等待吧,
        // 如果是这样, 万一这两根水管工作在主线程里, 界面不就卡死了吗,
        // 因此只能抛个异常来提醒我们.
        // 那如何解决这种情况呢, 很简单啦,
        // 下游直接调用request(Long.MAX_VALUE)就行了,
        // 或者根据上游发送事件的数量来request就行了, 比如这里request(3)就可以了

        //解释不要2, 异步
        //为什么上下游没有工作在同一个线程时, 上游却正确的发送了所有的事件呢?
        // 这是因为在Flowable里默认有一个大小为128的水缸,
        // 当上下游工作在不同的线程中时, 上游就会先把事件发送到这个水缸中,
        // 因此, 下游虽然没有调用request, 但是上游在水缸中保存着这些事件,
        // 只有当下游调用request时, 才从水缸里取出事件发给下游.

        //可以把Subscription保存起来, 做个点击事件, 点击一次, 调用一次 request
//        public static void request(long n) {
//            mSubscription.request(n); //在外部调用request请求上游
//        }


    }

    private void rxdemo8() {
        //一是从数量上进行治理, 减少发送进水缸里的事件
        //二是从速度上进行治理, 减缓事件发送进水缸的速度

        //1. 减少进入水缸的事件的数量:     ---缺点: 丢失了大部分的事件
        // filter 过滤    只允许满足条件的事件通过
//        Observable
//                .create(new ObservableOnSubscribe<Integer>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                        for (int i = 0; ; i++) {
//                            emitter.onNext(i);
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .filter(new Predicate<Integer>() {
//                    @Override
//                    public boolean test(Integer integer) throws Exception {
//                        return integer % 10 == 0;   //只允许能被10整除的事件通过
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.d(TAG, "" + integer);
//                    }
//                });


        // sample 取样    每隔指定的时间就从上游中取出一个事件发送给下游
//        Observable
//                .create(new ObservableOnSubscribe<Integer>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                        for (int i = 0; ; i++) {
//                            emitter.onNext(i);
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .sample(2, TimeUnit.SECONDS)  //sample取样, 每隔2秒,取出一个事件给下游
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.d(TAG, "" + integer);
//                    }
//                });

        //2. 减缓速度
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {
                            emitter.onNext(i);
                            Thread.sleep(2000);  //每次发送完事件延时2秒
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });
        //上游通过适当的延时,
        // 不但减缓了事件进入水缸的速度, 也可以让下游有充足的时间从水缸里取出事件来处理 ,
        // 这样一来, 就不至于导致大量的事件涌进水缸, 也就不会OOM啦

        //参考解决 demo7 中的 zip 爆内存
        //方法1. 减少数量 : 过滤/取样
        //方法2. 减缓速度 : 延迟
    }

    private void rxdemo7() {
        //Zip可以将多个上游发送的事件组合起来发送给下游, 那大家有没有想过一个问题,
        // 如果其中一个水管A发送事件特别快, 而另一个水管B 发送事件特别慢,
        // 那就可能出现这种情况, 发得快的水管A 已经发送了1000个事件了, 而发的慢的水管B 才发一个出来,
        // 组合了一个之后水管A 还剩999个事件, 这些事件需要继续等待水管B 发送事件出来组合,
        // 那么这么多的事件是放在哪里的呢? 总有一个地方保存吧?
        // 没错, Zip给我们的每一根水管都弄了一个水缸 , 用来保存这些事件,

        //zip给我们的水缸!
        // 它将每根水管发出的事件保存起来, 等两个水缸都有事件了之后就分别从水缸中取出一个事件来组合,
        // 当其中一个水缸是空的时候就处于等待的状态.

        //水缸有什么特点呢?
        // 它是按顺序保存的, 先进来的事件先取出来, 这个特点就是我们熟知的队列,
        // 这个水缸在Zip内部的实现就是用的队列, 可以翻看源码查看

        //这个水缸有大小限制吗? 要是一直往里存会怎样?
//        Observable<Integer> ov1 = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0; ; i++) {   //无限循环发事件
//                    emitter.onNext(i);
//                }
//            }
//        }).subscribeOn(Schedulers.io());    //IO 线程1
//
//        Observable<String> ov2 = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                emitter.onNext("A");
//            }
//        }).subscribeOn(Schedulers.io());    //IO 线程2
//
//        Observable.zip(ov1, ov2, new BiFunction<Integer, String, String>() {
//            @Override
//            public String apply(Integer integer, String s) throws Exception {
//                return integer + s;
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.d(TAG, s);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.w(TAG, throwable);
//                    }
//                });
        //分别创建了两根水管,
        // 第一根水管用机器指令的执行速度来无限循环发送事件,
        // 第二根水管随便发送点什么,
        // 由于我们没有发送Complete事件, 因此第一根水管会一直发事件到它对应的水缸里去,
        //结果:
        //内存占用以斜率为1的直线迅速上涨, 几秒钟就300多M , 最终报出了OOM:

        // Backpressure 背压
        //Backpressure其实就是为了控制流量, 水缸存储的能力毕竟有限,
        // 因此我们还得从源头去解决问题, 既然你发那么快, 数据量那么大, 那我就想办法不让你发那么快

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0; ; i++) {   //无限循环发事件
//                    emitter.onNext(i);
//                }
//            }
//        }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                Thread.sleep(2000);
//                Log.d(TAG, "" + integer);
//            }
//        });
        //由于上下游在同一个线程, 内存正常
        // 每次上游执行一个onNext, 下游就会执行一次 accept,
        // 所以上游也会延迟2秒发送, 内存正常

        //加个线程, 例:
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {    //无限循环发事件
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())               //上游 IO线程发送
                .observeOn(AndroidSchedulers.mainThread())  //下游 主线程接受
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(2000);
                        Log.d(TAG, "" + integer);
                    }
                });
        //效果: 内存又爆掉了, 上下游不在同一线程

        //为什么不加线程和加上线程区别这么大呢, 这就涉及了同步和异步的知识了.

        //当上下游工作在同一个线程中时, 这时候是一个同步的订阅关系,
        // 也就是说上游每发送一个事件必须等到下游接收处理完了以后才能接着发送下一个事件.

        //当上下游工作在不同的线程中时, 这时候是一个异步的订阅关系,
        // 这个时候上游发送数据不需要等待下游接收,
        // 为什么呢,
        // 因为两个线程并不能直接进行通信,
        // 因此上游发送的事件并不能直接到下游里去,
        // 这个时候就需要一个田螺姑娘来帮助它们俩, 这个田螺姑娘就是我们刚才说的水缸 !
        // 上游把事件发送到水缸里去, 下游从水缸里取出事件来处理,
        // 因此, 当上游发事件的速度太快, 下游取事件的速度太慢, 水缸就会迅速装满,
        // 然后溢出来, 最后就OOM了

        //同步和异步的区别仅仅在于是否有水缸 --- 同步无, 异步有(因为异步要靠水缸在不同线程间通信)

    }

    private void rxdemo6_2() {  //zip
        //比如一个界面需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取,
        // 并且只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了:
        Observable<UserInfoResponse> ov1 = mRxApi.getUserInfo(new UserInfoRequest()).subscribeOn(Schedulers.io());//获取用户信息
        Observable<DeviceInfoResponse> ov2 = mRxApi.getDeviceInfo(new DeviceInfoRequest()).subscribeOn(Schedulers.io());//假设配置信息

        Observable
                .zip(ov1, ov2, new BiFunction<UserInfoResponse, DeviceInfoResponse, UserInfo>() {
                    @Override
                    public UserInfo apply(UserInfoResponse userInfoResponse, DeviceInfoResponse deviceInfoResponse) throws Exception {
                        Log.d(TAG, "observable: " + userInfoResponse.toString() + "," + deviceInfoResponse.toString());
                        return new UserInfo(userInfoResponse.toString() + "," + deviceInfoResponse.toString());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        Log.d(TAG, "observe: " + userInfo.getS());
                    }
                });

    }

    private void rxdemo6() {    //zip
        // Zip 通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件.
        // 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。

        //组合的过程是分别从 两根水管里各取出一个事件 来进行组合, 并且一个事件只能被使用一次,
        // 组合的顺序是严格按照事件发送的顺利 来进行的
        //最终下游收到的事件数量 是和上游中发送事件最少的那一根水管的事件数量 相同.
        // 这个也很好理解, 因为是从每一根水管 里取一个事件来进行合并, 最少的 那个肯定就最先取完 ,
        // 这个时候其他的水管尽管还有事件 , 但是已经没有足够的事件来组合了, 因此下游就不会收到剩余的事件了.

        //例:
//        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                Log.d(TAG, "emit 1");
//                emitter.onNext(1);
//                Log.d(TAG, "emit 2");
//                emitter.onNext(2);
//                Log.d(TAG, "emit 3");
//                emitter.onNext(3);
//                Log.d(TAG, "emit 4");
//                emitter.onNext(4);
//                Log.d(TAG, "emit complete1");
//                emitter.onComplete();
//            }
//        });
//
//        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                Log.d(TAG, "emit A");
//                emitter.onNext("A");
//                Log.d(TAG, "emit B");
//                emitter.onNext("B");
//                Log.d(TAG, "emit C");
//                emitter.onNext("C");
//                Log.d(TAG, "emit complete2");
//                emitter.onComplete();
//            }
//        });
//
//        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
//            @Override
//            public String apply(Integer integer, String s) throws Exception {
//                return "new String integer=" + integer + " , s=" + s;
//            }
//        }).subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "onSubscribe");
//            }
//
//            @Override
//            public void onNext(String value) {
//                Log.d(TAG, "onNext: " + value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "onError");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//            }
//        });

        //效果    好像真的是先发送的水管一再发送的水管二呢, 为什么会有这种情况呢?
        // 因为我们两根水管都是运行在同一个线程里, 同一个线程里执行代码肯定有先后顺序呀.
//       onSubscribe
//       emit 1
//       emit 2
//       emit 3
//       emit 4
//       emit complete1
//       emit A
//       onNext: new String integer=1 , s=A
//       emit B
//       onNext: new String integer=2 , s=B
//       emit C
//       onNext: new String integer=3 , s=C
//       emit complete2
//       onComplete

        //切换线程
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Thread.sleep(1000);

                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Thread.sleep(1000);

                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                Thread.sleep(1000);

                Log.d(TAG, "emit complete1");
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "emit B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "emit C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.d(TAG, "emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return "new String integer=" + integer + " , s=" + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
        //效果     在各自的 IO 线程里发送事件, 两根水管同时开始发送, 每发送一个, Zip就组合一个, 再将组合结果发送给下游.
//        onSubscribe
//        emit A
//        emit 1
//        onNext: new String integer=1 , s=A
//        emit B
//        emit 2
//        onNext: new String integer=2 , s=B
//        emit C
//        emit 3
//        onNext: new String integer=3 , s=C
//        emit complete2
//        onComplete

    }

    private void rxdemo5_2() {
        //变换操作符 map
        // 它的作用就是对上游发送的每一个事件应用一个函数, 使得每一个事件都按照指定的函数去变化.
        // 通过Map, 可以将上游发来的事件转换为任意的类型, 可以是一个Object, 也可以是一个集合
        //例如 :
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        }).map(new Function<Integer, String>() {
//            @Override
//            public String apply(Integer integer) throws Exception {
//                return "I am new Result " + integer;
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, s);
//            }
//        });
        //效果
//      I am new Result 1
//      I am new Result 2
//      I am new Result 3


        // FlatMap
        // FlatMap将一个发送事件的上游Observable变换为多个发送事件的 Observables，
        // 然后将它们发射的事件合并后放进一个单独的Observable里.

        //上游每发送一个事件, flatMap都将创建一个新的水管, 然后发送转换之后的新的事件, 下游接收到的就是这些新的水管发送的数据.
        // 注意: flatMap并不保证事件的顺序, 并不是事件1就在事件2的前面. 如果需要保证顺序则需要使用 concatMap.

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).flatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Exception {
//                final ArrayList<String> list = new ArrayList<>();
//                for (int i = 0; i < 3; i++) {
//                    list.add("I am flatmap " + i + " value " + integer);
//                }
//                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, s);
//            }
//        });
        //效果    flatmap   事件倍数   不保证顺序
//     I am flatmap 0 value 2
//     I am flatmap 1 value 2
//     I am flatmap 2 value 2
//     I am flatmap 0 value 1
//     I am flatmap 1 value 1
//     I am flatmap 2 value 1
//     I am flatmap 0 value 3
//     I am flatmap 1 value 3
//     I am flatmap 2 value 3

        // concatMap.
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).concatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Exception {
//                final ArrayList<String> list = new ArrayList<>();
//                for (int i = 0; i < 3; i++) {
//                    list.add("I am flatmap " + i + " value " + integer);
//                }
//                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, s);
//            }
//        });
        //效果    concatMap  严格保证顺序
//       I am flatmap 0 value 1
//       I am flatmap 1 value 1
//       I am flatmap 2 value 1
//       I am flatmap 0 value 2
//       I am flatmap 1 value 2
//       I am flatmap 2 value 2
//       I am flatmap 0 value 3
//       I am flatmap 1 value 3
//       I am flatmap 2 value 3

        //嵌套请求
        mRxApi.register(new RegisterRequest())
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求注册结果
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        //先根据注册的响应结果去做一些操作
                    }
                })
                .observeOn(Schedulers.io())                 //回到IO线程去发起登录请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        return mRxApi.login(new LoginRequest());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求登录的结果
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(SplashActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void rxdemo5() {
        mRxApi.register(new RegisterRequest())
                .subscribeOn(Schedulers.io())               //上游 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //下游 回到主线程去处理请求结果
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        Toast.makeText(SplashActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        login();   //注册成功, 调用登录的方法
                        rxdemo4();   //注册成功, 调用登录的方法
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(SplashActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void rxdemo4() {

        mRxApi.login(new LoginRequest())
                .subscribeOn(Schedulers.io())       //io 线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //主线程处理结果
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponse value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SplashActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void rxdemo3() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);

            }
        });

        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "Observer thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        Consumer consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "accept: " + integer);
            }
        };

        //效果1
//        observable.subscribe(observer);

        //效果2
//        observable
//                .subscribeOn(Schedulers.newThread())    //子线程发送
//                .observeOn(AndroidSchedulers.mainThread())  //主线程接受
//                .subscribe(consumer);   //建立连接

        //效果1
//         Observable thread is : main
//         emit 1
//         Observer thread is : main
//         onNext: 1
        //效果2
//         Observable thread is : RxNewThreadScheduler-1
//         emit 1
//         Observer thread is : main
//         accept: 1


        // subscribeOn() 指定的是上游发送事件的线程,--- 只有第一次指定有效
        // observeOn()  指定的是下游接收事件的线程. ---每指定一次, 线程切换一次
        //注意:
        //多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
        //多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的接收线程就会切换一次.

        //效果3
//        observable
//                .subscribeOn(Schedulers.newThread())    //上游 1
//                .subscribeOn(Schedulers.io())           //上游2
//                .observeOn(AndroidSchedulers.mainThread())  //下游1
//                .observeOn(Schedulers.io())                 //下游2
//                .subscribe(consumer);

        //效果3
//         Observable thread is : RxNewThreadScheduler-1        //对应 上游1
//         emit 1
//         Observer thread is :RxCachedThreadScheduler-2        //对应 下游2
//         accept: 1


        observable
                .subscribeOn(Schedulers.newThread())    //上游1
                .subscribeOn(Schedulers.io())           //上游2
                .observeOn(AndroidSchedulers.mainThread())  //下游1 线程
                .doOnNext(new Consumer<Integer>() {         //下游1 事件
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())         //下游2 线程
                .doOnNext(new Consumer<Integer>() { //下游2 事件
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(io), current thread is : " + Thread.currentThread().getName());
                    }
                })
                .subscribe(consumer);               //下游3 事件
        // subscribeOn 上游只有第一次指定有效
        // observeOn 指定一次切换一次

        //效果4
//        Observable thread is : RxNewThreadScheduler-1                         //对应 上游1
//        emit 1
//        After observeOn(mainThread), current thread is: main                  //对应 下游1
//        After observeOn(io), current thread is : RxCachedThreadScheduler-2    //对应 下游2
//        Observer thread is : RxCachedThreadScheduler-2                        //对应 下游2的线程
//        accept: 1


        //注:
        //在RxJava中, 已经内置了很多线程选项供我们选择, 例如有:

//        Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
//        Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
//        Schedulers.newThread() 代表一个常规的新线程
//        AndroidSchedulers.mainThread() 代表Android的主线程

//        这些内置的Scheduler已经足够满足我们开发的需求, 因此我们应该使用内置的这些选项,
//        在RxJava内部使用的是线程池来维护这些线程, 所有效率也比较高.


        //如果在请求的过程中Activity已经退出了, 这个时候如果回到主线程去更新UI, 那么APP肯定就崩溃了, 怎么办呢,
        // 上一节我们说到了Disposable , 说它是个开关, 调用它的dispose()方法时就会切断水管, 使得下游收不到事件,
        // 既然收不到事件, 那么也就不会再去更新UI了.
        // 因此我们可以在Activity中将这个Disposable 保存起来, 当Activity退出时, 切断它即可.

        //那如果有多个Disposable 该怎么办呢,
        // RxJava中已经内置了一个容器 CompositeDisposable,
        // 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中,
        // 在退出的时候, 调用CompositeDisposable.clear() 即可切断所有的水管.


    }

    private void rxdemo2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit onNext 1");
                emitter.onNext("1");

                Log.d(TAG, "emit onNext 2");
                emitter.onNext("2");

                Log.d(TAG, "emit onNext 3");
                emitter.onNext("3");


                Log.d(TAG, "emit onComplete");
                emitter.onComplete();
//                emitter.onError();


                Log.d(TAG, "emit onNext 4");
                emitter.onNext("4");

            }
        }).subscribe(new Observer<String>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "" + value);
                i++;
//                if (i == 2) {
//                    Log.d(TAG, "isDisposed : " + mDisposable.isDisposed());
//                    Log.d(TAG, "dispose");
//                    mDisposable.dispose();
//                    Log.d(TAG, "isDisposed : " + mDisposable.isDisposed());
//
//                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });
    }

    private void rxdemo1() {

        //1. 创建一个上游 --- Observable
        Observable<Object> observable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                // ObservableEmitter 时间发射器
                //请注意，并不意味着你可以随意乱七八糟发射事件，需要满足一定的规则：
//             上游可以发送无限个onNext, 下游也可以接收无限个onNext.
//             当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
//             当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
//             上游可以不发送onComplete或onError.
//             最为关键的是onComplete和onError必须唯一并且互斥,
// (即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然)

                //注: 关于onComplete和onError唯一并且互斥这一点, 是需要自行在代码中进行控制,
                // 如果你的代码逻辑中违背了这个规则, 并不一定会导致程序崩溃.
                // 比如发送多个onComplete是可以正常运行的, 依然是收到第一个onComplete就不再接收了,
                // 但若是发送多个onError, 则收到第二个onError事件会导致程序会崩溃.


                emitter.onNext(new Object());   //发送的值
                emitter.onError(new Throwable());   //发送异常
                emitter.onComplete();   //发送完成
                /**
                 * Sets a Cancellable on this emitter; any previous Disposable
                 * or Cancellation will be unsubscribed/cancelled.
                 * @param c the cancellable resource, null is allowed
                 */
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {

                    }
                });

                // Disposable 一次用品, 用完可丢弃的, 在此: 切断连接
                // 当调用它的dispose()方法时, 它就会将两根管道切断, 从而导致下游收不到事件.
                //注意: 调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件. 只是下游不再收到

//切断了水管, 但是上游仍然发送了3, complete, 4这几个事件, 而且上游并没有因为发送了onComplete而停止.
// 同时可以看到下游的 onSubscribe() 方法是最先调用的.

                /**
                 * Sets a Disposable on this emitter; any previous Disposable
                 * or Cancellation will be unsubscribed/cancelled.
                 * @param d the disposable, null is allowed
                 */
                emitter.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {

                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });

                /**
                 * Returns true if the downstream disposed the sequence.
                 * @return true if the downstream disposed the sequence
                 */
                emitter.isDisposed();

                /**
                 * Ensures that calls to onNext, onError and onComplete are properly serialized.
                 * @return the serialized ObservableEmitter
                 */
                emitter.serialize();
            }
        });

        //2. 创建一个下游 --- Observer
        Observer observer = new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        //3. 建立连接
        observable.subscribe(observer);
        //注意: 只有当上游和下游建立连接之后, 上游才会开始发送事件.
        // 也就是调用了上游的subscribe() 方法, 开始发送事件.

        // subscribe()有多个重载的方法:
//        public final Disposable subscribe() {}
//        public final Disposable subscribe(Consumer<? super T> onNext) {}
//        public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {}
//        public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
//        public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
//        public final void subscribe(Observer<? super T> observer) {}

        //最后一个带有Observer参数的, 使用见上
        //不带任何参数的subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 老子可不管你发什么.
        //带有一个Consumer参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见, 因此我们如果只需要onNext事件可以这么写:
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {    //注意此处的 lambda 表达式
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "onNext: " + integer);
            }
        });
    }
}
