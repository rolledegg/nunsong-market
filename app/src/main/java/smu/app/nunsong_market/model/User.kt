package smu.app.nunsong_market.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("userName") val userName: String,
    @SerializedName("userId") val uid: String,
    @SerializedName("email") val email: String
    )