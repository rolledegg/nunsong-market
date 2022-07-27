package smu.app.nunsong_market.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // TODO:buzzi에서 적용하던 것들 어떻게 쓰이는 걸까 정리할 때 살펴보기

//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .client(httpClient.build())
//        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}