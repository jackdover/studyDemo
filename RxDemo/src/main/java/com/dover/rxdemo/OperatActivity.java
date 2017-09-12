package com.dover.rxdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dover.rxdemo.rxapi.IRxApi;
import com.dover.rxdemo.rxapi.RxApiImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by d on 2017/9/11.
 */
public class OperatActivity extends AppCompatActivity {
    private static final String TAG = "OperatActivity";

    private IRxApi mRxApi;
    private Disposable intervalDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRxApi = RxApiImp.getRxApi();

        // RxJava 操作符

//        demo1_map();
        // map 对上游Observable发送的每一个事件使用一个函数，使得每一个事件都按照指定的函数去变化

//        demo2_flatMap();
        // FlatMap 将一个发送事件的上游Observable变换为多个发送事件的 Observables，
        // 然后将它们发射的事件合并后再放进一个单独的Observable里.
        //对比:
        // 1. 把上游Observable发送的每一个事件变换为 n个发送事件的 Observables,
        //    所有的事件的所有 Observables发射的事件合并后再放进一个单独的Observable里.
        // 2. flatMap 并不保证事件的顺序

//        demo3_concatMap();
        //concatMap 与 FlatMap 的唯一区别就是 concatMap 保证了顺序

//        demo4_zip();
        //zip 专用于合并事件，该合并不是连接（连接操作符后面会说），而是两两配对，
        // 也就意味着，最终配对出的 Observable 发射事件数目只和少的那个相同。
        // 一个事件只能被使用一次
        // 组合的顺序是严格按照事件发送的顺利 来进行的 (无论线程如何)

//        demo5_concat();
        //concat 连接事件, 把多个事件连接成一个事件, 按照指定顺序。有序
        //只有前一个 onComplete, 后一个才会发送, 否则只会发送前一个的
        //concat 连接的多个 Observable 的泛型应当保持一致

//        demo5_2_merge();
        //merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。
        // 注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。无序

//        demo6_filter();
        // Filter 过滤操作符     只允许满足条件的事件通过, 过滤掉不符合条件的事件

//        demo7_sample();
        //Sample, 取样, 每隔一段时间对上游数据进行取样,发送到下游

//        demo8_take();     // take 接受一个 long 型参数 count ，代表至多发送 count 个事件
        // take 和 takeLast 方法可以将上游事件中的前N项或者最后N项发送到下游,其他事件则进行过滤

//        demo9_repeat_distinct();
        // repeat       生成重复事件
        // distinct     去除重复事件

//        demo10_buffer();
        // buffer(count,skip)  跳跃式发送事件, 每次最多发送 count个, 每发送一次跳过 skip个事件
        // buffer(long timespan, TimeUnit unit)  把事件全部放到缓冲区, 时间到之后, 再发送

        // timer 和 interval 均默认在新线程。
//        demo11_timer(); //timer 相当于一个定时任务, 一定时间后,发送事件,
        // 第一次必然延迟
//        demo12_interval(); //interval 操作符用于间隔时间执行某个操作，
        // 其接受三个参数，分别是第一次发送延迟，间隔时间，时间单位( long initialDelay, long period, TimeUnit unit )
        // 第一次延迟3秒, 之后每隔2秒, 发送事件
        //  第一次不延迟, 也可以接收两个参数,这样第一次就不会延迟
        // subscribe(Cousumer<? super T> onNext)返回的是Disposable, 需要在activity的销毁中停止事件

//        demo13_doOnNext();  //doOnNext 作用是让订阅者在接收到数据之前干点有意思的事情

//        demo14_skip();  //skip 接受一个 long 型参数 count ，代表跳过 count 个事件发送后边的事件

//        demo15_just();  //just，就是一个简单的发射器依次调用 onNext() 方法

//        demo16_single();    //Single 只会接收一个参数，而 SingleObserver 只会调用 onError() 或者 onSuccess()

//        demo17_debounce();  // debounce 去除发送频率过快的项

//        demo18_defer(); //defer 每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable

//        demo19_last();  //last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项

//        demo20_reduce();    //reduce  每次用一个方法处理一个值，可以有一个 seed 作为初始值。(所有数据的累计处理方法)
        //只有最后一次才调用下游接收 获取最终结果

//        demo21_scan(); //scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而 scan 会始终如一地把每一个步骤都输出。
        //每次操作都调用下游接收 本次结果

//        demo22_window();    //window 按照实际划分窗口，将数据发送给不同的 Observable

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intervalDisposable != null && !intervalDisposable.isDisposed()) {
            intervalDisposable.dispose();
        }
    }


    private void demo22_window() {
        //window 按照实际划分窗口，将数据发送给不同的 Observable
        Observable.interval(1, TimeUnit.SECONDS)    //间隔1秒, 发送一次事件
                .take(15)   //最多接收15个
                .window(3, TimeUnit.SECONDS)        //每3秒 拆成一个单元 发送给不同下游
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception {
                        Log.d(TAG, "Sub Divide begin...\n");
                        longObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d(TAG, "Next:" + aLong + "\n");
                            }
                        });
                    }
                });
        //效果
