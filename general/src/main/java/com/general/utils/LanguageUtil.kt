package com.general.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.general.data.shareddata.Prefs
import java.util.*

object LanguageUtil {
    const val AppLanguageState = "AppLanguageState"
    var ARABIC_LANGUAGE = "ar"
    var ENGLISH_LANGUAGE = "en"
    fun saveLanguage(language: String?) {
        Prefs.putStringNow(AppLanguageState, language)
    }

    val deviceLocale: String
        get() = Resources.getSystem().configuration.locale.language

    @JvmStatic
    fun setAppLocale(context: Context, language: String?): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(
            context,
            language
        )
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        configuration.setLayoutDirection(locale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun getCertainLocaleContext(context: Context, language: String?): Context {
        val config = Configuration(context.resources.configuration)
        val newLocale = Locale(language)
        config.setLocale(newLocale)
        return context.createConfigurationContext(config)
    }

    fun getCertainLocaleString(context: Context, resId: Int, language: String?): String {
        return getCertainLocaleContext(context, language).getString(resId)
    }

    @JvmStatic
    val appLanguage: String
        get() = Prefs.getString(
            AppLanguageState,
            ENGLISH_LANGUAGE
        )
}