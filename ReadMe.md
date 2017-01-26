#RxJava使用笔记

##RxJava基础

###创建Observable 
<pre>
Observable的创建操作符：
	比如： create(),from(),just(),repeat(),defer(),range(),
				 interval(),和timer()等等
</pre>

###创建Observer
<pre>
Observer用于处理Observable发送过来的各类事件。
可以用Operators(操作符)对事件进行各种拦截和操作。
    例子:public abstract class Subscriber<T> implements Observer<T>

RxJava 还创建了一个继承了 Observer 的抽象类：Subscriber：
    Subscriber 进行了一些扩展，基本使用方式是一样的，这也是以后我们主要用到的一个类
</pre>

###Subscribe 订阅
<pre>
    通过subscribe()方法订阅，把observable和observer关联起来
	订阅后，observable就会调用observer的onNext()、onCompleted()、onError()等方法。
</pre>

###RxJava的操作符
<pre>
RxJava中提供了大量不同种类，不同场景的Operators(操作符)，RxJava的强大性就来自于它所定义的操作符。主要分类：

    创建操作：用于创建Observable的操作符；
    变换操作：这些操作符可用于对Observable发射的数据进行变换；
    过滤操作：这些操作符用于从Observable发射的数据中进行选择；
    组合操作：组合操作符用于将多个Observable组合成一个单一的Observable；
    异常处理：这些操作符用于从错误通知中恢复；
    辅助操作：一组用于处理Observable的操作符；

    其实我们创建Observable的create()，from()，just()等方法，都属于创建操作符。那么，让我们通过代码，来看看各种操作符的实现。


    最重要 操作符map()和flatMap()
    map:把原有类型的被观察者，转换为目标类型的观察者，并且发送出去
    flatMap:把原有类型的被观察者，转换为一个被观察者集合，再扁平化的放入一个新的被观察者，最后发送出去

1.创建操作
    defer( ) — 只有当订阅者订阅才创建Observable；为每个订阅创建一个新的Observable
    没有订阅的时候，call方法是不会被定义与调用的

    Observable.defer(new Func0<Observable<Object>>() {
        @Override
        public Observable<Object> call() {
            //创建并返回新的Observable，
            return null;
        }
    });


    repeat( ) — 创建一个重复发射指定数据或数据序列的Observable
    结果：将数字1，重复发射3次
    Observable.just(1).repeat(3).subscribe(new Action1<Integer>() {
        @Override
        public void call(Integer integer) {
            Log.d("RxJava", String.valueOf(integer));
        }
    });


    range( ) — 创建一个发射指定范围的整数序列的Observable
    结果：发射1-4的整数
    Observable.range(1,4).subscribe(new Action1<Integer>() {
        @Override
        public void call(Integer integer) {
            Log.d("RxJava", String.valueOf(integer));
        }
    });


    interval( ) — 创建一个按照给定的时间间隔发射整数序列的Observable
    Observable.interval(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
        @Override
        public void call(Long i) {
            Log.d("RxJava", String.valueOf(i));
        }
    });
    结果：每个1秒，发射一个整数序列中的数

    //timer( ) — 创建一个在给定的延时之后发射单个数据的Observable
    //结果：3秒后，发射了一个包含数字0的Observable
    Observable.timer(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
        @Override
        public void call(Long i) {
            Log.d("RxJava", i);
        }
    });

    //empty( ) — 创建一个什么都不做直接通知完成的Observable
    //结果：发送了一个空的Observable，直接调用了onCompleted()方法
    Observable.empty().subscribe(new Subscriber<Object>() {
        @Override
        public void onCompleted() {
            Log.d("RxJava", "发送了一个空的的Observable");
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Object o) {

        }
    });

    //error( ) — 创建一个什么都不做直接通知错误的Observable
    //结果：发送了一个空的Observable，直接调用了onError()方法
    Observable.empty().subscribe(new Subscriber<Object>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
           Log.d("RxJava", "发送了一个空的的Observable");
        }

        @Override
        public void onNext(Object o) {

        }
    });


