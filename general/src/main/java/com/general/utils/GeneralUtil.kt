package com.general.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.general.R
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtil {
    fun getRealActivity(cont: Context?): Activity? {
        if (cont == null) return null else if (cont is Activity) return cont else if (cont is ContextWrapper) return getRealActivity(
            cont.baseContext
        )
        return null
    }

    fun getLanguagesArrayCodes(context: Context): ArrayList<String> {
        val languages_str_arr = context.resources.getStringArray(R.array.languages_codes)
        val languages_arr_str = ArrayList<String>()
        for (i in languages_str_arr.indices) {
            languages_arr_str.add(languages_str_arr[i])
        }
        return languages_arr_str
    }

    fun getLanguagesArrayNames(context: Context): ArrayList<String> {
        val languages_str_arr = context.resources.getStringArray(R.array.languages_names)
        val languages_arr_str = ArrayList<String>()
        for (i in languages_str_arr.indices) {
            languages_arr_str.add(languages_str_arr[i])
        }
        return languages_arr_str
    }

    fun convertDateFromLongToStr(longDate: Long): String {
        val date = Date(longDate)
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
        return simpleDateFormat.format(date)
    }
}