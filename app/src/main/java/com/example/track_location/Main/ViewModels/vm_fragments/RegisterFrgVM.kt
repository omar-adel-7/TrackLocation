package com.example.track_location.Main.ViewModels.vm_fragments

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.example.track_location.Main.ViewModels.BaseViewModel
import com.example.track_location.data.AuthRemote.bean.AuthResponse
import com.general.data.bean.User

class RegisterFrgVM(application: Application) : BaseViewModel(application) {
    fun register(
        appCompatActivity: AppCompatActivity, email: String?,
        password: String?
    ): LiveData<AuthResponse> {
        return authDataManager.register(
            appCompatActivity, email,
            password
        )
    }

    fun createUser(
        appCompatActivity: AppCompatActivity, user_id: String, email: String, name: String
    ): LiveData<User> {
        return dataManager.createUser(
            appCompatActivity, user_id, email,
            name
        )
    }
}