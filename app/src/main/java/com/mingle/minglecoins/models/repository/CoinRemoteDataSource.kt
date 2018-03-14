package com.mingle.minglecoins.models.repository

import com.mingle.minglecoins.models.Coin
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class CoinRemoteDataSource {
    fun getCoins() : Observable<Resource<List<Coin>>>{
        val lstCoins = ArrayList<Coin>()
        (1..10).mapTo(lstCoins) {
            Coin(id = Integer.toString(it), name = "Coin$it")
        }
        val resource = Resource<List<Coin>>(Status.SUCCESS, lstCoins)
        return Observable.just(resource).delay(2, TimeUnit.SECONDS)
    }
}