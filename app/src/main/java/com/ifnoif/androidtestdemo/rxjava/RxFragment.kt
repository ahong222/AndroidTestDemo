package com.ifnoif.androidtestdemo.rxjava

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ifnoif.androidtestdemo.BaseFragment
import com.ifnoif.androidtestdemo.R
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.rx_fragment.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit


/**
 * Created by shen on 17/7/17.
 */
class RxFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(context).inflate(R.layout.rx_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservableTest()
        initConsumerTest()
        initMap()
        initFlatMap()
        initZipText()
        initFlowableTest()
        initDoubleSubscribe()
        initTransformer()
        initMerge()
    }

    fun initObservableTest() {
        var data = Observable.create(ObservableOnSubscribe<String> {
            item: ObservableEmitter<String> ->
            kotlin.run {
                item.onNext("test1")
                try {
                    item.onError(IllegalStateException("test"))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                item.onComplete()
                item.onNext("test2")

            }
        })

        var listener = object : Observer<String> {
            var disposable: Disposable? = null
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onNext(p0: String?) {
                Log.d(TAG, "onNext ${p0}")
//                disposable?.dispose()
            }

            override fun onSubscribe(p0: Disposable?) {
                Log.d(TAG, "onSubscribe")
                disposable = p0
            }

            override fun onError(p0: Throwable?) {
                Log.d(TAG, "not implemented ${p0}")
            }
        }

        ObservableTest.setOnClickListener {
            data.subscribe(listener)
        }
    }

    fun initConsumerTest() {
        var observable = Observable.create<String> {
            it.onNext("Test1")
        }
        var doOnNexconsumer = Consumer<String> {
            Log.d(TAG, "doOnNext accept:${it}")
        }

        var consumer = Consumer<String> {
            Log.d(TAG, "accept:${it}")
        }
        ConsumerTest.setOnClickListener {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(doOnNexconsumer).subscribe(consumer)
        }
    }

    fun initMap() {
        var observable = Observable.create<String> {
            it.onNext("1test")
        }
        var consumer = Consumer<Int> {
            Log.d(TAG, "accept:${it}")
        }

        MapTest.setOnClickListener {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map { p0 -> p0?.substring(0, 1)?.toInt() ?: -1 }.subscribe(consumer)
        }
    }

    fun initFlatMap() {
        var observable = Observable.create<String> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "在这里注册, thread:${Looper.getMainLooper().isCurrentThread}")
            }
            it.onNext("Success")
        }
        var registerConsumer = Consumer<Int> {
            Log.d(TAG, "登陆结果:${if (it.toInt() == 1) "success" else "fail"}")
        }

        FlatMapTest.setOnClickListener {
            var flatMapper = object : Function<String, ObservableSource<Int>> {
                override fun apply(p0: String?): ObservableSource<Int> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d(TAG, "注册结果:${p0} thread:${Looper.getMainLooper().isCurrentThread}")
                    }
                    var list = ArrayList<String>();
                    var charArray = p0?.toCharArray()
                    charArray?.forEach { list.add(it.toString()) }
                    return Observable.fromCallable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.d(TAG, "在这里登陆,thread:${Looper.getMainLooper().isCurrentThread}")
                        }
                        if ("success".equals(p0 ?: "", true)) 1 else 0
                    }.delay(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                }
            }

            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext({
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "doOnNext 注册结果 ${it},thread:${Looper.getMainLooper().isCurrentThread}")
                }
            }).flatMap(flatMapper).subscribe(registerConsumer)

            //以下可以测试是否转换
