package smu.app.nunsong_market.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.dto.Promise

interface PromiseApi {

    @GET("/promise/user/{userUID}")
    fun getPromise(@Path("userUID") userUID: String): Call<List<Promise>>

    @POST("/promise")
    fun postPromise(@Body Promise: Promise): Call<Promise>

    @PUT("/promise/{id}")
    fun changePromise(@Path("id") id: Long, @Body Promise: Promise): Call<Promise>

    @PUT("/promise/acceptance/deal/{id}")
    fun changePromiseStatus(@Path("id") id: Long, @Body Promise: Promise, @Query("status") status: Int): Call<Promise>

    @DELETE("/promise/{id}")
    fun deletePromise(@Path("id") id: Long): Call<Int>

}