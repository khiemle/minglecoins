package com.mingle.minglecoins.models.repository

import com.mingle.minglecoins.models.Coin
import io.reactivex.Observable

class CoinRepository {
    private val coinRemoteDataSource = CoinRemoteDataSource()
    fun getCoins() : Observable<Resource<List<Coin>>> {
        return coinRemoteDataSource.getCoins().flatMap {
            val lstCoin = it.data
            val resource = Resource<List<Coin>>(Status.SUCCESS, lstCoin)
            return@flatMap Observable.just(resource)
        }
    }


}