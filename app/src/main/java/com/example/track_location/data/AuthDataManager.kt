package com.example.track_location.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.track_location.BuildConfig
import com.example.track_location.MyApp
import com.example.track_location.R
import com.example.track_location.data.AuthRemote.AuthRetrofitApiHelper
import com.example.track_location.data.AuthRemote.bean.AuthResponse
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.data.Remote.RetrofitApiError
import com.general.data.local.CacheApiDatabase
import com.general.utils.BaseUtil
import com.general.utils.CustomAlertDialog
import com.general.utils.CustomProgressBar
import com.general.utils.MyConstants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class AuthDataManager
@Inject
constructor
    (context: Context) : RetrofitApiError {

    @Inject
    lateinit var authRetrofitApiHelper: AuthRetrofitApiHelper

    @Inject
    lateinit var cacheApiDatabase: CacheApiDatabase

    @Inject
    lateinit var customProgressBar: CustomProgressBar

    fun login(
        appCompatActivity: AppCompatActivity,
        email: String?,
        password: String?
    ): LiveData<AuthResponse> {
        val customAlertDialog = CustomAlertDialog(appCompatActivity)
        beforeNetworkCall(appCompatActivity)
        val result = MutableLiveData<AuthResponse>()
        val jsonParams: HashMap<String?, Any?> = HashMap<String?, Any?>()
        jsonParams["email"] = email
        jsonParams["password"] = password
        jsonParams["returnSecureToken"] = true
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = JSONObject(jsonParams).toString().toRequestBody(
            mediaType)
        val call = authRetrofitApiHelper.getAPIService().login(
            body, BuildConfig.FirebaseApiKey
        )
        call.enqueue(object : Callback<AuthResponse?> {
            override fun onResponse(call: Call<AuthResponse?>, response: Response<AuthResponse?>) {
                afterNetworkCall(appCompatActivity)
                if (response.code() == MyConstants.ResponseCodeSuccess) {
                    result.postValue(response.body())
                } else if (response.code() == MyConstants.ResponseCode400) {
                    customAlertDialog.alertDialog(appCompatActivity.getString(R.string.error_login))
                }
            }

            override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                BaseUtil.logMessage("onResponseError", call.request().url.toUrl().toString())
                onResponseError(appCompatActivity, call.request(), t)
             }
        })
        return result
    }

    /////////////////////////write
    fun register(
        appCompatActivity: AppCompatActivity, email: String?,
        password: String?
    ): LiveData<AuthResponse> {
        val customAlertDialog = CustomAlertDialog(appCompatActivity)
        beforeNetworkCall(appCompatActivity)
        val result = MutableLiveData<AuthResponse>()
        val jsonParams: HashMap<String?, Any?> = HashMap<String?, Any?>()
        jsonParams["email"] = email
        jsonParams["password"] = password
        jsonParams["returnSecureToken"] = true
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = JSONObject(jsonParams).toString().toRequestBody(
            mediaType)
        val call = authRetrofitApiHelper.getAPIService().register(
            body, BuildConfig.FirebaseApiKey
        )
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                afterNetworkCall(appCompatActivity)
                if (response.code() == MyConstants.ResponseCodeSuccess) {
                    result.postValue(response.body())
                } else if (response.code() == MyConstants.ResponseCode400) {
                    customAlertDialog.alertDialog(appCompatActivity.getString(R.string.err_in_register))
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
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
        authRetrofitApiHelper.setRetrofitApiError(this)
        customProgressBar = CustomProgressBar()
    }
}