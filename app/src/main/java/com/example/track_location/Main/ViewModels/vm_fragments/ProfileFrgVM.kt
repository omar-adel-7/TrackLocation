package com.example.track_location.Main.ViewModels.vm_fragments

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.example.track_location.Main.ViewModels.BaseViewModel
import com.general.data.bean.User
import okhttp3.ResponseBody

class ProfileFrgVM(application: Application) : BaseViewModel(application) {
    var myUser: User? = null
    fun updateUserName(
        appCompatActivity: AppCompatActivity,
        name: String?
    ): LiveData<ResponseBody> {
        return dataManager.updateUserName(
            appCompatActivity,
            name
        )
    }
}