//        09-11 09:44:22.740 11614-11614/? D/OperatActivity: Sub Divide begin...
//        09-11 09:44:23.717 11614-11635/? D/OperatActivity: Next:0
//        09-11 09:44:24.717 11614-11635/? D/OperatActivity: Next:1
//        09-11 09:44:25.717 11614-11614/? D/OperatActivity: Sub Divide begin...
//        09-11 09:44:25.718 11614-11635/? D/OperatActivity: Next:2
//        09-11 09:44:26.718 11614-11635/? D/OperatActivity: Next:3
//        09-11 09:44:27.718 11614-11635/? D/OperatActivity: Next:4
//        09-11 09:44:28.716 11614-11614/? D/OperatActivity: Sub Divide begin...
//        09-11 09:44:28.719 11614-11635/? D/OperatActivity: Next:5
//        09-11 09:44:29.718 11614-11635/? D/OperatActivity: Next:6
//        09-11 09:44:30.718 11614-11635/? D/OperatActivity: Next:7
//        09-11 09:44:31.715 11614-11614/? D/OperatActivity: Sub Divide begin...
//        09-11 09:44:31.718 11614-11635/? D/OperatActivity: Next:8
//        09-11 09:44:32.718 11614-11635/? D/OperatActivity: Next:9
//        09-11 09:44:33.718 11614-11635/? D/OperatActivity: Next:10
//        09-11 09:44:34.716 11614-11614/? D/OperatActivity: Sub Divide begin...
//        09-11 09:44:34.717 11614-11635/? D/OperatActivity: Next:11
//        09-11 09:44:35.718 11614-11635/? D/OperatActivity: Next:12
//        09-11 09:44:36.718 11614-11635/? D/OperatActivity: Next:13
//        09-11 09:44:37.716 11614-11614/? D/OperatActivity: Sub Divide begin...
//        09-11 09:44:37.719 11614-11635/? D/OperatActivity: Next:14
    }

    private void demo21_scan() {
        //scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而 scan 会始终如一地把每一个步骤都输出。

        Observable.just(1, 2, 3, 4)
//                .scan(8, new BiFunction<Integer, Integer, Integer>() {  //参1 起始值, 在此基础上做累计处理
                .scan(new BiFunction<Integer, Integer, Integer>() {  //参1 起始值, 在此基础上做累计处理
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "apply: scan : " + (integer + integer2) + "\n");
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: scan : " + integer + "\n");
            }
        });
        //效果    //每次操作都调用下游接收 本次结果, 最先执行的是下游的 onSubscribe 或者 accept方法
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: accept: scan : 8
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: apply: scan : 9
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: accept: scan : 9
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: apply: scan : 11
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: accept: scan : 11
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: apply: scan : 14
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: accept: scan : 14
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: apply: scan : 18
//        09-11 09:29:03.233 11284-11284/? D/OperatActivity: accept: scan : 18
        //无起始值
