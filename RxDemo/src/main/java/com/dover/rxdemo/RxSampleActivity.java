package com.dover.rxdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RxSampleActivity extends AppCompatActivity {
    private static final String TAG = "RxSampleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_sample);


        demo1();    //线程切换 --- 后台执行耗时操作, 实时通知 UI 更新
        //subscribeOn   指定 上游发送事件的线程
        //observeOn     指定下游接收事件的线程
        //多次调用subscribeOn，会以第一次的为准; 而多次调用observeOn, 每调用一次,切换一次

// Schedulers.computation()：    用于计算任务，默认线程数等于处理器的数量。
// Schedulers.from(Executor executor)：使用Executor作为调度器，线程池 Executor 框架
// Schedulers.immediate( )：     在当前线程执行任务
// Schedulers.io( )：            用于IO密集型任务，例如访问网络、数据库操作等，也是我们最常使用的。
// Schedulers.newThread( )：     为每一个任务创建一个新的线程。
// Schedulers.trampoline( )：    当其它排队的任务完成后，在当前线程排队开始执行。
// Schedulers.single()：         所有任务共用一个后台线程。
// 以上是在io.reactivex.schedulers包中，提供的Schedulers，

// 而如果我们导入了下面的依赖，那么在io.reactivex.android.schedulers下，还有额外的两个Schedulers可选：
// compile 'io.reactivex.rxjava2:rxandroid:2.0.1'

// AndroidSchedulers.mainThread()：          运行在应用程序的主线程。
// AndroidSchedulers.from(Looper looper)：   运行在该looper对应的线程当中。

        demo2();    //buffer

    }

    private void demo2() {
        //统计一段时间内用户点击了多少次按钮
        //计算一段时间内的平均数据 (例如统计一段时间内的平均温度，或者统计一段时间内的平均位置, 平均距离等)
        //以前的方式: 将这段时间内统计到的数据都暂时存起来，等到需要更新的时间点到了之后，再把这些数据结合起来，并计算平均值。

        //rxjava的实现
        PublishSubject<Double> publishSubject = PublishSubject.create();
        DisposableObserver<List<Double>> disposableObserver2 = new DisposableObserver<List<Double>>() {
            @Override
            public void onNext(List<Double> o) {
                double result = 0;
                if (o.size() > 0) {
                    for (Double d : o) {
                        result += d;
                    }
                    result = result / o.size();
                }
                Log.d(TAG, "更新平均温度：" + result);
                //mTv.setText("过去3秒收到了" + o.size() + "个数据， 平均温度为：" + result);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        publishSubject.buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver2);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(disposableObserver2);

//        public void updateTemperature(double temperature) {
//            Log.d(TAG, "温度测量结果：" + temperature);
//            publishSubject.onNext(temperature);
//        }

    }

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    //使用 CompositeDisposable 对下游进行管理

    //如果Activity要被销毁时，我们的后台任务没有执行完，那么就会导致Activity不能正常回收，
    // 而对于每一个Observer，都会有一个Disposable对象用于管理，
    // 而RxJava提供了一个CompositeDisposable类用于管理这些Disposable，
    // 我们只需要将其将入到该集合当中，
    // 在Activity的onDestroy方法中，调用它的clear方法，就能避免内存泄漏的发生。

    private void demo1() {
        //进行一些耗时操作，例如下载、访问数据库等，为了不阻塞主线程，往往会将其放在后台进行处理，
        // 同时在处理的过程中、处理完成后通知主线程更新UI，这里就涉及到了后台线程和主线程之间的切换。
        //以前的方式: 1. Handler 2.AsyncTask 3.EventBus

        //rxjava 的实现
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //模拟下载的操作。
                for (int i = 0; i < 100; i++) {
                    if (i % 20 == 0) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException exception) {
                            if (!e.isDisposed()) {
                                e.onError(exception);
                            }
                        }
                        e.onNext(i);
                    }
                }
                e.onComplete();
            }
        });

        DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext=" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError=" + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        integerObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
