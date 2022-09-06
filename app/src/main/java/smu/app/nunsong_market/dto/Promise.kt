package smu.app.nunsong_market.dto

import com.google.gson.annotations.SerializedName


data class Promise (
    @SerializedName("id") val promiseId: Long? = null,
    @SerializedName("itemId") val itemId: Long,
    @SerializedName("title") val title: String, //약속 이름
    @SerializedName("senderUID") val senderUid: String, //본인 id
    @SerializedName("receiverUID") val receiverUid:String,
    @SerializedName("promiseDate") val date:String,

    // 0: pending, 1: rejected. 2:accepted
    @SerializedName("status") val status:Int,
    @SerializedName("location") val location:String,
    @SerializedName("memo") val memo:String? = null
        )
