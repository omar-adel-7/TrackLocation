package com.example.track_location.di

import com.example.track_location.Main.ViewModels.BaseViewModel
import com.example.track_location.data.AuthDataManager
import com.example.track_location.data.DataManager
import com.example.track_location.data.MapDataManager
import com.example.track_location.firebase.CustomFirebaseMessagingService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MyModule::class, AppModule::class])
interface MyComponent {
    fun inject(baseViewModel: BaseViewModel)
    fun inject(dataManager: DataManager)
    fun inject(dataManager: AuthDataManager)
    fun inject(dataManager: MapDataManager)
    fun inject(customFirebaseMessagingService: CustomFirebaseMessagingService)
}