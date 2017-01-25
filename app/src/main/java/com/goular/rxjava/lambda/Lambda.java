package com.goular.rxjava.lambda;

import android.app.Activity;
import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by lotus on 2017/1/25.
 * 用于体验jdk8中lambda的使用
 */

public class Lambda {

    /**
     * 原始写法
     */
    public static void test01(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("","Test01-Run");
            }
        }).run();
    }

    /**
     * lambda写法
     */
    public static void testLambda(Activity activity){
        new Thread(()-> {
            activity.runOnUiThread(()-> Logger.d("","Test02-lambda"));
        }).start();
    }
}
