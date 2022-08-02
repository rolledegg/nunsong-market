package smu.app.nunsong_market.model

import com.google.gson.annotations.SerializedName
/**for fake api**/
//data class Product(
//    @SerializedName("id") val id:Int,
//    @SerializedName("title") val title:String,
//    @SerializedName("description") val description:String,
//    @SerializedName("image") val coverSmallUrl:String,
//    @SerializedName("price") val price:String,
//    @SerializedName("category") val category:String,
//    val date: String ="5월 4일 오후 2:00",
//    val status: String ="거래완료"
//)

/** for real api**/
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
