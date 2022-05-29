package smu.app.nunsong_market.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import smu.app.nunsong_market.model.Product

interface ProductApi {
    @GET("/products")
    fun getProducts(): Call<List<Product>>

    @POST("/products")
    fun postProduct(@Body Product:Product):Call<Product>
}