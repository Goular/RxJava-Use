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
