package smu.app.nunsong_market.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id:Int,
    @SerializedName("title") val title:String,
    @SerializedName("price") val price:Int,
    @SerializedName("category") val category:String,
    @SerializedName("images") val coverSmallUrl:String,
    @SerializedName("content") val description:String,
    @SerializedName("sellerName") val sellerName:String,
    @SerializedName("sellerUID") val sellerUid:String,
    @SerializedName("status") val status:String,
    @SerializedName("trans") val trans:String,
    @SerializedName("registerDate") val date:String
)
