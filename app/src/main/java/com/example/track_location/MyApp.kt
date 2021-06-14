package com.example.track_location

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.track_location.di.AppModule
import com.example.track_location.di.DaggerMyComponent
import com.example.track_location.di.MyComponent
import com.example.track_location.di.MyModule
import com.general.data.shareddata.Prefs

class MyApp : Application() {
    lateinit var myComponent: MyComponent
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(false)
            .build()
       myComponent = createMyComponent()
    }

    private fun createMyComponent(): MyComponent {
        return DaggerMyComponent
            .builder()
            .myModule(MyModule())
            .appModule(AppModule(this))
            .build()
    }
}