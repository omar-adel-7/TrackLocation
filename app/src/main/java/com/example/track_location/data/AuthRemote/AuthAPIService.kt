package com.example.track_location.data.AuthRemote

import com.example.track_location.data.AuthRemote.bean.AuthResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPIService {
    @POST("./accounts:signInWithPassword")
    fun login(
        @Body body: RequestBody,
        @Query("key") apiKey: String?
    ): Call<AuthResponse>

    @POST("./accounts:signUp")
    fun register(
        @Body body: RequestBody,
        @Query("key") apiKey: String?
    ): Call<AuthResponse>
}