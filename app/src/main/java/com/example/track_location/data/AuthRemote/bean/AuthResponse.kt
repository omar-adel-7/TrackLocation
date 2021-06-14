package com.example.track_location.data.AuthRemote.bean

import com.google.gson.annotations.SerializedName

data class AuthResponse (
    @SerializedName("error")
    val errorAuth: ErrorAuth? = null,
    val expiresIn: String? = null,
    var kind: String? = null,
    val idToken: String? = null,
    val localId: String ,
    val email: String,
    val refreshToken: String? = null
    )