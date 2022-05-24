package smu.app.nunsong_market.api

import retrofit2.Call
import retrofit2.http.GET
import smu.app.nunsong_market.model.Product

interface ProductApi {
    @GET("/products")
    fun getProducts(): Call<List<Product>>
}