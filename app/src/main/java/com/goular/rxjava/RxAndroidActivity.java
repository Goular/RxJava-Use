package com.goular.rxjava;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * RxAndroid使用
 */
public class RxAndroidActivity extends AppCompatActivity {

    public static final String TAG = "RxAndroidActivity";
    private Button button;
    private TextView textView;
    private Looper myHandlerThreadLooper;
    private Subscription subscription;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);

        initView();

        //使用自定义Logger类时，需要使用初始化定义方法
        Logger.init(RxJavaBaseUseActivity.class.getSimpleName()).logLevel(LogLevel.FULL);


        MyHandlerThread myHandlerThread = new MyHandlerThread(TAG);
        myHandlerThread.start();

        myHandlerThreadLooper = myHandlerThread.getLooper();
    }

    //自定义子线程
    class MyHandlerThread extends HandlerThread {

        public MyHandlerThread(String name) {
            super(name);
        }
    }

    //初始化控件
    private void initView() {
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(view -> sendData());
    }

    private StringBuffer stringBuffer = new StringBuffer();

    private void sendData() {
        subscription = initObservable()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(()->handler.sendEmptyMessage(0))
                .observeOn(AndroidSchedulers.mainThread())//设置订阅者所在线程
                .subscribe(s ->  {
                    stringBuffer.append(s + " ");//获取到数据后，添加到stringbuffer
                    textView.setText(stringBuffer.toString());
                });
    }

    //模拟耗时操作，创建并返回被观察者
    private Observable<String> initObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try {
                    Thread.sleep(8000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放subscription
        if(subscription != null){
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