//        09-11 09:33:12.775 11413-11413/? D/OperatActivity: accept: scan : 1
//        09-11 09:33:12.775 11413-11413/? D/OperatActivity: apply: scan : 3
//        09-11 09:33:12.776 11413-11413/? D/OperatActivity: accept: scan : 3
//        09-11 09:33:12.776 11413-11413/? D/OperatActivity: apply: scan : 6
//        09-11 09:33:12.776 11413-11413/? D/OperatActivity: accept: scan : 6
//        09-11 09:33:12.776 11413-11413/? D/OperatActivity: apply: scan : 10
//        09-11 09:33:12.776 11413-11413/? D/OperatActivity: accept: scan : 10
    }

    private void demo20_reduce() {  //只有最后一次才调用下游接收 获取最终结果
        //reduce  每次用一个方法处理一个值，可以有一个 seed 作为初始值。
        Observable.just(1, 2, 3, 4)
//                .reduce(8, new BiFunction<Integer, Integer, Integer>() {  //参1 起始值, 在此基础上做累计处理
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "apply: reduce : " + (integer + integer2) + "\n");
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: reduce : " + integer + "\n");
            }
        });
        // 效果   //只有最后一次才调用下游接收 获取最终结果
//        09-11 09:23:24.291 11127-11127/? D/OperatActivity: apply: reduce : 9
//        09-11 09:23:24.291 11127-11127/? D/OperatActivity: apply: reduce : 11
//        09-11 09:23:24.291 11127-11127/? D/OperatActivity: apply: reduce : 14
//        09-11 09:23:24.291 11127-11127/? D/OperatActivity: apply: reduce : 18
//        09-11 09:23:24.291 11127-11127/? D/OperatActivity: accept: reduce : 18
    }

    private void demo5_2_merge() {
        //merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。
        // 注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
        Observable.merge(Observable.just(4, 5, 6, 7, 8, 9, 4, 5, 6),    //just 最多9个
                Observable.just(1, 2, 3))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "merge : " + integer + "\n");
                    }
                });

    }

    private void demo19_last() {
        //last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项
        Observable.just(1, 2, 3)
                .last(0)    //参数是获取不到的时候的默认值, 类似SharePreference
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "last : " + integer + "\n");
                    }
                });
        //效果  输出 3 (即最后一个)
    }

    private void demo18_defer() {
        //defer 每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable
        Observable<Integer> deferObservable = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(1, 2, 3);
            }
        });

        deferObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "defer : " + value + "\n");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "defer : onError : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "defer : onComplete\n");
            }
        });
    }

    private void demo17_debounce() {
        // debounce 去除发送频率过快的项

        // 去除发送间隔时间小于 500 毫秒的发射事件
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 模拟等待一定时间, 发送事件
                emitter.onNext(1); // skip
                Thread.sleep(400);

                emitter.onNext(2); // deliver
                Thread.sleep(505);

                emitter.onNext(3); // skip
                Thread.sleep(100);

                emitter.onNext(4); // deliver
                Thread.sleep(605);

                emitter.onNext(5); // deliver
                Thread.sleep(510);

                emitter.onComplete();
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "debounce :" + integer + "\n");
                    }
                });
        //效果 2,4,5
    }

    private void demo16_single() {
        //Single 只会接收一个参数，而 SingleObserver 只会调用 onError() 或者 onSuccess()
        Single.just(1)      //只能接受一个参数
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer value) {
                        Log.d(TAG, "single : onSuccess : " + value + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "single : onError : " + e.getMessage() + "\n");
                    }
                });
        // 只接受一个参数, SingleObserver 只执行 onSuccess 或者 onError 中的一个
    }

    private void demo15_just() {
        //just，就是一个简单的发射器依次调用 onNext() 方法
        Observable.just("1", "2", "3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e(TAG, "accept : onNext : " + s + "\n");
                    }
                });
    }


    private void demo14_skip() {

        Observable.just(1, 2, 3, 4, 5)
                .skip(2)    //跳过2个, 发送后边的事件
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.d(TAG, "skip : " + integer + "\n");
                    }
                });
        //效果
