package com.goular.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.goular.rxjava.model.Author;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_map);
        //使用自定义Logger类时，需要使用初始化定义方法
        Logger.init(this.getClass().getSimpleName()).logLevel(LogLevel.FULL);

        //testMap();
        testFlatMap();
    }

    //Map()变换操作符
    public void testMap() {
        Integer[] array = new Integer[]{1, 2, 3, 4};

        Observable.from(array)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer.toString();
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.d("testMap::" + s);
            }
        });
    }

    //Map()变换操作符
    public void testFlatMap() {
        Observable.from(getData())
                .flatMap((Func1<Author, Observable<?>>) author -> {
                    Log.d("RxJava", author.name);
                    return Observable.from(author.Articles);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(article -> {
                    Log.d("RxJava-->>", article.toString());
                });
    }


    //耗时操作
    public static List<Integer> getData2() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public static List<Author> getData() {
        final int[] i = {0};
        List<Author> authorList = new ArrayList<>();

        Observable.just("方式", "二维", "同意")
                .map(name -> new Author(name))
                .map(author -> {
                    author.getArticles().add("article" + i[0]++);
                    author.getArticles().add("article" + i[0]++);
                    author.getArticles().add("article" + i[0]++);
                    return author;
                })
                .map(author -> {
                    authorList.add(author);
                    return authorList;
                })
                .subscribe();

        return authorList;

    }
}
