package smu.app.nunsong_market.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.dto.Promise

interface PromiseApi {


    @GET("/items/{userName}")
    fun getMyProducts(@Path("userName") userName: String): Call<List<Promise>>


    @GET("/items/search")
    fun getProductsSearch(@Query("keyword") keyword: String ): Call<List<Promise>>

    @POST("/promise")
    fun postPromise(@Body Promise: Promise): Call<Promise>


}