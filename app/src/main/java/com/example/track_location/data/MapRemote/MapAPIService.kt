package com.example.track_location.data.MapRemote

import com.example.track_location.data.MapRemote.bean.DirectionResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MapAPIService {
    @GET("directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Call<DirectionResponses>
}