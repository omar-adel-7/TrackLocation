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
import com.example.track_location.data.Remote.RetrofitApiHelper
import com.example.track_location.utils.MyUserData.userId
import com.example.track_location.utils.MyUserData.userTokenId
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.data.Remote.RetrofitApiError
import com.general.data.bean.User
import com.general.data.local.CacheApiDatabase
import com.general.utils.BaseUtil
import com.general.utils.CustomProgressBar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class DataManager
@Inject
constructor
    (context: Context) : RetrofitApiError {
    @Inject
    lateinit var retrofitApiHelper: RetrofitApiHelper

    @Inject
    lateinit var cacheApiDatabase: CacheApiDatabase

    @Inject
    lateinit var customProgressBar: CustomProgressBar

    fun createUser(
        appCompatActivity: AppCompatActivity, user_id: String,
        email: String,
        name: String
    ): LiveData<User> {
        beforeNetworkCall(appCompatActivity)
        val result = MutableLiveData<User>()
        val user = User(
            user_id, email, name,
            userTokenId, 0.0, 0.0, 0L, Date().time
        )
        val jsonString = Gson().toJson(user)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonString.toRequestBody(
            mediaType
        )
        val call = retrofitApiHelper.getAPIService()
            .createUser(
                body, user_id
            )
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                afterNetworkCall(appCompatActivity)
                result.postValue(response.body()) //correct
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                BaseUtil.logMessage("onResponseError", call.request().url.toUrl().toString())
                onResponseError(appCompatActivity, call.request(), t)
            }
        })
        return result
    }

    fun updateUserTokenId(): LiveData<ResponseBody?> {
        val result = MutableLiveData<ResponseBody?>()
        val jsonParams: HashMap<String?, Any?> = HashMap<String?, Any?>()
        jsonParams["token"] = userTokenId
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = JSONObject(jsonParams).toString().toRequestBody(
            mediaType
        )
        val call = retrofitApiHelper.getAPIService().updateUserTokenId(
            body, userId
        )
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                result.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                BaseUtil.logMessage("onResponseError", call.request().url.toUrl().toString())
            }
        })
        return result
    }

    fun updateUserLocation(
        appCompatActivity: AppCompatActivity,
        longitude: Double?,
        latitude: Double?
    ): LiveData<ResponseBody> {
        beforeNetworkCall(appCompatActivity)
        val result = MutableLiveData<ResponseBody>()
        val jsonParams: HashMap<String?, Any?> = HashMap<String?, Any?>()
        jsonParams["longitude"] = longitude
        jsonParams["latitude"] = latitude
        jsonParams["locationUpdatedDate"] = Date().time
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = JSONObject(jsonParams).toString().toRequestBody(
            mediaType
        )
        val call = retrofitApiHelper.getAPIService()
            .updateUserLocation(
                body, userId
            )
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                afterNetworkCall(appCompatActivity)
                result.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                BaseUtil.logMessage("onResponseError", call.request().url.toUrl().toString())
                onResponseError(appCompatActivity, call.request(), t)
            }
        })
        return result
    }

    fun updateUserName(
        appCompatActivity: AppCompatActivity, name: String?
    ): LiveData<ResponseBody> {
        beforeNetworkCall(appCompatActivity)
        val result = MutableLiveData<ResponseBody>()
        val jsonParams: HashMap<String?, Any?> = HashMap<String?, Any?>()
        jsonParams["name"] = name
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = JSONObject(jsonParams).toString().toRequestBody(
            mediaType
        )
        val call = retrofitApiHelper.getAPIService()
            .updateUserName(
                body, userId
            )
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                afterNetworkCall(appCompatActivity)
                result.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
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
        retrofitApiHelper.setRetrofitApiError(this)
        customProgressBar = CustomProgressBar()
    }
}