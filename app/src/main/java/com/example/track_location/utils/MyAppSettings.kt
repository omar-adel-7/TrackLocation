package com.example.track_location.utils

import com.general.data.shareddata.Prefs
import com.general.utils.MyConstants

object MyAppSettings {
    val isFirstRun: Boolean
        get() = Prefs.getBoolean(MyConstants.FIRST_RUN_KEY, true)

    fun saveFirstRun(firstRun: Boolean) {
        Prefs.putBoolean(MyConstants.FIRST_RUN_KEY, firstRun)
    }
}