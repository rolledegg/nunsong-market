package smu.app.nunsong_market.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import smu.app.nunsong_market.model.Product

interface ProductApi {
    /** for fake api **/
//    @GET("/products")
//    fun getProducts(): Call<List<Product>>
//
//    @POST("/products")
//    fun postProduct(@Body Product:Product):Call<Product>

    @GET("/items")
    fun getProducts(): Call<List<Product>>

    @GET("/items/{userName}")
    fun getMyProducts(@Path("userName") userName:String): Call<List<Product>>

    @POST("/items")
    fun postProduct(@Body Product:Product):Call<Product>
}