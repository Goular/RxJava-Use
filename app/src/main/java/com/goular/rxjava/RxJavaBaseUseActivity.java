package com.goular.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.goular.rxjava.lambda.Lambda;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * RxJava基本使用
 */
public class RxJavaBaseUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用自定义Logger类时，需要使用初始化定义方法
        Logger.init(RxJavaBaseUseActivity.class.getSimpleName()).logLevel(LogLevel.FULL);


        //执行Observable - create 的基本用法
        //test01();

        //执行Observable - from 的基本用法
        //执行的是:
        // onNext():""Hello World!"
        // onNext():"Hello KeYe"
        // onNext():"Hello Goular"
        // onCompleted()
        //test02();

        //注意：From和Just最大的区别是，From是数组每一个内容分离，单独处理，而Just是原样处理，Just遇到数组也是直接返回数据，而不会将其分离

        //执行Observable - just 的基本用法,执行与上面的from相同
        //test03();

        //执行Observable - subscribe 注册Action0,Action1的基本用法,执行与上面的from相同
        //test04();

        //链式编程体验
        //test00();

        //直接体验lamdba的表达式的体验
        Lambda.testLambda(this);
    }

    /**
     * 使用 Observable.create() 方法来创建一个 Observable 并为它定义事件触发规则
     */
    public void test01(){
        //创建Observable
        Observable<String> observable = Observable.create(new OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World!");
                subscriber.onNext("Hello Goular!");
                subscriber.onCompleted();
            }
        });

        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("RxJava == > onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("RxJava == > onError(Throwable e)");
            }

            @Override
            public void onNext(String s) {
                Logger.d("RxJava == > onNext(String s)");
            }
        };

        //绑定订阅关系
        observable.subscribe(observer);
    }

    /**
     * 执行Observable - from 的基本用法
     */
    public void test02(){
        String[] array = new String[]{"Hello World!","Hello KeYe","Hello Goular"};

        Observable observable = Observable.from(array);//会将每个T包装成为Observable
        final List<String> list = new ArrayList<>();


        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("RxJava == > onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("RxJava == > onError(Throwable e)");
            }

            @Override
            public void onNext(String s) {
                list.add(s);
                Logger.d("RxJava == > onNext(String s)::"+s);
            }
        };

        //订阅事件
        observable.subscribe(observer);
    }

    /**
     * Observable操作符just()
     *
     */
    public void test03(){
        //创建Observable
        Observable<String> observable = Observable.just("Hello", "RxJava", "Goular");//会将每个变量原样输出到订阅者中

        //创建Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("RxJava -->> onCompleted()!+test03");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.d("RxJava -->> onNext()+test03::" + s);
            }
        };

        //订阅事件
        observable.subscribe(observer);
    }

    /**
     * subscribe()不完整回调
     */
    public void test04(){
        //创建Observable
        Observable observable = Observable.just("Hello", "RxJava", "Goular");

        //不完整回调接口
        //Action1村子返回的参数，所以一般当做是onNext方法来使用
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.d("RxJava:onNextAction:call(String s):" + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.d("RxJava:onErrorAction:call(Throwable throwable):" + throwable.getMessage());
            }
        };

        //Action0 用于没有参数的方法的回调设置
        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                Logger.d("RxJava:onCompletedAction:call()");
            }
        };

        //订阅事件
        //observable.subscribe(onNextAction);
        //observable.subscribe(onNextAction,onErrorAction);
        observable.subscribe(onNextAction,onErrorAction,onCompleteAction);
    }

    /**
     * 简单尝试使用链式编程来写代码
     */
    public void test00(){
        Observable.just("Hello Goular!","Hello Jack!").subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("test00:onCompleted::");
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("test00:e::");
            }

            @Override
            public void onNext(String s) {
                Logger.d("test00:s::"+s);
            }
        });
    }

}
