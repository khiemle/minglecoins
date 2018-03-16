package com.mingle.minglecoins.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.mingle.minglecoins.models.Coin
import com.mingle.minglecoins.models.repository.CoinRepository
import com.mingle.minglecoins.models.repository.Price
import com.mingle.minglecoins.models.repository.Resource
import com.mingle.minglecoins.models.repository.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}


class CoinsViewModel : ViewModel() {
    val isLoading = ObservableField(false)
    private val coinRepository = CoinRepository()
    var coins = MutableLiveData<List<Coin>>()

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
                            getPrices()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                })

    }

    fun getPrices() {
        isLoading.set(true)
        val symbols = ArrayList<String>()
        coins.value?.forEach { coin ->
            coin.name?.let { symbols.add(coin.name) }
        }
        val SYMBOLS_PER_PAGE = 50
        var page = 0;
        compositeDisposable += coinRepository.getPrice(symbols).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object  : DisposableObserver<Resource<List<Price>>>() {
                    override fun onComplete() {
                        isLoading.set(false)
                    }

                    override fun onNext(result: Resource<List<Price>>) {
                        if (result.status == Status.SUCCESS) {
                            result.data?.mapIndexed { index, price -> coins.value?.get(index)?.price = price}
                            coins.postValue(coins.value)
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
