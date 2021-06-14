package com.example.track_location.LocationMap

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.example.track_location.Main.ViewModels.BaseViewModel
import com.google.android.gms.maps.model.Marker
import okhttp3.ResponseBody
import java.util.*

abstract class MapBaseFrgVM(application: Application) : BaseViewModel(application) {
    val markerArray = ArrayList<Marker?>()
    fun updateUserLocation(
        appCompatActivity: AppCompatActivity,
        longitude: Double?,
        latitude: Double?
    ): LiveData<ResponseBody> {
        return dataManager.updateUserLocation(
            appCompatActivity,
            longitude, latitude
        )
    }

    abstract fun removeFirebaseListeners()
}