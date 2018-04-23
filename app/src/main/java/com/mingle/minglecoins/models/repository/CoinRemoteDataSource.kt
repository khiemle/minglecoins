package com.mingle.minglecoins.models.repository

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.mingle.minglecoins.BuildConfig
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit


interface CoinAPI {
    @GET("data/coinlist")
    fun getCoinList(): Observable<CoinsListResponse>
}

interface CoinAPIv2 {
    @GET("data/pricemulti")
    fun getPrices(@Query("fsyms") fsyms : String,@Query("tsyms") tsyms : String): Observable<PricesListResponse>

    @GET("data/price")
    fun getPrice(@Query("fsym") fsyms : String,@Query("tsyms") tsyms : String): Observable<Price>
}

class CoinRemoteDataSource {

    val END_POINT = "https://www.cryptocompare.com/api/"
    val END_POINT_V2 = "https://min-api.cryptocompare.com/"

    private val retrofit : Retrofit
    private val retrofitv2 : Retrofit
    private val coinAPI : CoinAPI
    private val coinAPIv2 : CoinAPIv2

    init {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                .registerTypeAdapter(CoinsListResponse::class.java, CoinsListResponseDeserializer())
                .registerTypeAdapter(PricesListResponse::class.java, PricesListResponseDeserializer())
                .create()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).build()

        retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        retrofitv2 = Retrofit.Builder()
                .client(client)
                .baseUrl(END_POINT_V2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        coinAPI = retrofit.create(CoinAPI::class.java)
        coinAPIv2 = retrofitv2.create(CoinAPIv2::class.java)
    }

    fun getCoins() : Observable<CoinsListResponse>{
        return coinAPI.getCoinList()
    }

    fun getPrice(symbol : String) : Observable<Price> {
        return coinAPIv2.getPrice(symbol, "USD")
    }

    fun getPrices(symbols : List<String>) : Observable<PricesListResponse> {
        var fsyms = ""
        symbols.map { symbol -> {
            fsyms += ",$symbol"
        }}
        fsyms = fsyms.substring(1)
        return coinAPIv2.getPrices(fsyms, "USD")
    }
}