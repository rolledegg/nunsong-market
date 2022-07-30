package smu.app.nunsong_market.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import smu.app.nunsong_market.model.User

interface UserApi {
    @POST("/users")
    fun postUser(@Body user: User): Call<User>
}