package com.general.data.Remote

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Request

interface RetrofitApiError {
    fun onInternetUnavailable(context: Context, request: Request)
    fun onResponseError(appCompatActivity: AppCompatActivity, request: Request, t: Throwable)
}