//        09-11 08:42:38.743 9938-9938/? D/OperatActivity: skip : 3
//        09-11 08:42:38.743 9938-9938/? D/OperatActivity: skip : 4
//        09-11 08:42:38.744 9938-9938/? D/OperatActivity: skip : 5
    }

    private void demo13_doOnNext() {
        //doOnNext 应该不算一个操作符，但考虑到其常用性。它的作用是让订阅者在接收到数据之前干点有意思的事情。
        // 假如我们在获取到数据之前想先保存一下它，无疑我们可以这样实现

        Observable
                .just(1, 2, 3, 4)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext 保存 " + integer + "成功" + "\n");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "subscribe :" + integer + "\n");
                    }
                });
        //效果
//        09-11 08:40:32.909 9804-9804/? D/OperatActivity: doOnNext 保存 1成功
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: subscribe :1
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: doOnNext 保存 2成功
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: subscribe :2
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: doOnNext 保存 3成功
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: subscribe :3
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: doOnNext 保存 4成功
//        09-11 08:40:32.910 9804-9804/? D/OperatActivity: subscribe :4
    }

    private void demo12_interval() {
        Log.e(TAG, "interval start : " + System.currentTimeMillis() + "\n");
        // 由于interval默认在新线程，所以我们应该切回主线程
        intervalDisposable = Observable.interval(3, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.e(TAG, "interval :" + aLong + " at " + System.currentTimeMillis() + "\n");
                    }
                });

        // 第一次延迟3秒, 之后每隔2秒, 发送事件
    }

    private void demo11_timer() {

        // 一定时间后, 发送事件
        Log.e(TAG, "timer start : " + System.currentTimeMillis() + "\n");
        Observable
                .timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())               //不调用, 也默认在新线程
                .observeOn(AndroidSchedulers.mainThread())  // timer 默认在新线程，所以需要切换回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Log.d(TAG, "timer :" + aLong + " at " + System.currentTimeMillis() + "\n");
                    }
                });


    }

    private void demo10_buffer() {
        //buffer 操作符接受两个参数，buffer(count,skip)，
        // 作用是将 Observable 中的数据按 skip (步长) 分成最大不超过 count 的 buffer ，
        // 然后生成一个  Observable

        // 每次最多发送count个事件, 每次跳过 skip 个事件

        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integers) throws Exception {
                        Log.e(TAG, "buffer size : " + integers.size() + "\n");
                        Log.e(TAG, "buffer value : ");
                        for (Integer i : integers) {
                            Log.e(TAG, i + "");
                        }
                        Log.e(TAG, "\n");
                    }
                });

        // 1,2,3    3,4,5   5
        // 每次跳过2个, 最多发3个事件
    }


    private void demo9_repeat_distinct() {
        // repeat       生成重复事件
        // distinct     去除重复事件
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 50; i++) {
                    e.onNext(i);
                }
            }
        })
                .take(4)        // 只发送前4个事件
                .repeat(3)      //生成重复事件    (0,1,2,3, 0,1,2,3, 0,1,2,3)
                .distinct()     //去除重复事件    (0,1,2,3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });


    }

    private void demo8_take() {
        //take 和 takeLast 方法可以将上游事件中的前N项或者最后N项发送到下游,其他事件则进行过滤

        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; ; i++) {
                            e.onNext(i);
                        }
                    }
                })
                .take(3)      //只发送前3个事件
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });


    }

    private void demo7_sample() {
        //Sample,取样,
        // 其功能是,sample会每隔一段时间对上游数据进行取样,发送到下游,
        // 但是这样会导致丢失了大量事件,比较适合特定场合,如对一组数中进行抽样

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).sample(1, TimeUnit.SECONDS)      //每隔1秒, 取样, (每隔一秒, 对上游数据采样一次,发送到下游)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, integer + "");
                    }
                });

    }

    private void demo6_filter() {
        // filter 过滤    只允许满足条件的事件通过
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 10 == 0;   //只允许能被10整除的事件通过
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });

    }

    private void demo5_concat() {
//        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        Log.d(TAG, "concat 1 : " + integer + "\n");
//                    }
//                });


        Observable<Integer> o1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onComplete();   //TODO 填坑, 前一个必须 onComplete, 后一个才会发送, 否则只会发送前一个的
            }
        });

        Observable<Integer> o2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        });

        Observable.concat(o1, o2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "concat 2 : " + integer + "\n");
            }
        });


        // 填坑, 前一个必须调用 onComplete, 后一个事件才会发送, 否则, 只发送前一个
