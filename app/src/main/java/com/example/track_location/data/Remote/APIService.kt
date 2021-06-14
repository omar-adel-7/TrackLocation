package com.example.track_location.data.Remote

import com.general.data.bean.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIService {
    @PUT("users/{user_id}.json")
    fun createUser(
        @Body body: RequestBody,
        @Path("user_id") user_id: String?
    ): Call<User>

    @PATCH("users/{user_id}.json")
    fun updateUserTokenId(
        @Body body: RequestBody,
        @Path("user_id") user_id: String?
    ): Call<ResponseBody>

    @PATCH("users/{user_id}.json")
    fun updateUserLocation(
        @Body body: RequestBody,
        @Path("user_id") user_id: String?
    ): Call<ResponseBody>

    @PATCH("users/{user_id}.json")
    fun updateUserName(
        @Body body: RequestBody,
        @Path("user_id") user_id: String?
    ): Call<ResponseBody>
}