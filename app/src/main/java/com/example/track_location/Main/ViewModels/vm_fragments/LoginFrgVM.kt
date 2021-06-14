package com.example.track_location.Main.ViewModels.vm_fragments

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.example.track_location.Main.ViewModels.BaseViewModel
import com.example.track_location.data.AuthRemote.bean.AuthResponse

class LoginFrgVM(application: Application) : BaseViewModel(application) {
    fun login(
        appCompatActivity: AppCompatActivity, email: String?,
        password: String?
    ): LiveData<AuthResponse> {
        return authDataManager.login(
            appCompatActivity, email,
            password
        )
    }
}