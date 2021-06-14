package com.example.track_location.data.MapRemote

import android.content.Context
import com.general.data.Remote.NetworkConnectionInterceptor
import com.general.data.Remote.RetrofitApiError
import com.general.utils.BaseUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRetrofitApiHelper


@Inject
constructor(context: Context) {

    private var retrofitApiError: RetrofitApiError? = null

    fun getAPIService(): MapAPIService {
        return retrofit.create(MapAPIService::class.java)
    }

    companion object {
        private val API_BASE_URL = "https://maps.googleapis.com/maps/api/"
    }

    val retrofit: Retrofit

    init {
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        val httpClient = httpClientBuilder
            .addInterceptor(object : NetworkConnectionInterceptor() {

                override val isInternetAvailable: Boolean
                    get() = BaseUtil.checkInternet(context)

                override fun onInternetUnavailable(request: Request) {
                    // we can broadcast this event to activity/fragment/service
                    // through LocalBroadcastReceiver or
                    // RxBus/EventBus
                    // also we can call our own interface method
                    // like this.
                    retrofitApiError?.onInternetUnavailable(context, request)
                }
            }).build()

        val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        retrofit = builder.client(httpClient).build()
    }


    fun setRetrofitApiError(listener: RetrofitApiError?) {
        retrofitApiError = listener
    }

    fun removeApiError() {
        retrofitApiError = null
    }
}