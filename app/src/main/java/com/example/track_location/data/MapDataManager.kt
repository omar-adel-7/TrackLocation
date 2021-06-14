package com.example.track_location.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.track_location.MyApp
import com.example.track_location.R
import com.example.track_location.data.MapRemote.MapRetrofitApiHelper
import com.example.track_location.data.MapRemote.bean.DirectionResponses
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.data.Remote.RetrofitApiError
import com.general.data.local.CacheApiDatabase
import com.general.utils.BaseUtil
import com.general.utils.CustomProgressBar
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MapDataManager
@Inject
constructor(context: Context) : RetrofitApiError {

    @Inject
    lateinit var mapRetrofitApiHelper: MapRetrofitApiHelper

    @Inject
    lateinit var cacheApiDatabase: CacheApiDatabase

    @Inject
    lateinit var customProgressBar: CustomProgressBar

    fun getDirection(
        appCompatActivity: AppCompatActivity, origin: String,
        destination: String
    ): LiveData<DirectionResponses> {
        beforeNetworkCall(appCompatActivity)
        val result = MutableLiveData<DirectionResponses>()
        val call = mapRetrofitApiHelper.getAPIService()
            .getDirection(
                origin, destination, appCompatActivity.getString(R.string.MapApiKey)
            )
        call.enqueue(object : Callback<DirectionResponses?> {
            override fun onResponse(
                call: Call<DirectionResponses?>,
                response: Response<DirectionResponses?>
            ) {
                afterNetworkCall(appCompatActivity)
                result.postValue(response.body())
            }

            override fun onFailure(call: Call<DirectionResponses?>, t: Throwable) {
                BaseUtil.logMessage("onResponseError", call.request().url.toUrl().toString())
                onResponseError(appCompatActivity, call.request(), t)
             }
        })
        return result
    }

    override fun onInternetUnavailable(context: Context, request: Request) {
        BaseUtil.logMessage("onInternetUnavailable", request.url.toUrl().toString())
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                context.getString(R.string.noInternetConnection),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResponseError(
        appCompatActivity: AppCompatActivity,
        request: Request,
        t: Throwable
    ) {
        afterNetworkCall(appCompatActivity)
        BaseUtil.logMessage("onResponseError", request.url.toUrl().toString())
        Toast.makeText(
            appCompatActivity,
            appCompatActivity.getString(R.string.err_happened),
            Toast.LENGTH_LONG
        ).show()
    }

    fun onResponseError(appCompatActivity: AppCompatActivity, errorMessage: String?) {
        afterNetworkCall(appCompatActivity)
        BaseUtil.logMessage("onResponseError errorMessage", errorMessage)
        Toast.makeText(
            appCompatActivity,
            appCompatActivity.getString(R.string.err_happened),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun beforeNetworkCall(appCompatActivity: AppCompatActivity) {
        appCompatActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        customProgressBar.show(
            appCompatActivity,
            false,
            (appCompatActivity as BaseParentActivity).getProgressBarView(),
            null,
            false,
            null,
            false,
            0,
            R.style.MyProgressDialogStyle
        )
    }

    private fun afterNetworkCall(appCompatActivity: AppCompatActivity) {
        appCompatActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        customProgressBar.dismissProgress(appCompatActivity)
    }

    init {
        (context as MyApp).myComponent.inject(this)
        mapRetrofitApiHelper.setRetrofitApiError(this)
        customProgressBar = CustomProgressBar()
    }
}