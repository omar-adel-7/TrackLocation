package com.example.track_location.Main.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.track_location.MyApp
import com.example.track_location.data.AuthDataManager
import com.example.track_location.data.DataManager
import com.example.track_location.data.MapDataManager
import com.example.track_location.utils.FireBaseReferences
import com.general.firebase.FireBaseManager
import javax.inject.Inject

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
     lateinit var dataManager: DataManager

    @Inject
    lateinit  var authDataManager: AuthDataManager

    @Inject
    lateinit var mapDataManager: MapDataManager

    @Inject
    lateinit var fireBaseManager: FireBaseManager

    @Inject
    lateinit var fireBaseReferences: FireBaseReferences
    protected fun injectMe(application: Application) {
        (application as MyApp).myComponent.inject(this)
    }

    init {
        injectMe(application)
    }
}