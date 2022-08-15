package smu.app.nunsong_market.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import smu.app.nunsong_market.dto.User

interface UserApi {
    @POST("/users")
    fun postUser(@Body User: User): Call<User>
}