//            var consumer = Consumer<String>{
//                Log.d(TAG, "accept:${it}")
//            }
//            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap { p0 ->
//                var list = ArrayList<String>()
//                var charArray = p0?.toCharArray()
//                charArray?.forEach { list.add(it.toString()) }
//                Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS)
//            }.subscribe(consumer)
        }
    }

    fun initZipText() {
        var observable1 = Observable.create<String> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "create data1 ,thread:${Thread.currentThread().id}")
            }
            it.onNext("A")
            it.onNext("B")
        }.subscribeOn(Schedulers.io())
        var observable2 = Observable.create<Int> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "create data2 ,thread:${Thread.currentThread().id}")
            }
            it.onNext(1)
            it.onNext(2)
        }.filter({
            it.toInt() > 1
        }).subscribeOn(Schedulers.io())
        ZipTest.setOnClickListener {
            Observable.zip(observable1, observable2, BiFunction<String, Int, String> { p0, p1 -> p0 + p1 })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d(TAG, "result:${it}")
                    })
        }
    }

    fun initFlowableTest() {
        var observable1 = Flowable.create(object : FlowableOnSubscribe<String> {
            override fun subscribe(p0: FlowableEmitter<String>?) {
                for (i in 0..1000) {
                    p0?.onNext("str:" + i + "_")
                    Thread.sleep((10 * Math.random()).toInt().toLong())
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
        var observable2 = Flowable.create(object : FlowableOnSubscribe<Int> {
            override fun subscribe(p0: FlowableEmitter<Int>?) {
                for (i in 0..1000) {
                    p0?.onNext(i)
                    Thread.sleep((50 * Math.random()).toInt().toLong())
                }
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())
        FlowableTest.setOnClickListener {
            //            Flowable.zip(observable1, observable2, BiFunction<String, Int, String> { p0, p1 -> p0 + p1 })
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
//                        Log.d(TAG, "result:${it}")
//                    })

            Flowable
                    .create(FlowableOnSubscribe<Int>() {
                        Log.d(TAG, "current requested: " + it.requested())
                    }, BackpressureStrategy.ERROR)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Int> {

                        var mSubscription: Subscription? = null

                        override fun onNext(t: Int?) {
                            Log.d(TAG, "onNext:$t")
                            mSubscription?.request(2)
                        }

                        override fun onComplete() {
                            Log.d(TAG, "onComplete");
                        }

                        override fun onError(t: Throwable?) {
                            Log.d(TAG, "onError");
                        }

                        override fun onSubscribe(s: Subscription?) {
                            Log.d(TAG, "onSubscribe")
                            mSubscription = s
                            mSubscription?.request(2)
                        }
                    })
        }


    }

    fun initDoubleSubscribe() {
        multiSubscribe.setOnClickListener {
            Observable.just("a", "b", "c").subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<String> {
                override fun onNext(p0: String) {
                    println("p0:" + p0 + " thred:" + (Thread.currentThread().id == Looper.getMainLooper().thread.id))
                }

                override fun onComplete() {
                    println("onComplete")
                }

                override fun onError(p0: Throwable) {
                    println("onError")
                }

                override fun onSubscribe(p0: Disposable) {
                    println("onSubscribe")
                }
            })
        }
    }

    fun initTransformer() {

        transformer.setOnClickListener {
            var transformer = ObservableTransformer<String, String>() {
                a ->
                ////                val consumer2 = Consumer<String> { s -> Log.d("MainActivity", Thread.currentThread().name + " String:" + s) }
////
                var consumer = Consumer<String> { i -> println("transformer" + i) }
                a.subscribe(consumer)
                a.doOnSubscribe {
                    println("doOnSubscribe:"+it+(Thread.currentThread().id == Looper.getMainLooper().thread.id))
                }
                a.doOnComplete {
                    println("onComplete:"+it+(Thread.currentThread().id == Looper.getMainLooper().thread.id))
                }
            }
            Observable.create<String> {
                var1->
                var1.onNext("123")
                var1.onNext("456")
                var1.onComplete()
            }.compose(transformer).subscribeOn(Schedulers.io()).subscribe(object : Observer<String> {
                override fun onNext(p0: String) {
                    println("p0:" + p0 + " thred:" + (Thread.currentThread().id == Looper.getMainLooper().thread.id))
                }

                override fun onComplete() {
                    println("onComplete")
                }

                override fun onError(p0: Throwable) {
                    println("onError")
                }

                override fun onSubscribe(p0: Disposable) {
                    println("onSubscribe")
                }
            })
        }
    }

    fun initMerge() {
        merge.setOnClickListener {
            MergeUtils.testMerge()
        }
    }
}