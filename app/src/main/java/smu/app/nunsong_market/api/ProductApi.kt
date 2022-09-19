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
    fun getProductsByCategory(@Query("keyword") catagory: String): Call<List<Product>>

    @GET("/items/search")
    fun getProductsSearch(@Query("keyword") keyword: String): Call<List<Product>>


/*    fun postProduct(
        images: MultipartBody.Part,
        title: RequestBody,
        price: RequestBody,
        category: RequestBody,
        description: RequestBody,
        sellerName: RequestBody,
        sellerUid: RequestBody,
        status: RequestBody,
        trans: RequestBody,
        isImgEnpty: Boolean
    ) : Call<Product>{
        when (isImgEnpty) {
            true -> return postProductNoImage(title,price,category,description,sellerName,sellerUid,status,trans)
            false -> return postProductImage(images,title,price,category,description,sellerName,sellerUid,status,trans)
        }
    }*/

    @Multipart
    @POST("/items/imagesitems")
    fun postProductImage(
        @Part images: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part("content") description: RequestBody,
        @Part("sellerName") sellerName: RequestBody,
        @Part("sellerUID") sellerUid: RequestBody,
        @Part("status") status: RequestBody,
        @Part("trans") trans: RequestBody,
    ): Call<Product>

    @Multipart
    @PUT("/items/app/{id}")
    fun editProductImage(
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part("content") description: RequestBody,
        @Part("sellerName") sellerName: RequestBody,
        @Part("sellerUID") sellerUid: RequestBody,
        @Part("status") status: RequestBody,
        @Part("trans") trans: RequestBody,
        @Part images: MultipartBody.Part? = null,
    ): Call<Product>

/*    @POST("/items/imagesitems")
    fun postProductNoImage(
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part("content") description: RequestBody,
        @Part("sellerName") sellerName: RequestBody,
        @Part("sellerUID") sellerUid: RequestBody,
        @Part("status") status: RequestBody,
        @Part("trans") trans: RequestBody,
    ): Call<Product>*/

    @DELETE("/items/{id}")
    fun deleteArticle(@Path("id") id: Int): Call<Void>
}