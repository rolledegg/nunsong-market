package smu.app.nunsong_market.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
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

    @GET("/items/search/category/{category}")
    fun getProductsByCategory(@Path("category") category:String): Call<List<Product>>

    @POST("/items")
    fun postProduct(@Body Product:Product):Call<Product>

    @Multipart
    @POST("/items/imagesitems")
    fun postImage(
        @Part("images") images:MultipartBody.Part?,
        @Part("title") title:RequestBody,
        @Part("price") price:RequestBody,
        @Part("category") category:RequestBody,
        @Part("content") description:RequestBody,
        @Part("sellerName") sellerName:RequestBody,
        @Part("status") status:RequestBody,
        @Part("trans") trans:RequestBody,
    ):Call<Product>

}