package com.mingle.minglecoins.models.repository

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*


interface CoinAPI {
    @GET("data/coinlist")
    fun getCoinList(): Observable<CoinsListResponse>
}

class CoinRemoteDataSource {

    val END_POINT = "https://www.cryptocompare.com/api/"
    val END_POINT_V2 = "https://min-api.cryptocompare.com/"

    private val retrofit : Retrofit
    private val retrofitv2 : Retrofit
    private val coinAPI : CoinAPI

    init {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                .registerTypeAdapter(CoinsListResponse::class.java, CoinsListResponseDeserializer())
                .create()

        retrofit = Retrofit.Builder()
                .baseUrl(END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        retrofitv2 = Retrofit.Builder()
                .baseUrl(END_POINT_V2)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        coinAPI = retrofit.create(CoinAPI::class.java)
    }



    fun getCoins() : Observable<CoinsListResponse>{
        return coinAPI.getCoinList()
    }
}