2.变换操作
    scan( ) — 对Observable发射的每一项数据应用一个函数，然后按顺序依次发射每一个值
    结果：将自定义函数应用于数据序列，并将这个函数的结果作为函数下一次的参数1使用，1+0=1，1+2=3 ，3+3=6
    注意：scan返回值是key的值，value还是原来just的那些内容

    Observable.just(1, 2, 3)
        .scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                //自定义函数
                return integer + integer2;
            }
        })
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("RxJava", String.valueOf(integer));
            }
    });

    groupBy( ) — 将Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
    结果：在第一个func1函数中，设置key，最后生成一个Observable集合，并把每一个groupedObservable，并依次发射出去

    Observable.just(1, 2, 3, 4)
        .groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                //这里返回的结果为key
                return integer + 1;
            }
        })
        .subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
            @Override
            public void call(GroupedObservable<Integer, Integer> groupedObservable) {

                groupedObservable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("RxJava", "key:" + groupedObservable.getKey() + ",value:" + integer);
                    }
                });
            }
        });

    buffer( ) — 它定期从Observable收集数据到一个集合，然后把这些数据集合打包发射，而不是一次发射一个
    结果：buffer()有两个参数count和skip，count指定List的大小，skip指定每次发射一个List需要跳过几个数；buffer(2, 1)：每组2个数，每次跳过1个数，结果如下：
    keye.com.rxjavaobserver D/RxJava: [1, 2]
    keye.com.rxjavaobserver D/RxJava: [4, 5]
    keye.com.rxjavaobserver D/RxJava: [7]

    Observable.just(1, 2, 3, 4,5,6,7)
                  .buffer(2, 1)
                  .subscribe(new Action1<List<Integer>>() {
                      @Override
                      public void call(List<Integer> integers) {
                        Log.d("RxJava", integers + "");
                      }
        });

    window()  定期将来自Observable的数据分拆成一些Observable窗口，然后发射这些窗口，而不是每次发射一项

    Observable.just(1, 2, 3, 4, 5, 6, 7)
                  .window(2, 2)
                  .subscribe(new Action1<Observable<Integer>>() {
                      @Override
                      public void call(Observable<Integer> observable) {
                          Log.d("RxJava", "window" );
                          observable.subscribe(new Action1<Integer>() {
                              @Override
                              public void call(Integer integer) {
                                  Log.d("RxJava", integer + "");
                              }
                          });
                      }
        });

    结果：window()操作符和buffer()类似，都是缓存一段数据集合，再打包发射出去
    buffer返回的是集合类数据结果，而window返回的是observable的单一结果，但是是连续一段时间内发，所以都是缓存一部分数据在发送出去