//        09-11 10:28:38.629 12293-12293/? D/OperatActivity: concat 2 : 4
//        09-11 10:28:38.630 12293-12293/? D/OperatActivity: concat 2 : 5
//        09-11 10:28:38.630 12293-12293/? D/OperatActivity: concat 2 : 6
//        09-11 10:28:38.630 12293-12293/? D/OperatActivity: concat 2 : 1
//        09-11 10:28:38.630 12293-12293/? D/OperatActivity: concat 2 : 2
//        09-11 10:28:38.630 12293-12293/? D/OperatActivity: concat 2 : 3
    }

    private void demo4_zip() {
        //zip 专用于合并事件，该合并不是连接（连接操作符后面会说），而是两两配对，
        // 也就意味着，最终配对出的 Observable 发射事件数目只和少的那个相同。

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
        }).subscribeOn(Schedulers.io());    //io线程

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
        }).subscribeOn(Schedulers.io());    //io线程

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

    private void demo3_concatMap() {        // concatMap.

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {   //转换成 n个Observable, 且有序
                final ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am flatmap " + i + " value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
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
    }

    private void demo2_flatMap() {

        // FlatMap
        // FlatMap将一个发送事件的上游Observable变换为多个发送事件的 Observables，
        // 然后将它们发射的事件合并后放进一个单独的Observable里.

        //上游每发送一个事件, flatMap都将创建一个新的水管, 然后发送转换之后的新的事件, 下游接收到的就是这些新的水管发送的数据.
        // 注意: flatMap并不保证事件的顺序, 并不是事件1就在事件2的前面. 如果需要保证顺序则需要使用 concatMap.

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {   //转换成 n个Observable, 且无序
                final ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am flatmap " + i + " value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
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
    }

    private void demo1_map() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
                e.onNext(4);
            }
        }).map(new Function<Integer, String>() {    //转换成一个新事件
            @Override
            public String apply(Integer integer) throws Exception {
                return "new String " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

        //效果
//        09-11 03:57:28.965 5093-5093/? D/OperatActivity: new String 1
//        09-11 03:57:28.965 5093-5093/? D/OperatActivity: new String 2
//        09-11 03:57:28.965 5093-5093/? D/OperatActivity: new String 3


        //实战: 读取一篇英文文章,将文章中的字符全部转换为大写.

        //模拟一篇文章
        String article = "fkjdsalijfofldaJFOIEjfldanlJR2OnfldajilwafkndaIUPO32,LFKjlijuJFLMA";
        final char[] chars = article.toCharArray();

        Observable
                .create(new ObservableOnSubscribe<Character>() {
                    @Override
                    public void subscribe(ObservableEmitter<Character> emitter) throws Exception {
                        for (int i = 0; i < chars.length; i++) {
                            emitter.onNext(chars[i]);
                        }
                    }
                })
                .delay(2, TimeUnit.SECONDS)     //延迟2秒发送
                .map(new Function<Character, String>() {    //map 事件转换
                    @Override
                    public String apply(Character c) throws Exception {
                        if (c >= 'a' && c <= 'z') {
                            return c.toString().toUpperCase();
                        } else {
                            return c.toString();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())       //io 线程发送
                .observeOn(AndroidSchedulers.mainThread())  //主线程接收
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, s);
                    }
                });

    }
}
