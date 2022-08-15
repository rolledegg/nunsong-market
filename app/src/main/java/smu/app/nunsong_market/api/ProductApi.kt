package smu.app.nunsong_market.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import smu.app.nunsong_market.dto.Product

interface ProductApi {
    @GET("/items")
    fun getProducts(): Call<List<Product>>

    @GET("/items/item/{id}")
    fun getProductById(@Path("id") id: Int): Call<Product>


    @GET("/items/{userName}")
    fun getMyProducts(@Path("userName") userName: String): Call<List<Product>>

    @GET("/items/search/category")
    fun getProductsByCategory(@Query("keyword") catagory: String ): Call<List<Product>>

    @GET("/items/search")
    fun getProductsSearch(@Query("keyword") keyword: String ): Call<List<Product>>

    @POST("/items")
    fun postProduct(@Body Product: Product): Call<Product>

    @Multipart
    @POST("/items/imagesitems")
    fun postProductImage(
        @Part images:MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("price") price:RequestBody,
        @Part("category") category:RequestBody,
        @Part("content") description:RequestBody,
        @Part("sellerName") sellerName:RequestBody,
        @Part("sellerUID") sellerUid:RequestBody,
        @Part("status") status:RequestBody,
        @Part("trans") trans:RequestBody,
    ): Call<Product>

    @POST("/items/imagesitems")
    fun postProductImage(
        @Part("title") title: RequestBody,
        @Part("price") price:RequestBody,
        @Part("category") category:RequestBody,
        @Part("content") description:RequestBody,
        @Part("sellerName") sellerName:RequestBody,
        @Part("sellerUID") sellerUid:RequestBody,
        @Part("status") status:RequestBody,
        @Part("trans") trans:RequestBody,
    ): Call<Product>

}