3.过滤操作
    filter( ) — 过滤数据
    Observable.just(1, 2, 3, 4, 5, 6)
            .filter(new Func1<Integer, Boolean>() {
                @Override
                public Boolean call(Integer integer) {
                    //从数组中，筛选偶数
                    return integer % 2 == 0;
                }
            }).subscribe(new Action1<Integer>() {
                @Override
                 public void call(Integer i) {
                    Log.d("RxJava", String.valueOf(i));
            }
    });

    结果：
    11-06 03:42:04.747 4213-4213/? D/RxJava: 2
    11-06 03:42:04.747 4213-4213/? D/RxJava: 4
    11-06 03:42:04.747 4213-4213/? D/RxJava: 6


    takeLast( ) — 只发射最后的N项数据

    Observable.just(1, 2, 3, 4, 5, 6)
        .takeLast(3)  //取最后3项数据
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                Log.d("RxJava", String.valueOf(i));
            }
        });
    结果：
    11-06 03:44:18.307 6379-6379/keye.com.rxjavaobserver D/RxJava: 4
    11-06 03:44:18.307 6379-6379/keye.com.rxjavaobserver D/RxJava: 5
    11-06 03:44:18.307 6379-6379/keye.com.rxjavaobserver D/RxJava: 6


    last( ) — 只发射最后的一项数据
    Observable.just(1, 2, 3, 4, 5, 6)
            .last()
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer i) {
                    Log.d("RxJava", String.valueOf(i));
                }
            });
    结果：
    11-06 03:49:46.710 6582-6582/? D/RxJava: 6


    skip( ) — 跳过开始的N项数据
    Observable.just(1, 2, 3, 4, 5, 6)
            .skip(3)
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer i) {
                    Log.d("RxJava", String.valueOf(i));
                }
            });
    结果：
    11-06 03:49:46.710 6582-6582/? D/RxJava: 4
    11-06 03:49:46.710 6582-6582/? D/RxJava: 5
    11-06 03:49:46.710 6582-6582/? D/RxJava: 6


    take( ) — 只发射开始的N项数据
    Observable.just(1, 2, 3, 4, 5, 6)
            .take(3)
            .subscribe(i -> {
                Log.d("RxJava", String.valueOf(i));
            });
    结果：
    11-06 03:49:46.710 6582-6582/? D/RxJava: 1
    11-06 03:49:46.710 6582-6582/? D/RxJava: 2
    11-06 03:49:46.710 6582-6582/? D/RxJava: 3

    first( ) and takeFirst( ) — 只发射第一项数据，或者满足某种条件的第一项数据
    Observable.just(1, 2, 3, 4, 5, 6)
            .first()
            .subscribe(i -> {
                Log.d("RxJava", String.valueOf(i));
            });
    结果：
    11-06 03:49:46.710 6582-6582/? D/RxJava: 1


    elementAt( ) — 发射第N项数据
    Observable.just(1, 2, 3, 4, 5, 6)
            .elementAt(3)
            .subscribe(i -> {
                Log.d("RxJava", String.valueOf(i));
            });
    结果：
    11-06 03:49:46.710 6582-6582/? D/RxJava: 4

    sample( ) or throttleLast( ) — 定期发射Observable最近的数据
    Observable.interval(1,TimeUnit.SECONDS)
            .sample(4, TimeUnit.SECONDS)
            .subscribe(i -> {
                Log.d("RxJava", String.valueOf(i));
            });
    结果：interval()每隔一秒发送整数序列，sample()每隔4秒，获取Observable的数据，结果如下：
    -16618/keye.com.rxjavaobserver D/RxJava: 3
    -16618/keye.com.rxjavaobserver D/RxJava: 6
    -16618/keye.com.rxjavaobserver D/RxJava: 10
    -16618/keye.com.rxjavaobserver D/RxJava: 14
    -16618/keye.com.rxjavaobserver D/RxJava: 18
    -16618/keye.com.rxjavaobserver D/RxJava: 22


    debounce( ) — 只有当Observable在指定的时间后还没有发射数据时，才发射一个数据
    Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            try {
                for (int i = 1; i < 10; i++) {
                    subscriber.onNext(i);
                    Thread.sleep(i * 1000); //每次发送，延迟i*1秒
                }
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }
    })
            .subscribeOn(Schedulers.newThread())
            .debounce(3000, TimeUnit.MILLISECONDS) //3秒没有数据，则发送
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    Log.d("RxJava", String.valueOf(integer));
                }
            });
    结果：前3个数延迟短，没有触发debounce()操作符，第4个数延迟3秒，debounce()生效
    30534-30550/keye.com.rxjavaobserver D/RxJava: 4
    30534-30550/keye.com.rxjavaobserver D/RxJava: 5
    30534-30550/keye.com.rxjavaobserver D/RxJava: 6
    30534-30550/keye.com.rxjavaobserver D/RxJava: 7
    30534-30550/keye.com.rxjavaobserver D/RxJava: 8


    distinct( ) — 过滤掉重复数据
    Observable.just(1, 2, 1, 4, 1, 6)
            .distinct()
            .subscribe(i -> {
                Log.d("RxJava", String.valueOf(i));
            });
    结果：
    11-06 04:38:49.987 19504-19504/keye.com.rxjavaobserver D/RxJava: 1
    11-06 04:38:49.987 19504-19504/keye.com.rxjavaobserver D/RxJava: 2
    11-06 04:38:49.987 19504-19504/keye.com.rxjavaobserver D/RxJava: 4
    11-06 04:38:49.988 19504-19504/keye.com.rxjavaobserver D/RxJava: 6


    ofType( ) — 只发射指定类型的数据
    Observable.just(1, "2", 3, "4", 5, 6)
            .ofType(Integer.class)
            .subscribe(i -> {
                Log.d("RxJava", String.valueOf(i));
            });
    结果：
    11-06 04:44:28.321 25785-25785/keye.com.rxjavaobserver D/RxJava: 1
    11-06 04:44:28.321 25785-25785/keye.com.rxjavaobserver D/RxJava: 3
    11-06 04:44:28.321 25785-25785/keye.com.rxjavaobserver D/RxJava: 5
    11-06 04:44:28.321 25785-25785/keye.com.rxjavaobserver D/RxJava: 6
</pre>

###线程控制-Scheduler
<pre>
1.Scheduler的API：
用于控制操作符和被观察者事件，所执行的线程
不同的调度器，对应不同的线程

调度器的分类
Schedulers.immediate():默认线程
Schedulers.newThread():新建线程
Schedulers.io():适用于I/O操作(线程池)
Schedulers.computation():适用于计算工作（线程池）
Schedulers.trampoline():当前线程，队列执行

如何进行线程调度?
subscribeOn():指定subscribe()所发生的线程，即Observable.OnSubscribe被激活时所处的线程。或者叫事件产生的线程
observeOn():指定Subscriber所运行的线程。获取叫时间消费的线程

在哪里产生事件就在那里消费事件，但是消费事件的时候可以选择多种线程调度方式，但是生产仅有一种方式
subscribeOn仅仅调用离自己最近的线程，所以只能调用一次
observeOn能调用多次

</pre>

###RxAndroid使用
<pre>
    由于Rx已经将各种各样的内容从RxAndroid中分开，所以RxAndriod使用的仅仅留下Android的调度器，即MainThread和from(looper)


</pre>

###doOnSubscribe
<pre>
    Rxandroid中doOnSubscribe()，如何指定其运行的线程？
    //在订阅后，事件发射前，执行一些代码
</pre>

###
<pre>

</pre>

###
<pre>

</pre>
###
<pre>

</pre>
###
<pre>

</pre>
###
<pre>

</pre>
