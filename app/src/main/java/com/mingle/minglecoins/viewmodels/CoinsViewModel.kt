package com.mingle.minglecoins.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.mingle.minglecoins.models.Coin
import com.mingle.minglecoins.models.repository.CoinRepository
import com.mingle.minglecoins.models.repository.Resource
import com.mingle.minglecoins.models.repository.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CoinsViewModel : ViewModel() {
    val isLoading = ObservableField(false)
    private val coinRepository = CoinRepository()
    var coins = MutableLiveData<List<Coin>>()

    fun getCoins() {
        isLoading.set(true)
        coinRepository.getCoins()
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

}
