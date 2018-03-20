package com.mingle.minglecoins.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.mingle.minglecoins.models.Coin
import com.mingle.minglecoins.models.repository.CoinRepository
import com.mingle.minglecoins.models.repository.Price
import com.mingle.minglecoins.models.repository.Resource
import com.mingle.minglecoins.models.repository.Status
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}


class CoinsViewModel : ViewModel() {
    val isLoading = ObservableField(false)
    private val coinRepository = CoinRepository()
    var coins = MutableLiveData<List<Coin>>()

    fun loadedCoinList() : Boolean {
        coins.value?.isNotEmpty()?.let {
            return it
        }
        return false
    }

    private val compositeDisposable = CompositeDisposable()
    fun getCoins() {
        isLoading.set(true)
        compositeDisposable += coinRepository.getCoins()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Resource<List<Coin>>>() {
                    override fun onComplete() {
                        isLoading.set(false)
                    }

                    override fun onNext(result: Resource<List<Coin>>) {
                        if (result.status == Status.SUCCESS) {
                            coins.value = result.data
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                })

    }

    fun getPrices() {
        coins.value?.let {
            val source = Flowable.create<Int>({ emitter ->
                for (i in 0..it.size) {
                    if (!emitter.isCancelled) {
                        emitter.onNext(i)
                    }
                }
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER)

            compositeDisposable += source.subscribeOn(Schedulers.newThread()).subscribeWith(object : DisposableSubscriber<Int>() {
                        override fun onComplete() {

                        }

                        override fun onNext(t: Int?) {
                            val coin = coins.value?.get(t!!)
                            coin?.name?.let {
                                compositeDisposable += coinRepository.getPrice(it)
                                        .subscribeOn(Schedulers.computation())
                                        .subscribeWith(object : DisposableObserver<Resource<Price>>() {
                                            override fun onComplete() {
                                            }

                                            override fun onNext(result: Resource<Price>) {
                                                if (result.status == Status.SUCCESS) {
                                                    coin.price = result.data
                                                }
                                            }

                                            override fun onError(e: Throwable) {

                                            }
                                        })
                            }
                        }

                        override fun onError(t: Throwable?) {

                        }
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
