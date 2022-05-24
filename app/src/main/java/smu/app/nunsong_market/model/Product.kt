package smu.app.nunsong_market.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id:Long,
    @SerializedName("title") val title:String,
    @SerializedName("description") val description:String,
    @SerializedName("image") val coverSmallUrl:String,
    @SerializedName("price") val price:String,
    @SerializedName("category") val category:String,
    val date: String ="5월 4일 오후 2:00",
    val status: String ="거래완료"
)