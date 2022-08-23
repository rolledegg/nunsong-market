package smu.app.nunsong_market.dto

import com.google.gson.annotations.SerializedName


data class Promise (
    @SerializedName("id") val itemId: Long,
    @SerializedName("userId") val myUid: String? = null, //본인 id
    @SerializedName("title") val title: String? = null, //약속 이름
    @SerializedName("price") val price: Int? = null,
    @SerializedName("images") val coverSmallUrl:String? = null, //상품 사진
    @SerializedName("buyerId") val buyerUid:String? = null,
    @SerializedName("trans") val trans: String? = null, //거래 방법 (직거래, 택배, 기타)
    @SerializedName("promiseDate") val date:String? = null,
    @SerializedName("where_meet") val where:String? = null,
    @SerializedName("memo") val memo:String? = null
        )
