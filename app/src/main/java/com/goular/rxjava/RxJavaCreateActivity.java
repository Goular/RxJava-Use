package com.goular.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

/**
 * RxJava创建操作的例子
 * create(),from(),just(),defer(),repeat()
 */
public class RxJavaCreateActivity extends AppCompatActivity {

    private int i = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_create);
        //使用自定义Logger类时，需要使用初始化定义方法
        Logger.init(this.getClass().getSimpleName()).logLevel(LogLevel.FULL);

        //testDefer();

        //testRepeat();

        //testRange();

        //testInterval();

        //testTimer();

        //testEmpty();

        //testError();

        //testScan();

        //testGroupBy();

        //testBuffer();

        //testWindow();

        //testFilter();

        testTakeLast();
    }

    //defer( ) — 只有当订阅者订阅才创建Observable；为每个订阅创建一个新的Observable
    public void testDefer() {
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.from(new String[]{"Hello World!", "Hello P&G!", "Hello Goular!"});
            }
        });

        i = 20;

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //直接打印i出来，会展示为20，说明i的定义期间不会走onNext方法
                Logger.d(s + "::" + i);
            }
        });
    }

    //repeat( ) — 创建一个重复发射指定数据或数据序列的Observable
    public void testRepeat() {
        Observable.just("Hello Goular!").repeat(6).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Logger.d("testRepeat::onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.d("testRepeat::onNext::" + s);
            }
        });
    }

    //range( ) — 创建一个发射指定范围的整数序列的Observable
    public void testRange() {
        Observable.range(3, 6).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Logger.d("testRange::call::" + integer);
            }
        });
    }

    //interval( ) — 创建一个按照给定的时间间隔发射整数序列的Observable
    public void testInterval() {
        Observable.interval(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Logger.d("aLong::" + aLong);
            }
        });
    }

    //timer( ) — 创建一个在给定的延时之后发射单个数据的Observable
    public void testTimer() {
        Observable.timer(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Logger.d("testTimer::aLong::" + aLong);
            }
        });
    }

    //empty( ) — 创建一个什么都不做直接通知完成的Observable
    //结果：发送了一个空的Observable，直接调用了onCompleted()方法
    public void testEmpty() {
        Observable.empty().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Logger.d("testEmpty::onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("testEmpty::onError");
            }

            @Override
            public void onNext(Object o) {
                Logger.d("testEmpty::onNext");
            }
        });
    }

    //error( ) — 创建一个什么都不做直接通知错误的Observable
    //结果：发送了一个空的Observable，直接调用了onError()方法
    public void testError() {
        Exception ex = new Exception();

        Observable.error(ex).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Logger.d("testError::onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("testError::onError");
            }

            @Override
            public void onNext(Object o) {
                Logger.d("testError::onNext");
            }
        });
    }

    //scan( ) — 对Observable发射的每一项数据应用一个函数，然后按顺序依次发射每一个值
    //参数一，第一个参数的数据类型
    //参数二，第二个参数的数据类型
    //参数三，处理后结果的返回类型

    //结果：将自定义函数应用于数据序列，并将这个函数的结果作为函数下一次的参数1使用
    //将自定义函数应用于数据序列，并将这个函数的结果作为函数下一次的参数1使用，1+0=1，1+2=3 ，3+3=6
    public void testScan() {
        Observable.just(1, 2, 3, 4, 5).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Logger.d("RxJava::" + integer);
            }
        });

        //Lambda写法
        //Observable.just(1, 2, 3, 4, 5)
        //        .scan((integer1,integer2)->{return integer1+integer2;})
        //        .subscribe((integer)->Logger.d("RxJava::" + integer));
    }

    //groupBy( ) — 将Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
    public void testGroupBy() {
        Observable.just(1, 2, 3, 4, 5).groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * 2;
            }
        })
                .subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void call(GroupedObservable<Integer, Integer> groupedObservable) {
                        groupedObservable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Logger.d("testGroupBy::key:" + groupedObservable.getKey() + ":value:" + integer);
                            }
                        });
                    }
                });
    }

    //buffer( ) — 它定期从Observable收集数据到一个集合，然后把这些数据集合打包发射，而不是一次发射一个
    public void testBuffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .buffer(2, 1)
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        String result = "";
                        for (Integer str : integers) {
                            result += str;
                        }
                        Logger.d("testBuffer::" + result);
                    }
                });
    }

    //window( ) — 定期将来自Observable的数据分拆成一些Observable窗口，然后发射这些窗口，而不是每次发射一项
    public void testWindow() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .window(2, 1)
                .subscribe(new Action1<Observable<Integer>>() {
                    @Override
                    public void call(Observable<Integer> observable) {
                        observable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Logger.d("testWindow::" + integer);
                            }
                        });
                    }
                });
    }

    //filter( ) — 过滤数据
    public void testFilter() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Logger.d("testFilter::" + integer);
            }
        });
    }

    //takeLast( ) — 只发射最后的N项数据
    public void testTakeLast() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .takeLast(4)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Logger.d("testTakeLast::" + integer);
                    }
                });
    }

    //last( ) — 只发射最后的一项数据
    public void testLast(){

    }
}
